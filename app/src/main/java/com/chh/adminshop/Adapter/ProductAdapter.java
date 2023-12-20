package com.chh.adminshop.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chh.adminshop.Activity.ChangeProductActivity;
import com.chh.adminshop.Activity.DetailActivity;
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<Product> listItemSelected;

    public ProductAdapter(ArrayList<Product> listItemSelected) {
        this.listItemSelected = listItemSelected;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(listItemSelected.get(position).getName());
        holder.price.setText(listItemSelected.get(position).getPrice()/1000 + "k");

        Glide.with(holder.itemView.getContext()).load(listItemSelected.get(position).getPicUrl()).error(R.drawable.mk1).into(holder.picUrl);

        holder.changeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChangeProductActivity.class);
            intent.putExtra("object", (Serializable) listItemSelected.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Bạn chắc chắn muốn xóa sản phẩm?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String pathObject = String.valueOf(listItemSelected.get(position).getId());
                            DatabaseReference myRef = database.getReference("products/" + pathObject);
                            myRef.removeValue();

                        }
                    }).setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", (Serializable) listItemSelected.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price;
        ImageView picUrl, changeBtn, deleteBtn;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.titleTxt);
            picUrl = itemView.findViewById(R.id.picProduct);
            price = itemView.findViewById(R.id.price);
            changeBtn = itemView.findViewById(R.id.changeBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}

