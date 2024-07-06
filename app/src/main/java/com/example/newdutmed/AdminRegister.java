package com.example.newdutmed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminRegister extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword, phoneNumber, department;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        department = findViewById(R.id.department);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String confirmPasswordText = confirmPassword.getText().toString().trim();

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(AdminRegister.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (emailText.isEmpty() || passwordText.isEmpty() || firstName.getText().toString().trim().isEmpty() ||
                    lastName.getText().toString().trim().isEmpty() || phoneNumber.getText().toString().trim().isEmpty() ||
                    department.getText().toString().trim().isEmpty()) {
                Toast.makeText(AdminRegister.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Admin admin = new Admin(
                                    firstName.getText().toString().trim(),
                                    lastName.getText().toString().trim(),
                                    emailText,
                                    phoneNumber.getText().toString().trim(),
                                    department.getText().toString().trim(),
                                    "admin"  // Role set as admin
                            );

                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(admin)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(AdminRegister.this, "Admin registered successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(AdminRegister.this, AdminMainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(AdminRegister.this, "Failed to save admin profile", Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(AdminRegister.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
