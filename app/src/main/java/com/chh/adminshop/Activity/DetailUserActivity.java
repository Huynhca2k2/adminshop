package com.chh.adminshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.Domain.User;
import com.chh.adminshop.R;

public class DetailUserActivity extends AppCompatActivity {

    private TextView idUserTxt, addressTxt, emailUserTxt, emailUserTxt2, nameUserTxt, roleUserTxt;
    private ImageView imgUser;
    private User object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_detail_user);

        initView();
        handle();
    }

    private void initView(){

        idUserTxt = findViewById(R.id.idUserTxt);
        addressTxt = findViewById(R.id.addressTxt);
        emailUserTxt = findViewById(R.id.emailUserTxt);
        emailUserTxt2 = findViewById(R.id.emailUserTxt2);
        nameUserTxt = findViewById(R.id.nameUserTxt);
        roleUserTxt = findViewById(R.id.roleUserTxt);
        imgUser = findViewById(R.id.imgUser);
    }

    private void handle(){
        object = (User) getIntent().getSerializableExtra("object");

        Glide.with(this).load(object.getPicUrl()).error(R.drawable.grey_bg).into(imgUser);
        idUserTxt.setText(object.getId());
        addressTxt.setText(object.getAddress());
        emailUserTxt.setText(object.getEmail());
        emailUserTxt2.setText(object.getEmail());
        nameUserTxt.setText(object.getName());
        roleUserTxt.setText(object.getRole());

    }
}