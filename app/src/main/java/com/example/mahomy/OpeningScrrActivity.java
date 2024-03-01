package com.example.mahomy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mahomy.MainActivity;
import com.example.mahomy.gameSeqActivity;

public class OpeningScrrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_scrr);

        Button buttonGame1 = findViewById(R.id.button_game1);
        Button buttonGame2 = findViewById(R.id.button_game2);
        Button buttonGame3 = findViewById(R.id.button_game3);

        buttonGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the first mini game activity
                Intent intent = new Intent(OpeningScrrActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the second mini game activity
                Intent intent = new Intent(OpeningScrrActivity.this, gameSeqActivity.class);
                startActivity(intent);
            }
        });

      //  buttonGame3.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
                // Start the third mini game activity (for future use)
              //  Intent intent = new Intent(OpeningScrrActivity.this, MiniGame3Activity.class);
             //   startActivity(intent);
           // }
       // });
    }
}
