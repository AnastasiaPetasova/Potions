package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.game.CreatedObject;
import com.anastasia.potions.game.CupboardCell;
import com.anastasia.potions.view.CreatedObjectView;
import com.anastasia.potions.view.CupboardCellView;

import java.util.List;

public class CreatedObjectAdapter extends AbstractListAdapter<CreatedObject, CreatedObjectView> {

    public CreatedObjectAdapter(Context context, List<CreatedObject> createdObjects) {
        super(context, createdObjects);
    }

    @Override
    protected CreatedObjectView createView(CreatedObject createdObject) {
        CreatedObjectView createdObjectView = new CreatedObjectView(context);
        createdObjectView.setCreatedObject(createdObject);

        return createdObjectView;
    }
}
