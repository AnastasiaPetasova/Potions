package com.anastasia.potions.game.creating;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.created.CreatedObject;
import com.anastasia.potions.util.MultiSet;

import java.util.List;

public class RecipeCreatingResult {

    public final Card createdCard;
    public final List<CreatedObject> takenObjects;
    public final MultiSet<Recipe> takenRecipeCounts;

    public RecipeCreatingResult(Card createdCard, List<CreatedObject> takenObjects, MultiSet<Recipe> takenRecipeCounts) {
        this.createdCard = createdCard;
        this.takenObjects = takenObjects;
        this.takenRecipeCounts = takenRecipeCounts;
    }
}
