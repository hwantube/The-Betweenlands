package thebetweenlands.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import thebetweenlands.common.capability.base.AbstractCapability;

public class NBTHelper {
	/**
	 * Returns the ItemStack NBT and creates a new NBTTagCompound if necessary
	 * @param stack
	 * @return
	 */
	public static NBTTagCompound getStackNBTSafe(ItemStack stack) {
		if(stack.getTag() == null)
			stack.setTagCompound(new NBTTagCompound());
		return stack.getTag();
	}

	/**
	 * Returns <tt>true</tt> if the two specifies item stacks' NBT
	 * compound tags are <i>equal</i> to one another.
	 * 
	 * @param a one NBT compound tag to be tested for equality
	 * @param b the other NBT compound tag to be tested for equality
	 * @param exclusions a list of capabilities to be excluded in checking equality
	 * @return <tt>true</tt> if the two NBT compounds tags are equal
	 * @see #areNBTCompoundsEquals(NBTTagCompound, NBTTagCompound, List)
	 */
	public static boolean areItemStackTagsEqualWithoutCapabilities(ItemStack a, ItemStack b, AbstractCapability<?, ?, ?>... exclusions) {
		List<String> strExclusions = new ArrayList<String>();
		for(AbstractCapability<?, ?, ?> exclusion : exclusions) {
			strExclusions.add("ForgeCaps." + exclusion.getID().toString());
		}
		return areItemStackTagsEqual(a, b, strExclusions);
	}
	
	/**
	 * Returns <tt>true</tt> if the two specifies item stacks' NBT
	 * compound tags are <i>equal</i> to one another.
	 * 
	 * @param a one NBT compound tag to be tested for equality
	 * @param b the other NBT compound tag to be tested for equality
	 * @param exclusions a list of tags to be excluded in checking equality
	 * @return <tt>true</tt> if the two NBT compounds tags are equal
	 * @see #areNBTCompoundsEquals(NBTTagCompound, NBTTagCompound, List)
	 */
	public static boolean areItemStackTagsEqual(ItemStack a, ItemStack b, Collection<String> exclusions) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null && b != null) {
			if (a.getTag() == null && b.getTag() == null) {
				return true;
			}
			if (a.getTag() == null ^ b.getTag() == null) {
				return false;
			}
			return areNBTCompoundsEquals(a.getTag(), b.getTag(), exclusions);
		}
		return false;
	}

	/**
	 * Returns <tt>true</tt> if the two specified NBT compound tags
	 * are <i>equal</i> to one another. Two NBT compound tags are
	 * considered equal if both NBT compounds tags contain all of
	 * the same keys with the same values, while ignoring tags
	 * whose keys are in the exclusions list.
	 * 
	 * @param a one NBT compound tag to be tested for equality
	 * @param b the other NBT compound tag to be tested for equality
	 * @param exclusions a list of tags to be excluded in checking equality
	 * @return <tt>true</tt> if the two NBT compounds tags are equal
	 */
	public static boolean areNBTCompoundsEquals(NBTTagCompound a, NBTTagCompound b, Collection<String> exclusions) {
		Stack<String> tagOwners = new Stack<String>();
		Stack<NBTTagCompound> aTagCompounds = new Stack<NBTTagCompound>();
		Stack<NBTTagCompound> bTagCompounds = new Stack<NBTTagCompound>();
		tagOwners.push("");
		aTagCompounds.push(a);
		bTagCompounds.push(b);
		while (!aTagCompounds.isEmpty()) {
			String tagOwner = tagOwners.pop();
			NBTTagCompound aCurrentTagCompound = aTagCompounds.pop();
			NBTTagCompound bCurrentTagCompound = bTagCompounds.pop();
			Set<String> aKeys = aCurrentTagCompound.getKeySet();
			Set<String> bKeys = bCurrentTagCompound.getKeySet();
			for (String key : bKeys) {
				if (exclusions.contains(key)) {
					continue;
				}
				if (!aKeys.contains(key)) {
					return false;
				}
			}
			for (String key : aKeys) {
				String totalKey = tagOwner == "" ? key : tagOwner + '.' + key;
				if (exclusions.contains(totalKey)) {
					continue;
				}
				NBTBase aTag = aCurrentTagCompound.getTag(key);
				NBTBase bTag = bCurrentTagCompound.getTag(key);
				if (aTag instanceof NBTTagCompound && bTag instanceof NBTTagCompound) {
					tagOwners.push(totalKey);
					aTagCompounds.push((NBTTagCompound) aTag);
					bTagCompounds.push((NBTTagCompound) bTag);
				} else {
					if (!aTag.equals(bTag)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
