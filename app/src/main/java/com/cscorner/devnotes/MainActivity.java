package com.cscorner.devnotes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent; //For intent
import android.os.Handler; //for Handler

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 2 seconds, move to LoginActivity
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish(); // Close splash screen
            }
        }, SPLASH_TIME);
    }
}


