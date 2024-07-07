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
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientLogin extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonCreateClient;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textViewRegister;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textViewRegister = findViewById(R.id.registerNow);
        buttonCreateClient = findViewById(R. id.buttonCreateClient);

        setupButtonListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRoleAndRedirect(currentUser);
        }
    }

    private void checkUserRoleAndRedirect(FirebaseUser user) {
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String role = task.getResult().getString("role");
                if ("client".equals(role)) {
                    navigateToMainActivity();
                } else {
                    redirectToLogin(); // Redirect non-client users to login
                }
            } else {
                redirectToLogin(); // Redirect on failure to fetch user role
            }
        });
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
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserRoleAndRedirect(user);
                        }
                    } else {
                        handleLoginFailure(task);
                    }
                });
    }

    private void handleLoginFailure(@NonNull Task<AuthResult> task) {
        if (task.getException() instanceof FirebaseAuthException) {
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
        redirectToLogin(); // Redirect to login on any failure
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, ClientLogin.class);
        startActivity(intent);
        finish();
    }
}
