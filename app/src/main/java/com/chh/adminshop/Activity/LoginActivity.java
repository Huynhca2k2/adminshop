package com.chh.adminshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chh.adminshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView btnRegister;
    private EditText emailLoginTxt, passwordLoginTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initListener();
    }

    private void initUi() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        emailLoginTxt = findViewById(R.id.emailLoginTxt);
        passwordLoginTxt = findViewById(R.id.passwordLoginTxt);
    }

    private void initListener() {

        // Khởi tạo AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Loading...");

        // Khởi tạo ProgressBar
        ProgressBar progressBar = new ProgressBar(this);
        alertDialogBuilder.setView(progressBar);

        // Tạo AlertDialog từ AlertDialog.Builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        btnLogin.setOnClickListener(view -> {
            alertDialog.show();

            String strEmail = emailLoginTxt.getText().toString().trim();
            String strPassword = passwordLoginTxt.getText().toString().trim();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                alertDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finishAffinity();
                            } else {
                                alertDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        btnRegister.setOnClickListener(view -> startActivity(new Intent( LoginActivity.this, SignupActivity.class)));
    }

    private void onclickSignin() {

    }

}