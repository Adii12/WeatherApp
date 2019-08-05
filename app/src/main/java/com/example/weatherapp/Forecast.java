package com.example.weatherapp;

public class Forecast {
    private int cnt;
    private List[] list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }
}

class List{

    private long dt;
    private MainForecast main;
    private String dt_txt;
    private ForecastWeather[] weather;



    public ForecastWeather[] getForecastWeather() {
        return weather;
    }

    public void setForecastWeather(ForecastWeather[] weather) {
        this.weather = weather;
    }



    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }



    public MainForecast getMain() {
        return main;
    }

    public void setMain(MainForecast main) {
        this.main = main;
    }




    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}

class MainForecast{
    private double temp;


    public double getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return ""+temp;
    }
}

class ForecastWeather{
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }
}