package com.example.mahomy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Link to Firebase authentication object:
        firebaseAuth = FirebaseAuth.getInstance();

// If authorized, move to MainActivity page:
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        public void register()
    {
            String email = emailText.getText().toString();
            String pass = passwordText.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(intent);
                            } else {
                                task.getException();
                            }
                        }
                    });
        }

        public void login() {
            String email = emailText.getText().toString();
            String pass = passwordText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(intent);
                            } else {
                                task.getException();
                            }
                        }
                    });
        }


        public static void logout() {
            firebaseAuth.signOut();
        }

    }
}