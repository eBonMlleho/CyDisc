package coms309.sb_c_4_cydisc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;

//TODO: add API to this class
public class RemoteFetch {

    //TODO: Add unit preference (metric / imperial) : add  " &units=<metric/imperial> "
    // default unit is Kelvin
    private static final String WEATHER_API_COORDS =
            "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=imperial";
    private static final String WEATHER_API_CITY =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial";

    //TODO: add API to this method
    public static JSONObject getJSON(Context context, String city) {
        try {
            URL url = new URL(String.format(WEATHER_API_CITY, city));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // OpenWeatherApi expects api key in x-api-key property
            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_apikey));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp;

            // while there is data left in the json
            while((tmp = reader.readLine()) != null) {
                json.append(tmp).append('\n');
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // check to see if data returned was malformed
            if(data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch(Exception e) {
            return null;
        }
    }

    //TODO: add API to this method
    public static JSONObject getJSON(Context context, double latitude, double longitude) {
        try {
            URL url = new URL(String.format(WEATHER_API_COORDS, latitude, longitude));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // OpenWeatherApi expects api key in x-api-key property
            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_apikey));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp;

            // while there is data left in the json
            while((tmp = reader.readLine()) != null) {
                json.append(tmp).append('\n');
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // check to see if data returned was malformed
            if(data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch(Exception e) {
            return null;
        }
    }
}
