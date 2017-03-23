package com.anastasia.potions.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.card.RecipePictures;
import com.anastasia.potions.card.Recipe;

public class RecipeView extends LinearLayout {

    public RecipeView(Context context) {
        super(context);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_recipe, this);
    }

    public void setRecipe(Recipe recipe) {
        @DrawableRes int recipePictureId = RecipePictures.getPictureId(recipe);
        getImageView().setImageResource(recipePictureId);
        getTextView().setText(recipe.getLocalName());
    }

    public ImageView getImageView() {
        return (ImageView)findViewById(R.id.card_image);
    }

    public TextView getTextView() {
        return (TextView)findViewById(R.id.card_title);
    }
}
