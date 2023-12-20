package com.chh.adminshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chh.adminshop.Domain.Marketing;
import com.chh.adminshop.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.Viewholder> {

    ArrayList<Marketing> marketings;
    DecimalFormat formatter;

    public MarketingAdapter(ArrayList<Marketing> marketings) {
        this.marketings = marketings;
        formatter = new DecimalFormat("###,###,###,###");
    }

    @NonNull
    @Override
    public MarketingAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketingAdapter.Viewholder holder, int position) {

        holder.titleMk.setText(marketings.get(position).getTitle());
        holder.descMk.setText(marketings.get(position).getDescription());

        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(marketings.get(position).getPicUrl(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.logoCrypto);
    }

    @Override
    public int getItemCount() {
        return marketings.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleMk, descMk;
        ImageView logoCrypto;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            titleMk = itemView.findViewById(R.id.titleMk);
            descMk = itemView.findViewById(R.id.descMk);
            logoCrypto = itemView.findViewById(R.id.pic);
        }
    }
}
