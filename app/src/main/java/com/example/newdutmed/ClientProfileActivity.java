package com.example.newdutmed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewPhone, textViewGender, textViewYearOfBirth;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewGender = findViewById(R.id.textViewGender);
        textViewYearOfBirth = findViewById(R.id.textViewYearOfBirth);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Client client = documentSnapshot.toObject(Client.class);
                            if (client != null) {
                                textViewName.setText(client.getFirstName() + " " + client.getLastName());
                                textViewEmail.setText(client.getEmail());
                                textViewPhone.setText(client.getPhoneNumber());
                                textViewGender.setText(client.getGender());
                                textViewYearOfBirth.setText(client.getYearOfBirth());
                            }
                        } else {
                            Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch profile.", Toast.LENGTH_SHORT).show());
        }
    }
}
