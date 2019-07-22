package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.Typeface;
import android.widget.Toast;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView weatherIcon, currentTemperature, cityField, updateField;
    Typeface weatherFont;
    EditText cityChange;
    String city;

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
        weatherIcon.setText(Html.fromHtml(Function.setIcon(800,1485720272,1485766550)));
        currentTemperature.setText("28 ºC");

    }

    public void ChangeCity(View view){
        cityField=findViewById(R.id.cityField);
        cityChange=findViewById(R.id.cityText);
        city = cityChange.getText().toString();
        cityField.setText(city);
    }

    public void Update(View view){
        String lastUpdated = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
        updateField=findViewById(R.id.updated_field);
        updateField.setText("Last Updated: "+lastUpdated);
    }
}
