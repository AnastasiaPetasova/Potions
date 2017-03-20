package com.anastasia.potions.game.creating;

import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.util.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class RecipeBuildChecker implements RecipeBuildConstants {

    private final Map<Recipe, Integer> need, taken;

    private final int needSize;
    private int takenSize;

    public static RecipeBuildChecker create(Recipe buildingRecipe) {
        Map<Recipe, Integer> need = new HashMap<>();
        int needSize = 0;

        for (Recipe recipe : buildingRecipe.mixedRecipes) {
            MapUtils.increase(need, recipe);
            ++needSize;
        }

        Map<Recipe, Integer> taken = new HashMap<>();
        int takenSize = 0;

        return new RecipeBuildChecker(need, taken, needSize, takenSize);
    }

    private RecipeBuildChecker(Map<Recipe, Integer> need, Map<Recipe, Integer> taken,
                               int needSize, int takenSize) {
        this.need = need;
        this.taken = taken;

        this.needSize = needSize;
        this.takenSize = takenSize;
    }

    public boolean canBuild() {
        return needSize == takenSize;
    }

    public int tryTake(Recipe recipe) {
        int needCount = MapUtils.getCount(need, recipe);
        int takenCount = MapUtils.getCount(taken, recipe);

        if (0 == needCount) return NOT_NEEDED;
        if (takenCount >= needCount) return ALREADY_FULL;

        MapUtils.increase(taken, recipe);
        ++takenSize;

        return TAKEN;
    }

    public void removeFromBuild(Recipe recipe) {
        int takenCount = MapUtils.getCount(taken, recipe);
        if (takenCount > 0) {
            MapUtils.decrease(taken, recipe);
            --takenSize;
        }
    }
}
