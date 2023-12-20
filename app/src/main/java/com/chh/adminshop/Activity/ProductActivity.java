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
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> items = new ArrayList<>();
    private TextView emptyTxt;
    private ImageView addBtn;
    private ConstraintLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        layoutLoading = findViewById(R.id.progressBar);
        layoutLoading.setVisibility(View.VISIBLE);
        initView();
        getListProduct();
        handle();
    }

    private void initView() {
        addBtn = findViewById(R.id.addBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
        recyclerView = findViewById(R.id.view3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new ProductAdapter(items);
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
        addBtn.setOnClickListener(view -> startActivity(new Intent(ProductActivity.this, AddProductActivity.class)));
    }
    private void getListProduct(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                items.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    System.out.println("items.size" + dataSnapshot.getKey());
                    Product product = dataSnapshot.getValue(Product.class);
                    items.add(product);
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