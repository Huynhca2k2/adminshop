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

import com.chh.adminshop.Domain.Product;
import com.chh.adminshop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private Spinner spinner;
    private static final int MY_REQUEST_CODE = 10;
    private RatingBar ratingBar;
    private ImageView imageUpload, backBtn;
    private TextView edtProductName, edtProductPrice, editTextMultiLine;
    private Button btnAddProduct;
    private Uri selectedImage;
    private float ratingStar = 0;
    private String selectedItem = "Danh Mục 1";
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
        setContentView(R.layout.activity_add_product);

        init();
        adapterSpinner();
        hanldle();

        onClickDialog();

    }

    private void init(){
        spinner = findViewById(R.id.spinnerCate);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        imageUpload = findViewById(R.id.imageUpload);
        ratingBar = findViewById(R.id.ratingBar);
        editTextMultiLine = findViewById(R.id.editTextMultiLine);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        backBtn = findViewById(R.id.backBtnAdd);
    }

    private void hanldle(){
        backBtn.setOnClickListener(view -> startActivity(new Intent(AddProductActivity.this, ProductActivity.class)));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingStar = rating;
            }
        });
    }

    private void onClickDialog(){

        imageUpload.setOnClickListener(view -> onClickRequestPermission());

        btnAddProduct.setOnClickListener(view -> {

            uploadImageToFirebase(selectedImage);
        });
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

    private void uploadImageToFirebase(Uri imageUri) {

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
        String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Upload thành công
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Product product = new Product(
                                currentTimestamp,
                                edtProductName.getText()+"",
                                Double.parseDouble(edtProductPrice.getText()+""),
                                editTextMultiLine.getText()+"",
                                selectedItem,
                                (int) ratingStar,
                                uri.toString());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("products" );

                        myRef.child(currentTimestamp).setValue(product, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                alertDialog.dismiss();
                                //dialog.dismiss();
                                backBtn.callOnClick();
                            }
                        });
                    });
                })
                .addOnFailureListener(e -> {
                    // Upload thất bại
                    e.printStackTrace();
                });
    }
}