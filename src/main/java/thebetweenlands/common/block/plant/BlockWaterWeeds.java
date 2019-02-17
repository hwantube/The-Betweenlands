package thebetweenlands.common.block.plant;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.common.item.herblore.ItemPlantDrop.EnumItemPlantDrop;

public class BlockWaterWeeds extends BlockPlantUnderwater {
	public BlockWaterWeeds() {
		this.setSickleDrop(EnumItemPlantDrop.WATER_WEEDS_ITEM.create(1)).setReplaceable(true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (world.rand.nextInt(120) == 0) {
			BLParticles.WATER_BUG.spawn(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		}
	}
}
