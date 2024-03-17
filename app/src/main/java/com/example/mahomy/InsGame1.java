package com.example.mahomy;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class InsGame1 extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_game1);

        textToSpeech = new TextToSpeech(this, this);

        // Show the initial message
        speak("how are you");

        // Set up the repeat button
        Button buttonRepeat = findViewById(R.id.buttonRepeat);
        buttonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak("how are you");
            }
        });

        // Set up the continue button
        Button buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsGame1.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set up the navigate to OpeningScrrActivity button
        Button buttonOpenScrr = findViewById(R.id.buttonOpenScrr);
        buttonOpenScrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsGame1.this, OpeningScrrActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for speech
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported
            }
        } else {
            // Handle initialization failure
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown text-to-speech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void speak(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
