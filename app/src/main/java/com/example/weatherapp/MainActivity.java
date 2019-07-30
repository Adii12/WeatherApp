package com.example.weatherapp;


import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ProgressBar loader;
    TextView weatherIcon, currentTemperature, cityField, updateField, infoField, dateField, humidityField, pressureField, windField, maxminField, sunriseField, sunsetField;
    Typeface weatherFont;
    Button selectCity;
    LinearLayout temperatureLayout, moreDetailsLayout;
    RelativeLayout mainLayout;

    String city="targu-mures";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        temperatureLayout=findViewById(R.id.temperatureLayout);
        moreDetailsLayout=findViewById(R.id.moredetails_layout);
        mainLayout=findViewById(R.id.main_layout);

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
               temperatureLayout.setVisibility(View.GONE);
               moreDetailsLayout.setVisibility(View.VISIBLE);

           }
       });

        moreDetailsLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                temperatureLayout.setVisibility(View.VISIBLE);
                moreDetailsLayout.setVisibility(View.GONE);

            }
        });



}

    protected void onResume(){
        super.onResume();
        Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        loadTask(city);
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
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
            if(weather.getMain()==null){//ca sa nu mai dea crash daca orasul nu exista
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

                loader.setVisibility(View.GONE);
            }
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

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
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
}
