package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Game {

    final static Random rnd = new Random();

    final static int DIFFERENT_CARDS_COUNT = 6, EACH_CARD_COUNT = 5;
    final static int PLAYER_HAND_SIZE = 7;

    public Deque<Card> deck;
    public List<CupboardCell> cupboard;

    public List<CreatedObject> createdObjects;

    int currentPlayerIndex;
    public List<PlayerInfo> players;

    public static Game create() {
        List<PlayerInfo> players = new ArrayList<>();

        players.add(
                new PlayerInfo(new Player("Human 1"))
        );

        players.add(
                new PlayerInfo(new Player("Human 2"))
        );

        List<CupboardCell> cupboard = new ArrayList<>();

        List<Card> deckList = generateCards(DIFFERENT_CARDS_COUNT, EACH_CARD_COUNT);
        Collections.shuffle(deckList);

        Deque<Card> deck = new ArrayDeque<>(deckList);

        List<CreatedObject> createdObjects = new ArrayList<>();

        return new Game(deck, cupboard, createdObjects, players);
    }

    private static List<Card> generateCards(int differentCardsCount, int eachCardCount) {
        List<Recipe> ingredients = new ArrayList<>();
        List<Recipe> complexRecipes = new ArrayList<>();

        for (Recipe recipe : Recipe.values()) {
            if (recipe.isIngredient()) {
                ingredients.add(recipe);
            } else {
                complexRecipes.add(recipe);
            }
        }

        Set<Card> generatedCards = new HashSet<>();
        List<Card> deckList = new ArrayList<>();

        while (generatedCards.size() < differentCardsCount) {
            final int ingredientIndex = rnd.nextInt(ingredients.size());
            final int complexRecipeIndex = rnd.nextInt(complexRecipes.size());

            Card card = Card.create(
                    ingredients.get(ingredientIndex),
                    complexRecipes.get(complexRecipeIndex)
            );

            // если такой еще не было, то генерим ее копии в колоду
            if (generatedCards.add(card)) {
                for (int duplicateIndex = 0; duplicateIndex < eachCardCount; ++duplicateIndex) {
                    deckList.add(card.clone());
                }
            }
        }

        return deckList;
    }

    private Game(Deque<Card> deck, List<CupboardCell> cupboard,
                 List<CreatedObject> createdObjects, List<PlayerInfo> players) {
        this.deck = deck;
        this.cupboard = cupboard;
        this.createdObjects = createdObjects;
        this.currentPlayerIndex = 0;
        this.players = players;
    }

    public void start() {
        for (int handSize = 0; handSize < PLAYER_HAND_SIZE; ++handSize) {
            for (PlayerInfo playerInfo : players) {
                playerInfo.addCard(deck.poll());
            }
        }

        currentPlayerIndex = 0;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        while (deck.size() > 0 && getCurrentPlayer().getCards().size() < PLAYER_HAND_SIZE) {
            getCurrentPlayer().addCard(deck.poll());
        }
    }

    public void addToCupboard(Card card) {
        for (CupboardCell cupboardCell : cupboard) {
            if (cupboardCell.ingredient == card.ingredient) {
                cupboardCell.addCard(card);
                return;
            }
        }

        CupboardCell cupboardCell = new CupboardCell(card.ingredient);
        cupboardCell.addCard(card);

        cupboard.add(cupboardCell);
    }

    public int getCupboardCount(Recipe ingredient) {
        for (CupboardCell cupboardCell : cupboard) {
            if (cupboardCell.ingredient == ingredient) {
                return cupboardCell.getCardsCount();
            }
        }

        return 0;
    }

    public List<CupboardCell> getCupboard() {
        return cupboard;
    }

    public void addToCreatedObjects(Card card) {
        // TODO change with actual info about creating
        createdObjects.add(
                new CreatedObject(
                    getCurrentPlayer().player, card.complexRecipe, card, new ArrayList<Card>()
                )
        );
    }

    public List<CreatedObject> getCreatedObjects() {
        return createdObjects;
    }

    public PlayerInfo getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean ended() {
        // TODO add logic
        return false;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
