// gameSeqActivity.java

package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gameSeqActivity extends AppCompatActivity {

    // Buttons array for the game grid
    private Button[][] buttons = new Button[3][3];
    // List to store the sequence of buttons to be remembered
    private List<Button> sequence = new ArrayList<>();
    // Index to keep track of the current button in the sequence
    private int sequenceIndex = 0;
    // Variable to store the starting time of the game
    private long startTime;
    // Variable to keep track of the current level of the game
    private int level = 1;
    // Variable to store the number of buttons to remember
    private int buttonsToRemember = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_seq);

        // Initialize buttons on the game grid
        initializeButtons();
        // Start the game
        startGame();
    }

    // Method to initialize buttons on the game grid
    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick((Button) v);
                    }
                });
                // Set initial background color of buttons to blue
                buttons[i][j].setBackgroundColor(Color.BLUE);
            }
        }
    }

    // Method to start the game
    private void startGame() {
        // Remove any pending callbacks to prevent accumulation of delayed actions
        new Handler().removeCallbacksAndMessages(null);

        // Clear the sequence and generate a new one
        sequence.clear();
        generateSequence();
        // Play the generated sequence
        playSequence();
    }

    // Method to generate a sequence of buttons to be remembered
    private void generateSequence() {
        Random random = new Random();
        int previousRow = -1;
        int previousCol = -1;

        for (int i = 0; i < buttonsToRemember; i++) {
            int row, col;

            // Generate random row and column until it's different from the previous one
            do {
                row = random.nextInt(3);
                col = random.nextInt(3);
            } while (row == previousRow && col == previousCol);

            // Store the current row and column as the previous ones for the next iteration
            previousRow = row;
            previousCol = col;

            // Add the button to the sequence
            sequence.add(buttons[row][col]);
        }
    }

    // Method to play the generated sequence
    private void playSequence() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playNextButtonInSequence(0);
            }
        }, 1000); // Start after 1 second
    }

    // Method to play the next button in the sequence
    private void playNextButtonInSequence(final int index) {
        if (index < buttonsToRemember) {
            Button button = sequence.get(index);
            button.setBackgroundColor(Color.RED);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundColor(Color.BLUE);
                    playNextButtonInSequence(index + 1); // Move to the next button in the sequence
                }
            }, 1000); // Change color for 1 second
        } else {
            // All buttons in the sequence have been played, start the game timer
            startTime = System.currentTimeMillis();
        }
    }

    // Method called when a button on the grid is clicked
    private void onButtonClick(Button button) {
        if (button == sequence.get(sequenceIndex)) {
            // Correct button pressed, move to the next button in the sequence
            sequenceIndex++;
            if (sequenceIndex == buttonsToRemember) {
                // If all buttons in the sequence are pressed correctly, increase the level and start a new game
                buttonsToRemember++;
                sequenceIndex = 0;
                startGame();
            }
        } else {
            // Wrong button pressed, handle accordingly (for simplicity, restart the game)
            Toast.makeText(this, "Wrong button pressed! Try again.", Toast.LENGTH_SHORT).show();
            buttonsToRemember = 1; // Reset to level 1
            startGame();
        }

        // Check if the current level is the highest reached and upload it to Firebase

            uploadHighestLevelToFirebase(buttonsToRemember);

    }

    // Method to upload the highest level reached to Firebase
    private void uploadHighestLevelToFirebase(int currentResult) {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Reference to the user's node in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Retrieve the user's highest level for game 2 from the database
        userRef.child("highest_level_game2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current highest level for game 2 from the database
                    Integer currentHighestLevel = dataSnapshot.getValue(Integer.class);
                    // Check if the current result is higher than the highest level in the database
                    if (currentHighestLevel == null || currentResult > currentHighestLevel) {
                        // If so, update the highest level for game 2 in the database
                        userRef.child("highest_level_game2").setValue(currentResult);
                    }
                } else {
                    // If no data exists, simply set the current result as the highest level for game 2
                    userRef.child("highest_level_game2").setValue(currentResult);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    // Method called when the activity is destroyed to remove any pending callbacks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove all pending callbacks to prevent memory leaks
        new Handler().removeCallbacksAndMessages(null);
    }

    // Method called when the activity is stopped to remove any pending callbacks
    @Override
    protected void onStop() {
        super.onStop();
        // Remove all pending callbacks to prevent memory leaks
        new Handler().removeCallbacksAndMessages(null);
    }
}
