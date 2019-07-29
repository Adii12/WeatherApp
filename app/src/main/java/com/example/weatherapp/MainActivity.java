package com.example.weatherapp;


import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.Typeface;
import android.view.View;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    GestureDetector gestureDetector;
    ProgressBar loader;
    TextView weatherIcon, currentTemperature, cityField, updateField, infoField, dateField, humidityField, pressureField, windField, maxminField, sunriseField, sunsetField;
    Typeface weatherFont;
    Button selectCity;
    LinearLayout temperatureLayout, moreDetailsLayout;

    String city="targu-mures";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        gestureDetector = new GestureDetector(this,this);
        temperatureLayout=findViewById(R.id.temperatureLayout);
        moreDetailsLayout=findViewById(R.id.moredetails_layout);
        dateField=findViewById(R.id.date_field);
        cityField=findViewById(R.id.cityField);
        updateField=findViewById(R.id.updated_field);
        weatherIcon=findViewById(R.id.weather_icon);
        currentTemperature=findViewById(R.id.temperature_field);
        selectCity=findViewById(R.id.button);
        loader=findViewById(R.id.progress);
        infoField=findViewById(R.id.info_field);

        humidityField=findViewById(R.id.humidity_field);
        pressureField=findViewById(R.id.pressure_field);
        windField=findViewById(R.id.wind_field);
        maxminField=findViewById(R.id.maxmin_field);
        sunriseField=findViewById(R.id.sunrise_field);
        sunsetField=findViewById(R.id.sunset_field);

        weatherFont = getResources().getFont(R.font.icons);
        weatherIcon.setTypeface(weatherFont);

        moreDetailsLayout.setVisibility(View.GONE);
        loadTask(city);

       selectCity.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
               alert.setTitle("Change city");

               final EditText input = new EditText(MainActivity.this);
               input.setText(city);

               LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
               input.setLayoutParams(lp);

               alert.setView(input);

               alert.setPositiveButton("Change",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialogInterface, int i) {
                               city = input.getText().toString();
                               loadTask(city);
                           }
                       });
               alert.setNegativeButton("Cancel",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                           }
                       });
               alert.show();
           }
       });




    }

    protected void onResume(){
        super.onResume();
        Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        loadTask(city);
    }


    public void loadTask(String query){
        if(Function.isNetworkAvailable(getApplicationContext())) {
            getWeather task = new getWeather();
            task.execute(query);
        }
        else{
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }


    class getWeather extends AsyncTask<String, Void, String>{

        WeatherJSON weather = new WeatherJSON();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            cityField.setText("Loading...");
            currentTemperature.setText("");
            infoField.setText("");
        }


        protected String doInBackground(String...args){

            String jsonString = Function.getConnection("http://api.openweathermap.org/data/2.5/weather?q="+args[0]+"&units=metric&appid=2397cac5640f1ba782245157aab0343b");
            ObjectMapper mapper = new ObjectMapper();

            try{
                weather = mapper.readValue(jsonString,WeatherJSON.class);
            }catch (JsonParseException e){
                e.printStackTrace();
            }catch (JsonMappingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }


            return jsonString;
        }


        @Override
        protected void onPostExecute(String jsonString){
            int temp = (int)Math.round(weather.getMain().getTemp());
            int temp_min = (int)Math.round(weather.getMain().getTemp_min());
            int temp_max = (int)Math.round(weather.getMain().getTemp_max());

            SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
            Date hours = new Date(System.currentTimeMillis());

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");
            Date date = new Date(System.currentTimeMillis());

            cityField.setText(weather.getName()+", "+weather.getSys().getCountry());
            dateField.setText(formatDate.format(date));
            updateField.setText(formatHour.format(hours));

            currentTemperature.setText(temp+"º");
            infoField.setText(weather.getWeather()[0].getDescription());
            weatherIcon.setText(Html.fromHtml(Function.setIcon(weather.getWeather()[0].getId(),weather.getSys().getSunrise()*1000,weather.getSys().getSunset()*1000)));

            humidityField.setText("Humidity: "+weather.getMain().getHumidity()+"%");
            pressureField.setText("Pressure: "+weather.getMain().getPressure()+"hpa");
            windField.setText("Wind: "+weather.getWind().getSpeed()+"m/s");

            maxminField.setText("Min/Max: "+temp_min+"/"+temp_max+"º");
            sunriseField.setText("Sunrise: "+formatHour.format(weather.getSys().getSunrise()*1000));
            sunsetField.setText("Sunset: "+formatHour.format(weather.getSys().getSunset()*1000));

            loader.setVisibility(View.GONE);

        }
    }

    public void showDetails(){
        temperatureLayout.setVisibility(View.GONE);
        moreDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void showTemp(){
        temperatureLayout.setVisibility(View.VISIBLE);
        moreDetailsLayout.setVisibility(View.GONE);


    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float X, float Y) {
        if (motionEvent1.getY() - motionEvent2.getY() > 50) {
            Toast.makeText(MainActivity.this, "You Swiped up!", Toast.LENGTH_LONG).show();
            return true;
        }

        if (motionEvent2.getY() - motionEvent1.getY() > 50) {
            Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
            loadTask(city);
            return true;
        }

        if (motionEvent1.getX() - motionEvent2.getX() > 50) {
            Toast.makeText(MainActivity.this, "You Swiped Left!", Toast.LENGTH_LONG).show();
            showDetails();
            return true;
        }

        if (motionEvent2.getX() - motionEvent1.getX() > 50) {
            Toast.makeText(MainActivity.this, "You Swiped Right!", Toast.LENGTH_LONG).show();

            showTemp();
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
