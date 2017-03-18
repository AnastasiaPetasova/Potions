package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.view.HandCardView;

public class HandCardAdapter extends GameListAdapter<Card, HandCardView> {

    public HandCardAdapter(Context context) {
        super(context);
    }

    @Override
    protected HandCardView createView(Card card) {
        HandCardView handCardView = new HandCardView(context);
        handCardView.setCard(card);

        return handCardView;
    }
}
