package com.example.weatherapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String SP_WEATHER = "SP_WEATHER";//adauga in app

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_WEATHER, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("WEATHER_JSON_STRING", "");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Date date  = new Date(System.currentTimeMillis());

        if (!jsonString.isEmpty()) {
            WeatherJSON weather = new WeatherJSON();
            try {
                weather = new ObjectMapper().readValue("http://api.openweathermap.org/data/2.5/weather?q=targu-mures&units=metric&appid=2397cac5640f1ba782245157aab0343b", WeatherJSON.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Log.d("debug", "weather.getMain(): " + weather.getMain());
            if (weather.getMain() != null) {
                int temp = (int) Math.round(weather.getMain().getTemp());
                CharSequence widgetText = context.getString(R.string.appwidget_text);
                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
                views.setTextViewText(R.id.appwidget_text, widgetText);
                views.setTextViewText(R.id.temperature, temp + "º");
                views.setTextViewText(R.id.city,weather.getName());
                views.setTextViewText(R.id.info, weather.getWeather()[0].getDescription());
                views.setTextViewText(R.id.date, sdf.format(date));
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }



    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

