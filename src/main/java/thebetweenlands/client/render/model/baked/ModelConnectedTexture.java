package thebetweenlands.client.render.model.baked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import thebetweenlands.util.QuadBuilder;

public class ModelConnectedTexture implements IModel {
	protected static class Vertex {
		protected final Vec3d pos;
		protected final Vec2f uv;

		protected Vertex(double x, double y, double z, float u, float v) {
			this.pos = new Vec3d(x, y, z);
			this.uv = new Vec2f(u, v);
		}

		protected Vertex add(Vertex vertex) {
			return new Vertex(this.pos.x + vertex.pos.x, this.pos.y + vertex.pos.y, this.pos.z + vertex.pos.z, this.uv.x + vertex.uv.x, this.uv.y + vertex.uv.y);
		}

		protected Vertex subtract(Vertex vertex) {
			return new Vertex(this.pos.x - vertex.pos.x, this.pos.y - vertex.pos.y, this.pos.z - vertex.pos.z, this.uv.x - vertex.uv.x, this.uv.y - vertex.uv.y);
		}

		protected Vertex scale(float scale) {
			return new Vertex(this.pos.x * scale, this.pos.y * scale, this.pos.z * scale, this.uv.x * scale, this.uv.y * scale);
		}
	}

	protected static class ConnectedTextureQuad {
		protected final ResourceLocation[] textures;
		protected final String[] indices;
		protected final Vertex[] verts;
		protected final EnumFacing cullFace;
		protected final int tintIndex;

		protected final BakedQuad[][] quads;

		protected ConnectedTextureQuad(ResourceLocation[] textures, String[] indices, Vertex[] verts, EnumFacing cullFace, int tintIndex) {
			this.textures = textures;
			this.indices = indices;
			this.verts = verts;
			this.cullFace = cullFace;
			this.tintIndex = tintIndex;
			this.quads = new BakedQuad[4][this.textures.length];
		}

		public BakedQuad[][] getQuads() {
			return this.quads;
		}

		public void bake(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
				Optional<TRSRTransformation> transformation, ImmutableMap<TransformType, TRSRTransformation> transforms,
				VertexFormat format) {
			Vertex d01 = this.verts[1].subtract(this.verts[0]);
			Vertex d12 = this.verts[2].subtract(this.verts[1]);
			Vertex d23 = this.verts[3].subtract(this.verts[2]);
			Vertex d30 = this.verts[0].subtract(this.verts[3]);

			float e01 = this.extrapolateClamp(this.verts[0].uv.y, this.verts[1].uv.y, 0.5F);
			float e12 = this.extrapolateClamp(this.verts[1].uv.x, this.verts[2].uv.x, 0.5F);
			float e23 = this.extrapolateClamp(this.verts[2].uv.y, this.verts[3].uv.y, 0.5F);
			float e30 = this.extrapolateClamp(this.verts[3].uv.x, this.verts[0].uv.x, 0.5F);

			Vertex p01 = this.verts[0].add(d01.scale(e01));
			Vertex p12 = this.verts[1].add(d12.scale(e12));
			Vertex p23 = this.verts[2].add(d23.scale(e23));
			Vertex p30 = this.verts[3].add(d30.scale(e30));

			Vertex d0123 = p23.subtract(p01);

			float ecp = this.extrapolateClamp(this.verts[0].uv.x + (this.verts[1].uv.x - this.verts[0].uv.x) * e01, this.verts[2].uv.x + (this.verts[3].uv.x - this.verts[2].uv.x) * e23, 0.5F);

			Vertex cp = p01.add(d0123.scale(ecp));

			TextureAtlasSprite[] sprites = new TextureAtlasSprite[this.textures.length];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = bakedTextureGetter.apply(this.textures[i]);
			}

			this.quads[0] = this.bakeTextureVariants(format, transformation, sprites, new Vertex[] {this.verts[0], p01, cp, p30});
			this.quads[1] = this.bakeTextureVariants(format, transformation, sprites, new Vertex[] {p01, this.verts[1], p12, cp});
			this.quads[2] = this.bakeTextureVariants(format, transformation, sprites, new Vertex[] {cp, p12, this.verts[2], p23});
			this.quads[3] = this.bakeTextureVariants(format, transformation, sprites, new Vertex[] {p30, cp, p23, this.verts[3]});
		}

		protected float extrapolateClamp(float v1, float v2, float v) {
			float extrapolant = (v - v1) / (v2 - v1);
			return MathHelper.clamp(extrapolant, 0, 1);
		}

		protected BakedQuad[] bakeTextureVariants(VertexFormat format, Optional<TRSRTransformation> transformation,
				TextureAtlasSprite[] sprites, Vertex[] verts) {
			QuadBuilder builder = new QuadBuilder(4 * this.textures.length, format);
			if(transformation.isPresent()) {
				builder.setTransformation(transformation.get());
			}
			for(int i = 0; i < sprites.length; i++) {
				builder.setSprite(sprites[i]);
				builder.addVertex(verts[0].pos, verts[0].uv.x * 16.0F, verts[0].uv.y * 16.0F);
				builder.addVertex(verts[1].pos, verts[1].uv.x * 16.0F, verts[1].uv.y * 16.0F);
				builder.addVertex(verts[2].pos, verts[2].uv.x * 16.0F, verts[2].uv.y * 16.0F);
				builder.addVertex(verts[3].pos, verts[3].uv.x * 16.0F, verts[3].uv.y * 16.0F);
			}
			return builder.build(b -> b.setQuadTint(this.tintIndex)).nonCulledQuads.toArray(new BakedQuad[0]);
		}
	}

	protected final List<ConnectedTextureQuad> connectedTextures;
	protected final ResourceLocation particleTexture;
	protected final boolean ambientOcclusion;

	public ModelConnectedTexture() {
		this(TextureMap.LOCATION_MISSING_TEXTURE, true, ImmutableList.of());
	}

	public ModelConnectedTexture(ResourceLocation particleTexture, boolean ambientOcclusion, List<ConnectedTextureQuad> textures) {
		this.particleTexture = particleTexture;
		this.ambientOcclusion = ambientOcclusion;
		this.connectedTextures = textures;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		if(!this.connectedTextures.isEmpty()) {
			List<ResourceLocation> textures = new ArrayList<>();
			for(ConnectedTextureQuad quad : this.connectedTextures) {
				Collections.addAll(textures, quad.textures);
			}
			return textures;
		}
		return ImmutableList.of();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> map = PerspectiveMapWrapper.getTransforms(state);
		Optional<TRSRTransformation> transformation = state.apply(Optional.empty());
		for(ConnectedTextureQuad connectedTexture : this.connectedTextures) {
			connectedTexture.bake(bakedTextureGetter, transformation, map, format);
		}
		return new ModelBakedConnectedFace(transformation, map, format, bakedTextureGetter.apply(this.particleTexture),
				this.ambientOcclusion, this.connectedTextures);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	public static class ModelBakedConnectedFace implements IBakedModel {
		protected final ConnectedTextureQuad[][] connectedTextures;
		protected final TextureAtlasSprite particleTexture;
		protected final boolean ambientOcclusion;
		protected final VertexFormat format;
		protected final TRSRTransformation transformation;
		protected final ImmutableMap<TransformType, TRSRTransformation> transforms;

		private ModelBakedConnectedFace(Optional<TRSRTransformation> transformation, ImmutableMap<TransformType, TRSRTransformation> transforms, 
				VertexFormat format, TextureAtlasSprite particleTexture, boolean ambientOcclusion, List<ConnectedTextureQuad> connectedTextures) {
			this.ambientOcclusion = ambientOcclusion;
			this.format = format;
			this.transforms = transforms;
			this.transformation = transformation.isPresent() ? transformation.get() : null;
			this.particleTexture = particleTexture;

			this.connectedTextures = new ConnectedTextureQuad[EnumFacing.VALUES.length + 1][];
			List<ConnectedTextureQuad> connectedTextureQuads = new ArrayList<>();
			for(int i = 0; i < EnumFacing.VALUES.length + 1; i++) {
				EnumFacing face = i == 0 ? null : EnumFacing.VALUES[i - 1];
				connectedTextureQuads.clear();
				for(ConnectedTextureQuad tex : connectedTextures) {
					if(face == tex.cullFace) {
						connectedTextureQuads.add(tex);
					}
				}
				this.connectedTextures[i == 0 ? 0 : face.getIndex() + 1] = connectedTextureQuads.toArray(new ConnectedTextureQuad[0]);
			}
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState stateOld, EnumFacing side, long rand) {
			IExtendedBlockState state = (IExtendedBlockState) stateOld;
			ImmutableMap<IUnlistedProperty<?>, Optional<?>> properties = state.getUnlistedProperties();
			int faceIndex = side == null ? 0 : side.getIndex() + 1;
			ConnectedTextureQuad[] connectedTextures = this.connectedTextures[faceIndex];

			if(connectedTextures.length > 0) {
				ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

				for(ConnectedTextureQuad tex : connectedTextures) {
					int[] indices = new int[4];

					for(Entry<IUnlistedProperty<?>, Optional<?>> entry : properties.entrySet()) {
						String property = entry.getKey().getName();

						if(tex.indices[0].equals(property)) {
							indices[0] = (Integer) entry.getValue().get();
						} else if(tex.indices[1].equals(property)) {
							indices[1] = (Integer) entry.getValue().get();
						} else if(tex.indices[2].equals(property)) {
							indices[2] = (Integer) entry.getValue().get();
						} else if(tex.indices[3].equals(property)) {
							indices[3] = (Integer) entry.getValue().get();
						}
					}

					for(int i = 0; i < 4; i++) {
						builder.add(tex.quads[i][indices[i]]);
					}
				}

				return builder.build();
			}

			return ImmutableList.of();
		}

		@Override
		public boolean isAmbientOcclusion() {
			return this.ambientOcclusion;
		}

		@Override
		public boolean isGui3d() {
			return false;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		@Override
		public ItemCameraTransforms getItemCameraTransforms() {
			return ItemCameraTransforms.DEFAULT;
		}

		@Override
		public ItemOverrideList getOverrides() {
			return ItemOverrideList.NONE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type) {
			return PerspectiveMapWrapper.handlePerspective(this, this.transforms, type);
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return this.particleTexture;
		}
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {
		JsonParser parser = new JsonParser();

		boolean changed = false;

		List<ConnectedTextureQuad> connectedTextures = this.connectedTextures;
		if(customData.containsKey("connected_texture_faces")) {
			changed = true;
			connectedTextures = new ArrayList<>();

			JsonElement jsonObj = parser.parse(customData.get("connected_texture_faces"));
			Preconditions.checkState(jsonObj != null && !jsonObj.isJsonNull() && jsonObj.isJsonArray(), "Connected texture model must provide a connected_texture_faces array");
			JsonArray array = jsonObj.getAsJsonArray();

			for(JsonElement element : array) {
				JsonObject ctJson = element.getAsJsonObject();

				Preconditions.checkState(ctJson.has("indices") && ctJson.get("indices").isJsonArray() && ctJson.get("indices").getAsJsonArray().size() == 4, "Connected texture face must provide 4 indices");
				String[] indices = new String[4];
				JsonArray indicesArray = ctJson.get("indices").getAsJsonArray();
				for(int i = 0; i < 4; i++) {
					indices[i] = indicesArray.get(i).getAsString();
				}

				Preconditions.checkState(ctJson.has("vertices") && ctJson.get("vertices").isJsonArray() && ctJson.get("vertices").getAsJsonArray().size() == 4, "Connected texture face must provide 4 vertices");
				Vertex[] vertices = new Vertex[4];
				JsonArray verticesArray = ctJson.get("vertices").getAsJsonArray();
				for(int i = 0; i < 4; i++) {
					JsonObject vertexJson = verticesArray.get(i).getAsJsonObject();
					vertices[i] = new Vertex(vertexJson.get("x").getAsDouble(), vertexJson.get("y").getAsDouble(), vertexJson.get("z").getAsDouble(), vertexJson.get("u").getAsFloat(), vertexJson.get("v").getAsFloat());
				}

				Preconditions.checkState(ctJson.has("connected_textures") && ctJson.get("connected_textures").isJsonArray() && ctJson.get("connected_textures").getAsJsonArray().size() == 5, "Connected texture face must provide 5 textures");
				ResourceLocation[] textures = new ResourceLocation[5];
				JsonArray texturesArray = ctJson.get("connected_textures").getAsJsonArray();
				for(int i = 0; i < 5; i++) {
					textures[i] = new ResourceLocation(texturesArray.get(i).getAsString());
				}

				int tintIndex = -1;
				if(ctJson.has("tintindex")) {
					tintIndex = ctJson.get("tintindex").getAsInt();
				}

				EnumFacing cullFace = null;
				if(ctJson.has("cullface")) {
					cullFace = EnumFacing.byName(ctJson.get("cullface").getAsString());
				}

				connectedTextures.add(new ConnectedTextureQuad(textures, indices, vertices, cullFace, tintIndex));
			}
		}

		boolean ambientOcclusion = this.ambientOcclusion;
		if(customData.containsKey("ambient_occlusion")) {
			changed = true;
			ambientOcclusion = parser.parse(customData.get("ambient_occlusion")).getAsBoolean();
		}

		ResourceLocation particleTexture = this.particleTexture;
		if(customData.containsKey("particle_texture")) {
			changed = true;
			particleTexture = new ResourceLocation(parser.parse(customData.get("particle_texture")).getAsString());
		}

		if(changed) {
			return new ModelConnectedTexture(particleTexture == null ? TextureMap.LOCATION_MISSING_TEXTURE : particleTexture, ambientOcclusion, connectedTextures);
		}

		return this;
	}
}