package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.game.CreatedObject;
import com.anastasia.potions.view.CreatedObjectView;

public class CreatedObjectAdapter extends GameListAdapter<CreatedObject, CreatedObjectView> {

    public CreatedObjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected CreatedObjectView createView(CreatedObject createdObject) {
        CreatedObjectView createdObjectView = new CreatedObjectView(context);
        createdObjectView.setCreatedObject(createdObject);

        return createdObjectView;
    }
}
