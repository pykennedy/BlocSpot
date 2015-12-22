package com.example.peter.blocspot.ui.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;

import com.example.peter.blocspot.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Menu mMenu;
    private Toolbar mToolbar;

    private boolean mMenuIsOn, mAddIsOn, mNotifyIsOn, mSearchIsOn, mSettingsIsOn;

    private boolean setLights(String title) {
        boolean openWindow = false;
        switch(title) {
            case "menu":
                mMenu.getItem(1).setIcon(R.drawable.add_dark);
                mAddIsOn = false;
                mMenu.getItem(3).setIcon(R.drawable.search_dark);
                mSearchIsOn = false;
                mMenu.getItem(4).setIcon(R.drawable.settings_dark);
                mSettingsIsOn = false;
                mMenuIsOn = !mMenuIsOn;
                if(mMenuIsOn) {
                    openWindow = true;
                    mMenu.getItem(0).setIcon(R.drawable.menu_light);
                }
                else
                    mMenu.getItem(0).setIcon(R.drawable.menu_dark);
                break;
            case "add":
                mMenu.getItem(0).setIcon(R.drawable.menu_dark);
                mMenuIsOn = false;
                mMenu.getItem(3).setIcon(R.drawable.search_dark);
                mSearchIsOn = false;
                mMenu.getItem(4).setIcon(R.drawable.settings_dark);
                mSettingsIsOn = false;
                mAddIsOn = !mAddIsOn;
                if(mAddIsOn) {
                    openWindow = true;
                    mMenu.getItem(1).setIcon(R.drawable.add_light);
                }
                else
                    mMenu.getItem(1).setIcon(R.drawable.add_dark);
                break;
            case "notify":
                mNotifyIsOn = !mNotifyIsOn;
                if(mNotifyIsOn) {
                    openWindow = true;
                    mMenu.getItem(2).setIcon(R.drawable.notify_light);
                }
                else
                    mMenu.getItem(2).setIcon(R.drawable.notify_dark);
                break;
            case "search":
                mMenu.getItem(0).setIcon(R.drawable.menu_dark);
                mMenuIsOn = false;
                mMenu.getItem(1).setIcon(R.drawable.add_dark);
                mAddIsOn = false;
                mMenu.getItem(4).setIcon(R.drawable.settings_dark);
                mSettingsIsOn = false;
                mSearchIsOn = !mSearchIsOn;
                if(mSearchIsOn) {
                    openWindow = true;
                    mMenu.getItem(3).setIcon(R.drawable.search_light);
                }
                else
                    mMenu.getItem(3).setIcon(R.drawable.search_dark);
                break;
            case "settings":
                mMenu.getItem(0).setIcon(R.drawable.menu_dark);
                mMenuIsOn = false;
                mMenu.getItem(1).setIcon(R.drawable.add_dark);
                mAddIsOn = false;
                mMenu.getItem(3).setIcon(R.drawable.search_dark);
                mSearchIsOn = false;
                mSettingsIsOn = !mSettingsIsOn;
                if(mSettingsIsOn) {
                    openWindow = true;
                    mMenu.getItem(4).setIcon(R.drawable.settings_light);
                }
                else
                    mMenu.getItem(4).setIcon(R.drawable.settings_dark);
                break;
            default:
                break;
        }
        return openWindow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mToolbar = (Toolbar) findViewById(R.id.tb_maps_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupEvenlyDistributedToolbar();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_bar_menu, menu);
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // returns true if the user is wanted to open a window
        if(setLights(item.getTitle().toString())) {
            //////////////
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupEvenlyDistributedToolbar(){
        // Use Display metrics to get Screen Dimensions
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_maps_activity);
        // Inflate your menu
        mToolbar.inflateMenu(R.menu.bottom_bar_menu);

        // Add 10 spacing on either side of the toolbar
        mToolbar.setContentInsetsAbsolute(10, 10);

        // Get the ChildCount of your Toolbar, this should only be 1
        int childCount = mToolbar.getChildCount();
        // Get the Screen Width in pixels
        int screenWidth = metrics.widthPixels;

        // Create the Toolbar Params based on the screenWidth
        Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(screenWidth, ActionMenuView.LayoutParams.WRAP_CONTENT);

        // Loop through the child Items
        for(int i = 0; i < childCount; i++){
            // Get the item at the current index
            View childView = mToolbar.getChildAt(i);
            // If its a ViewGroup
            if(childView instanceof ViewGroup){
                // Set its layout params
                childView.setLayoutParams(toolbarParams);
                // Get the child count of this view group, and compute the item widths based on this count & screen size
                int innerChildCount = ((ViewGroup) childView).getChildCount();
                int itemWidth  = (screenWidth / innerChildCount);
                // Create layout params for the ActionMenuView
                ActionMenuView.LayoutParams params = new ActionMenuView.LayoutParams(itemWidth, ActionMenuView.LayoutParams.WRAP_CONTENT);
                // Loop through the children
                for(int j = 0; j < innerChildCount; j++){
                    View grandChild = ((ViewGroup) childView).getChildAt(j);
                    if(grandChild instanceof ActionMenuItemView){
                        // set the layout parameters on each View
                        grandChild.setLayoutParams(params);
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        try {
            Location userLocation = locationManager.getLastKnownLocation(provider);
            double lat = userLocation.getLatitude();
            double lng = userLocation.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        } catch (SecurityException e) {
            System.err.println("MapsActivity.onMapReady() -- caught SecurityException");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }
}
