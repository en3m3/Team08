package com.example.tapordie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;

/**
 * starts snow effect
 */
public class SNOW extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        WeatherView weatherView = findViewById (R.id.weather_view);
        weatherView.setWeatherData (PrecipType.SNOW);
    }
}