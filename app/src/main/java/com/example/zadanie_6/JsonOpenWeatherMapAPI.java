package com.example.zadanie_6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonOpenWeatherMapAPI {
    @GET("data/2.5/weather?,pl&units=metric&APPID=d9138185a62487c81ccc410a6b34606e")
    Call<WeatherData> getWeatherData(@Query("q") String city);
}
