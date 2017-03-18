package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

    Player player;
    final List<Card> cards;
    int score;

    public PlayerInfo(Player player) {
        this.player = player;
        this.cards = new ArrayList<>();
        this.score = 0;
    }

    public void increaseScore(int additionalScore) {
        this.score += additionalScore;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card getCard(int index) { return cards.get(index); }

    public Card removeCard(int index) {
        return cards.remove(index);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return player.name;
    }
}
