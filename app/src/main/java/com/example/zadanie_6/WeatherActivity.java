package com.example.zadanie_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private TextView currentCity;
    private TextView currentTemp;
    private TextView currentPressure;
    private TextView currentHumidity;
    private TextView currentTempMin;
    private TextView currentTempMax;
    private ImageView currentImageWeather;
    private TextView connectionInternetErrorWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        currentCity = findViewById(R.id.currentCity);
        currentTemp = findViewById(R.id.currentTemp);
        currentPressure = findViewById(R.id.currentPressure);
        currentHumidity = findViewById(R.id.currentHumidity);
        currentTempMin = findViewById(R.id.currentTempMin);
        currentTempMax = findViewById(R.id.currentTempMax);
        currentImageWeather = findViewById(R.id.currentImageWeather);
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        connectionInternetErrorWeatherActivity = findViewById(R.id.connectionInternetErrorWeatherActivity);

        Intent intent = getIntent();
        String city = intent.getStringExtra("KEY_CITY_SELECTION");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonOpenWeatherMapAPI jsonOpenWeatherMapAPI = retrofit.create(JsonOpenWeatherMapAPI.class);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
                if (connected == false) {
                    connectionInternetErrorWeatherActivity.setVisibility(View.VISIBLE);
                    currentCity.setText("-");
                    currentTemp.setText("- °C");
                    currentPressure.setText("- hPa");
                    currentHumidity.setText("- %");
                    currentTempMin.setText("- °C");
                    currentTempMax.setText("- °C");
                } else {
                    connectionInternetErrorWeatherActivity.setVisibility(View.INVISIBLE);

                    Call<WeatherData> call = jsonOpenWeatherMapAPI.getWeatherData(city);

                    call.enqueue(new Callback<WeatherData>() {
                        @Override
                        public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                            if (response.code() > 399) {
                                finish();
                            } else {
                                WeatherData weatherData = response.body();

                                currentCity.setText(weatherData.getName());
                                currentTemp.setText(weatherData.getTemp().toString() + " °C");
                                currentPressure.setText(weatherData.getPressure().toString() + " hPa");
                                currentHumidity.setText(weatherData.getHumidity().toString() + " %");
                                currentTempMin.setText(weatherData.getTempMin().toString() + " °C");
                                currentTempMax.setText(weatherData.getTempMax().toString() + " °C");
                                Picasso.get().load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + "@2x.png").into(currentImageWeather);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherData> call, Throwable t) {
                            currentCity.setText(t.getMessage());
                            currentTemp.setText(t.getMessage());
                            currentPressure.setText(t.getMessage());
                            currentHumidity.setText(t.getMessage());
                            currentTempMin.setText(t.getMessage());
                            currentTempMax.setText(t.getMessage());
                        }
                    });
                }

                pullToRefresh.setRefreshing(false);
            }
        });

        Thread threadTime = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView currentTime = findViewById(R.id.currentTime);

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
                                String time = simpleDateFormat.format(calendar.getTime());
                                currentTime.setText(time);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        Thread threadWeather = new Thread() {

            @Override
            public void run() {
                try {
                    int ms = 1;
                    while (!isInterrupted()) {
                        Thread.sleep(ms);
                        ms = 300000;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                                boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
                                if (connected == false) {
                                    connectionInternetErrorWeatherActivity.setVisibility(View.VISIBLE);
                                    currentCity.setText("-");
                                    currentTemp.setText("- °C");
                                    currentPressure.setText("- hPa");
                                    currentHumidity.setText("- %");
                                    currentTempMin.setText("- °C");
                                    currentTempMax.setText("- °C");
                                } else {
                                    connectionInternetErrorWeatherActivity.setVisibility(View.INVISIBLE);

                                    Call<WeatherData> call = jsonOpenWeatherMapAPI.getWeatherData(city);

                                    call.enqueue(new Callback<WeatherData>() {
                                        @Override
                                        public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                                            if (response.code() > 399) {
                                                finish();
                                            } else {
                                                WeatherData weatherData = response.body();

                                                currentCity.setText(weatherData.getName());
                                                currentTemp.setText(weatherData.getTemp().toString() + " °C");
                                                currentPressure.setText(weatherData.getPressure().toString() + " hPa");
                                                currentHumidity.setText(weatherData.getHumidity().toString() + " %");
                                                currentTempMin.setText(weatherData.getTempMin().toString() + " °C");
                                                currentTempMax.setText(weatherData.getTempMax().toString() + " °C");
                                                Picasso.get().load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + "@2x.png").into(currentImageWeather);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<WeatherData> call, Throwable t) {
                                            currentCity.setText(t.getMessage());
                                            currentTemp.setText(t.getMessage());
                                            currentPressure.setText(t.getMessage());
                                            currentHumidity.setText(t.getMessage());
                                            currentTempMin.setText(t.getMessage());
                                            currentTempMax.setText(t.getMessage());
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        threadTime.start();
        threadWeather.start();
    }
}