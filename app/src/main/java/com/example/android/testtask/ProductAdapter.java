package com.example.android.testtask;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Integer[]ids;
    private String[] names;
    private double[] prices;
    private Listener listener;

    public static interface Listener{
        public void itemClicked(int position);
    }

    public ProductAdapter(Integer[] ids, String[] names, double[] prices) {
        this.ids = ids;
        this.names = names;
        this.prices = prices;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView)LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_product, parent, false);
        return new ViewHolder(cv);
    }

    /**
     * Заполнение полей карточки
     */
    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textId = (TextView)cardView.findViewById(R.id.text_id);
        textId.setText(String.valueOf(ids[position]));
        TextView textName = (TextView)cardView.findViewById(R.id.text_name);
        textName.setText(names[position]);
        TextView textPrice = (TextView)cardView.findViewById(R.id.text_price);
        textPrice.setText(String.valueOf(prices[position]));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ids.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
