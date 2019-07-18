package com.example.weatherapp;

    import android.content.Context;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import java.io.*;
    import java.net.*;
    import java.util.Date;

public class Function {

    //functia verifica daca user-ul este conectat la internet
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED){
            return true;
        }
        else
            return false;
    }

    //TO DO :::
    public static void executGet(String targetURL) {

        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);


        }catch (Exception ex){
            //return null;
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }


    }


}
