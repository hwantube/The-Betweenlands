package thebetweenlands.common.block.terrain;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.block.BasicBlock;
import thebetweenlands.common.block.property.BooleanPropertyUnlisted;
import thebetweenlands.common.block.property.IntegerPropertyUnlisted;

public class BlockStalactite extends BasicBlock {
	public static final BooleanPropertyUnlisted NO_BOTTOM = new BooleanPropertyUnlisted("no_bottom");
	public static final BooleanPropertyUnlisted NO_TOP = new BooleanPropertyUnlisted("no_top");
	public static final IntegerPropertyUnlisted DIST_UP = new IntegerPropertyUnlisted("dist_up");
	public static final IntegerPropertyUnlisted DIST_DOWN = new IntegerPropertyUnlisted("dist_down");
	public static final IntegerPropertyUnlisted POS_X = new IntegerPropertyUnlisted("pos_x");
	public static final IntegerPropertyUnlisted POS_Y = new IntegerPropertyUnlisted("pos_x");
	public static final IntegerPropertyUnlisted POS_Z = new IntegerPropertyUnlisted("pos_z");

	public BlockStalactite() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setCreativeTab(BLCreativeTabs.BLOCKS);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {POS_X, POS_Y, POS_Z, NO_BOTTOM, NO_TOP, DIST_UP, DIST_DOWN});
	}

	@Override
	public boolean isBlockNormalCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getExtendedState(IBlockState oldState, IWorldReader worldIn, BlockPos pos) {
		IExtendedBlockState state = (IExtendedBlockState) oldState;

		final int maxLength = 32;
		int distUp = 0;
		int distDown = 0;
		boolean noTop = false;
		boolean noBottom = false;

		IBlockState blockState;
		//Block block;
		for(distUp = 0; distUp < maxLength; distUp++) {
			blockState = worldIn.getBlockState(pos.add(0, 1 + distUp, 0));
			if(blockState.getBlock() == this)
				continue;
			if(blockState.getBlock() == Blocks.AIR || !blockState.isOpaqueCube())
				noTop = true;
			break;
		}
		for(distDown = 0; distDown < maxLength; distDown++)
		{
			blockState = worldIn.getBlockState(pos.add(0, -(1 + distDown), 0));
			if(blockState.getBlock() == this)
				continue;
			if(blockState.getBlock() == Blocks.AIR || !blockState.isOpaqueCube())
				noBottom = true;
			break;
		}

		return state.withProperty(POS_X, pos.getX()).withProperty(POS_Y, pos.getY()).withProperty(POS_Z, pos.getZ()).withProperty(DIST_UP, distUp).withProperty(DIST_DOWN, distDown).withProperty(NO_TOP, noTop).withProperty(NO_BOTTOM, noBottom);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
	@Override
    public BlockFaceShape getBlockFaceShape(IWorldReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    	return BlockFaceShape.UNDEFINED;
    }
}
