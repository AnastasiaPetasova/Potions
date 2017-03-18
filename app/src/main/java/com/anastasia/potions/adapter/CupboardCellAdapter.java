package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.view.CupboardCellView;

public class CupboardCellAdapter extends GameListAdapter<CupboardCell, CupboardCellView> {

    public CupboardCellAdapter(Context context) {
        super(context);
    }

    @Override
    protected CupboardCellView createView(CupboardCell cell) {
        CupboardCellView cupboardCellView = new CupboardCellView(context);
        cupboardCellView.setCupboardCell(cell);

        return cupboardCellView;
    }
}
