package com.anastasia.potions.adapter;

import android.content.Context;

import com.anastasia.potions.R;
import com.anastasia.potions.card.Card;
import com.anastasia.potions.view.CardView;

public class CardAdapter extends GameListAdapter<Card, CardView> {

    public CardAdapter(Context context) {
        super(context);
    }

    @Override
    protected CardView createView(Card card) {
        CardView cardView = new CardView(context);
        cardView.setBackgroundResource(R.drawable.background_selector_card);

        cardView.setCard(card);

        return cardView;
    }
}
