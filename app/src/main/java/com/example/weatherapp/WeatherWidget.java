package com.example.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Date date  = new Date(System.currentTimeMillis());

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_WEATHER, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("WEATHER_JSON_STRING", "");

        if (!jsonString.isEmpty()) {
            WeatherJSON weather = new WeatherJSON();
            try {
                weather = new ObjectMapper().readValue(jsonString, WeatherJSON.class);
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

            try{
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setComponent(new ComponentName(context.getPackageName(),"MainActivity.class"));

                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
                views.setOnClickPendingIntent(R.id.temperature, pendingIntent);
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }catch (ActivityNotFoundException e){
                Toast.makeText(context.getApplicationContext(), "Problem loading the appliaction", Toast.LENGTH_SHORT).show();
            }

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