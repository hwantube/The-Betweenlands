package thebetweenlands.compat.jei.recipes.misc;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.compat.jei.BetweenlandsJEIPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CoatingRecipeJEI implements ICraftingRecipeWrapper, ICustomCraftingRecipeWrapper {

    private static List<ItemStack> coatableItems = NonNullList.create();
    private final ICraftingGridHelper craftingGridHelper;

    public CoatingRecipeJEI(IGuiHelper guiHelper) {
        craftingGridHelper = guiHelper.createCraftingGridHelper(1, 0);
    }

    public static void setCoatableItems(IIngredientRegistry registry) {
        coatableItems.clear();
        for (ItemStack stack: registry.getAllIngredients(ItemStack.class)) {
            if (!stack.isEmpty() && stack.getItem() instanceof ICorrodible)
                coatableItems.add(stack);
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputLists = new ArrayList<>();
        List<List<ItemStack>> outputLists = new ArrayList<>();
        ItemStack scabyst = ItemMisc.EnumItemMisc.SCABYST.create(1);

        List<ItemStack> inputs = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack stack: coatableItems) {
            ItemStack coated = stack.copy();
            ((ICorrodible) coated.getItem()).setCoating(coated, ((ICorrodible)coated.getItem()).getMaxCoating(coated));

            inputs.add(stack);
            outputs.add(coated);
        }
        inputLists.add(inputs);
        outputLists.add(outputs);

        for (int i = 0; i < 8; i++)
            inputLists.add(Collections.singletonList(scabyst));

        ingredients.setInputLists(ItemStack.class, inputLists);
        ingredients.setOutputLists(ItemStack.class, outputLists);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IIngredients ingredients) {
        IStackHelper stackHelper = BetweenlandsJEIPlugin.jeiHelper.getStackHelper();
        recipeLayout.setShapeless();

        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IFocus focus = recipeLayout.getFocus();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

        if (focus != null && focus.getValue() instanceof ItemStack && ((ItemStack) focus.getValue()).getItem() instanceof ICorrodible) {
            if (focus.getMode() == IFocus.Mode.INPUT) {
                outputs.get(0).removeIf(stack -> !stackHelper.isEquivalent((ItemStack) focus.getValue(), stack));
            } else {
                inputs.get(0).removeIf(stack -> !stackHelper.isEquivalent((ItemStack) focus.getValue(), stack));
            }
        }

        craftingGridHelper.setInputs(guiItemStacks, inputs);
        guiItemStacks.set(0, outputs.get(0));
    }
}