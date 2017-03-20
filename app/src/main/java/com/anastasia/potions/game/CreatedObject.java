package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreatedObject {

    public final Player player;

    public final Recipe baseRecipe;
    public final Card baseCard;

    private List<Card> usedCards;

    CreatedObject(Player player, Recipe baseRecipe, Card baseCard, List<Card> usedCards) {
        this.player = player;
        this.baseRecipe = baseRecipe;
        this.baseCard = baseCard;
        this.usedCards = usedCards;
    }

    public Serializable getSerializableUsedCards() {
        return (ArrayList<Card>) usedCards;
    }
}
