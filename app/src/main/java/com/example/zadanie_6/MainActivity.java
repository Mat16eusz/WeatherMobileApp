package com.example.zadanie_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonNextActivity;
    private EditText citySelection;
    private TextView connectionInternetErrorMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNextActivity = findViewById(R.id.buttonCheckTheWeather);
        citySelection = findViewById(R.id.citySelection);
        connectionInternetErrorMainActivity = findViewById(R.id.connectionInternetErrorMainActivity);

        loadData();

        buttonNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySelection = findViewById(R.id.citySelection);
                String city = citySelection.getText().toString();
                saveData(city);

                ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
                if (connected == false) {
                    connectionInternetErrorMainActivity.setVisibility(View.VISIBLE);
                } else {
                    connectionInternetErrorMainActivity.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);

                    intent.putExtra("KEY_CITY_SELECTION", city);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

    private void saveData(String city) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CITY_KEY", city);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String city = sharedPreferences.getString("CITY_KEY", "Default value");
        citySelection.setText(city);
    }
}