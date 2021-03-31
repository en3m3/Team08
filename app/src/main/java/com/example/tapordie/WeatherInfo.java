package com.example.tapordie;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static androidx.core.content.ContextCompat.getSystemService;

public class WeatherInfo implements Runnable {
    static String apikey = "e578e419099ed577e63332bc72fa801d";
    private WeatherType description;
    private TimeOfDay time;
    private double lat;
    private double lon;
    private URL httpURL;
    Context context;
    Location location;

    public WeatherInfo(){}

    public WeatherInfo(Context ctx) {
        double DEFAULT_LAT = 111.7924;
        double DEFAULT_LON = 43.8231;
        this.context = ctx;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale((Activity) context,"This app uses location data to detect local weather.");
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            try {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Boolean gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                Boolean networkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(gpsOn) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    location.setLatitude(location.getLatitude());
                    location.setLongitude(location.getLongitude());
                }else if (networkOn) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    location.setLatitude(location.getLatitude());
                    location.setLongitude(location.getLongitude());
                }else {
                    Log.i("api call", "no provider available");
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    location.setLatitude(location.getLatitude());
                    location.setLongitude(location.getLongitude());
                }
            }catch(Exception e) {
                Log.w("Location","Could not access phones location");
            }
        } else{

        }
    }


    @Override
    public void run() {


        try {

//            URLConnection connection = new URL(c.apiCall.getApiCall()).openConnection();
//            connection.setRequestProperty("Accept-Charset", charset);
//            InputStream response = connection.getInputStream();
//            try (Scanner scanner = new Scanner(response)) {
//                String responseBody = scanner.useDelimiter("\\A").next();
//                Gson gson = new Gson();
//                List<Gson> g = new ArrayList<>();
//                c.weather = gson.fromJson(responseBody,CityWeather.class); /* converts the JSON into a city's weather object */

            Log.i("api call","preparing");
            URL httpsURL = new URL("https://api.openweathermap.org/data/2.5/weather?lat=111.7924&lon=43.8231&appid=e578e419099ed577e63332bc72fa801d");

//            httpURL = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&appid="+apikey);
            Log.i("api call","opening connection");
            HttpsURLConnection httpsCon = (HttpsURLConnection) httpsURL.openConnection();
            InputStream response = httpsCon.getInputStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            JSONObject responseData = new JSONObject(httpsCon.getResponseMessage());

//            httpsCon.setRequestMethod("GET");
//            httpsCon.setRequestProperty("Content-Type", "application/json");
//            httpsCon.setConnectTimeout(7500);
//            httpsCon.setReadTimeout(7500);
//            Integer connectionStatus = httpsCon.getResponseCode();

//            Log.i("api call","data received, response code: "+connectionStatus);
//            Log.i("api call",responseData);
        }catch(Exception e) {
            Log.e("api error","couldn't retrieve data");
        }
    }
}
/*.

URL url = new URL("http://example.com");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("GET");
 */
