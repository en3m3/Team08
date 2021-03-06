package com.example.tapordie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;

/**
 * starts the rain effect
 */
public class RAIN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        WeatherView weatherView = findViewById (R.id.weather_view);
        weatherView.setWeatherData (PrecipType.RAIN);
    }
}