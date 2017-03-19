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
import com.anastasia.potions.adapter.HandCardAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.card.Recipe;
import com.anastasia.potions.game.CreatedObject;
import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.game.Game;
import com.anastasia.potions.game.PlayerInfo;
import com.anastasia.potions.util.StringUtils;

import org.lucasr.twowayview.TwoWayView;

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
                new HandCardAdapter(this)
        );

        getHandView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PlayerInfo currentPlayerInfo = game.getCurrentPlayer();
                final Card card = currentPlayerInfo.getCard(position);

                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать ингредиент или сложный рецепт?")
                        .setCancelable(true)
                        .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(GameActivity.this, CardInfoActivity.class);

                                intent.putExtra(OWNER, "В руке игрока " + currentPlayerInfo.getName());
                                intent.putExtra(INGREDIENT, card.ingredient);
                                intent.putExtra(RECIPE, card.complexRecipe);

                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("Ингрeдиент", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentPlayerInfo.removeCard(position);
                                game.addToCupboard(card);

                                updateScore(card.ingredient);

                                GameListAdapter.updateValues(
                                        getCupboardView().getAdapter()
                                );

                                GameListAdapter.updateValues(
                                        getHandView().getAdapter()
                                );
                            }
                        })
                        .setNegativeButton("Сложный рецепт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentPlayerInfo.removeCard(position);
                                game.addToCreatedObjects(card);

                                updateScore(card.complexRecipe);

                                GameListAdapter.updateValues(
                                        getCreatedObjectsView().getAdapter()
                                );

                                GameListAdapter.updateValues(
                                        getHandView().getAdapter()
                                );
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
                final CupboardCell cell = game.getCupboard().get(position);

                Intent intent = new Intent(GameActivity.this, CardInfoActivity.class);

                intent.putExtra(OWNER, "В буфете ингредиент " + cell.ingredient.getLocaleName());
                intent.putExtra(INGREDIENT, cell.ingredient);

                startActivity(intent);
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
                final CreatedObject createdObject = game.getCreatedObjects().get(position);

                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действиe")
                        .setMessage("Использовать рецепт в сборке?")
                        .setCancelable(true)
                        .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(GameActivity.this, CardInfoActivity.class);

                                intent.putExtra(OWNER, "Рецепт, созданный игроком " + createdObject.player.name);
                                intent.putExtra(INGREDIENT, createdObject.baseCard.ingredient);
                                intent.putExtra(RECIPE, createdObject.baseCard.complexRecipe);

                                startActivity(intent);
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
