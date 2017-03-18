package com.anastasia.potions.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.anastasia.potions.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class GameListAdapter<ValueType, ViewType extends View> extends BaseAdapter {

    public static <ValueType> void setValues(Adapter adapter, List<ValueType> values){
        ClassUtils.<GameListAdapter<ValueType, ?>>cast(adapter).setValues(values);
    }

    Context context;
    private List<ValueType> values;

    GameListAdapter(Context context) {
        this.context = context;
        this.values = new ArrayList<>();
    }

    private void setValues(List<ValueType> values) {
        this.values = values;
        notifyDataSetChanged();
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

        return createView(value);
    }
}
