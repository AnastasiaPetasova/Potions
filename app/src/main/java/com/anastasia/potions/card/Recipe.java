package com.anastasia.potions.card;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum Recipe implements Iterable<Recipe> {

    WATER(), ICE(), FIRE(), SUN(),
    HATE(FIRE, ICE), STEAM(WATER, SUN);

    List<Recipe> mixedRecipes;

    Recipe(Recipe... recipes) {
        this.mixedRecipes = Arrays.asList(recipes);
    }

    public boolean isIngredient() {
        return mixedRecipes.isEmpty();
    }

    @Override
    public Iterator<Recipe> iterator() {
        return mixedRecipes.iterator();
    }
}
