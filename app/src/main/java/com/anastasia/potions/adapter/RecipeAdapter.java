package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.view.RecipeView;

public class RecipeAdapter extends GameListAdapter<Recipe, RecipeView> {

    public RecipeAdapter(Context context) {
        super(context);
    }

    @Override
    protected RecipeView createView(Recipe recipe) {
        RecipeView recipeView = new RecipeView(context);
        recipeView.setRecipe(recipe);

        return recipeView;
    }
}
