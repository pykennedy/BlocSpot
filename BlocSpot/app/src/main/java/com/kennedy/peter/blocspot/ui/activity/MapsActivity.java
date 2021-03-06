package com.kennedy.peter.blocspot.ui.activity;

import android.Manifest;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kennedy.peter.blocspot.BlocSpotApplication;
import com.kennedy.peter.blocspot.R;
import com.kennedy.peter.blocspot.api.DataSource;
import com.kennedy.peter.blocspot.api.model.PoiItem;
import com.kennedy.peter.blocspot.geofencing.GeofenceHelper;
import com.kennedy.peter.blocspot.geofencing.SharedPreferencesHandler;
import com.kennedy.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.kennedy.peter.blocspot.ui.delegates.PoiDetailWindowDelegate;
import com.kennedy.peter.blocspot.ui.delegates.SearchWindowDelegate;
import com.kennedy.peter.blocspot.ui.fragment.MenuWindow;
import com.kennedy.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.kennedy.peter.blocspot.ui.fragment.SearchWindow;
import com.kennedy.peter.blocspot.ui.fragment.SettingsWindow;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private GoogleMap mMap;
    private Menu mMenu;
    private Toolbar mToolbar;
    static View view;
    public static Marker pendingMarker;
    public static List<Marker> yelpMarkers = new ArrayList<>();
    private List<Geofence> mGeofenceList = new ArrayList<>();
    private GoogleApiClient apiClient;
    private PendingIntent pendingIntent;
    private List<PoiItem> poiItemList;

    public static LatLng user;
    private LatLng targetPOI;

    private static int targetHeight = 0;

    private boolean mIntentToAdd;
    public static boolean windowIsOpen;

    public static MenuItem menu, add, notify, search, settings;
    public static String activeMenu = "";

    private final String MENU_TITLE = "menu";
    private final String SEARCH_TITLE = "search";
    private final String SETTINGS_TITLE = "settings";
    private View MENU_INDICATOR;
    private View SEARCH_INDICATOR;
    private View SETTINGS_INDICATOR;

    public static final int STANDARD_CAMERA_SPEED = 400;
    private static final int SLOWER_CAMERA_SPEED = 700;

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
        setButtonsToDark();
        setIndicatorsToDark();
        mIntentToAdd = false;

        if(activeMenu.equals(itemPressed)){
            activeMenu = "";
            openWindow = false;
        }else{
            activeMenu = itemPressed;
            switch (itemPressed){
                case MENU_TITLE:
                    menu.setIcon(R.drawable.menu_light);
                    MENU_INDICATOR.setVisibility(View.INVISIBLE);
                    MenuWindow menuWindow = MenuWindow.inflateMenuWindow();
                    menuWindow.setMap(mMap);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.popupWindowContent, menuWindow)
                            .commit();
                    break;
                case SEARCH_TITLE:
                    search.setIcon(R.drawable.search_light);
                    SEARCH_INDICATOR.setVisibility(View.INVISIBLE);
                    SearchWindow searchWindow = SearchWindow.inflateSearchWindow();
                    searchWindow.setDelegate(new SearchWindowDelegate(), mMap);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.popupWindowContent, searchWindow)
                            .commit();
                    break;
                case SETTINGS_TITLE:
                    settings.setIcon(R.drawable.settings_light);
                    SETTINGS_INDICATOR.setVisibility(View.INVISIBLE);
                    SettingsWindow settingsWindow = SettingsWindow.inflateSettingsWindow();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.popupWindowContent, settingsWindow)
                            .commit();
                    break;
            }
        }
        return openWindow;
    }

    private void toggleNotify(){
        add.setIcon(R.drawable.add_dark);
        mIntentToAdd = false;
        activeMenu = "";
        SharedPreferencesHandler.setNotifyIsOn(
                this, !SharedPreferencesHandler.getNotifyIsOn(this));
        notify.setIcon(SharedPreferencesHandler.getNotifyIsOn(this)
                ? R.drawable.notify_light : R.drawable.notify_dark);
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

        view =  findViewById(R.id.popupWindow);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (targetHeight == 0) {
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

        if(apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        notify.setIcon(SharedPreferencesHandler.getNotifyIsOn(this)
        ? R.drawable.notify_light : R.drawable.notify_dark);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        turnOffActiveMarker();
        if(item == this.notify){
            toggleNotify();
        } else if (item == this.add) {
            toggleAdd();
            if(windowIsOpen) {
                BlocSpotAnimator.collapse(view);
                BlocSpotAnimator.centerMapOnPoint(user, targetHeight, mMap);
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
                    BlocSpotAnimator.offsetCenterMapOnPoint(user, SLOWER_CAMERA_SPEED, mMap);
                    windowIsOpen = true;
                }
                else {
                    BlocSpotAnimator.offsetCenterMapOnPoint(user, STANDARD_CAMERA_SPEED, mMap);
                    BlocSpotAnimator.expand(view, targetHeight);
                    windowIsOpen = true;
                }
            } else {
                BlocSpotAnimator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, mMap);
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
        if(mIntentToAdd) {
            add.setIcon(R.drawable.add_dark);
            mIntentToAdd = false;
        }
        else if(activeMenu.length()<2 && !windowIsOpen)
            if(yelpMarkers.size() > 0 || !yelpMarkers.isEmpty()) {
                clearUnsavedYelpMarkers();
                pendingMarker = null;
                Toast.makeText(this, "Unsaved Markers Cleared", Toast.LENGTH_SHORT).show();
            } else {
                pendingMarker = null;
                super.onBackPressed();
            }
        else {
            turnOffActiveMarker();
            BlocSpotAnimator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, mMap);
            activeMenu = "";
            setIndicatorsToDark();
            setButtonsToDark();
            BlocSpotAnimator.collapse(view);
            windowIsOpen=false;
        }
    }

    public void setupEvenlyDistributedToolbar(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        mToolbar = (Toolbar) findViewById(R.id.tb_maps_activity);
        mToolbar.inflateMenu(R.menu.bottom_bar_menu);

        mToolbar.setContentInsetsAbsolute(10, 10);
        int childCount = mToolbar.getChildCount();
        int screenWidth = metrics.widthPixels;

        Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(screenWidth,
                ActionMenuView.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < childCount; i++){
            View childView = mToolbar.getChildAt(i);
            if(childView instanceof ViewGroup){
                childView.setLayoutParams(toolbarParams);
                int innerChildCount = ((ViewGroup) childView).getChildCount();
                int itemWidth  = (screenWidth / innerChildCount);
                ActionMenuView.LayoutParams params = new ActionMenuView.LayoutParams(itemWidth,
                        ActionMenuView.LayoutParams.WRAP_CONTENT);
                for(int j = 0; j < innerChildCount; j++){
                    View grandChild = ((ViewGroup) childView).getChildAt(j);
                    if(grandChild instanceof ActionMenuItemView){
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
        pendingMarker = null;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        try {
            Location userLocation = locationManager.getLastKnownLocation(provider);
            double lat = userLocation.getLatitude();
            double lng = userLocation.getLongitude();
            user = new LatLng(lat, lng);
            BlocSpotAnimator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, mMap);
        } catch (SecurityException e) {
            System.err.println("MapsActivity.onMapReady() -- caught SecurityException");
        } catch (NullPointerException e) {
            user = new LatLng(45, 45);
            Toast.makeText(view.getContext(), "Failed to find current location. Please wait.",
                    Toast.LENGTH_SHORT).show();
        }
        DataSource dataSource = BlocSpotApplication.getSharedDataSource();
        poiItemList = dataSource.getPoiItemList();
        if(poiItemList != null || !poiItemList.isEmpty()) {
            for (int i = 0; i < poiItemList.size(); i++) {
                PoiItem poiItem = poiItemList.get(i);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(poiItem.getLatitude(), poiItem.getLongitude()))
                        .title(poiItem.getTitleID()));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        user = new LatLng(lat, lng);
        BlocSpotAnimator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, mMap);
    }

    @Override
    public void onMapClick(LatLng position) {
        targetPOI = position;
        if(mIntentToAdd) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            if(pendingMarker != null)
                pendingMarker.remove();
            pendingMarker = marker;
            PoiDetailWindow poiDetailWindow = PoiDetailWindow.inflateAddPOIMenuWindow(marker);
            poiDetailWindow.setDelegate(new PoiDetailWindowDelegate(), mMap, apiClient, mGeofenceList,
                    pendingIntent, this);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.popupWindowContent, poiDetailWindow)
                            .commit();
            toggleAdd();
            BlocSpotAnimator.offsetCenterMapOnPoint(targetPOI, STANDARD_CAMERA_SPEED, mMap);
            BlocSpotAnimator.expand(view, targetHeight);
            windowIsOpen = true;
        }
    }

    private void turnOffActiveMarker() {
        if(pendingMarker != null) {
            if(pendingMarker.getSnippet() != null)
                pendingMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            else
                pendingMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        turnOffActiveMarker();
        pendingMarker = marker;
        pendingMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        setButtonsToDark();
        setIndicatorsToDark();
        targetPOI = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        PoiDetailWindow poiDetailWindow = PoiDetailWindow.inflateAddPOIMenuWindow(marker);
        poiDetailWindow.setDelegate(new PoiDetailWindowDelegate(), mMap, apiClient, mGeofenceList,
                pendingIntent, this);
        if(windowIsOpen) {
            BlocSpotAnimator.collapse(view);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.popupWindowContent, poiDetailWindow)
                    .commit();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    BlocSpotAnimator.expand(view, targetHeight);
                }
            }, 350);
            BlocSpotAnimator.offsetCenterMapOnPoint(targetPOI, SLOWER_CAMERA_SPEED, mMap);
            windowIsOpen = true;
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.popupWindowContent, poiDetailWindow)
                    .commit();
            BlocSpotAnimator.offsetCenterMapOnPoint(targetPOI, STANDARD_CAMERA_SPEED, mMap);
            BlocSpotAnimator.expand(view, targetHeight);
            windowIsOpen = true;
        }
        return true;
    }
    public static View getCurrentWindow() {
        return view;
    }

    public static void clearUnsavedYelpMarkers() {
        for(int i = 0; i < yelpMarkers.size(); i++) {
            Marker marker = yelpMarkers.get(i);
            if(marker != null)
                marker.remove();
        }
        yelpMarkers.clear();
    }

    public static int getYelpMarkerIndex(String markerID) {
        for(int i = 0; i < yelpMarkers.size(); i++) {
            if(yelpMarkers.get(i).getId().equals(markerID))
                return i;
        }
        return -1;
    }

    @Override
    public void onConnected(Bundle bundle) {
        GeofenceHelper.updateAllFences(apiClient, mGeofenceList, pendingIntent, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(Result result) {

    }
}