package com.example.peter.blocspot.ui.delegates;

import com.example.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Peter on 1/2/2016.
 */
public class PoiDetailWindowDelegate implements PoiDetailWindow.Delegate {

    @Override
    public void onEnterClicked(String poiTitle) {
        System.out.println(poiTitle);
    }

    @Override
    public void onDeleteClicked(Marker marker) {

    }

    @Override
    public void onSaveClicked(Marker marker) {

    }

    @Override
    public void onCancelClicked() {

    }
}
