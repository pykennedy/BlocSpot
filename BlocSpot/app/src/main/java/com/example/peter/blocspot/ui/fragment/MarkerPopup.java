package com.example.peter.blocspot.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.peter.blocspot.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerPopup implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater = null;
    View popup = null;

    public MarkerPopup(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return popup;
    }

    @Override
    public View getInfoContents(Marker marker) {
        popup=inflater.inflate(R.layout.marker_popup, null);
        TextView tv = (TextView)popup.findViewById(R.id.popup_title);
        tv.setText(marker.getTitle());

        return popup;
    }
}
