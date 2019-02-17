package thebetweenlands.common.block.terrain;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.client.render.particle.ParticleFactory.ParticleArgs;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.registries.BlockRegistryOld.IStateMappedBlock;
import thebetweenlands.util.AdvancedStateMap;

public class BlockLeavesBetweenlands extends BlockLeaves implements IStateMappedBlock {
	private int[] decayBlockCache;

	public BlockLeavesBetweenlands() {
		super();
		setCreativeTab(BLCreativeTabs.BLOCKS);
		setDefaultState(blockState.getBaseState().with(CHECK_DECAY, true).with(DECAYABLE, true));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IWorldReader blockAccess, BlockPos pos, EnumFacing side) {
		return !Minecraft.getInstance().gameSettings.fancyGraphics && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : internalShouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {
		super.beginLeavesDecay(state, world, pos);
	}

	@OnlyIn(Dist.CLIENT)
	private boolean internalShouldSideBeRendered(IBlockState blockState, IWorldReader blockAccess, BlockPos pos, EnumFacing side) {
		AxisAlignedBB axisalignedbb = blockState.getBoundingBox(blockAccess, pos);
		switch (side) {
			case DOWN:
				if (axisalignedbb.minY > 0.0D) return true;
				break;
			case UP:
				if (axisalignedbb.maxY < 1.0D) return true;
				break;
			case NORTH:
				if (axisalignedbb.minZ > 0.0D) return true;
				break;
			case SOUTH:
				if (axisalignedbb.maxZ < 1.0D) return true;
				break;
			case WEST:
				if (axisalignedbb.minX > 0.0D) return true;
				break;
			case EAST:
				if (axisalignedbb.maxX < 1.0D) return true;
		}
		return !blockAccess.getBlockState(pos.offset(side)).doesSideBlockRendering(blockAccess, pos.offset(side), side.getOpposite());
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return Blocks.LEAVES.isOpaqueCube(state);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return Blocks.LEAVES.getRenderLayer();
	}

	@Override
	public NonNullList<ItemStack> onSheared(ItemStack item, IWorldReader world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().with(DECAYABLE, (meta & 4) == 0).with(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).with(DECAYABLE, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		if (!state.get(DECAYABLE))
			i |= 4;
		if (state.get(CHECK_DECAY))
			i |= 8;
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}

	@Override
	public EnumType getWoodType(int meta) {
		return EnumType.OAK;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(world.rand.nextInt(160) == 0) {
			if(world.isAirBlock(pos.down())) {
				BLParticles.WEEDWOOD_LEAF.spawn(world, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), ParticleArgs.get().withScale(1.0F + rand.nextFloat() * 1.25F));
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setStateMapper(AdvancedStateMap.Builder builder) {
		builder.ignore(BlockLeavesBetweenlands.CHECK_DECAY, BlockLeavesBetweenlands.DECAYABLE);		
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			if (state.get(CHECK_DECAY) && state.get(DECAYABLE)) {
				byte logReach = 5;
				int checkRadius = logReach + 1;
				byte cacheSize = 32;
				int cacheSquared = cacheSize * cacheSize;
				int cacheHalf = cacheSize / 2;

				if (this.decayBlockCache == null) {
					this.decayBlockCache = new int[cacheSize * cacheSize * cacheSize];
				}

				//states:
				//0: can sustain leaves
				//-1: can't sustain leaves
				//-2: is leaves block

				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				if (worldIn.isAreaLoaded(new BlockPos(x - checkRadius, y - checkRadius, z - checkRadius), new BlockPos(x + checkRadius, y + checkRadius, z + checkRadius))) {
					BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

					//Pupulate block cache
					for (int xo = -logReach; xo <= logReach; ++xo) {
						for (int yo = -logReach; yo <= logReach; ++yo) {
							for (int zo = -logReach; zo <= logReach; ++zo) {
								IBlockState blockState = worldIn.getBlockState(mutableBlockPos.setPos(x + xo, y + yo, z + zo));
								Block block = blockState.getBlock();

								if (!block.canSustainLeaves(blockState, worldIn, mutableBlockPos.setPos(x + xo, y + yo, z + zo))) {
									if (block.isLeaves(blockState, worldIn, mutableBlockPos.setPos(x + xo, y + yo, z + zo))) {
										this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] = -2;
									} else {
										this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] = -1;
									}
								} else {
									this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] = 0;
								}
							}
						}
					}

					//Iterate multiple times over the block cache
					for (int distancePass = 1; distancePass <= logReach; ++distancePass) {
						for (int xo = -logReach; xo <= logReach; ++xo) {
							for (int yo = -logReach; yo <= logReach; ++yo) {
								for (int zo = -logReach; zo <= logReach; ++zo) {
									//If value != distancePass - 1 then it's not connected to a log
									if (this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] == distancePass - 1) {
										//Check for adjacent leaves and set their value to the current distance pass

										if (this.decayBlockCache[(xo + cacheHalf - 1) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] == -2) {
											this.decayBlockCache[(xo + cacheHalf - 1) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] = distancePass;
										}

										if (this.decayBlockCache[(xo + cacheHalf + 1) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] == -2) {
											this.decayBlockCache[(xo + cacheHalf + 1) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf] = distancePass;
										}

										if (this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf - 1) * cacheSize + zo + cacheHalf] == -2) {
											this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf - 1) * cacheSize + zo + cacheHalf] = distancePass;
										}

										if (this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf + 1) * cacheSize + zo + cacheHalf] == -2) {
											this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf + 1) * cacheSize + zo + cacheHalf] = distancePass;
										}

										if (this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + (zo + cacheHalf - 1)] == -2) {
											this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + (zo + cacheHalf - 1)] = distancePass;
										}

										if (this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf + 1] == -2) {
											this.decayBlockCache[(xo + cacheHalf) * cacheSquared + (yo + cacheHalf) * cacheSize + zo + cacheHalf + 1] = distancePass;
										}
									}
								}
							}
						}
					}
				}

				//Get distance to log at center block
				int distanceToLog = this.decayBlockCache[cacheHalf * cacheSquared + cacheHalf * cacheSize + cacheHalf];

				if (distanceToLog >= 0) {
					worldIn.setBlockState(pos, state.with(CHECK_DECAY, Boolean.FALSE), 4);
				} else {
					this.removeLeaves(worldIn, pos);
				}
			}
		}
	}

	protected void removeLeaves(World world, BlockPos pos) {
		this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
		world.setBlockToAir(pos);
	}
}