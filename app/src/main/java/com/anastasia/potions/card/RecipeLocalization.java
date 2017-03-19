package com.anastasia.potions.card;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecipeLocalization {

    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    private static final Locale RUSSIAN_LOCALE = new Locale("ru");

    private static Map<Locale, Map<Recipe, String>> localRecipeNames;

    private static Locale currentLocale;

    private static Map<Recipe, String> getLocalNames(Locale locale) {
        Map<Recipe, String> localNames = localRecipeNames.get(locale);
        if (null == localNames) {
            localRecipeNames.put(locale, localNames = new HashMap<>());
        }

        return localNames;
    }

    static {
        currentLocale = RUSSIAN_LOCALE;

        localRecipeNames = new HashMap<>();

        putLocalName(RUSSIAN_LOCALE, Recipe.FIRE, "Стихия огня");
        putLocalName(ENGLISH_LOCALE, Recipe.FIRE, "Fire");

        putLocalName(RUSSIAN_LOCALE, Recipe.WATER, "Стихия воды");
        putLocalName(ENGLISH_LOCALE, Recipe.WATER, "Water");

        putLocalName(RUSSIAN_LOCALE, Recipe.TOOTH, "Зуб дракона");
        putLocalName(ENGLISH_LOCALE, Recipe.TOOTH, "Dragon tooth");

        putLocalName(RUSSIAN_LOCALE, Recipe.ICE, "Кубик льда");
        putLocalName(ENGLISH_LOCALE, Recipe.ICE, "Ice cube");

        putLocalName(RUSSIAN_LOCALE, Recipe.SUN, "Солнечный зайчик");
        putLocalName(ENGLISH_LOCALE, Recipe.SUN, "Sunny bunny");

        putLocalName(RUSSIAN_LOCALE, Recipe.PRISM, "Призма");
        putLocalName(ENGLISH_LOCALE, Recipe.PRISM, "Prism");
    }

    private static void putLocalName(Locale locale, Recipe recipe, String localName) {
        getLocalNames(locale).put(recipe, localName);
    }

    static String getLocalName(Recipe recipe) {
        return getLocalNames(currentLocale).get(recipe);
    }
}
