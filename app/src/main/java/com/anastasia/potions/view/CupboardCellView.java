package com.anastasia.potions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anastasia.potions.R;
import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.util.StringUtils;

public class CupboardCellView extends LinearLayout {

    public CupboardCellView(Context context) {
        super(context);
        init();
    }

    public CupboardCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CupboardCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_cupboard_cell, this);
    }

    public void setCupboardCell(CupboardCell cell) {
        CardView ingredientCard = getIngredientView();
        ingredientCard.setRecipe(cell.ingredient);

        setCount(cell.getCardsCount());
    }

    public void setCount(int count) {
        getCountView().setText(
                StringUtils.intToString(count)
        );

        refreshDrawableState();
    }

    public CardView getIngredientView() {
        return (CardView)findViewById(R.id.ingredient_view);
    }

    public TextView getCountView() { return (TextView) findViewById(R.id.ingredient_count_view); }
}
