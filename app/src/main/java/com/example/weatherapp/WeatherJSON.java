package com.example.weatherapp;

public class WeatherJSON {
    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private String visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private long timezone;
    private int id;
    private String name;
    private int cod;

    public WeatherJSON(){}

    public Coord getCoord() {
        return coord;
    }
    public void setCoord(Coord coord) {
        this.coord = coord;
    }
    public Weather[] getWeather() {
        return weather;
    }
    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    public Main getMain() {
        return main;
    }
    public void setMain(Main main) {
        this.main = main;
    }
    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public Wind getWind() {
        return wind;
    }
    public void setWind(Wind wind) {
        this.wind = wind;
    }
    public Clouds getClouds() {
        return clouds;
    }
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }
    public long getDt() {
        return dt;
    }
    public void setDt(long dt) {
        this.dt = dt;
    }
    public Sys getSys() {
        return sys;
    }
    public void setSys(Sys sys) {
        this.sys = sys;
    }
    public long getTimezone(){
        return timezone;
    }
    public void setTimezone(){
        this.timezone=timezone;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCod() {
        return cod;
    }
    public void setCod(int cod) {
        this.cod = cod;
    }
}

class Coord{
    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
}

class Weather{
    private int id;
    private String main;
    private String description;
    private String icon;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}

class Main{
    private double temp;
    private int pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;

    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public double getTemp_min() {
        return temp_min;
    }
    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }
    public double getTemp_max() {
        return temp_max;
    }
    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}

class Wind{
    private double speed;
    private int deg;

    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public int getDeg() {
        return deg;
    }
    public void setDeg(int deg) {
        this.deg = deg;
    }
}

class Clouds{
    private int all;

    public int getAll(){
        return all;
    }

    public void setAll(int all){
        this.all=all;
    }
}

class Sys{
    private int type;
    private int id;
    private double message;
    private String country;
    private long sunrise;
    private long sunset;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getMessage() {
        return message;
    }
    public void setMessage(double message) {
        this.message = message;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public long getSunrise() {
        return sunrise;
    }
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    public long getSunset() {
        return sunset;
    }
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}

