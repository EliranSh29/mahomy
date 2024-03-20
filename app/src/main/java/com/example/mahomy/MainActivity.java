package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private Button targetButton;
    private long startTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeButtons();
        startColorChange();

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


    private void startColorChange() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int row = random.nextInt(3);
                int col = random.nextInt(3);
                targetButton = buttons[row][col];
                targetButton.setBackgroundColor(Color.RED);
                startTime = System.currentTimeMillis();
            }
        }, 3000); // Change color every 3 seconds
    }




    private void onButtonClick(Button button) {
        if (button == targetButton) {
            long reactionTime = System.currentTimeMillis() - startTime;
            Toast.makeText(this, "Reaction time: " + reactionTime + " milliseconds", Toast.LENGTH_SHORT).show();
            button.setBackgroundColor(Color.BLUE);
            startColorChange(); // Start next color change
        } else {
            // Wrong button pressed, handle accordingly
        }
    }

}