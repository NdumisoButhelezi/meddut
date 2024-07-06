package com.example.newdutmed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonCreateAdmin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textViewRegister = findViewById(R.id.registerNow);
        buttonCreateAdmin = findViewById(R.id.buttonCreateAdmin);

        setupListeners();
    }

    private void setupListeners() {
        textViewRegister.setOnClickListener(v -> startActivity(new Intent(this, AdminRegister.class)));
        buttonCreateAdmin.setOnClickListener(v -> startActivity(new Intent(this, AdminRegister.class)));

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                return;
            }
            authenticateUser(email, password);
        });
    }

    private void authenticateUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        checkAdminRole();
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkAdminRole() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Admin admin = documentSnapshot.toObject(Admin.class);
                    if (admin != null && "admin".equals(admin.getRole())) {
                        startActivity(new Intent(this, AdminMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Access Denied: Not an Admin", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to verify admin status", Toast.LENGTH_SHORT).show());
    }
}
