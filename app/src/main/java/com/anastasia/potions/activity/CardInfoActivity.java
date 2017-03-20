package com.anastasia.potions.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.adapter.GameListAdapter;
import com.anastasia.potions.adapter.CardAdapter;
import com.anastasia.potions.adapter.RecipeAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.view.RecipeView;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;
import java.util.Locale;

public class CardInfoActivity extends Activity implements CardInfoIntentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        getPositionView().setFocusable(false);
        getIngredientInfoView().setFocusable(false);
        getRecipeInfoView().setFocusable(false);
        getCardsListView().setFocusable(false);

        Intent cardsInfo = getIntent();
        showInfo(cardsInfo);
    }

    private void showInfo(Intent cardsInfo) {
        showCardPositionInfo(cardsInfo);
        showIngredientInfo(cardsInfo);
        showRecipeInfo(cardsInfo);
        showCardsInfo(cardsInfo);
    }

    private void showCardPositionInfo(Intent cardsInfo) {
        String ownerText = cardsInfo.getStringExtra(POSITION);
        getPositionView().setText(ownerText);
    }

    private Recipe getIngredient(Intent cardsInfo) {
        Card card = (Card) cardsInfo.getSerializableExtra(CARD);

        if (null == card) {
            return (Recipe)cardsInfo.getSerializableExtra(INGREDIENT);
        } else {
            return card.ingredient;
        }
    }

    private void showIngredientInfo(Intent cardsInfo) {
        Recipe ingredient = getIngredient(cardsInfo);
        if (null == ingredient) {
            getIngredientInfoLayout().setVisibility(View.INVISIBLE);
        } else {
            getIngredientInfoView().setRecipe(ingredient);
        }
    }

    private Recipe getComplexRecipe(Intent cardsInfo) {
        Card card = (Card) cardsInfo.getSerializableExtra(CARD);

        if (null == card) {
            return (Recipe)cardsInfo.getSerializableExtra(RECIPE);
        } else {
            return card.complexRecipe;
        }
    }

    private void showRecipeInfo(Intent cardsInfo) {
        Recipe complexRecipe = getComplexRecipe(cardsInfo);
        if (null == complexRecipe) {
            getRecipeInfoLayout().setVisibility(View.INVISIBLE);
        } else {
            String scoreText = (
                    complexRecipe.score < 5 ? "очка" : "очков"
                    );

            getRecipeInfoLabel().setText(
                    String.format(Locale.US, "Рецепт (%d %s)", complexRecipe.score, scoreText)
            );

            getRecipeInfoView().setRecipe(complexRecipe);
        }
    }

    private void showCardsInfo(Intent cardsInfo) {
        String cardsListName = cardsInfo.getStringExtra(CARDS_LIST_NAME);
        if (null == cardsListName) {
            Recipe complexRecipe = getComplexRecipe(cardsInfo);

            if (null != complexRecipe) {
                getCardsListLabel().setText("Состав рецепта");

                getCardsListView().setAdapter(
                        new RecipeAdapter(this)
                );

                GameListAdapter.setValues(
                        getCardsListView().getAdapter(),
                        complexRecipe.mixedRecipes
                );
            }
        } else {
            getCardsListLabel().setText(cardsListName);

            final List<Card> cards = (List<Card>) cardsInfo.getSerializableExtra(CARDS_LIST);

            getCardsListView().setAdapter(
                    new CardAdapter(this)
            );

            GameListAdapter.setValues(
                    getCardsListView().getAdapter(),
                    cards
            );

            final String cardInCardsListPosition = cardsInfo.getStringExtra(CARD_IN_CARDS_LIST_POSITION);

            getCardsListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Card card = cards.get(position);

                    Intent intent = new Intent(CardInfoActivity.this, CardInfoActivity.class);

                    intent.putExtra(POSITION,
                            String.format(Locale.US, cardInCardsListPosition, position + 1)
                    );

                    intent.putExtra(CARD, card);

                    startActivity(intent);
                }
            });
        }
    }

    private TextView getPositionView() {
        return (TextView) findViewById(R.id.position_text_view);
    }

    private LinearLayout getIngredientInfoLayout() {
        return (LinearLayout) findViewById(R.id.ingredient_info_layout);
    }

    private RecipeView getIngredientInfoView() {
        return (RecipeView) findViewById(R.id.ingredient_info_view);
    }

    private LinearLayout getRecipeInfoLayout() {
        return (LinearLayout) findViewById(R.id.recipe_info_layout);
    }

    private TextView getRecipeInfoLabel() { return (TextView) findViewById(R.id.recipe_info_text_label); }

    private RecipeView getRecipeInfoView() {
        return (RecipeView) findViewById(R.id.recipe_info_view);
    }

    private TextView getCardsListLabel() {
        return (TextView) findViewById(R.id.cards_list_label);
    }

    private TwoWayView getCardsListView() {
        return (TwoWayView) findViewById(R.id.cards_list_view);
    }
}
