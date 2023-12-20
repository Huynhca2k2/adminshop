package com.chh.adminshop.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class ChangeProductActivity extends AppCompatActivity {

    private TextView titleTxt, descriptionTxt, priceTxt;
    private static final int MY_REQUEST_CODE = 10;
    private RatingBar ratingBar;
    private float ratingValue;
    private Spinner spinner;
    private String selectedItem;
    private String linkImage = "";
    private ImageView imageUpload, backBtn;
    private Uri selectedImage = Uri.parse("");
    private Button btnEditProduct;
    private Product object;
    private String[] items = {"Danh Mục 1", "Danh Mục 2", "Danh Mục 3", "Danh Mục 4"};
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        selectedImage = data.getData();
                        //uploadImageToFirebase(selectedImage);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            imageUpload.setImageBitmap(bitmap);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_product);

        initView();
        adapterSpinner();
        handle();
    }

    private void initView(){

        titleTxt = findViewById(R.id.edtProductName2);
        descriptionTxt = findViewById(R.id.editTextMultiLine2);
        imageUpload = findViewById(R.id.imageUpload2);
        backBtn = findViewById(R.id.backBtnAdd);
        ratingBar = findViewById(R.id.ratingBar2);
        priceTxt = findViewById(R.id.edtProductPrice2);
        spinner = findViewById(R.id.spinnerCate2);
        btnEditProduct = findViewById(R.id.btnEditProduct);
    }

    private void adapterSpinner(){
        // Tạo Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán Adapter cho Spinner
        spinner.setAdapter(adapter);

        // Xử lý sự kiện khi một mục được chọn
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });
    }

    private void handle(){
        object = (Product) getIntent().getSerializableExtra("object");

        //gan du lieu chuan bi chinh sua
        Glide.with(this).load(object.getPicUrl()).error(R.drawable.grey_bg).into(imageUpload);
        titleTxt.setText(object.getName());
        priceTxt.setText(object.getPrice() + "");
        descriptionTxt.setText(object.getDescription());

        ratingValue = object.getStar();
        ratingBar.setRating(ratingValue);

        int indexCate = 0;
        for (int i = 0; i < items.length; i++){
            if(object.getCategory().equals(items[i])){
                indexCate = i;
                break;
            }
        }
        spinner.setSelection(indexCate);
        selectedItem = object.getCategory();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
            }
        });

        //up anh moi
        imageUpload.setOnClickListener(view -> onClickRequestPermission());
        //luu chinh sua san pham
        btnEditProduct.setOnClickListener(view -> {
            if(selectedImage.toString().equals(""))
                saveEdit();
            else
                uploadImageToFirebase(selectedImage);
        });

        backBtn.setOnClickListener(view -> startActivity(new Intent(ChangeProductActivity.this, ProductActivity.class)));
    }

    private void onClickRequestPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void uploadImageToFirebase(Uri imageUri){

        // Khởi tạo AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Loading...");

        // Khởi tạo ProgressBar
        ProgressBar progressBar = new ProgressBar(this);
        alertDialogBuilder.setView(progressBar);

        // Tạo AlertDialog từ AlertDialog.Builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        alertDialog.show();
        
        String imageName = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Upload thành công
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                linkImage = uri.toString();
                saveEdit();
                alertDialog.dismiss();
            });
        }).addOnFailureListener(e -> {
                // Upload thất bại
                    e.printStackTrace();
        });

    }

    private void saveEdit(){
        Product product = new Product(
                object.getId(),
                titleTxt.getText()+"",
                Double.parseDouble(priceTxt.getText()+""),
                descriptionTxt.getText()+"",
                selectedItem,
                (int) ratingValue,
                linkImage.equals("") ? object.getPicUrl() : linkImage);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products" );

        myRef.child(object.getId()).setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                backBtn.callOnClick();
            }
        });
    }
}