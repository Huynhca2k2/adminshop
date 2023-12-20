package com.chh.adminshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chh.adminshop.Adapter.ProductAdapter;
import com.chh.adminshop.Adapter.UserAdapter;
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.Domain.User;
import com.chh.adminshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<User> items = new ArrayList<>();
    private TextView emptyTxt;
    private ImageView addBtn;
    private ConstraintLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        layoutLoading = findViewById(R.id.progressBar);
        layoutLoading.setVisibility(View.VISIBLE);
        initView();
        getListUser();
        handle();
    }

    private void initView() {
        addBtn = findViewById(R.id.addBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
        recyclerView = findViewById(R.id.viewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new UserAdapter(items);
        recyclerView.setAdapter(adapter);

        if (items.isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //layoutLoading.setVisibility(View.GONE);

        }else {
            emptyTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void handle(){
        addBtn.setOnClickListener(view -> startActivity(new Intent(UserActivity.this, AddUserActivity.class)));
    }
    private void getListUser(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                items.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    items.add(user);
                }
                layoutLoading.setVisibility(View.GONE);
                initView();
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }
}