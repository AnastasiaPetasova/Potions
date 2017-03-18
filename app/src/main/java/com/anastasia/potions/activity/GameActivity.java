package com.anastasia.potions.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.adapter.GameListAdapter;
import com.anastasia.potions.adapter.CreatedObjectAdapter;
import com.anastasia.potions.adapter.CupboardCellAdapter;
import com.anastasia.potions.adapter.HandCardAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.Game;
import com.anastasia.potions.game.PlayerInfo;
import com.anastasia.potions.util.StringUtils;

import org.lucasr.twowayview.TwoWayView;

public class GameActivity extends Activity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initGameField();
        startGame();
    }

    void initGameField() {
        getHandView().setAdapter(
                new HandCardAdapter(this)
        );

        getCupboardView().setAdapter(
                new CupboardCellAdapter(this)
        );

        getCreatedObjectsView().setAdapter(
                new CreatedObjectAdapter(this)
        );
    }

    void startGame() {
        this.game = Game.create();
        game.start();

        startTurn();
    }

    void startTurn() {
        updatePlayerName();
        fillPlayerHand();
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

    private static final int[] SCORE_BUTTON_IDS = {
            R.id.first_score_button,
            R.id.second_score_button
    };

    Button getScoreButton(int playerIndex) {
        return (Button) findViewById(SCORE_BUTTON_IDS[playerIndex]);
    }

    void updateScore(Recipe recipe) {
        game.getCurrentPlayer().increaseScore(recipe.score);

        int updatedScore = game.getCurrentPlayer().getScore();
        String updatedScoreText = StringUtils.intToString(updatedScore);

        getScoreButton(
                game.getCurrentPlayerIndex()
        ).setText(updatedScoreText);
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

        getHandView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать ингредиент или сложный рецепт?")
                        .setCancelable(true)
                        .setNeutralButton("Никак", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Ингрeдиент", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Card card = currentPlayerInfo.removeCard(position);
                                game.addToCupboard(card);

                                updateScore(card.ingredient);

                                GameListAdapter.setValues(
                                        getCupboardView().getAdapter(),
                                        game.getCupboard()
                                );

                                GameListAdapter.setValues(
                                        getHandView().getAdapter(),
                                        currentPlayerInfo.getCards()
                                );
                            }
                        })
                        .setNegativeButton("Сложный рецепт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Card card = currentPlayerInfo.removeCard(position);
                                game.addToCreatedObjects(card);

                                updateScore(card.complexRecipe);

                                GameListAdapter.setValues(
                                        getCreatedObjectsView().getAdapter(),
                                        game.getCreatedObjects()
                                );

                                GameListAdapter.setValues(
                                        getHandView().getAdapter(),
                                        currentPlayerInfo.getCards()
                                );
                            }
                        }).show();
            }
        });
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
}
