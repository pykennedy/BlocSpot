package com.example.peter.blocspot.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ActionMenuView;

import com.example.peter.blocspot.R;
import com.example.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.example.peter.blocspot.ui.fragment.MarkerPopup;
import com.example.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
                            GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private GoogleMap mMap;
    private Menu mMenu;
    private Toolbar mToolbar;
    View view;
    private MarkerPopup markerPopup;

    private LatLng user;
    private LatLng targetPOI;

    private static int targetHeight = 0;

    private boolean mNotifyIsOn, mIntentToAdd, windowIsOpen;

    private MenuItem menu, add, notify, search, settings;
    //used for determining which menu item is active
    private String activeMenu = "";

    private final String MENU_TITLE = "menu";
    private final String SEARCH_TITLE = "search";
    private final String SETTINGS_TITLE = "settings";
    private View MENU_INDICATOR;
    private View SEARCH_INDICATOR;
    private View SETTINGS_INDICATOR;

    private final int STANDARD_CAMERA_SPEED = 400;
    private final int SLOWER_CAMERA_SPEED = 700;

    private void setButtonsToDark() {
        menu.setIcon(R.drawable.menu_dark);
        add.setIcon(R.drawable.add_dark);
        search.setIcon(R.drawable.search_dark);
        settings.setIcon(R.drawable.settings_dark);
    }

    private void setIndicatorsToDark() {
        MENU_INDICATOR.setVisibility(View.VISIBLE);
        SEARCH_INDICATOR.setVisibility(View.VISIBLE);
        SETTINGS_INDICATOR.setVisibility(View.VISIBLE);
    }

    private boolean toggleMenuPressed(String itemPressed){
        boolean openWindow = true;

        //set all of the icons and windowIndicators to dark
        setButtonsToDark();
        setIndicatorsToDark();
        mIntentToAdd = false;

        //if the currently active menu is pressed, hide it
        if(activeMenu.equals(itemPressed)){
            activeMenu = "";
            openWindow = false;
        }else{
            //otherwise, set the new item pressed and turn on the icon
            activeMenu = itemPressed;
            //TODO - Swap appropriate fragmemnt
            switch (itemPressed){
                case MENU_TITLE:
                    menu.setIcon(R.drawable.menu_light);
                    MENU_INDICATOR.setVisibility(View.INVISIBLE);
                    break;
                case SEARCH_TITLE:
                    search.setIcon(R.drawable.search_light);
                    SEARCH_INDICATOR.setVisibility(View.INVISIBLE);
                    break;
                case SETTINGS_TITLE:
                    settings.setIcon(R.drawable.settings_light);
                    SETTINGS_INDICATOR.setVisibility(View.INVISIBLE);
                    break;
            }
        }
        return openWindow;
    }

    private void toggleNotify(){
        mNotifyIsOn = !mNotifyIsOn;
        add.setIcon(R.drawable.add_dark);
        mIntentToAdd = false;
        activeMenu = "";
        notify.setIcon(mNotifyIsOn ? R.drawable.notify_light : R.drawable.notify_dark);
    }

    private void toggleAdd() {
        mIntentToAdd = !mIntentToAdd;
        activeMenu = "";
        setIndicatorsToDark();
        setButtonsToDark();
        add.setIcon(mIntentToAdd ? R.drawable.add_light : R.drawable.add_dark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mToolbar = (Toolbar) findViewById(R.id.tb_maps_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupEvenlyDistributedToolbar();
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.

        view =  findViewById(R.id.popupWindow);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(targetHeight == 0){
                        targetHeight = view.getHeight();
                        view.setVisibility(View.GONE);
                    }
                }
            });
        }
        if (!canAccessLocation() || !canAccessContacts()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, 1);
            }
        }else{
            initMaps();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        initMaps();
    }

    private void initMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCamera() {
        return(hasPermission(Manifest.permission.CAMERA));
    }

    private boolean canAccessContacts() {
        return(hasPermission(Manifest.permission.READ_CONTACTS));
    }


    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_bar_menu, menu);
        this.mMenu = menu;

        this.menu = mMenu.getItem(0);
        this.add = mMenu.getItem(1);
        this.notify = mMenu.getItem(2);
        this.search = mMenu.getItem(3);
        this.settings = mMenu.getItem(4);

        MENU_INDICATOR = findViewById(R.id.menu_indicator_0);
        SEARCH_INDICATOR = findViewById(R.id.menu_indicator_3);
        SETTINGS_INDICATOR = findViewById(R.id.menu_indicator_4);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == this.notify){
            toggleNotify();
        } else if (item == this.add) {
            toggleAdd();
            if(windowIsOpen) {
                BlocSpotAnimator.collapse(view);
                centerMapOnPoint(user, targetHeight);
                windowIsOpen = false;
            }
        } else {
            if(toggleMenuPressed(item.getTitle().toString())){
                if(windowIsOpen) {
                    BlocSpotAnimator.collapse(view);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            BlocSpotAnimator.expand(view, targetHeight);
                        }
                    }, 350);
                    offsetCenterMapOnPoint(user, SLOWER_CAMERA_SPEED);
                    windowIsOpen = true;
                }
                else {
                    offsetCenterMapOnPoint(user, STANDARD_CAMERA_SPEED);
                    BlocSpotAnimator.expand(view, targetHeight);
                    windowIsOpen = true;
                }
            } else {
                centerMapOnPoint(user, STANDARD_CAMERA_SPEED);
                activeMenu = "";
                setIndicatorsToDark();
                BlocSpotAnimator.collapse(view);
                windowIsOpen = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(activeMenu.length()<2)
            super.onBackPressed();
        else {
            centerMapOnPoint(user, STANDARD_CAMERA_SPEED);
            activeMenu = "";
            setIndicatorsToDark();
            BlocSpotAnimator.collapse(view);
        }
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
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        markerPopup = new MarkerPopup(getLayoutInflater());
        mMap.setInfoWindowAdapter(markerPopup);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        try {
            System.out.println("GOT TO ATTEMPTING CAMERA SHIFT");
            Location userLocation = locationManager.getLastKnownLocation(provider);
            double lat = userLocation.getLatitude();
            double lng = userLocation.getLongitude();
            user = new LatLng(lat, lng);
            centerMapOnPoint(user, STANDARD_CAMERA_SPEED);
        } catch (SecurityException e) {
            System.err.println("MapsActivity.onMapReady() -- caught SecurityException");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        user = new LatLng(lat, lng);
        centerMapOnPoint(user, STANDARD_CAMERA_SPEED);
    }

    private void centerMapOnPoint(LatLng location, int speed) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                // do nothing i dont care i hope this works
            }

            @Override
            public void onCancel() {
                // do nothing i dont care i hope this works
            }
        });
    }
    private void offsetCenterMapOnPoint(LatLng location, int speed) {
        double offset = (location == user) ? 0.0014 : 0.0012;
        LatLng temp = new LatLng(location.latitude - offset, location.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 17), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                // do nothing i dont care i hope this works
            }

            @Override
            public void onCancel() {
                // do nothing i dont care i hope this works
            }
        });
    }

    @Override
    public void onMapClick(LatLng position) {
        targetPOI = position;
        if(mIntentToAdd) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("temporary")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            // fragment logic
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.popupContent, PoiDetailWindow.inflateAddPOIMenuWindow(marker))
                    .commit();
            // fragment logic end

            toggleAdd();
            offsetCenterMapOnPoint(targetPOI, STANDARD_CAMERA_SPEED);
            BlocSpotAnimator.expand(view, targetHeight);
            windowIsOpen = true;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        markerPopup.getInfoWindow(marker);

        //  not sure how to change the popup to be unique to each marker?

        return false;
    }
}