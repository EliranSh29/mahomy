package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRecordsActivity extends AppCompatActivity {

    private TextView reactionTimeTextView;
    private TextView highestLevelGame2TextView;
    private TextView levelGame3TextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_records);

        reactionTimeTextView = findViewById(R.id.reactionTimeTextView);
        highestLevelGame2TextView = findViewById(R.id.highestLevelGame2TextView);
        levelGame3TextView = findViewById(R.id.levelGame3TextView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long reactionTime = dataSnapshot.child("reaction_time").getValue(Long.class);
                        Integer highestLevelGame2 = dataSnapshot.child("highest_level_game2").getValue(Integer.class);
                        Integer levelGame3 = dataSnapshot.child("level_game3").getValue(Integer.class);

                        if (reactionTime != null) {
                            reactionTimeTextView.setText(String.valueOf(reactionTime));
                        }
                        if (highestLevelGame2 != null) {
                            highestLevelGame2TextView.setText(String.valueOf(highestLevelGame2));
                        }
                        if (levelGame3 != null) {
                            levelGame3TextView.setText(String.valueOf(levelGame3));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
}
