package thebetweenlands.api.item;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import thebetweenlands.util.NBTHelper;

public interface ICorrodible {
	/**
	 * Returns an array of item variants that use a corroded texture
	 * @return
	 */
	@OnlyIn(Dist.CLIENT)
	@Nullable
	default ResourceLocation[] getCorrodibleVariants() {
		return null;
	}

	/**
	 * Returns an arry of item variants that use a corroded texture. If the item
	 * doesn't specify any the default variant is returned
	 * @param item
	 * @return
	 */
	public static <I extends Item & ICorrodible> ResourceLocation[] getItemCorrodibleVariants(I item) {
		ResourceLocation[] variants = item.getCorrodibleVariants();
		if (variants == null) {
			return new ResourceLocation[] { item.getRegistryName() };
		}
		return variants;
	}

	/**
	 * Returns the maximum amount of coating for the specified item stack
	 * @param stack
	 * @return
	 */
	default int getMaxCoating(ItemStack stack) {
		return 600;
	}

	/**
	 * Returns the maximum amount of corrosion for the specified item stack
	 * @param stack
	 * @return
	 */
	default int getMaxCorrosion(ItemStack stack) {
		return 255;
	}

	/**
	 * Returns the amount of coating on the specified item stack
	 * @param stack
	 * @return
	 */
	default int getCoating(ItemStack stack) {
		NBTTagCompound nbt = stack.getTag();
		if(nbt != null && nbt.contains(CorrosionHelper.ITEM_COATING_NBT_TAG, Constants.NBT.TAG_INT)) {
			return nbt.getInt(CorrosionHelper.ITEM_COATING_NBT_TAG);
		}
		return 0;
	}

	/**
	 * Returns the amount of corrosion on the specified item stack
	 * @param stack
	 * @return
	 */
	default int getCorrosion(ItemStack stack) {
		NBTTagCompound nbt = stack.getTag();
		if(nbt != null && nbt.contains(CorrosionHelper.ITEM_CORROSION_NBT_TAG, Constants.NBT.TAG_INT)) {
			return nbt.getInt(CorrosionHelper.ITEM_CORROSION_NBT_TAG);
		}
		return 0;
	}

	/**
	 * Sets the amount of coating of the specified item stack
	 * @param itemStack
	 * @param coating
	 */
	default void setCoating(ItemStack stack, int coating) {
		NBTTagCompound nbt = NBTHelper.getStackNBTSafe(stack);
		nbt.setInt(CorrosionHelper.ITEM_COATING_NBT_TAG, coating);
	}

	/**
	 * Sets the amount of corrosion of the specified item stack
	 * @param stack
	 * @param corrosion
	 */
	default void setCorrosion(ItemStack stack, int corrosion) {
		NBTTagCompound nbt = NBTHelper.getStackNBTSafe(stack);
		nbt.setInt(CorrosionHelper.ITEM_CORROSION_NBT_TAG, corrosion);
	}
}
