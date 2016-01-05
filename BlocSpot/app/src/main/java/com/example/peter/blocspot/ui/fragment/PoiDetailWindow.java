package com.example.peter.blocspot.ui.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.blocspot.R;
import com.example.peter.blocspot.ui.delegates.PoiDetailWindowDelegate;
import com.google.android.gms.maps.model.Marker;

import java.lang.ref.WeakReference;

public class PoiDetailWindow extends Fragment implements View.OnClickListener {

    private WeakReference<Delegate> delegate;
    private View view;
    private Button delete, save, cancel;
    private CheckBox viewed;
    private EditText title, notes;
    private static Marker currentMarker;
    //private PoIModel model;

    public static interface Delegate {
        public void onDeleteClicked(Marker marker);
        public void onSaveClicked(Marker marker);
        public void onCancelClicked(Marker marker);
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
        currentMarker = marker;

        //poiDetailWindow.setModel

        return poiDetailWindow;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.poi_detail_window, container, false);
        setDelegate(new PoiDetailWindowDelegate());

        title = (EditText) view.findViewById(R.id.poi_detail_title);
        viewed = (CheckBox) view.findViewById(R.id.poi_detail_viewed);
        notes = (EditText) view.findViewById(R.id.poi_detail_notes);
        delete = (Button) view.findViewById(R.id.poi_detail_delete);
        delete.setOnClickListener(this);
        save = (Button) view.findViewById(R.id.poi_detail_save);
        save.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.poi_detail_cancel);
        cancel.setOnClickListener(this);

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(v.getContext(), "title", Toast.LENGTH_LONG).show();

                return false;
            }
        });
        notes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(v.getContext(), "notes", Toast.LENGTH_LONG).show();

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

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == delete) {
            Toast.makeText(v.getContext(), "delete", Toast.LENGTH_LONG).show();
        }
        if( v == save) {
            Toast.makeText(v.getContext(), "save", Toast.LENGTH_LONG).show();
        }
        if(v == cancel) {
            getDelegate().onCancelClicked(currentMarker);
        }
    }
}