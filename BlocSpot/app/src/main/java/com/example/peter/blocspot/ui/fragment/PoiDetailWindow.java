package com.example.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.blocspot.R;
import com.google.android.gms.maps.model.Marker;

public class PoiDetailWindow extends Fragment {

    public static PoiDetailWindow inflateAddPOIMenuWindow (Marker marker) {
        // presumably somewhere im going to be using this marker i passed for database stuff
        PoiDetailWindow poiDetailWindow = new PoiDetailWindow();
        return poiDetailWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.poi_detail_window, container, false);
    }
}
