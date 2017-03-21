package com.anastasia.potions.game.created;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.player.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class CreatedObject {

    private final PlayerInfo player;
    private final Card baseCard;
    private List<Card> usedCards;

    public CreatedObject(PlayerInfo player, Card baseCard, List<Card> usedCards) {
        this.player = player;
        this.baseCard = baseCard;
        this.usedCards = usedCards;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public Card getBaseCard() {
        return baseCard;
    }

    public Recipe getBaseRecipe() {
        return baseCard.complexRecipe;
    }

    /**
     * Возвращает ArrayList вместо List, чтобы был Serializable
     * @return список карт, использованных при сборке рецепта
     */
    public ArrayList<Card> getUsedCards() {
        return (ArrayList<Card>) usedCards;
    }
}
