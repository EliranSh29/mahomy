package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

        initializeButtons();
        startFirstColorChange();
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
                buttons[i][j].setBackgroundColor(Color.BLUE); // Set background color to blue
            }
        }
    }

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

    private void onButtonClick(Button button) {
        if (button == targetButton) {
            colorChangeTimer.cancel(); // Stop the color change timer
            long reactionTime = System.currentTimeMillis() - startTime;
            Toast.makeText(this, "Reaction time: " + reactionTime + " milliseconds", Toast.LENGTH_SHORT).show();
            button.setBackgroundColor(Color.BLUE);

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
}
