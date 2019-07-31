package com.example.weatherapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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


    //realizeaza conexiunea cu api
    public static String getConnection(String targetURL) {

        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;

            int status = connection.getResponseCode();

            if(status != HttpURLConnection.HTTP_OK){
                is=connection.getErrorStream();
            }
            else{
                is=connection.getInputStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line="";

            StringBuffer response = new StringBuffer();



            while( (line=br.readLine()) != null ){
                response.append(line);
                response.append("\n");
           }

            br.close();

            return response.toString();

        }catch (Exception ex){
            ex.printStackTrace();
            return "";
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
                icon = "&#xf00d"; //sunny day
            }
            else{
                icon= "&#xf02e"; //clear night
            }
        }
        else{
            switch (id){
                case 2:
                    icon="&#xf01e"; //thunderstorm
                    break;

                case 3:
                    icon="&#xf01c"; //drizzle
                    break;

                case 5:
                    icon="&#xf019"; //rain
                    break;

                case 6:
                    icon="&#xf01b"; //snow
                    break;

                case 7:
                    icon="&#xf014"; //fog
                    break;

                case 8:
                    icon="&#xf002"; //cloudy
                    break;
            }
        }

        return icon;
    }


}
