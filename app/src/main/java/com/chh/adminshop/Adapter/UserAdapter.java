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
import com.chh.adminshop.Activity.ChangeUserActivity;
import com.chh.adminshop.Activity.DetailActivity;
import com.chh.adminshop.Activity.DetailUserActivity;
import com.chh.adminshop.Domain.User;
import com.chh.adminshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> listItemSelected;

    public UserAdapter(ArrayList<User> listItemSelected) {
        this.listItemSelected = listItemSelected;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.email.setText(listItemSelected.get(position).getEmail());
        holder.role.setText(listItemSelected.get(position).getRole());

        Glide.with(holder.itemView.getContext()).load(listItemSelected.get(position).getPicUrl()).error(R.drawable.user_profile).into(holder.picUrl);

        holder.changeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChangeUserActivity.class);
            intent.putExtra("object", (Serializable) listItemSelected.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Bạn chắc chắn muốn xóa người dùng?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String pathObject = String.valueOf(listItemSelected.get(position).getId());
                            DatabaseReference myRef = database.getReference("users/" + pathObject);
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
            Intent intent = new Intent(holder.itemView.getContext(), DetailUserActivity.class);
            intent.putExtra("object", (Serializable) listItemSelected.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView email, role;
        ImageView picUrl, changeBtn, deleteBtn;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            email = itemView.findViewById(R.id.email);
            picUrl = itemView.findViewById(R.id.picUser);
            role = itemView.findViewById(R.id.roleTxt);
            changeBtn = itemView.findViewById(R.id.changeBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}

