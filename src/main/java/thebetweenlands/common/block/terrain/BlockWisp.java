package thebetweenlands.common.block.terrain;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.item.BLMaterialRegistry;
import thebetweenlands.common.registries.BlockRegistryOld.IStateMappedBlock;
import thebetweenlands.common.tile.TileEntityWisp;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thebetweenlands.common.world.storage.location.LocationCragrockTower;
import thebetweenlands.common.world.storage.location.LocationSpiritTree;
import thebetweenlands.util.AdvancedStateMap.Builder;

public class BlockWisp extends BlockContainer implements IStateMappedBlock {
	protected static final AxisAlignedBB WISP_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);

	public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 3);

	public BlockWisp() {
		super(BLMaterialRegistry.WISP);
		this.setDefaultState(this.getBlockState().getBaseState().with(COLOR, 0));
		setSoundType(SoundType.STONE);
		setCreativeTab(BLCreativeTabs.BLOCKS);
		setHardness(0);
	}

	public static boolean canSee(World world, BlockPos pos) {
		BetweenlandsWorldStorage worldStorage = BetweenlandsWorldStorage.forWorld(world);
		if(worldStorage.getEnvironmentEventRegistry().auroras.isActive()) {
			return true;
		}
		if(!worldStorage.getLocalStorageHandler().getLocalStorages(LocationCragrockTower.class, pos.getX(), pos.getZ(), location -> location.isInside(pos)).isEmpty()) {
			return true;
		}
		if(!worldStorage.getLocalStorageHandler().getLocalStorages(LocationSpiritTree.class, pos.getX(), pos.getZ(), location -> location.isInside(pos)).isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{COLOR});
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IWorldReader source, BlockPos pos) {
		return WISP_AABB;
	}

	@SuppressWarnings("deprecation")
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end){
		if(canSee(world, pos))
			return super.collisionRayTrace(blockState, world, pos, start, end);
		return null;
	}

	@Override
	public IItemProvider getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
		return Items.AIR;
	}

	@Override
	public void onPlayerDestroy(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote && canSee(world, pos)) {
			EntityItem wispItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(Item.getItemFromBlock(this), 1));
			world.spawnEntity(wispItem);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityWisp();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.setBlockState(pos, this.getDefaultState().with(COLOR, world.rand.nextInt(COLORS.length / 2)), 2);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IWorldReader worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isReplaceable(IWorldReader worldIn, BlockPos pos) {
		return true;
	}

	// Colors can be added here, always add a pair of colors for outer color and inner color
	public static final int[] COLORS = new int[] {
			0xFF7F1659, 0xFFFFFFFF, // Pink/White
			0xFF0707C8, 0xFFC8077B, // Blue/Pink
			0xFF0E2E0B, 0xFFC8077B, // Green/Yellow/White
			0xFF9A6908, 0xFF4F0303 // Red/Yellow/White
	};

	/**
	 * Sets the block at the giving position to a wisp block with a random color
	 *
	 * @param world
	 * @param pos
	 */
	public void generateBlock(World world, BlockPos pos) {
		world.setBlockState(pos, this.getDefaultState().with(COLOR, world.rand.nextInt(COLORS.length / 2)), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().with(COLOR, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.get(COLOR);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setStateMapper(Builder builder) {
		builder.ignore(COLOR);
	}
	
	@Override
    public BlockFaceShape getBlockFaceShape(IWorldReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    	return BlockFaceShape.UNDEFINED;
    }
}
