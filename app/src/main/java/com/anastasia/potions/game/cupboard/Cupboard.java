package com.anastasia.potions.game.cupboard;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;

import java.util.ArrayList;
import java.util.List;

public class Cupboard {

    private int[] cellIndicesByIngredient;
    private List<CupboardCell> cells;

    public static Cupboard createWith(List<Recipe> ingredients) {
        int[] cellIndicesByIngredient = new int[Recipe.values().length];

        List<CupboardCell> cells = new ArrayList<>();
        for (Recipe ingredient : ingredients) {
            cellIndicesByIngredient[ingredient.ordinal()] = cells.size();
            cells.add(
                    new CupboardCell(ingredient)
            );
        }

        return new Cupboard(cellIndicesByIngredient, cells);
    }

    private Cupboard(int[] cellIndicesByIngredient, List<CupboardCell> cells) {
        this.cellIndicesByIngredient = cellIndicesByIngredient;
        this.cells = cells;
    }

    private CupboardCell getCupboardCellFor(final Recipe ingredient) {
        int cellIndex = cellIndicesByIngredient[ingredient.ordinal()];
        return cells.get(cellIndex);
    }

    public void add(Card card) {
        CupboardCell cupboardCell = getCupboardCellFor(card.ingredient);
        cupboardCell.addCard(card);
    }

    public int getCardsCountFor(Recipe ingredient) {
        CupboardCell cupboardCell = getCupboardCellFor(ingredient);
        return cupboardCell.getCardsCount();
    }

    public List<CupboardCell> getCells() {
        return cells;
    }
}
