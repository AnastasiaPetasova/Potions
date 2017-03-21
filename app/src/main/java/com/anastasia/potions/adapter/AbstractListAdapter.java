package com.anastasia.potions.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.view.HandCardView;

import java.util.List;

public abstract class AbstractListAdapter<ValueType, ViewType extends View> extends BaseAdapter {

    Context context;
    List<ValueType> values;

    protected AbstractListAdapter(Context context, List<ValueType> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public ValueType getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return values.get(position).hashCode();
    }

    protected abstract ViewType createView(ValueType value);

    @Override
    public ViewType getView(int position, View convertView, ViewGroup parent) {
        ValueType value = getItem(position);

        ViewType view = createView(value);
        return view;
    }
}
