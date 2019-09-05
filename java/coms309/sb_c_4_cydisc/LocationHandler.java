package coms309.sb_c_4_cydisc;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

//TODO: add API to this class
public class LocationHandler  {

    private static final int LOCATION_REQUEST = 1;

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Activity activity;


    //TODO: add API to this constructor
    public LocationHandler(GoogleApiClient googleApiClient, FusedLocationProviderClient mFusedLocationProviderClient, Activity activity) {
        this.googleApiClient = googleApiClient;
        this.mFusedLocationProviderClient = mFusedLocationProviderClient;
        this.activity = activity;
        this.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }

    //TODO: add API to this method
    public Location getLocation() {
        if (this.lastLocation == null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        return lastLocation;
    }

    //TODO: add API to this method
    public void setLocation(Location location) {
        this.lastLocation = location;
    }

    // Google API methods

    //TODO: add API to this method
    public void onConnected(@Nullable Bundle bundle, Activity activity, Context context) {
        if (checkPermissions(activity, context)) return;
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lastLocation = location;
                        }
                    }
                });

        if (lastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) activity);
        }
    }

    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    mFusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this.activity, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                        lastLocation = location;
                                    }
                                }
                            });
                    break;
                }
                // no break since else permission denied
            case Activity.RESULT_CANCELED:
                //permission not granted
                //set location to Coover Hall
                this.lastLocation = new Location("");
                this.lastLocation.setLatitude(42.028380d);
                this.lastLocation.setLongitude(-93.650993d);
                break;
            default:
        }
    }

    //TODO: add API to this method
    public void onConnectionSuspended(int i) {
        Log.d("app", "onConnectionSuspended()! Trying to reconnect.");
        googleApiClient.connect();
    }

    //TODO: add API to this method
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("app", "onConnectionFailed()! Trying to reconnect.");
        googleApiClient.connect();
    }

    //TODO: add API to this method
    public void removeUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationListener) activity);
    }

    private boolean checkPermissions(Activity activity, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return true;
        }
        return false;
    }
}
