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
    public static void fetchData(String city) {
        final String API_KEY="2397cac5640f1ba782245157aab0343b";


        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;

        }catch (Exception ex){
            //return null;
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }


    }

    public static String setIcon(int actualId, long sunrise, long sunset){
        int id = actualId/100;
        String icon="";

        if(actualId==800){
            long currentTime=new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset){
                icon = "&xf00d";
            }
            else{
                icon= "&#xf02e";
            }
        }
        else{
            switch (id){
                case 2:
                    icon="&#xf01e";
                    break;

                case 3:
                    icon="&#xf01e";
                    break;

                case 5:
                    icon="&#xf019";
                    break;

                case 6:
                    icon="&#xf01b";
                    break;

                case 7:
                    icon="&#xf01c";
                    break;

                case 8:
                    icon="&#xf014";
                    break;
            }
        }

        return icon;
    }


}
