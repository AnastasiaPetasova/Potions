package com.anastasia.potions.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.adapter.GameListAdapter;
import com.anastasia.potions.adapter.RecipeAdapter;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.view.CardView;

import org.lucasr.twowayview.TwoWayView;

public class CardInfoActivity extends Activity implements CardInfoIntentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        Intent cardsInfo = getIntent();
        showInfo(cardsInfo);
    }

    private void showInfo(Intent cardsInfo) {
        showOwnerInfo(cardsInfo);
        showIngredientInfo(cardsInfo);
        showRecipeInfo(cardsInfo);
    }

    private void showOwnerInfo(Intent cardsInfo) {
        String ownerText = cardsInfo.getStringExtra(OWNER);
        getOwnerView().setText(ownerText);
    }

    private void showIngredientInfo(Intent cardsInfo) {
        Recipe ingredient = Recipe.valueOf(cardsInfo.getStringExtra(INGREDIENT));
        getIngredientInfoView().setRecipe(ingredient);
    }

    private void showRecipeInfo(Intent cardsInfo) {
        Recipe complexRecipe = Recipe.valueOf(cardsInfo.getStringExtra(RECIPE));
        getRecipeInfoView().setRecipe(complexRecipe);

        getRecipeNeedsView().setAdapter(
                new RecipeAdapter(this)
        );

        GameListAdapter.setValues(
                getRecipeNeedsView().getAdapter(),
                complexRecipe.mixedRecipes
        );
    }

    private TextView getOwnerView() {
        return (TextView) findViewById(R.id.owner_text_view);
    }

    private CardView getIngredientInfoView() {
        return (CardView) findViewById(R.id.ingredient_info_view);
    }

    private CardView getRecipeInfoView() {
        return (CardView) findViewById(R.id.recipe_info_view);
    }

    private TwoWayView getRecipeNeedsView() {
        return (TwoWayView) findViewById(R.id.recipe_needs_view);
    }
}
