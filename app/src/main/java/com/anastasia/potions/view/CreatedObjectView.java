package com.anastasia.potions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.game.creating.CreatedObject;

public class CreatedObjectView extends LinearLayout {

    public CreatedObjectView(Context context) {
        super(context);
        init();
    }

    public CreatedObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreatedObjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_created_object, this);
    }

    public void setCreatedObject(CreatedObject createdObject) {
        RecipeView recipeCard = getRecipeView();
        recipeCard.setRecipe(createdObject.getBaseRecipe());

        getPlayerNameView().setText(createdObject.getPlayer().getName());
    }

    public RecipeView getRecipeView() {
        return (RecipeView)findViewById(R.id.recipe_view);
    }

    public TextView getPlayerNameView() { return (TextView) findViewById(R.id.player_name_view); }
}
