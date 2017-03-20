package com.anastasia.potions.game.creating;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.player.PlayerInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreatedObject {

    public final PlayerInfo player;

    public final Recipe baseRecipe;
    public final Card baseCard;

    private List<Card> usedCards;

    public CreatedObject(PlayerInfo player, Recipe baseRecipe, Card baseCard, List<Card> usedCards) {
        this.player = player;
        this.baseRecipe = baseRecipe;
        this.baseCard = baseCard;
        this.usedCards = usedCards;
    }

    public Serializable getSerializableUsedCards() {
        return (ArrayList<Card>) usedCards;
    }
}
