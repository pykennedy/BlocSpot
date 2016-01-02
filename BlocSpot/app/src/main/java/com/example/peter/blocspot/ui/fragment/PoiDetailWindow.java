package com.example.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.peter.blocspot.R;
import com.google.android.gms.maps.model.Marker;

import java.lang.ref.WeakReference;

public class PoiDetailWindow extends Fragment implements View.OnClickListener {

    private WeakReference<Delegate> delegate;
    private View view;

    public static interface Delegate {
        public void onEnterClicked(String poiTitle);
        public void onDeleteClicked(Marker marker);
        public void onSaveClicked(Marker marker);
        public void onCancelClicked();
    }

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

    public static PoiDetailWindow inflateAddPOIMenuWindow (Marker marker) {
        // presumably somewhere im going to be using this marker i passed for database stuff
        PoiDetailWindow poiDetailWindow = new PoiDetailWindow();
        return poiDetailWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.poi_detail_window, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == view.findViewById(R.id.poi_detail_title_button)) {
            if(getDelegate() != null) {
                EditText editText = (EditText)view.findViewById(R.id.poi_detail_title_text);
                getDelegate().onEnterClicked(editText.getText().toString());
            }
        }
    }
}
