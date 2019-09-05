package coms309.sb_c_4_cydisc;


import android.content.Context;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

//TODO: add API to this class
public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        HandlerPasser,
        LocationListener,
		OnDataPass {

    private DrawerLayout mDrawerLayout;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private String username;
    private String playerId;

    private CharSequence mTitle;

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationHandler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // overwrite the ActionBar with our toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find view for drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);

        // Bind DrawerLayout events to ActionBarToggle
        mDrawerLayout.addDrawerListener(mDrawerToggle);



        // fill content with blank fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.flContent, new LoginFragment()).commit();

        // make new Google API Client (used for Location services)
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationHandler = new LocationHandler(this.googleApiClient, this.mFusedLocationProviderClient, this);
        googleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(googleApiClient.isConnected()) {
            locationHandler.removeUpdates();
            googleApiClient.disconnect();
        }
    }

    //TODO: add API to this interface
    public interface HoldName{
        public void sendName(String name);
    }

    /* Commented because I forgot its use.
    @Override
    public void sendText(String text)
    {
        username = text;
    }*/

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    @Override
    public void onDataPass(Bundle data) {
        if(data != null) {
            username = data.getString("username");
            playerId = data.getString("playerid");
        }
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPlayerId() { return playerId; }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // sync the toggle state
        mDrawerToggle.syncState();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    //TODO: add API to this method
    public void selectDrawerItem(MenuItem item) {
        // create a fragment based on item selected
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        Class fragmentClass;
        bundle.putString("playerid", playerId); /////added by wen
        switch(item.getItemId()) {
            case R.id.nav_home_fragment:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_profile_fragment:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_friends_fragment:
                fragmentClass = FriendsFragment.class;
                break;
            case R.id.nav_leaderboard_fragment:
                fragmentClass = LeaderboardFragment.class;
                break;
            case R.id.nav_courses_fragment:
                fragmentClass = CoursesFragment.class;
                break;
            case R.id.nav_discBag_fragment:
                fragmentClass = DiscBagFragment.class;
                break;
            case R.id.nav_event_fragment:
                fragmentClass = EventFragment.class;
                break;
            case R.id.nav_login_fragment:
                fragmentClass = LoginFragment.class;
                break;
            case R.id.nav_register_fragment:
                fragmentClass = RegisterFragment.class;
                break;
            case R.id.nav_scorecard_fragment:
                bundle.putString("username", username);
                fragmentClass = NewGame.class;
                break;
            case R.id.nav_map:
                fragmentClass = MapFragment.class;
                break;
            case R.id.nav_weather:
                fragmentClass = WeatherFragment.class;
                break;
            case R.id.nav_gallery:
                fragmentClass = GalleryFragment.class;
                break;
            case R.id.nav_handbook:
                fragmentClass = HandbookFragment.class;
                break;
            case R.id.nav_analysis:
                fragmentClass = AnalysisFragment.class;
                break;
            default:
                //default to primary fragment
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            fragment.setArguments(bundle);

        } catch (Exception e) {

            e.printStackTrace();
        }

        // insert the fragment, replacing any existing fragment(s)
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // highlight selected item
        item.setChecked(true);
        // set action bar title
//        setTitle(item.getTitle());
        // close drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // catchcase
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(@NonNull CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // give any configuration change to drawer toggler
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationHandler.onConnected(bundle, this, this);
    }

    //TODO: add API to this method
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationHandler.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationHandler.onConnectionFailed(connectionResult);
    }

    @Override
    public LocationHandler getLocationHandler() {
        return this.locationHandler;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.locationHandler.setLocation(location);
    }
}
