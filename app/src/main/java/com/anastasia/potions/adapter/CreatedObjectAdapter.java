package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.R;
import com.anastasia.potions.game.created.CreatedObject;
import com.anastasia.potions.view.CreatedObjectView;

public class CreatedObjectAdapter extends GameListAdapter<CreatedObject, CreatedObjectView> {

    public CreatedObjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected CreatedObjectView createView(CreatedObject createdObject) {
        CreatedObjectView createdObjectView = new CreatedObjectView(context);
        createdObjectView.setBackgroundResource(R.drawable.background_selector_object);

        createdObjectView.setCreatedObject(createdObject);

        return createdObjectView;
    }
}
