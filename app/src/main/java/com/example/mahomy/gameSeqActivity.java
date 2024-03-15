package com.example.mahomy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class gameSeqActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private Button targetButton;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playSequence();
            }
        }, 1000); // Start after 1 second
    }

    private void playSequence() {
        playSequenceStep(0);
    }

    private void playSequenceStep(final int step) {
        if (step >= buttonsToRemember) {
            startTime = System.currentTimeMillis();
            return;
        }

        Random random = new Random();
        int row = random.nextInt(3);
        int col = random.nextInt(3);

        targetButton = buttons[row][col];

        targetButton.setBackgroundColor(Color.RED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                targetButton.setBackgroundColor(Color.BLUE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSequenceStep(step + 1);
                    }
                }, 500); // Change color back to blue after 0.5 seconds
            }
        }, 1000); // Change color to red after 1 second
    }


    private void onButtonClick(Button button) {
        if (button==targetButton)
        {

            targetButton.setBackgroundColor(Color.BLUE);
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
