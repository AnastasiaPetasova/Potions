package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.creating.CreatedObject;
import com.anastasia.potions.game.cupboard.Cupboard;
import com.anastasia.potions.game.cupboard.CupboardCell;
import com.anastasia.potions.game.player.Player;
import com.anastasia.potions.game.player.PlayerInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Game {

    private final static Random rnd = new Random();

    private final static int DIFFERENT_CARDS_COUNT = 9, EACH_CARD_COUNT = 5;
    private final static int PLAYER_HAND_SIZE = 7;

    private Deque<Card> deck;
    private Cupboard cupboard;
    private List<CreatedObject> createdObjects;

    private int currentPlayerIndex;
    private List<PlayerInfo> players;

    public static Game create() {
        List<PlayerInfo> players = new ArrayList<>();

        players.add(
                new PlayerInfo(new Player("Игрок 1"))
        );

        players.add(
                new PlayerInfo(new Player("Игрок 2"))
        );

        List<Recipe> ingredients = new ArrayList<>();
        List<Recipe> complexRecipes = new ArrayList<>();

        for (Recipe recipe : Recipe.values()) {
            if (recipe.isIngredient()) {
                ingredients.add(recipe);
            } else {
                complexRecipes.add(recipe);
            }
        }

        Collections.sort(ingredients, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe left, Recipe right) {
                String leftLocalName = left.getLocalName();
                String rightLocalName = right.getLocalName();

                return leftLocalName.compareTo(rightLocalName);
            }
        });

        List<Card> deckList = generateCards(ingredients, complexRecipes, DIFFERENT_CARDS_COUNT, EACH_CARD_COUNT);
        Collections.shuffle(deckList);

        Deque<Card> deck = new ArrayDeque<>(deckList);

        Cupboard cupboard = Cupboard.createWith(ingredients);

        List<CreatedObject> createdObjects = new ArrayList<>();

        return new Game(deck, cupboard, createdObjects, players);
    }

    private static List<Card> generateCards(
            List<Recipe> ingredients, List<Recipe> complexRecipes,
            int differentCardsCount, int eachCardCount) {
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

    private Game(Deque<Card> deck, Cupboard cupboard,
                 List<CreatedObject> createdObjects, List<PlayerInfo> players) {
        this.deck = deck;
        this.cupboard = cupboard;
        this.createdObjects = createdObjects;
        this.currentPlayerIndex = 0;
        this.players = players;
    }

    private void fillPlayerHand(PlayerInfo player) {
        while (deck.size() > 0 && player.getCards().size() < PLAYER_HAND_SIZE) {
            Card card = deck.poll();
            player.addCard(card);
        }
    }

    public void start() {
        for (PlayerInfo player : players) {
            fillPlayerHand(player);
        }

        currentPlayerIndex = 0;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        PlayerInfo currentPlayer = getCurrentPlayer();
        fillPlayerHand(currentPlayer);
    }

    public void addToCupboard(Card card) {
        cupboard.add(card);
    }

    public List<CupboardCell> getCupboardCells() {
        return cupboard.getCells();
    }

    public void addToCreatedObjects(Card card) {
        // TODO change with actual info about creating
        createdObjects.add(
                new CreatedObject(
                    getCurrentPlayer(), card, new ArrayList<Card>()
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
