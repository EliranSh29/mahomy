package com.example.mahomy;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class CustomButton extends androidx.appcompat.widget.AppCompatButton {

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // Set background color
        setBackgroundColor(Color.BLUE);


        // Set onClick listener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });

        // Set other properties as needed
    }

    // Method to initialize buttons
    public static void initializeButtons4(CustomButton[][] buttons, final View.OnClickListener listener) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                final CustomButton button = buttons[i][j];
                button.setOnClickListener(listener);
                button.setBackgroundColor(Color.BLUE); // Set default background color
            }
        }
    }
}
