package com.anastasia.potions.card;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum Recipe implements Iterable<Recipe> {

    WATER(), FIRE(), TOOTH(),
    ICE(2, WATER, WATER), SUN(2, FIRE, FIRE),
    PRISM(4, ICE, SUN);

    public final int score;
    public final List<Recipe> mixedRecipes;

    Recipe() {
        this(1);
    }

    Recipe(int score, Recipe... recipes) {
        this.score = score;
        this.mixedRecipes = Arrays.asList(recipes);
    }

    public String getLocaleName() {
        return RecipeLocalization.getLocalName(this);
    }

    public boolean isIngredient() {
        return mixedRecipes.isEmpty();
    }

    @Override
    public Iterator<Recipe> iterator() {
        return mixedRecipes.iterator();
    }
}
