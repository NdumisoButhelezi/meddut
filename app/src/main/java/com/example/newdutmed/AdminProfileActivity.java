package com.example.newdutmed;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextDepartment;
    private Button saveProfileButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        loadUserProfile();

        saveProfileButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Admin admin = documentSnapshot.toObject(Admin.class);
                            if (admin != null) {
                                editTextName.setText(admin.getFirstName() + " " + admin.getLastName());
                                editTextEmail.setText(admin.getEmail());
                                editTextPhone.setText(admin.getPhoneNumber());
                                editTextDepartment.setText(admin.getDepartment());
                            }
                        } else {
                            Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch profile.", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveUserProfile() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String department = editTextDepartment.getText().toString();

        // Split name into first and last names if necessary
        String[] names = name.split(" ");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("firstName", firstName);
        userUpdates.put("lastName", lastName);
        userUpdates.put("email", email);
        userUpdates.put("phoneNumber", phone);
        userUpdates.put("department", department);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid())
                    .update(userUpdates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(AdminProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(AdminProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
        }
    }
}