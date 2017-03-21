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
import com.anastasia.potions.adapter.CardAdapter;
import com.anastasia.potions.adapter.CreatedObjectAdapter;
import com.anastasia.potions.adapter.CupboardCellAdapter;
import com.anastasia.potions.adapter.GameListAdapter;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.game.Game;
import com.anastasia.potions.game.created.CreatedObject;
import com.anastasia.potions.game.creating.NotEnoughIngredientsException;
import com.anastasia.potions.game.creating.RecipeCreatingConstants;
import com.anastasia.potions.game.cupboard.CupboardCell;
import com.anastasia.potions.game.player.PlayerInfo;
import com.anastasia.potions.util.StringUtils;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

public class GameActivity extends Activity implements CardInfoIntentActivity, RecipeCreatingConstants {

    Game game;

    int creatingCardPosition;
    boolean[] isCreatedObjectTaken;

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
    }

    void startGame() {
        createGame();
        game.start();

        setPlayerTurnMode();
        updateScores();

        startTurn();
    }

    void createGame() {
        this.game = Game.create();

        updateCupboard();
        updateCreatedObjects();
    }

    void startTurn() {
        updatePlayerName();
        updatePlayerHand();
    }

    void nextTurn() {
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

    void updateScores() {
        for (PlayerInfo player : game.getPlayers()) {
            String updatedScoreText = StringUtils.intToString(
                    player.getScore()
            );

            getScoreButton(
                    player.getPlayerIndex()
            ).setText(updatedScoreText);
        }
    }

    void updatePlayerName() {
        TextView playerNameView = getConfirmTextView();
        playerNameView.setText(game.getCurrentPlayer().getName());
    }

    void updatePlayerHand() {
        PlayerInfo currentPlayer = game.getCurrentPlayer();

        GameListAdapter.setValues(
                getHandView().getAdapter(),
                currentPlayer.getCards()
        );
    }

    void updateCreatedObjects() {
        GameListAdapter.setValues(
                getCreatedObjectsView().getAdapter(),
                game.getCreatedObjects()
        );
    }

    void updateCupboard() {
        GameListAdapter.setValues(
                getCupboardView().getAdapter(),
                game.getCupboardCells()
        );
    }

    void updateCreateButton() {
        if (game.canCreate()) {
            getConfirmButton().setVisibility(View.VISIBLE);

            getConfirmButton().setBackgroundResource(R.drawable.create_card);
            getConfirmButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createCard();
                }
            });

            getConfirmTextView().setText("Собрать рецепт");
        } else {
            getConfirmButton().setVisibility(View.INVISIBLE);
            getConfirmTextView().setText("Добавьте рецепты в сборку");
        }
    }

    void playIngredient(int position) {
        game.playIngredient(position);

        updateScores();

        updatePlayerHand();
        updateCupboard();
    }

    boolean playComplexRecipe(int position) {
        try {
            game.startCreatingCardAt(position);
            setCreatingMode(position);

            return true;
        } catch (NotEnoughIngredientsException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Недостаточно ингредиентов")
                    .setMessage("Не хватает ингредиентов для создания рецепта")
                    .setCancelable(true)
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            return false;
        }
    }

    final AdapterView.OnItemClickListener PLAYER_TURN_HAND_CARD_CLICK_LISTENER = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Выберите действиe")
                    .setMessage("Использовать ингредиент или сложный рецепт?")
                    .setCancelable(true)
                    .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showHandCardInfo(position);
                        }
                    })
                    .setPositiveButton("Ингрeдиент", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            playIngredient(position);
                        }
                    })
                    .setNegativeButton("Сложный рецепт", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (playComplexRecipe(position)) {
                                view.setBackgroundResource(R.drawable.background_selector_selected_object);
                            }
                        }
                    }).show();
        }
    };

    final AdapterView.OnItemClickListener PLAYER_TURN_CREATED_OBJECT_CLICK_LISTENER = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            showCreatedObjectInfo(position);
        }
    };

    void setPlayerTurnMode() {
        this.creatingCardPosition = -1;
        this.isCreatedObjectTaken = null;

        getConfirmButton().setVisibility(View.VISIBLE);
        getConfirmButton().setBackgroundResource(R.drawable.next_turn);
        getConfirmButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTurn();
            }
        });

        updatePlayerName();

        getHandView().setOnItemClickListener(PLAYER_TURN_HAND_CARD_CLICK_LISTENER);
        getCreatedObjectsView().setOnItemClickListener(PLAYER_TURN_CREATED_OBJECT_CLICK_LISTENER);
    }

    final AdapterView.OnItemClickListener CREATING_HAND_CARD_CLICK_LISTENER = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            if (position != creatingCardPosition) {
                showHandCardInfo(position);
            } else {
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Выберите действие")
                        .setMessage("Отменить создание рецепта?")
                        .setCancelable(true)
                        .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showHandCardInfo(position);
                            }
                        })
                        .setPositiveButton("Отменить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeCreatingMode();
                                view.setBackgroundResource(R.drawable.background_selector_card);
                            }
                        }).show();
            }
        }
    };

    final AdapterView.OnItemClickListener CREATING_CREATED_OBJECT_CLICK_LISTENER = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Выберите действиe")
                    .setCancelable(true)
                    .setNeutralButton("Информация", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showCreatedObjectInfo(position);
                        }
                    });

            if (isCreatedObjectTaken[position]) {
                builder.setMessage("Отменить использование в сборке?")
                        .setPositiveButton("Отменить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (removeFromBuildOn(position)) {
                                    view.setBackgroundResource(R.drawable.background_selector_object);
                                    isCreatedObjectTaken[position] = false;
                                }

                                updateCreateButton();
                            }
                        });
            } else {
                builder.setMessage("Использовать рецепт в сборке?")
                        .setPositiveButton("Использовать", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (tryTakeCreatedObjectOn(position)) {
                                    view.setBackgroundResource(R.drawable.background_selector_selected_object);
                                    isCreatedObjectTaken[position] = true;
                                }

                                updateCreateButton();
                            }
                        });
            }

            builder.show();
        }
    };

    void setCreatingMode(int position) {
        this.creatingCardPosition = position;
        this.isCreatedObjectTaken = new boolean[getCreatedObjectsView().getCount()];

        updateCreateButton();

        getHandView().setOnItemClickListener(CREATING_HAND_CARD_CLICK_LISTENER);
        getCreatedObjectsView().setOnItemClickListener(CREATING_CREATED_OBJECT_CLICK_LISTENER);
    }

    boolean tryTakeCreatedObjectOn(int position) {
        int result = game.tryTakeCreatedObjectOn(position);

        if (NOT_NEEDED == result) {
            new AlertDialog.Builder(this)
                    .setTitle("Невозможное действие")
                    .setMessage("Данный объект не входит в создаваемый рецепт")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            return false;
        } else if (ALREADY_FULL == result) {
            new AlertDialog.Builder(this)
                    .setTitle("Невозможное действие")
                    .setMessage("Объектов данного типа уже достаточно для сборки рецепта")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            return false;
        } else {
            return true;
        }
    }

    boolean removeFromBuildOn(int position) {
        game.removeFromBuildOn(position);
        return true;
    }

    void createCard() {
        if (game.createCard()) {
            updateScores();

            updatePlayerHand();
            updateCupboard();
            updateCreatedObjects();

            closeCreatingMode();
        }
    }

    void closeCreatingMode() {
        game.closeCreatingMode();
        setPlayerTurnMode();
    }

    void showHandCardInfo(int position) {
        PlayerInfo currentPlayer = game.getCurrentPlayer();
        Card card = currentPlayer.getCard(position);

        Intent intent = new Intent(this, CardInfoActivity.class);

        intent.putExtra(POSITION, "В руке игрока " + currentPlayer.getName());
        intent.putExtra(CARD, card);

        startActivity(intent);
    }

    void showCupboardCellInfo(int position) {
        final CupboardCell cell = game.getCupboardCells().get(position);

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

        intent.putExtra(POSITION, "Рецепт, созданный игроком " + createdObject.getPlayer().getName());
        intent.putExtra(CARD, createdObject.getBaseCard());

        intent.putExtra(CARDS_LIST_NAME, "Карты в составе рецепта");
        intent.putExtra(CARDS_LIST, createdObject.getUsedCards());

        intent.putExtra(CARD_IN_CARDS_LIST_POSITION, "Карта в составе рецепта");

        startActivity(intent);
    }

    Button getConfirmButton() {
        return (Button) findViewById(R.id.confirm_button);
    }

    TextView getConfirmTextView() {
        return (TextView) findViewById(R.id.confirm_text_view);
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
