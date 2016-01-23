package com.kennedy.peter.blocspot.ui.delegates;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kennedy.peter.blocspot.R;
import com.kennedy.peter.blocspot.ui.activity.MapsActivity;
import com.kennedy.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.kennedy.peter.blocspot.ui.fragment.SearchWindow;
import com.kennedy.peter.blocspot.yelp.YelpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchWindowDelegate implements SearchWindow.Delegate {

    private List<MarkerOptions> markerOptionsList = new ArrayList<>();

    private void addToList(MarkerOptions markerOptions) {
        markerOptionsList.add(markerOptions);
    }

    private void addMarkers(GoogleMap mMap) {
        MapsActivity.clearUnsavedYelpMarkers();
        MapsActivity.yelpMarkers.clear();
        for(int i = 0; i < markerOptionsList.size(); i++) {
            Marker marker = mMap.addMarker(markerOptionsList.get(i));
            MapsActivity.yelpMarkers.add(marker);
        }
    }

    @Override
    public void searchClicked(final String searchParams, final GoogleMap mMap) {
        final GoogleMap map = mMap;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                YelpAPI yelpAPI = new YelpAPI();
                String result = yelpAPI.searchForBusinessesByLocation(searchParams, MapsActivity.user);
                markerOptionsList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("businesses");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectIncrementing = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectLevel1 = jsonObjectIncrementing.getJSONObject("location");
                        JSONObject jsonObject1Level2 = jsonObjectLevel1.getJSONObject("coordinate");
                        String latitude = jsonObject1Level2.getString("latitude");
                        String longitude = jsonObject1Level2.getString("longitude");
                        String jsonName = jsonObjectIncrementing.getString("name");

                        addToList(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                                .snippet(jsonName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                        System.out.println(i + ". " + jsonName + ": " + latitude + ", " + longitude);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            addMarkers(mMap);
            BlocSpotAnimator.centerMapOnPointZoomedOut(MapsActivity.user, MapsActivity.STANDARD_CAMERA_SPEED, mMap);
            BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
            MapsActivity.search.setIcon(R.drawable.search_dark);
            MapsActivity.windowIsOpen = false;
            MapsActivity.activeMenu = "";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
