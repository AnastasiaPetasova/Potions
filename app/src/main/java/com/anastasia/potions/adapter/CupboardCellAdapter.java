package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.view.CupboardCellView;

import java.util.List;

public class CupboardCellAdapter extends AbstractListAdapter<CupboardCell, CupboardCellView> {

    public CupboardCellAdapter(Context context, List<CupboardCell> cupboardCells) {
        super(context, cupboardCells);
    }

    @Override
    protected CupboardCellView createView(CupboardCell cell) {
        CupboardCellView cupboardCellView = new CupboardCellView(context);
        cupboardCellView.setCupboardCell(cell);

        return cupboardCellView;
    }
}
