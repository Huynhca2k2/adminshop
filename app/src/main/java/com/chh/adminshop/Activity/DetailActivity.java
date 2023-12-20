package com.chh.adminshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.R;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTxt, descriptionTxt, scoreTxt, priceTxt;
    private ImageView picItem, backBtn;
    private Product object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_detail);

        initView();
        handle();
    }

    private void initView(){

        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        picItem = findViewById(R.id.picItem);
        backBtn = findViewById(R.id.backBtn);
        scoreTxt = findViewById(R.id.scoreTxt);
        priceTxt = findViewById(R.id.priceTxt);
    }

    private void handle(){
        object = (Product) getIntent().getSerializableExtra("object");

        Glide.with(this).load(object.getPicUrl()).error(R.drawable.grey_bg).into(picItem);
        titleTxt.setText(object.getName());
        priceTxt.setText(object.getPrice()/1000 + "k vnd");
        descriptionTxt.setText(object.getDescription());
        scoreTxt.setText(object.getStar() + "");

        backBtn.setOnClickListener(view -> startActivity(new Intent(DetailActivity.this, ProductActivity.class)));
    }
}