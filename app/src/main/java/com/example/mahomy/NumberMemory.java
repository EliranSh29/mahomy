// NumberMemory.java

package com.example.mahomy;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

public class NumberMemory extends AppCompatActivity {

    private TextView numberTextView;
    private EditText userInputEditText;
    private Button startButton;
    private Button submitButton;

    private int currentLevel = 1;
    private int numberToRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_memory);

        // Initialize UI elements
        numberTextView = findViewById(R.id.numberTextView);
        userInputEditText = findViewById(R.id.userInputEditText);
        startButton = findViewById(R.id.startButton);
        submitButton = findViewById(R.id.submitButton);

        // Set click listeners for buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput();
            }
        });
    }

    // Method to start the number memory game
    private void startGame() {
        startButton.setEnabled(false);
        userInputEditText.setVisibility(View.GONE); // Hide the EditText initially
        userInputEditText.setText("");
        submitButton.setVisibility(View.VISIBLE); // Show the submit button
        generateNumber();
        showNumber();
    }

    // Method to generate a random number to remember
    private void generateNumber() {
        Random random = new Random();
        int minRange = (int) Math.pow(10, currentLevel - 1); // Minimum range based on current level
        int maxRange = (int) Math.pow(10, currentLevel) - 1; // Maximum range based on current level
        numberToRemember = random.nextInt(maxRange - minRange + 1) + minRange; // Generates a random number in the specified range
    }

    // Method to show the number to remember
    private void showNumber() {
        numberTextView.setVisibility(View.VISIBLE);
        numberTextView.setText(String.valueOf(numberToRemember));

        // Hide all buttons while showing the number
        startButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                numberTextView.setVisibility(View.GONE);
                userInputEditText.setVisibility(View.VISIBLE); // Show the EditText after displaying the number
                submitButton.setVisibility(View.VISIBLE); // Show the submit button
            }
        }, 6000); // Display number for 6 seconds
    }

    // Method to check user input and proceed to the next level or restart the game
    private void checkUserInput() {
        String userInput = userInputEditText.getText().toString().trim();
        if (!userInput.isEmpty()) {
            int userNumber = Integer.parseInt(userInput);
            if (userNumber == numberToRemember) {
                Toast.makeText(this, "Correct! Proceed to next level.", Toast.LENGTH_SHORT).show();
                currentLevel++;
            } else {
                Toast.makeText(this, "Wrong! Back to level 1.", Toast.LENGTH_SHORT).show();
                currentLevel = 1;
            }
        } else {
            Toast.makeText(this, "Please enter a number.", Toast.LENGTH_SHORT).show();
        }
        // Upload the current level to Firebase
        uploadLevelToFirebase(currentLevel);
        // Restart the game
        startGame();
    }

    // Method to upload the current level to Firebase database
    private void uploadLevelToFirebase(int level) {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Reference to the user's node in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Retrieve the user's current level for game 3 from the database
        userRef.child("level_game3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current level for game 3 from the database
                    Integer currentLevel = dataSnapshot.getValue(Integer.class);
                    // Check if the current level is higher than the level in the database
                    if (currentLevel == null || level > currentLevel) {
                        // If so, update the current level for game 3 in the database
                        userRef.child("level_game3").setValue(level);
                    }
                } else {
                    // If no data exists, simply set the current level as the level for game 3
                    userRef.child("level_game3").setValue(level);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
