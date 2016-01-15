package com.example.peter.blocspot.ui.delegates;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.api.DataSource;
import com.example.peter.blocspot.api.model.PoiItem;
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.example.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class PoiDetailWindowDelegate implements PoiDetailWindow.Delegate {

    private DataSource dataSource = BlocSpotApplication.getSharedDataSource();

    @Override
    public void onDeleteClicked(Marker marker, GoogleMap mMap) {

        dataSource.removePOI(dataSource.getPoiItem(marker.getTitle()).getId());

        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";

        marker.remove();

        MapsActivity.pendingMarker = null;
    }

    @Override
    public void onSaveClicked(Marker marker, PoiItem poiItem, GoogleMap mMap) {

        poiItem.setId(dataSource.getPoiItem(marker.getTitle()).getId());

        dataSource.savePOI(poiItem);

        BlocSpotAnimator.centerMapOnPoint(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude),
                MapsActivity.STANDARD_CAMERA_SPEED, mMap);
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";

        MapsActivity.pendingMarker = null;
    }

    @Override
    public void onCancelClicked(Marker marker) {
        if(marker.getTitle() == null)
            marker.remove();
        MapsActivity.pendingMarker = null;
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
        MapsActivity.windowIsOpen = false;
        MapsActivity.activeMenu = "";
    }
}
