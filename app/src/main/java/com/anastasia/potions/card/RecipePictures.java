package com.anastasia.potions.card;

import android.support.annotation.DrawableRes;

import com.anastasia.potions.R;

import java.util.HashMap;
import java.util.Map;

public class RecipePictures {

    private final static Map<Recipe, Integer> pictures;

    static {
        pictures = new HashMap<>();

        initRecipes();
    }

    public static @DrawableRes int getPictureId(Recipe recipe) {
        return pictures.get(recipe);
    }

    private static void addPicture(Recipe recipe, @DrawableRes int drawableRes) {
        pictures.put(recipe, drawableRes);
    }

    private static void initRecipes() {
        addPicture(Recipe.WATER, R.drawable.water);
        addPicture(Recipe.FIRE, R.drawable.fire);
        addPicture(Recipe.TOOTH, R.drawable.tooth);
        addPicture(Recipe.ICE, R.drawable.ice);
        addPicture(Recipe.SUN, R.drawable.sun);
        addPicture(Recipe.PRISM, R.drawable.prism);
    }
}
