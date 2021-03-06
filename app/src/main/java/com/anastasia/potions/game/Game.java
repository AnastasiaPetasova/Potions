package com.anastasia.potions.game;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.created.CreatedObject;
import com.anastasia.potions.game.created.CreatedObjectsList;
import com.anastasia.potions.game.creating.NotEnoughIngredientsException;
import com.anastasia.potions.game.creating.RecipeBuilder;
import com.anastasia.potions.game.creating.RecipeCreatingChecker;
import com.anastasia.potions.game.creating.RecipeCreatingResult;
import com.anastasia.potions.game.cupboard.Cupboard;
import com.anastasia.potions.game.cupboard.CupboardCell;
import com.anastasia.potions.game.player.Player;
import com.anastasia.potions.game.player.PlayerInfo;
import com.anastasia.potions.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Game {

    private final static Random rnd = new Random();

    private final static int DIFFERENT_CARDS_COUNT = 10, EACH_CARD_COUNT = 3;
    private final static int PLAYER_HAND_SIZE = 5;

    private final Deque<Card> deck;
    private final Cupboard cupboard;
    private final CreatedObjectsList createdObjects;

    private int currentPlayerIndex;
    private final List<PlayerInfo> players;

    private RecipeBuilder recipeBuilder;
    private boolean currentPlayerPlayedCard;

    private static void addPlayer(List<PlayerInfo> players, Player player) {
        int playerIndex = players.size();
        players.add(new PlayerInfo(player, playerIndex));
    }

    public static Game create() {
        List<PlayerInfo> players = new ArrayList<>();

        addPlayer(players, new Player("Игрок 1"));
        addPlayer(players, new Player("Игрок 2"));

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
        CreatedObjectsList createdObjects = new CreatedObjectsList();

        return new Game(deck, cupboard, createdObjects, players);
    }

    private static List<Card> generateCards(
            List<Recipe> ingredients, List<Recipe> complexRecipes,
            int differentCardsCount, int eachCardCount) {
        List<Card> deckList = new ArrayList<>();

        List<Pair> indexPairs = new ArrayList<>();
        for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ++ingredientIndex) {
            for (int complexRecipeIndex = 0; complexRecipeIndex < complexRecipes.size(); ++complexRecipeIndex) {
                indexPairs.add(new Pair(ingredientIndex, complexRecipeIndex));
            }
        }

        Collections.shuffle(indexPairs);

        differentCardsCount = Math.min(differentCardsCount, indexPairs.size());
        for (int i = 0; i < differentCardsCount; ++i) {
            final int ingredientIndex = indexPairs.get(i).first;
            final int complexRecipeIndex = indexPairs.get(i).second;

            Card card = Card.create(
                    ingredients.get(ingredientIndex),
                    complexRecipes.get(complexRecipeIndex)
            );

            for (int duplicateIndex = 0; duplicateIndex < eachCardCount; ++duplicateIndex) {
                deckList.add(card.clone());
            }
        }

        return deckList;
    }

    private Game(Deque<Card> deck,
                 Cupboard cupboard, CreatedObjectsList createdObjects,
                 List<PlayerInfo> players) {
        this.deck = deck;
        this.cupboard = cupboard;
        this.createdObjects = createdObjects;
        this.currentPlayerIndex = 0;
        this.players = players;

        this.recipeBuilder = null;
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

        currentPlayerIndex = -1;
        nextTurn();
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayerPlayedCard = false;

        PlayerInfo currentPlayer = getCurrentPlayer();
        fillPlayerHand(currentPlayer);
    }

    public boolean isCurrentPlayerPlayedCard() {
        return currentPlayerPlayedCard;
    }

    public boolean isEnded() {
        return getCurrentPlayer().getCards().isEmpty() && deck.isEmpty();
    }

    public void playIngredient(int position) {
        PlayerInfo currentPlayer = getCurrentPlayer();
        Card card = currentPlayer.getCard(position);

        getCurrentPlayer().removeCard(card);
        cupboard.add(card);

        getCurrentPlayer().increaseScore(card.ingredient.score);

        currentPlayerPlayedCard = true;
    }

    public List<CupboardCell> getCupboardCells() {
        return cupboard.getCells();
    }

    public void startCreatingCardAt(int position) throws NotEnoughIngredientsException {
        Card card = getCurrentPlayer().getCard(position);

        RecipeCreatingChecker creatingChecker = RecipeCreatingChecker.create(card.complexRecipe, cupboard);
        this.recipeBuilder = new RecipeBuilder(card, creatingChecker);
    }

    public int tryTakeCreatedObjectOn(int position) {
        CreatedObject createdObject = createdObjects.get(position);
        return recipeBuilder.tryTake(createdObject);
    }

    public void removeFromBuildOn(int position) {
        CreatedObject createdObject = createdObjects.get(position);
        recipeBuilder.removeFromBuild(createdObject);
    }

    public boolean canCreate() {
        return recipeBuilder.canCreate();
    }

    public boolean createCard() {
        RecipeCreatingResult recipeCreatingResult = recipeBuilder.create();
        if (null == recipeCreatingResult) {
            return false;
        }

        List<Card> usedCards = new ArrayList<>();
        Set<PlayerInfo> creators = new HashSet<>();

        PlayerInfo mainCreator = getCurrentPlayer();
        creators.add(mainCreator);

        List<Card> freeCards = new ArrayList<>();

        for (CreatedObject takenObject : recipeCreatingResult.takenObjects) {
            creators.add(takenObject.getPlayer());
            usedCards.add(takenObject.getBaseCard());

            createdObjects.remove(takenObject);
            freeCards.addAll(takenObject.getUsedCards());
        }

        for (Map.Entry<Recipe, Integer> takenRecipeCount : recipeCreatingResult.takenRecipeCounts) {
            Recipe recipe = takenRecipeCount.getKey();

            if (recipe.isIngredient()) {
                int count = takenRecipeCount.getValue();
                for (int i = 0; i < count; ++i) {
                    Card takenIngredientCard = cupboard.pollCardWith(recipe);
                    usedCards.add(takenIngredientCard);
                }
            }
        }

        // remove card from hand
        mainCreator.removeCard(recipeCreatingResult.createdCard);

        // add card to created objects list
        CreatedObject createdObject = new CreatedObject(
                getCurrentPlayer(), recipeCreatingResult.createdCard, usedCards
        );

        createdObjects.add(createdObject);

        // update player scores
        int score = createdObject.getBaseRecipe().score;
        for (PlayerInfo creator : creators) {
            if (mainCreator == creator) {
                creator.increaseScore(score);
            } else {
                creator.increaseScore(score / 2);
            }
        }

        // add cards to cupboard
        for (Card card : freeCards) {
            cupboard.add(card);
        }

        closeCreatingMode();

        currentPlayerPlayedCard = true;
        return true;
    }

    public void closeCreatingMode() {
        this.recipeBuilder = null;
    }

    public List<CreatedObject> getCreatedObjects() {
        return createdObjects.toList();
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public PlayerInfo getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}
