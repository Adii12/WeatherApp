package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.Typeface;
import android.widget.Toast;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ProgressBar loader;
    TextView weatherIcon, currentTemperature, cityField, updateField, infoField, dateField;
    Typeface weatherFont;
    Button selectCity;

    String city="targu-mures";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //face aplicatia fullscreen
        setContentView(R.layout.activity_main);


        dateField=findViewById(R.id.date_field);
        cityField=findViewById(R.id.cityField);
        updateField=findViewById(R.id.updated_field);
        weatherIcon=findViewById(R.id.weather_icon);
        currentTemperature=findViewById(R.id.temperature_field);
        selectCity=findViewById(R.id.button);
        loader=findViewById(R.id.progress);
        infoField=findViewById(R.id.info_field);

        weatherFont = getResources().getFont(R.font.icons);
        weatherIcon.setTypeface(weatherFont);


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

        final GestureDetector mDetector = new GestureDetector(MainActivity.this, new MyGestureDetector());

        weatherIcon.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mDetector.onTouchEvent(motionEvent);

            }
        });

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



        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            cityField.setText("Loading...");
            currentTemperature.setText("");
            infoField.setText("");
        }


        protected String doInBackground(String...args){

            String xml = Function.getConnection("http://api.openweathermap.org/data/2.5/weather?q="+args[0]+"&units=metric&appid=2397cac5640f1ba782245157aab0343b");
            return xml;

        }


        @Override
        protected void onPostExecute(String xml){
            try{
                JSONObject json = new JSONObject(xml);

                if(json != null){
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    JSONObject sys = json.getJSONObject("sys");
                    SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
                    Date hours = new Date(System.currentTimeMillis());

                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");
                    Date date = new Date(System.currentTimeMillis());

                    cityField.setText(json.getString("name")+", "+sys.getString("country"));
                    dateField.setText(formatDate.format(date));
                    updateField.setText(formatHour.format(hours));
                    currentTemperature.setText(main.getInt("temp")+"º");
                    infoField.setText(details.getString("main"));
                    weatherIcon.setText(Html.fromHtml(Function.setIcon(details.getInt("id"),sys.getLong("sunrise")*1000,sys.getLong("sunset")*1000)));



                    loader.setVisibility(View.GONE);
                }
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "Error. Wrong city name!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class MyGestureDetector implements GestureDetector.OnGestureListener{

        private static final int MIN_DISTANCE=10;
        private static final int VELOCITY_THRESHOLD=100;

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            boolean result=false;

           try {
               float diffY = event2.getY() - event1.getY();
               float diffx = event2.getX() - event2.getY();

               if(Math.abs(diffx)>Math.abs(diffY)){
                   if(Math.abs(diffx)>MIN_DISTANCE && Math.abs(velocityX)>VELOCITY_THRESHOLD){
                       if(diffx>0){
                           //swipe right
                       }
                       else {
                           //swipe left
                       }
                       result=true;
                   }
               }else if(Math.abs(diffY)>MIN_DISTANCE && Math.abs(velocityY)>VELOCITY_THRESHOLD){
                   if(diffY>0){
                       loadTask(city);
                   }
                   else{
                       //swipe top
                   }

                   result=true;
               }

           }catch (Exception ex){
               ex.printStackTrace();
           }
           return result;
        }


    }


}
