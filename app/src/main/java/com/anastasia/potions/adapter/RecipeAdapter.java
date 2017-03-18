package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.view.CardView;

public class RecipeAdapter extends GameListAdapter<Recipe, CardView> {

    public RecipeAdapter(Context context) {
        super(context);
    }

    @Override
    protected CardView createView(Recipe recipe) {
        CardView cardView = new CardView(context);
        cardView.setRecipe(recipe);

        return cardView;
    }
}
