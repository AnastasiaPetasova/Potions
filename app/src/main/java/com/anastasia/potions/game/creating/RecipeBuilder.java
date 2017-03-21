package com.anastasia.potions.game.creating;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.game.created.CreatedObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecipeBuilder implements RecipeCreatingConstants {

    private final Card creatingCard;
    private final RecipeCreatingChecker recipeCreatingChecker;

    private final Set<CreatedObject> takenObjects;

    public RecipeBuilder(Card creatingCard, RecipeCreatingChecker recipeCreatingChecker) {
        this.creatingCard = creatingCard;
        this.recipeCreatingChecker = recipeCreatingChecker;

        this.takenObjects = new HashSet<>();
    }

    public int tryTake(CreatedObject createdObject) {
        int takingResult = recipeCreatingChecker.tryTake(createdObject.getBaseRecipe());
        if (TAKEN == takingResult) {
            takenObjects.add(createdObject);
        }

        return takingResult;
    }

    public void removeFromBuild(CreatedObject createdObject) {
        recipeCreatingChecker.removeFromBuild(createdObject.getBaseRecipe());
    }

    public boolean canCreate() {
        return recipeCreatingChecker.canCreate();
    }

    public RecipeCreatingResult create() {
        if (canCreate()) {
            return new RecipeCreatingResult(
                    creatingCard,
                    new ArrayList<>(takenObjects),
                    recipeCreatingChecker.getTaken()
            );
        } else {
            return null;
        }
    }
}
