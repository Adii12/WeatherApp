package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView weatherIcon, currentTemperature;
    Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        if(Function.isNetworkAvailable(getApplicationContext())==false)
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();

        weatherFont = getResources().getFont(R.font.icons);
        weatherIcon=findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        currentTemperature=findViewById(R.id.temperature_field);
        weatherIcon.setText(Html.fromHtml(Function.setIcon(802,1485720272,1485766550)));
        currentTemperature.setText(String.format("%.2f",28.514)+"*C");

    }
}
