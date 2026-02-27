package com.example.androiduitesting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Initialize views
        TextView cityTextView = findViewById(R.id.city_text);
        Button backButton = findViewById(R.id.back_button);

        // Get the city name from the intent that started this activity
        String cityName = getIntent().getStringExtra("CITY_NAME");

        // Display the city name (with a default if null)
        if (cityName != null) {
            cityTextView.setText(cityName);
        } else {
            cityTextView.setText("No city selected");
        }

        // Set up back button to close this activity and return to MainActivity
        backButton.setOnClickListener(v -> {
            finish(); // This closes ShowActivity and returns to MainActivity
        });
    }
}