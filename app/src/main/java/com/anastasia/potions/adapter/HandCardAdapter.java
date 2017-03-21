package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.card.Card;
import com.anastasia.potions.view.HandCardView;

import java.util.List;

public class HandCardAdapter extends AbstractListAdapter<Card, HandCardView> {

    public HandCardAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    @Override
    protected HandCardView createView(Card card) {
        HandCardView handCardView = new HandCardView(context);
        handCardView.setCard(card);

        return handCardView;
    }
}
