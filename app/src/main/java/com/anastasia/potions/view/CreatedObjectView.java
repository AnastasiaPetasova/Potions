package com.anastasia.potions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.game.CreatedObject;

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
        CardView recipeCard = getRecipeView();
        recipeCard.setRecipe(createdObject.baseRecipe);

        getPlayerNameView().setText(createdObject.player.name);
    }

    public CardView getRecipeView() {
        return (CardView)findViewById(R.id.recipe_view);
    }

    public TextView getPlayerNameView() { return (TextView) findViewById(R.id.player_name_view); }
}
