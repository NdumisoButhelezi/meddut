package com.example.newdutmed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class ClientLogin extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonCreateClient;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textViewRegister;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance(); // Ensure FirebaseAuth is initialized here
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textViewRegister = findViewById(R.id.registerNow);
        buttonCreateClient = findViewById(R.id.buttonCreateClient);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        buttonCreateClient.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ClientRegister.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (validateForm(email, password)) {
                performLogin(email, password);
            }
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ClientRegister.class);
            startActivity(intent);
        });
    }

    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ClientLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(ClientLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        handleLoginFailure(task);
                    }
                });
    }

    private void handleLoginFailure(@NonNull Task<AuthResult> task) {
        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(ClientLogin.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(ClientLogin.this, "The password is incorrect.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(ClientLogin.this, "There is no user corresponding to this identifier.", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(ClientLogin.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
        startActivity(intent);
        finish();
    }
}