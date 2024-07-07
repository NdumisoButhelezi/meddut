package com.example.newdutmed;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewPhone, textViewDepartment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewDepartment = findViewById(R.id.textViewDepartment);
        Button adminProfileButton = findViewById(R.id.adminProfileButton);



        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Admin admin = documentSnapshot.toObject(Admin.class);
                            if (admin != null) {
                                textViewName.setText(admin.getFirstName() + " " + admin.getLastName());
                                textViewEmail.setText(admin.getEmail());
                                textViewPhone.setText(admin.getPhoneNumber());
                                textViewDepartment.setText(admin.getDepartment());
                            }
                        } else {
                            Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch profile.", Toast.LENGTH_SHORT).show());
        }
    }
}
