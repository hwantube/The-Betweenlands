package thebetweenlands.client.render.tileentity;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import thebetweenlands.blocks.BLBlockRegistry;
import thebetweenlands.entities.particles.EntityWispFX;
import thebetweenlands.event.render.WispHandler;
import thebetweenlands.tileentities.TileEntityWisp;
import thebetweenlands.utils.confighandler.ConfigHandler;

import com.sun.javafx.geom.Vec3d;

public class TileEntityWispRenderer extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
		WispHandler.INSTANCE.tileList.add(new SimpleEntry(new SimpleEntry(this, tileEntity), new Vector3d(x, y, z)));
		
		ArrayList<Object> particleList = ((TileEntityWisp)tileEntity).particleList;

		double dist = Minecraft.getMinecraft().renderViewEntity.getDistance(x + RenderManager.renderPosX, y + RenderManager.renderPosY, z + RenderManager.renderPosZ);
		if(dist > 50 || dist < 10) {
			return;
		}

		if(System.nanoTime() - ((TileEntityWisp)tileEntity).lastSpawn >= (500f - 500.0f * ConfigHandler.WISP_QUALITY / 100.0f) * 1000000L) {
			((TileEntityWisp)tileEntity).lastSpawn = System.nanoTime();

			int wispMeta = tileEntity.getBlockMetadata();

			particleList.add(new EntityWispFX(
					tileEntity.getWorldObj(), 
					x + 0.5 + RenderManager.renderPosX, 
					y + 0.5F +RenderManager.renderPosY, 
					z + 0.5 + RenderManager.renderPosZ,
					0.0D, 0.0D, 0.0D,
					3.0F, 50, BLBlockRegistry.wisp.colors[wispMeta]));

			particleList.add(new EntityWispFX(
					tileEntity.getWorldObj(), 
					x + 0.5 + RenderManager.renderPosX, 
					y + 0.5F + RenderManager.renderPosY, 
					z + 0.5 + RenderManager.renderPosZ,
					0.0D, 0.0D, 0.0D,
					2.0F, 50, BLBlockRegistry.wisp.colors[wispMeta + 1]));
		}
	}

	public void doRender(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
		ArrayList<Object> particleList = ((TileEntityWisp)tileEntity).particleList;

		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		Minecraft.getMinecraft().getTextureManager().bindTexture(EntityWispFX.TEXTURE);
		for(Object particle : particleList){
			((EntityWispFX)particle).renderParticle(tessellator, partialTicks, 
					ActiveRenderInfo.rotationX,
					ActiveRenderInfo.rotationXZ,
					ActiveRenderInfo.rotationZ,
					ActiveRenderInfo.rotationYZ,
					ActiveRenderInfo.rotationXY);
		}
		tessellator.draw();

		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}
}
