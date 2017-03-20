package com.anastasia.potions.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.adapter.CreatedObjectAdapter;
import com.anastasia.potions.adapter.CupboardCellAdapter;
import com.anastasia.potions.adapter.GameListAdapter;
import com.anastasia.potions.adapter.CardAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.CreatedObject;
import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.game.Game;
import com.anastasia.potions.game.PlayerInfo;
import com.anastasia.potions.util.StringUtils;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

public class GameActivity extends Activity implements CardInfoIntentActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initGameField();
        startGame();
    }

    void initGameField() {
        initHand();
        initCupboard();
        initCreatedObjects();
    }

    void initHand() {
        getHandView().setAdapter(
                new CardAdapter(this)
        );

        getHandView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PlayerInfo currentPlayer = game.getCurrentPlayer();

                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать ингредиент или сложный рецепт?")
                        .setCancelable(true)
                        .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showHandCardInfo(currentPlayer, position);
                            }
                        })
                        .setPositiveButton("Ингрeдиент", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                playIngredient(currentPlayer, position);
                            }
                        })
                        .setNegativeButton("Сложный рецепт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                playComplexRecipe(currentPlayer, position);
                            }
                        }).show();
            }
        });
    }

    void initCupboard() {
        getCupboardView().setAdapter(
                new CupboardCellAdapter(this)
        );

        getCupboardView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showCupboardCellInfo(position);
            }
        });
    }

    void initCreatedObjects() {
        getCreatedObjectsView().setAdapter(
                new CreatedObjectAdapter(this)
        );

        getCreatedObjectsView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать рецепт в сборке?")
                        .setCancelable(true)
                        .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showCreatedObjectInfo(position);
                            }
                        }).show();
            }
        });
    }

    void startGame() {
        createGame();

        game.start();

        startTurn();
    }

    void createGame() {
        this.game = Game.create();

        GameListAdapter.setValues(
                getCupboardView().getAdapter(),
                game.getCupboard()
        );

        GameListAdapter.setValues(
                getCreatedObjectsView().getAdapter(),
                game.getCreatedObjects()
        );
    }

    void startTurn() {
        updatePlayerName();
        fillPlayerHand();
    }

    void updatePlayerName() {
        TextView playerNameView = (TextView) findViewById(R.id.current_player_name_view);

        playerNameView.setText(game.getCurrentPlayer().getName());
    }

    void fillPlayerHand() {
        final PlayerInfo currentPlayerInfo = game.getCurrentPlayer();

        GameListAdapter.setValues(
                getHandView().getAdapter(),
                currentPlayerInfo.getCards()
        );
    }

    public void nextTurn(View v) {
        endTurn();
        game.nextTurn();
        startTurn();
    }

    void endTurn() {
        // здесь проверять?
        if (game.ended()) {
            AlertDialog endDialog = new AlertDialog.Builder(GameActivity.this)
                    .setCancelable(false)
                    .setTitle("Конец игры")
                    .setMessage("Игра закончена!")
                    .setPositiveButton("Рестартнуть игру?", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    })
                    .setNegativeButton("Вернуться к игровому полю", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();

            endDialog.show();
        }
    }

    void updateScore(Recipe recipe) {
        game.getCurrentPlayer().increaseScore(recipe.score);

        int updatedScore = game.getCurrentPlayer().getScore();
        String updatedScoreText = StringUtils.intToString(updatedScore);

        getScoreButton(
                game.getCurrentPlayerIndex()
        ).setText(updatedScoreText);
    }

    void playIngredient(PlayerInfo currentPlayer, int position) {
        Card card = currentPlayer.removeCard(position);
        game.addToCupboard(card);

        updateScore(card.ingredient);

        GameListAdapter.updateValues(
                getCupboardView().getAdapter()
        );

        GameListAdapter.updateValues(
                getHandView().getAdapter()
        );
    }

    void playComplexRecipe(PlayerInfo currentPlayer, int position) {
        Card card = currentPlayer.removeCard(position);
        game.addToCreatedObjects(card);

        updateScore(card.complexRecipe);

        GameListAdapter.updateValues(
                getCreatedObjectsView().getAdapter()
        );

        GameListAdapter.updateValues(
                getHandView().getAdapter()
        );
    }

    void showHandCardInfo(PlayerInfo currentPlayer, int position) {
        Card card = currentPlayer.getCard(position);

        Intent intent = new Intent(this, CardInfoActivity.class);

        intent.putExtra(POSITION, "В руке игрока " + currentPlayer.getName());
        intent.putExtra(CARD, card);

        startActivity(intent);
    }

    void showCupboardCellInfo(int position) {
        final CupboardCell cell = game.getCupboard().get(position);

        Intent intent = new Intent(GameActivity.this, CardInfoActivity.class);

        intent.putExtra(POSITION, "Ингредиент в шкафу");
        intent.putExtra(INGREDIENT, cell.ingredient);

        intent.putExtra(CARDS_LIST_NAME, "Стопка карт-ингредиентов");
        intent.putExtra(CARDS_LIST, new ArrayList<>(cell.cards));
        intent.putExtra(CARD_IN_CARDS_LIST_POSITION, "%d-я сверху карта в стопке");

        startActivity(intent);
    }

    void showCreatedObjectInfo(int position) {
        CreatedObject createdObject = game.getCreatedObjects().get(position);

        Intent intent = new Intent(GameActivity.this, CardInfoActivity.class);

        intent.putExtra(POSITION, "Рецепт, созданный игроком " + createdObject.player.name);
        intent.putExtra(CARD, createdObject.baseCard);

        intent.putExtra(CARDS_LIST_NAME, "Карты в составе рецепта");
        intent.putExtra(CARDS_LIST, createdObject.getSerializableUsedCards());

        intent.putExtra(CARD_IN_CARDS_LIST_POSITION, "Карта в составе рецепта");

        startActivity(intent);
    }

    private static final int[] SCORE_BUTTON_IDS = {
            R.id.first_score_button,
            R.id.second_score_button
    };

    Button getScoreButton(int playerIndex) {
        return (Button) findViewById(SCORE_BUTTON_IDS[playerIndex]);
    }

    TwoWayView getCupboardView() {
        return (TwoWayView) findViewById(R.id.cupboard_view);
    }

    TwoWayView getCreatedObjectsView() {
        return (TwoWayView) findViewById(R.id.created_objects_view);
    }

    TwoWayView getHandView() {
        return (TwoWayView) findViewById(R.id.player_hand_view);
    }
}
