package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Array to hold buttons in the grid
    private Button[][] buttons = new Button[3][3];
    private Button targetButton;
    private long startTime;
    private CountDownTimer colorChangeTimer;
    private CountDownTimer delayTimer;
    private boolean isSameButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons(); // Initialize grid buttons
        startFirstColorChange(); // Start the color change sequence
    }

    // Method to initialize grid buttons
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
                buttons[i][j].setBackgroundColor(Color.BLUE); // Set background color to blue
            }
        }
    }

    // Method to start the first color change
    private void startFirstColorChange() {
        delayTimer = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not needed
            }

            @Override
            public void onFinish() {
                startColorChange();
            }
        };
        delayTimer.start(); // Start the first color change after 1.5 seconds
    }

    // Method to start the continuous color change sequence
    private void startColorChange() {
        colorChangeTimer = new CountDownTimer(Long.MAX_VALUE, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                changeColor();
            }

            @Override
            public void onFinish() {
                // Not needed
            }
        };
        colorChangeTimer.start();
    }

    // Method to change the color of a random button
    private void changeColor() {
        Random random = new Random();
        int row, col;

        row = random.nextInt(3);
        col = random.nextInt(3);
        if (targetButton != null)
            targetButton.setBackgroundColor(Color.BLUE);
        targetButton = buttons[row][col];
        targetButton.setBackgroundColor(Color.RED);
        startTime = System.currentTimeMillis();
    }

    // Method called when a button in the grid is clicked
    private void onButtonClick(Button button) {
        if (button == targetButton) {
            colorChangeTimer.cancel(); // Stop the color change timer
            long reactionTime = System.currentTimeMillis() - startTime;
            Toast.makeText(this, "Reaction time: " + reactionTime + " milliseconds", Toast.LENGTH_SHORT).show();
            button.setBackgroundColor(Color.BLUE);

            // Upload the reaction time to Firebase
            uploadReactionTimeToFirebase(reactionTime);

            // Add a delay of 1.2 seconds before starting the next color change
            new CountDownTimer(1200, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Not needed
                }

                @Override
                public void onFinish() {
                    startColorChange();
                }
            }.start();
        } else {
            // Wrong button pressed, handle accordingly
        }
    }

    // Method to upload reaction time to Firebase
    private void uploadReactionTimeToFirebase(long reactionTime) {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Reference to the user's node in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Retrieve the user's current reaction time for game 1 from the database
        userRef.child("reaction_time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current reaction time for game 1 from the database
                    Long currentReactionTime = dataSnapshot.getValue(Long.class);
                    // Check if the current reaction time is lower than the reaction time in the database
                    if (currentReactionTime == null || reactionTime < currentReactionTime) {
                        // If so, update the reaction time for game 1 in the database
                        userRef.child("reaction_time").setValue(reactionTime);
                    }
                } else {
                    // If no data exists, simply set the current reaction time as the reaction time for game 1
                    userRef.child("reaction_time").setValue(reactionTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}
