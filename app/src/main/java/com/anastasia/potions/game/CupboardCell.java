package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;

import java.util.ArrayDeque;
import java.util.Deque;

public class CupboardCell {

    public final Recipe ingredient;
    public final Deque<Card> cards;

    public CupboardCell(Recipe ingredient) {
        this.ingredient = ingredient;
        this.cards = new ArrayDeque<>();
    }

    public int getCardsCount() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.addLast(card);
    }

    public Card pollCard() {
        return cards.pollLast();
    }
}
