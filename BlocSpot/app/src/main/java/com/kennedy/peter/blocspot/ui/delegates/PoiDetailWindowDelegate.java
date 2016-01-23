package com.kennedy.peter.blocspot.ui.delegates;

import android.app.PendingIntent;

import com.kennedy.peter.blocspot.BlocSpotApplication;
import com.kennedy.peter.blocspot.api.DataSource;
import com.kennedy.peter.blocspot.api.model.PoiItem;
import com.kennedy.peter.blocspot.geofencing.GeofenceHelper;
import com.kennedy.peter.blocspot.ui.activity.MapsActivity;
import com.kennedy.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.kennedy.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class PoiDetailWindowDelegate implements PoiDetailWindow.Delegate {

    private DataSource dataSource = BlocSpotApplication.getSharedDataSource();

    @Override
    public void onDeleteClicked(Marker marker, GoogleMap mMap, GoogleApiClient apiClient) {

        if(marker.getTitle() == null)
            marker.remove();
        else
            dataSource.removePOI(dataSource.getPoiItem(marker.getTitle()).getId());

        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";

        List<String> fenceToRemove = new ArrayList<>();
        fenceToRemove.add(marker.getTitle());
        LocationServices.GeofencingApi.removeGeofences(apiClient, fenceToRemove);
        marker.remove();

        MapsActivity.pendingMarker = null;
    }

    @Override
    public void onSaveClicked(Marker marker, PoiItem poiItem, GoogleMap mMap, GoogleApiClient apiClient,
                              List<Geofence> mGeofenceList, PendingIntent pendingIntent, MapsActivity mapsActivity) {

        dataSource.savePOI(poiItem);
        poiItem.setId(dataSource.getPoiItem(marker.getTitle()).getId());
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if(!MapsActivity.yelpMarkers.isEmpty())
            MapsActivity.yelpMarkers.remove(MapsActivity.getYelpMarkerIndex(marker.getTitle()));

        BlocSpotAnimator.centerMapOnPoint(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude),
                MapsActivity.STANDARD_CAMERA_SPEED, mMap);
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";

        GeofenceHelper.updateAllFences(apiClient, mGeofenceList, pendingIntent, mapsActivity);

        MapsActivity.pendingMarker = null;
    }

    @Override
    public void onCancelClicked(Marker marker) {
        if(marker.getTitle() == null && marker.getSnippet() == null)
            marker.remove();
        else if(marker.getSnippet() != null)
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        else
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        MapsActivity.pendingMarker = null;
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";
    }
}
