package com.example.tapordie;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

/**
 * Pulls weather api data on a thread using the latitude and longitude from the phone gps*not functioning*
 * sets up all the needed variables for the app.
 */
public class WeatherInfo implements Runnable, LocationListener{
    static String apikey = "e578e419099ed577e63332bc72fa801d";
    public LocationListener locListen;
    private WeatherType description;
    private String strWeatherDesc;
    private double lat;
    private double lon;
    private URL httpURL;
    Context context;
    Location loc;
    public JSONObject responseData;
    InputStream response;



    public WeatherInfo(){}

    public WeatherInfo(Context ctx) {
        this.lat = Math.random() * 80;
        this.lon = Math.random() * 80;;
        description = WeatherType.RAIN;

        this.context = ctx;
    }

    /**
     * takes the response data from the api and breaks it into the pieces needed
     * calls the method to normalize the weather description
     * @param response
     */
    public void processWeatherData(InputStream response) {
        try {
            JSONArray weatherList = responseData.getJSONArray("weather");
            for (int i = 0; i < weatherList.length(); i++) {
                JSONObject each = (JSONObject) weatherList.get(i);
                this.strWeatherDesc = each.getString("description");
                setWeatherDescription(this.strWeatherDesc);
            }

             Log.i("break", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return
     */
    public WeatherType getWeatherDescription() {
        return this.description;
    }

    /**
     * sets standard values for the description to make it useful
     * @param desc
     */
    public void setWeatherDescription(String desc) {
        switch(desc) {
            case "sunny":
                this.description = WeatherType.SUNNY;
                break;
            case "rainy":
            case "rain":
            case "heavy showers":
            case "light showers":
            case "scattered showers":
                this.description = WeatherType.RAIN;
                break;
            case "snow":
            case "heavy snow":
            case "snow flurries":
            case "light snow":
                this.description = WeatherType.SNOW;
                break;
            case "cloudy":
            case "partly cloudy":
                this.description = WeatherType.CLOUDY;
                break;
            default:
                switch((int)Math.random()*4) {
                    case 0:
                        this.description = WeatherType.SNOW;
                        break;
                    case 1:
                        this.description = WeatherType.SUNNY;
                        break;
                    case 2:
                        this.description = WeatherType.RAIN;
                        break;
                    case 3:
                        this.description = WeatherType.CLOUDY;
                        break;
                    default:
                        this.description = WeatherType.SUNNY;
                        break;
                }
            break;
        }

    }

    /**
     * called when the location changes automatically by location listener
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        this.lon = location.getLongitude();
        this.lat = location.getLatitude();
    }

    /**
     * Asks location permission
     * Calls the weather api to pull weather data
     * Runs on a background thread
     */
        @Override
    public void run() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean networkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                shouldShowRequestPermissionRationale((Activity) context,"This app uses location data to detect local weather.");
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                if(gpsOn) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10000,locListen);

                }else if (networkOn) {
                    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10000,locListen);

                }else {
                    Log.i("api call", "no provider available");
                    loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,10000,locListen);
                }
                lat = loc.getLatitude();
                URL httpsURL = null;
                httpURL = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+this.lat+"&lon="+this.lon+"&appid="+apikey);
                Log.i("api call","opening connection");
                HttpsURLConnection httpsCon = (HttpsURLConnection) httpsURL.openConnection();
                response = httpsCon.getInputStream();
                Scanner scanner = new Scanner(response);
                String responseBody = scanner.useDelimiter("\\A").next();
                responseData = new JSONObject(responseBody);
                processWeatherData(response);

            }
        } catch(Exception e) {
                Log.e("api error","couldn't retrieve data");
        }
    }
}
