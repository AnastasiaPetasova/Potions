package com.anastasia.potions.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.anastasia.potions.R;
import com.anastasia.potions.adapter.CreatedObjectAdapter;
import com.anastasia.potions.adapter.CupboardCellAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.game.Game;
import com.anastasia.potions.game.PlayerInfo;
import com.anastasia.potions.adapter.HandCardAdapter;

import org.lucasr.twowayview.TwoWayView;

public class GameActivity extends Activity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        drawGameField();
        startGame();
    }

    void drawGameField() {

    }

    void startGame() {
        this.game = Game.create();
        game.start();

        startTurn();
    }

    void startTurn() {
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

    void fillPlayerHand() {
        final PlayerInfo currentPlayerInfo = game.getCurrentPlayer();

        getHandView().setAdapter(
                new HandCardAdapter(this, currentPlayerInfo.getCards())
        );

        getHandView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать ингридиент или сложный рецепт?")
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

                                getCupboardView().setAdapter(
                                        new CupboardCellAdapter(
                                                GameActivity.this,
                                                game.getCupboard()
                                        )
                                );

                                getHandView().setAdapter(
                                        new HandCardAdapter(
                                                GameActivity.this,
                                                currentPlayerInfo.getCards()
                                        )
                                );
                            }
                        })
                        .setNegativeButton("Сложный рецепт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Card card = currentPlayerInfo.removeCard(position);
                                game.addToCreatedObjects(card);

                                getCreatedObjectsView().setAdapter(
                                        new CreatedObjectAdapter(
                                                GameActivity.this,
                                                game.getCreatedObjects()
                                        )
                                );

                                getHandView().setAdapter(
                                        new HandCardAdapter(
                                                GameActivity.this,
                                                currentPlayerInfo.getCards()
                                        )
                                );
                            }
                        }).show();
            }
        });
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

        game.nextTurn();
    }
}
