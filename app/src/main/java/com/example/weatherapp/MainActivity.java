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
    String city, lastUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        if(Function.isNetworkAvailable(getApplicationContext())==false)
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();

        updateField=findViewById(R.id.updated_field);
        cityField=findViewById(R.id.cityField);
        weatherFont = getResources().getFont(R.font.icons);
        weatherIcon=findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        cityField.setText("Enter city below");
        currentTemperature=findViewById(R.id.temperature_field);
        weatherIcon.setText(Html.fromHtml(Function.setIcon(800,1485720272,1485766550)));
        currentTemperature.setText("28 ºC");
        lastUpdated = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
        updateField.setText("Last Updated: "+lastUpdated);

    }

    public void ChangeCity(View view){

        cityChange=findViewById(R.id.cityText);
        city = cityChange.getText().toString();
        cityField.setText(city);
        Toast.makeText(getApplicationContext(),"City Changed!",Toast.LENGTH_SHORT).show();
    }

    public void Update(View view){
        lastUpdated = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
        updateField.setText("Last Updated: "+lastUpdated);
    }
}
