package coms309.sb_c_4_cydisc;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//TODO: add API to this class
public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap mMapView;
    private SupportMapFragment mMapFragment;

    HandlerPasser passer;
    Activity activity;

    Location location;
    static LocationHandler locationHandler;

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
    @TargetApi(17)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        mMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_container);

        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map_container, mMapFragment).commit();
        }

        this.locationHandler = passer.getLocationHandler();
        this.location = locationHandler.getLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_activity, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMapView == null) {
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
        if(location != null) {
            LatLng mLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMapView.addMarker(new MarkerOptions().position(mLocation)
                .title("Your Location"));
            mMapView.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
            mMapView.setMinZoomPreference(18);
        }
    }

    //Fragment content here
}