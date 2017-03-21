package com.anastasia.potions.game.player;

import com.anastasia.potions.card.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

    private final Player player;
    private final int playerIndex;

    private final List<Card> cards;
    private int score;

    public PlayerInfo(Player player, int playerIndex) {
        this.player = player;
        this.playerIndex = playerIndex;
        this.cards = new ArrayList<>();
        this.score = 0;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void increaseScore(int additionalScore) {
        this.score += additionalScore;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card getCard(int index) { return cards.get(index); }

    public void removeCard(Card card) {
        cards.remove(card);
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

    @Override
    public String toString() {
        return player.toString();
    }
}
