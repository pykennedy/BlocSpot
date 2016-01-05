package com.example.peter.blocspot.ui.delegates;

import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.example.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.maps.model.Marker;

public class PoiDetailWindowDelegate implements PoiDetailWindow.Delegate {

    @Override
    public void onDeleteClicked(Marker marker) {

    }

    @Override
    public void onSaveClicked(Marker marker) {

    }

    @Override
    public void onCancelClicked(Marker marker) {
        marker.remove();
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
    }
}
