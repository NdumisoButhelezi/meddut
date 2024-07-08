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

public class ClientProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextGender, editTextYearOfBirth;
    private Button saveProfileButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextGender = findViewById(R.id.editTextGender);
        editTextYearOfBirth = findViewById(R.id.editTextYearOfBirth);
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
                            Client client = documentSnapshot.toObject(Client.class);
                            if (client != null) {
                                editTextName.setText(client.getFirstName() + " " + client.getLastName());
                                editTextEmail.setText(client.getEmail());
                                editTextPhone.setText(client.getPhoneNumber());
                                editTextGender.setText(client.getGender());
                                editTextYearOfBirth.setText(client.getYearOfBirth());
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
        String gender = editTextGender.getText().toString();
        String yearOfBirth = editTextYearOfBirth.getText().toString();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("firstName", name.split(" ")[0]);
        userUpdates.put("lastName", name.split(" ").length > 1 ? name.split(" ")[1] : "");
        userUpdates.put("email", email);
        userUpdates.put("phoneNumber", phone);
        userUpdates.put("gender", gender);
        userUpdates.put("yearOfBirth", yearOfBirth);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid())
                    .update(userUpdates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ClientProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ClientProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
        }
    }
}
