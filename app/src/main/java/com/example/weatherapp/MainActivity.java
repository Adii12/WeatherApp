package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ProgressBar loader;
    TextView weatherIcon, currentTemperature, cityField, updateField, infoField, dateField, humidityField, pressureField, windField, maxminField, sunriseField, sunsetField;
    TextView day2Time, day2Temps, day3Time, day3Temps, day4Time, day4Temps, day5Time, day5Temps;
    Typeface weatherFont;
    Button selectCity;
    LinearLayout temperatureLayout, moreDetailsLayout;
    RelativeLayout mainLayout;
    LinearLayout forecastLayout;
    Animation in_left,in_right,out_left,out_right,in_up,in_down,out_down, out_up;
    Switch switchButton;


    double longitude, latitude;

    String city="targu-mures";
    String SP_WEATHER = "SP_WEATHER"; //adauga in app

    String jsonString;

    SharedPreferences sharedPreferences; //adauga in app

    GPSTracker gps;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//notification bar transparent



        jsonString="http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=2397cac5640f1ba782245157aab0343b";
        sharedPreferences = getSharedPreferences(SP_WEATHER, MODE_PRIVATE);

       switchButton = findViewById(R.id.switch_button);

        in_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.in_left);//IN de la dreapta la stanga
        in_right= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.in_right);//IN de la stanga la dreapta
        in_up=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.in_up);//IN de jos in sus
        in_down=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.in_down);//IN de sus in jos
        out_left= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.out_left);//OUT de la dreapta la stanga
        out_right= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.out_right);//OUT de la stanga la dreapta
        out_up=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.out_up);//OUT de jos in sus
        out_down=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.out_down);//OUT de sus in jos

        temperatureLayout=findViewById(R.id.temperatureLayout);
        moreDetailsLayout=findViewById(R.id.moredetails_layout);
        mainLayout=findViewById(R.id.main_layout);
        forecastLayout=findViewById(R.id.forecast_layout);

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

        day2Time=findViewById(R.id.day2_time);
        day2Temps=findViewById(R.id.day2_temps);
        day3Time=findViewById(R.id.day3_time);
        day3Temps=findViewById(R.id.day3_temps);
        day4Time=findViewById(R.id.day4_time);
        day4Temps=findViewById(R.id.day4_temps);
        day5Time=findViewById(R.id.day5_time);
        day5Temps=findViewById(R.id.day5_temps);

        weatherFont = getResources().getFont(R.font.icons);
        weatherIcon.setTypeface(weatherFont);

        moreDetailsLayout.setVisibility(View.GONE);
        forecastLayout.setVisibility(View.GONE);
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
                                updateWidget();
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

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 50);
                    }
                    gps = new GPSTracker(MainActivity.this, MainActivity.this);

                    if(gps.CanGetLocation()){
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        Toast.makeText(getApplicationContext(), "LAT="+latitude+"\nLON="+longitude, Toast.LENGTH_LONG).show();
                    }
                    jsonString="http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&appid=2397cac5640f1ba782245157aab0343b";
                    loadTask(city);
                    updateWidget();
                }else{
                    Toast.makeText(getApplicationContext(), "Location disabled", Toast.LENGTH_SHORT).show();
                    jsonString="http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=2397cac5640f1ba782245157aab0343b";
                    loadTask(city);
                    updateWidget();
                }
            }
        });

        weatherIcon.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeBottom(){
                super.onSwipeBottom();
                loadTask(city);
            }
        });

        temperatureLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                moreDetailsLayout.setVisibility(View.VISIBLE);
                temperatureLayout.startAnimation(out_left);
                moreDetailsLayout.startAnimation(in_left);
                temperatureLayout.setVisibility(View.GONE);

            }
        });

        moreDetailsLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                temperatureLayout.setVisibility(View.VISIBLE);
                temperatureLayout.startAnimation(in_right);
                moreDetailsLayout.startAnimation(out_right);
                moreDetailsLayout.setVisibility(View.GONE);

            }
        });

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                mainLayout.setVisibility(View.GONE);
                forecastLayout.startAnimation(in_up);
                mainLayout.startAnimation(out_up);
                forecastLayout.setVisibility(View.VISIBLE);

            }
        });

        forecastLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                mainLayout.setVisibility(View.VISIBLE);
                mainLayout.startAnimation(in_down);
                forecastLayout.startAnimation(out_down);
                forecastLayout.setVisibility(View.GONE);

            }
        });

    }

    protected void onResume(){
        super.onResume();
        Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        loadTask(city);
        updateWidget();
    }


    public void loadTask(String city_name){
        if(Function.isNetworkAvailable(getApplicationContext())) {
            getWeather task = new getWeather();
            task.execute(city_name);
        }
        else{
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }


    class getWeather extends AsyncTask<String, Void, String>{

        WeatherJSON weather = new WeatherJSON();
        Forecast forecast = new Forecast();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            cityField.setText("Loading...");
            currentTemperature.setText("");
            infoField.setText("");
        }


        @SuppressLint("ApplySharedPref")
        protected String doInBackground(String...args){

            String json = Function.getConnection(jsonString);
            String forecastString =Function.getConnection("http://api.openweathermap.org/data/2.5/forecast?q="+args[0]+"&units=metric&appid=2397cac5640f1ba782245157aab0343b");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            sharedPreferences.edit().putString("WEATHER_JSON_STRING", jsonString).commit();

            try{
                weather = mapper.readValue(json,WeatherJSON.class);
                forecast = mapper.readValue(forecastString,Forecast.class);
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
            if(weather.getMain()==null){//ca sa nu mai dea crash daca orasul nu exista(null pointer)
                Toast.makeText(getApplicationContext(),"Error. Wrong city name",Toast.LENGTH_LONG).show();
            }
            else {
                int temp = (int) Math.round(weather.getMain().getTemp());
                int temp_min = (int) Math.round(weather.getMain().getTemp_min());
                int temp_max = (int) Math.round(weather.getMain().getTemp_max());

                SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
                Date hours = new Date(System.currentTimeMillis());

                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");
                Date date = new Date(System.currentTimeMillis());

                //PT MAIN LAYOUT
                cityField.setText(weather.getName() + ", " + weather.getSys().getCountry());
                dateField.setText(formatDate.format(date));
                updateField.setText(formatHour.format(hours));

                currentTemperature.setText(temp + "º");
                infoField.setText(weather.getWeather()[0].getDescription());
                weatherIcon.setText(Html.fromHtml(Function.setIcon(weather.getWeather()[0].getId(), weather.getSys().getSunrise() * 1000, weather.getSys().getSunset() * 1000)));

                humidityField.setText("Humidity: " + weather.getMain().getHumidity() + "%");
                pressureField.setText("Pressure: " + weather.getMain().getPressure() + "hpa");
                windField.setText("Wind: " + weather.getWind().getSpeed() + "m/s");

                maxminField.setText("Min/Max: " + temp_min + "/" + temp_max + "º");
                sunriseField.setText("Sunrise: " + formatHour.format(weather.getSys().getSunrise() * 1000));
                sunsetField.setText("Sunset: " + formatHour.format(weather.getSys().getSunset() * 1000));
                setBackground(weather.getWeather()[0].getId(), weather.getSys().getSunrise() * 1000, weather.getSys().getSunset() * 1000);
                

                //PT FORECAST LAYOUT
                day2Time.setText(formatHour.format(forecast.getList()[0].getDt()*1000)+"\n"+formatDate.format(forecast.getList()[0].getDt()*1000));
                day2Temps.setText((int)Math.round(forecast.getList()[0].getMain().getTemp())+"º\n");

                day3Time.setText(formatHour.format(forecast.getList()[1].getDt()*1000)+"\n"+formatDate.format(forecast.getList()[1].getDt()*1000));
                day3Temps.setText((int)Math.round(forecast.getList()[1].getMain().getTemp())+"º");

                day4Time.setText(formatHour.format(forecast.getList()[2].getDt()*1000)+"\n"+formatDate.format(forecast.getList()[2].getDt()*1000));
                day4Temps.setText((int)Math.round(forecast.getList()[2].getMain().getTemp())+"º");

                day5Time.setText(formatHour.format(forecast.getList()[3].getDt()*1000)+"\n"+formatDate.format(forecast.getList()[3].getDt()*1000));
                day5Temps.setText((int)Math.round(forecast.getList()[3].getMain().getTemp())+"º\n");

                loader.setVisibility(View.GONE);

                Log.d("debug", "updating widget");
                updateWidget();
            }
        }
    }

    public void setBackground(int actualId, long sunrise, long sunset){
        int id = actualId/100;
        long currentTime=new Date().getTime();

        if(currentTime>=sunrise && currentTime<sunset){
            if(actualId==800) {
                mainLayout.setBackgroundResource(R.drawable.sunny_gradient); //sunny
            }
            else{
                switch (id){
                    case 2:
                        mainLayout.setBackgroundResource(R.drawable.rainy_gradient); //thunderstorm
                        break;

                    case 3:
                        mainLayout.setBackgroundResource(R.drawable.rainy_gradient); //drizzle
                        break;

                    case 5:
                        mainLayout.setBackgroundResource(R.drawable.rainy_gradient); //rain
                        break;

                    case 6:
                        mainLayout.setBackgroundResource(R.drawable.snowy_gradient); //snow
                        break;

                    case 7:
                        mainLayout.setBackgroundResource(R.drawable.rainy_gradient); //fog
                        break;

                    case 8:
                        mainLayout.setBackgroundResource(R.drawable.gradient); //cloudy
                        break;
                }

            }
        }
        else{
            mainLayout.setBackgroundResource(R.drawable.night_gradient); //night
        }
    }

    private void updateWidget() {
        Intent intent = new Intent(this, WeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WeatherWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

}

class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;  //lungimea swipe ului
        private static final int SWIPE_VELOCITY_THRESHOLD = 100; //viteza

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            /*
             *
             * MotionEvent1 - locul unde a fost pus degetul pe ecran
             * MotionEvent2 - locul pana unde s-a facut swipe
             *
             * */
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) { // daca lungimea swipe ului pe axa X(stanga/dreapta) este mai mare decat cea de pe axa Y(sus/jos) =>swipe orizontal
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) { //daca lungimea swipe ului este destul de mare && viteza este destul de mare
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }
    public void onSwipeBottom() {
    }
}