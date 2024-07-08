package com.example.newdutmed;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText queryEditText;
    private Button sendPromptButton, saveConversationButton;
    private ProgressBar sendPromptProgressBar;
    private ScrollView scrollView;
    private LinearLayout chatResponseLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views from layout
        queryEditText = findViewById(R.id.queryEditText);
        sendPromptButton = findViewById(R.id.sendPromptButton);
        saveConversationButton = findViewById(R.id.saveConversationButton);
        sendPromptProgressBar = findViewById(R.id.sendPromptProgressBar);
        scrollView = findViewById(R.id.scrollView);
        chatResponseLayout = findViewById(R. id.chatResponseLayout);

        // Set up button listeners
        sendPromptButton.setOnClickListener(v -> sendQuery());
        saveConversationButton.setOnClickListener(v -> saveConversation());
    }

    private void sendQuery() {
        String query = queryEditText.getText().toString();
        sendPromptProgressBar.setVisibility(ProgressBar.VISIBLE);

        // Assuming you have a method to get a ChatFutures instance
        ChatFutures chatModel = GeminiPro.getModel().chatFutures();

        // Use GeminiPro to send the query and handle the response
        GeminiPro.getResponse(chatModel, query, new ResponseCallBack() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    // Update UI with response
                    TextView responseView = new TextView(MainActivity.this);
                    responseView.setText(response);
                    chatResponseLayout.addView(responseView);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    sendPromptProgressBar.setVisibility(ProgressBar.GONE);
                });
            }

            @Override
            public void onError(Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    sendPromptProgressBar.setVisibility(ProgressBar.GONE);
                });
            }
        });
    }

    private void saveConversation() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String conversationText = queryEditText.getText().toString();

            // Create a conversation object
            Conversation conversation = new Conversation(conversationText, System.currentTimeMillis());

            // Save the conversation to Firestore
            db.collection("Users").document(userId).collection("Conversations")
                    .add(conversation)
                    .addOnSuccessListener(documentReference -> Toast.makeText(MainActivity.this, "Conversation saved successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to save conversation.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "You need to be logged in to save conversations.", Toast.LENGTH_SHORT).show();
        }
    }
}
