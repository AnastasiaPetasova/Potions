package com.anastasia.potions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.anastasia.potions.R;
import com.anastasia.potions.card.Card;

public class CardView extends LinearLayout {

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_card, this);
    }

    public void setCard(Card card) {
        RecipeView ingredientCard = getIngredientView();
        ingredientCard.setRecipe(card.ingredient);

        RecipeView complexRecipeCard = getComplexRecipeView();
        complexRecipeCard.setRecipe(card.complexRecipe);
    }

    @Override
    public void setBackgroundResource(int resid) {
        getLayout().setBackgroundResource(resid);
    }

    public LinearLayout getLayout() { return (LinearLayout) findViewById(R.id.card_view_layout); }

    public RecipeView getIngredientView() {
        return (RecipeView)findViewById(R.id.ingredient_view);
    }

    public RecipeView getComplexRecipeView() {
        return (RecipeView)findViewById(R.id.created_objects_view);
    }
}
