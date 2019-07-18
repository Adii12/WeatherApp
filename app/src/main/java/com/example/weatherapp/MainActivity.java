package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Typeface;

public class MainActivity extends AppCompatActivity {

    TextView weatherIcon;
    Typeface weatherFont = Typeface.createFromAsset(getAssets(),"res/font/icons.ttf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        weatherIcon=(TextView) findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        weatherIcon.setText("&#xf00d");

    }
}
