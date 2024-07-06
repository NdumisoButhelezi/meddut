package com.example.newdutmed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientRegister extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword, phoneNumber, gender, yearOfBirth;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar); // Make sure you have a ProgressBar in your XML

        // Initialize views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        gender = findViewById(R.id.gender);
        yearOfBirth = findViewById(R.id.yearOfBirth);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String confirmPasswordText = confirmPassword.getText().toString();

            if (!isValidEmail(emailText) || TextUtils.isEmpty(firstName.getText()) || TextUtils.isEmpty(lastName.getText())) {
                Toast.makeText(ClientRegister.this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(ClientRegister.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE); // Show progress bar

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE); // Hide progress bar
                        if (task.isSuccessful()) {
                            Client client = new Client(
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    emailText,
                                    phoneNumber.getText().toString(),
                                    gender.getText().toString(),
                                    yearOfBirth.getText().toString()
                            );
                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(client)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ClientRegister.this, "Client registered successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ClientRegister.this, ClientMainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ClientRegister.this, "Failed to save client profile", Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(ClientRegister.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
