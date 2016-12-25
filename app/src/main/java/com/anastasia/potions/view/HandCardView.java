package com.anastasia.potions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.anastasia.potions.R;
import com.anastasia.potions.card.Card;

public class HandCardView extends LinearLayout {

    public HandCardView(Context context) {
        super(context);
        init();
    }

    public HandCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HandCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_hand_card, this);
    }

    public void setCard(Card card) {
        CardView ingredientCard = getIngredientView();
        ingredientCard.setRecipe(card.ingredient);

        CardView complexRecipeCard = getComplexRecipeView();
        complexRecipeCard.setRecipe(card.complexRecipe);
    }

    public CardView getIngredientView() {
        return (CardView)findViewById(R.id.ingredient_view);
    }

    public CardView getComplexRecipeView() {
        return (CardView)findViewById(R.id.created_objects_view);
    }
}
