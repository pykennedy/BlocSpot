package com.example.peter.blocspot.ui.fragment;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.R;
import com.example.peter.blocspot.api.DataSource;
import com.example.peter.blocspot.api.model.PoiItem;
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.ui.delegates.PoiDetailWindowDelegate;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class PoiDetailWindow extends Fragment implements View.OnClickListener {

    private PoiDetailWindowDelegate delegate;
    private View view;
    private Button delete, save, cancel;
    private CheckBox viewed;
    private EditText title, notes;
    private Spinner category;
    private static Marker currentMarker;
    private GoogleMap mMap;
    GoogleApiClient apiClient;
    List<Geofence> mGeofenceList;
    PendingIntent pendingIntent;
    MapsActivity mapsActivity;
    //private PoIModel model;

    public static interface Delegate {
        public void onDeleteClicked(Marker marker, GoogleMap mMap, GoogleApiClient apiClient);
        public void onSaveClicked(Marker marker, PoiItem poiItem, GoogleMap mMap, GoogleApiClient apiClient,
                                  List<Geofence> mGeofenceList, PendingIntent pendingIntent, MapsActivity mapsActivity);
        public void onCancelClicked(Marker marker);
    }

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate;
    }

    public void setDelegate(PoiDetailWindowDelegate delegate, GoogleMap mMap, GoogleApiClient apiClient,
                            List<Geofence> mGeofenceList, PendingIntent pendingIntent, MapsActivity mapsActivity) {
        this.delegate = delegate;
        this.mMap = mMap;
        this.apiClient = apiClient;
        this.mGeofenceList = mGeofenceList;
        this.pendingIntent = pendingIntent;
        this.mapsActivity = mapsActivity;
    }

    public static PoiDetailWindow inflateAddPOIMenuWindow (Marker marker) {
        // presumably somewhere im going to be using this marker i passed for database stuff
        PoiDetailWindow poiDetailWindow = new PoiDetailWindow();
        currentMarker = marker;
        //poiDetailWindow.setDelegate(new PoiDetailWindowDelegate());

        //poiDetailWindow.setModel

        return poiDetailWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.poi_detail_window, container, false);

        title = (EditText) view.findViewById(R.id.poi_detail_title);
        viewed = (CheckBox) view.findViewById(R.id.poi_detail_viewed);
        notes = (EditText) view.findViewById(R.id.poi_detail_notes);
        category = (Spinner) view.findViewById(R.id.poi_detail_category_dropdown);
        delete = (Button) view.findViewById(R.id.poi_detail_delete);
        delete.setOnClickListener(this);
        save = (Button) view.findViewById(R.id.poi_detail_save);
        save.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.poi_detail_cancel);
        cancel.setOnClickListener(this);

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        notes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        viewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(buttonView.getContext(), "Viewed!", Toast.LENGTH_SHORT).show();
            }
        });

        if(currentMarker.getSnippet() != null)
            title.setText(currentMarker.getSnippet());
        if(currentMarker.getTitle()!=null) {
            DataSource dataSource = BlocSpotApplication.getSharedDataSource();
            PoiItem poiItem = dataSource.getPoiItem(currentMarker.getTitle());
            this.loadInfo(poiItem);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == delete) {
            getDelegate().onDeleteClicked(currentMarker, mMap, apiClient);
        }
        if( v == save) {
           // public PoiItem(String titleID, String name, String category, String notes,
            //long id, double longitude, double latitude, boolean viewed)
            if(currentMarker.getTitle() == null)
                currentMarker.setTitle(currentMarker.getId());
            String name = title.getText().toString();
            if(name.equals(""))
                name = "Unnamed POI";
            PoiItem poiItem = new PoiItem(currentMarker.getTitle(), name,
                    category.getSelectedItem().toString(), notes.getText().toString(),
                    -1, currentMarker.getPosition().longitude,
                    currentMarker.getPosition().latitude, viewed.isChecked());
            getDelegate().onSaveClicked(currentMarker, poiItem, mMap, apiClient, mGeofenceList,
                    pendingIntent, mapsActivity);

        }
        if(v == cancel) {
            getDelegate().onCancelClicked(currentMarker);
        }
    }

    public void loadInfo(PoiItem poiItem) {
        if(poiItem.getName().equals("Unnamed POI"))
            title.setText("");
        else
            title.setText(poiItem.getName());
        category.setSelection(getSpinnerPosition(poiItem.getCategory()));
        notes.setText(poiItem.getNotes());
        viewed.setChecked(poiItem.isViewed());
        if(viewed.isChecked())
            System.out.println("ITS CHECKED DUNNO WUTS BROKE");
    }
    public int getSpinnerPosition(String categoryString) {
        int index = 0;
        for(int i=0; i<category.getCount();i++) {
            if(category.getItemAtPosition(i).equals(categoryString))
                index = i;
        }
        return index;
    }
}