package coms309.sb_c_4_cydisc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

//TODO: add API to this class
public class WeatherFragment extends android.support.v4.app.Fragment {
    // this is how we display icons - by encoding them into Unicode characters via TrueType Font file
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    ImageButton refreshButton;
    Button textButton;
    boolean isManual;
    String city;

    Activity activity;
    Handler handler;
    HandlerPasser passer;

    Location location;
    static LocationHandler locationHandler;

    //TODO: add API to this constructor
    public WeatherFragment() {
        handler = new Handler();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
            activity = (Activity) context;
            try {
                passer = (HandlerPasser) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement HandlerPasser interface");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");

        this.locationHandler = passer.getLocationHandler();
        this.location = locationHandler.getLocation();
        if(location != null)
            updateWeatherData(this.location.getLatitude(), this.location.getLongitude());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.weather_activity, container, false);
        cityField = rootView.findViewById(R.id.city_field);
        updatedField = rootView.findViewById(R.id.updated_field);
        detailsField = rootView.findViewById(R.id.details_field);
        currentTemperatureField = rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon);

        refreshButton = rootView.findViewById(R.id.refresh_button);
        textButton = rootView.findViewById(R.id.text_button);
        isManual = false;
        addButtonListener();

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    /**
     * Public method to update weather information from city.
     * @param city Name of city
     */
    public void changeCity(String city) {
        this.city = city;
        updateWeatherData(city);
    }

    /**
     * Public method to update weather information from geographic coordinates,
     * as ordered pair of doubles (latitude, longitude)
     * @param latitude latitude
     * @param longitude longitude
     */
    public void changeCity(double latitude, double longitude) {
        updateWeatherData(latitude, longitude);
    }

    //Fragment content here

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                // handle malformed json
                if(json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                            isManual = false;
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void updateWeatherData(final double latitude, final double longitude) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), latitude, longitude);
                // handle malformed json
                if(json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    //TODO: appease the android gods about translations
    private void renderWeather(JSONObject json) {
        try {
            cityField.setText(
            json.getString("name").toUpperCase(Locale.US) +
            ", " +
            json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                    "\n" + "Pressure: " + main.getString("pressure") + "hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp")) + " °F");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
            updatedField.setText(
                    "Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);
        } catch(Exception e) {
            Log.e("WeatherFragment", "JSON object malformed.");
        }
    }

    private void setWeatherIcon(int actualID, long sunrise, long sunset) {
        /*
         * ID information:
         * 200 - 299 : thunderstorms
         * 300 - 399 : light rain (drizzle)
         * 500 - 599 : regular rain
         */
        int id = actualID / 100;
        String icon = "";

        if(actualID == 800) {
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_stormy);
                         break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
            }
        }

        weatherIcon.setText(icon);
    }

    private void addButtonListener() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = locationHandler.getLocation();
                if(location != null) {
                    if (!isManual)
                        updateWeatherData(location.getLatitude(), location.getLongitude());
                    else
                        updateWeatherData(city);
                }
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter City:");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isManual = true;
                        changeCity(input.getText().toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        isManual = false;
                    }
                });

                builder.show();
            }
        });
    }
}
