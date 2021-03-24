package com.example.tapordie;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static androidx.core.content.ContextCompat.getSystemService;

public class WeatherInfo {
    static String apikey = "e578e419099ed577e63332bc72fa801d";
    private WeatherType description;
    private TimeOfDay time;
    private Location currentLoc;
    private URL httpURL;

    public WeatherInfo(Context context) {
//
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        LocationManager locationMan = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        LocationProvider provider = locationMan.getProvider(LocationManager.GPS_PROVIDER);
       currentLoc.setLatitude(currentLoc.getLatitude());
       currentLoc.setLongitude(currentLoc.getLongitude());

        try {
            httpURL = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+currentLoc.getLatitude()+"&lon="+currentLoc.getLongitude()+"&appid="+apikey);
            HttpsURLConnection httpsCon = (HttpsURLConnection) httpURL.openConnection();
            httpsCon.setRequestMethod("GET");
            httpsCon.setRequestProperty("Content-Type", "application/json");
            httpsCon.setConnectTimeout(7500);
            httpsCon.setReadTimeout(7500);
            Integer connectionStatus = httpsCon.getResponseCode();
            String responseData = httpsCon.getResponseMessage();

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
