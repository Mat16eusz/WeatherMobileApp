package com.example.zadanie_6;

import java.util.ArrayList;

public class WeatherData {
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    private Main main;
    private String name;

    private Weather getWeather() {
        return weather.get(0);
    }

    private Main getMain() {
        return main;
    }

    public String getIcon() {
        return getWeather().icon;
    }

    public String getName() {
        return name;
    }

    public Float getTemp() {
        return getMain().temp;
    }

    public Float getTempMin() {
        return getMain().temp_min;
    }

    public Float getTempMax() {
        return getMain().temp_max;
    }

    public Integer getPressure() {
        return getMain().pressure;
    }

    public Integer getHumidity() {
        return getMain().humidity;
    }
}

class Weather {
    public String icon;
}

class Main {
    public Float temp;
    public Integer humidity;
    public Integer pressure;
    public Float temp_min;
    public Float temp_max;
}
