package com.example.mahomy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class gameSeq extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private Button targetButton;
    private long startTime;
    private int level = 1;
    private int buttonsToRemember = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playSequence();
            }
        }, 1000); // Start after 1 second
    }

    private void playSequence() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < buttonsToRemember; i++) {
                    int row = random.nextInt(3);
                    int col = random.nextInt(3);
                    Button button = buttons[row][col];
                    button.setBackgroundColor(Color.RED);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundColor(Color.WHITE);
                        }
                    }, 500); // Change color back to white after 0.5 seconds
                }
                startTime = System.currentTimeMillis();
            }
        }, 1000); // Delay before playing sequence
    }

    private void onButtonClick(Button button) {
        if (button == targetButton) {
            long reactionTime = System.currentTimeMillis() - startTime;
            Toast.makeText(this, "Reaction time: " + reactionTime + " milliseconds", Toast.LENGTH_SHORT).show();
            button.setBackgroundColor(Color.BLUE);
            if (buttonsToRemember < 9) {
                buttonsToRemember++;
                level++;
                startGame();
            } else {
                Toast.makeText(this, "Congratulations! You completed all levels!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Wrong button pressed, handle accordingly
        }
    }
}
