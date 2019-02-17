package thebetweenlands.compat.jei.recipes.purifier;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import thebetweenlands.api.recipes.IPurifierRecipe;
import thebetweenlands.common.recipe.purifier.PurifierRecipe;
import thebetweenlands.common.recipe.purifier.PurifierRecipeStandard;

public class PurifierRecipeMaker {

    @Nonnull
    public static List<PurifierRecipeJEI> getRecipes() {
        ArrayList<PurifierRecipeJEI> recipes = new ArrayList<PurifierRecipeJEI>();
        for (IPurifierRecipe recipe : PurifierRecipe.getRecipeList()) {
            if (recipe instanceof PurifierRecipeStandard)
                recipes.add(new PurifierRecipeJEI((PurifierRecipeStandard) recipe));
        }
        return recipes;
    }
}
