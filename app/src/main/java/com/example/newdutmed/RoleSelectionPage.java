package com.example.newdutmed;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection_page);

        Button buttonAdmin = findViewById(R.id.buttonAdmin);
        Button buttonClient = findViewById(R.id.buttonClient);

        buttonAdmin.setOnClickListener(v -> startActivity(new Intent(RoleSelectionPage.this, AdminLogin.class)));
        buttonClient.setOnClickListener(v -> startActivity(new Intent(RoleSelectionPage.this, ClientLogin.class)));
    }
}
