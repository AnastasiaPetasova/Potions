package com.anastasia.potions.game.creating;

import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.cupboard.Cupboard;
import com.anastasia.potions.util.MultiSet;

public class RecipeCreatingChecker implements RecipeCreatingConstants {

    private final MultiSet<Recipe> need, taken;

    public static RecipeCreatingChecker create(Recipe buildingRecipe, Cupboard cupboard)
            throws NotEnoughIngredientsException {
        MultiSet<Recipe> need = new MultiSet<>();
        for (Recipe recipe : buildingRecipe.mixedRecipes) {
            need.add(recipe);
        }

        MultiSet<Recipe> taken = new MultiSet<>();
        for (Recipe recipe : buildingRecipe.mixedRecipes) {
            if (recipe.isIngredient()) {
                int needCount = need.getCount(recipe);
                int canBeTakenCount = cupboard.getCardsCountFor(recipe);

                if (canBeTakenCount < needCount) {
                    throw new NotEnoughIngredientsException();
                } else {
                    taken.add(recipe);
                }
            }
        }

        return new RecipeCreatingChecker(need, taken);
    }

    private RecipeCreatingChecker(MultiSet<Recipe> need, MultiSet<Recipe> taken) {
        this.need = need;
        this.taken = taken;
    }

    public boolean canCreate() {
        return need.size() == taken.size();
    }

    int tryTake(Recipe recipe) {
        int needCount = need.getCount(recipe);
        int takenCount = taken.getCount(recipe);

        if (0 == needCount) return NOT_NEEDED;
        if (takenCount >= needCount) return ALREADY_FULL;

        taken.add(recipe);

        return TAKEN;
    }

    void removeFromBuild(Recipe recipe) {
        taken.remove(recipe);
    }

    MultiSet<Recipe> getTaken() {
        return taken;
    }
}
