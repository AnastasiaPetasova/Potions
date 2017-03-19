package com.anastasia.potions.card;

public class Card implements Cloneable {

    public Recipe ingredient;
    public Recipe complexRecipe;

    public static Card create(Recipe ingredient, Recipe complexRecipe) {
        if (!ingredient.isIngredient()) throw new RuntimeException("Первый параметр карты должен быть ингредиентом");
        if (complexRecipe.isIngredient()) throw new RuntimeException("Второй параметр карты должен быть сложным рецептом");

        return new Card(ingredient, complexRecipe);
    }

    private Card(Recipe ingredient, Recipe complexRecipe) {
        this.ingredient = ingredient;
        this.complexRecipe = complexRecipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (ingredient != card.ingredient) return false;
        return complexRecipe == card.complexRecipe;
    }

    @Override
    public int hashCode() {
        int result = ingredient.hashCode() * 137597241 + complexRecipe.hashCode() * 85178739;
        return result;
    }

    @Override
    public Card clone() {
        try {
            Card clonedCard = (Card) super.clone();

            clonedCard.ingredient = this.ingredient;
            clonedCard.complexRecipe = this.complexRecipe;

            return clonedCard;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();

            return null;
        }
    }
}
