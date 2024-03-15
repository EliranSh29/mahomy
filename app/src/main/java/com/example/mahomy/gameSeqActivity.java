package com.example.mahomy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gameSeqActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private List<Button> sequence = new ArrayList<>();
    private int sequenceIndex = 0;
    private long startTime;
    private int level = 1;
    private int buttonsToRemember = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_seq);

        initializeButtons();
        startGame();
    }

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
            }
        }
    }

    private void startGame() {
        // Cancel any pending callbacks to prevent accumulation of delayed actions
        new Handler().removeCallbacksAndMessages(null);

        sequence.clear();
        generateSequence();
        playSequence();
    }

    private void generateSequence() {
        for (int i = 0; i < buttonsToRemember; i++) {
            Random random = new Random();
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            sequence.add(buttons[row][col]);
        }
    }

    private void playSequence() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playNextButtonInSequence(0);
            }
        }, 1000); // Start after 1 second
    }

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
            // All buttons in the sequence have been played
            startTime = System.currentTimeMillis();
        }
    }


    private void onButtonClick(Button button) {
        if (button == sequence.get(sequenceIndex)) {
            sequenceIndex++;
            if (sequenceIndex == buttonsToRemember) {
                if (buttonsToRemember < 9) {
                    buttonsToRemember++;
                    sequenceIndex = 0;
                    startGame();
                } else {
                    Toast.makeText(this, "Congratulations! You completed all levels!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Wrong button pressed, handle accordingly
            // For simplicity, you can restart the game when the wrong button is pressed
            Toast.makeText(this, "Wrong button pressed! Try again.", Toast.LENGTH_SHORT).show();
            startGame();
        }
    }
}
