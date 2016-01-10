package com.example.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.peter.blocspot.R;
import com.example.peter.blocspot.ui.adapter.ItemAdapter;
import com.example.peter.blocspot.ui.delegates.MenuWindowDelegate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MenuWindow extends Fragment implements View.OnClickListener{
    private MenuWindowDelegate delegate;
    private View view;
    private GoogleMap mMap;
    private ItemAdapter itemAdapter;
    private Spinner category;

    public static interface Delegate {
        public void onItemClicked(Marker marker, GoogleMap mMap);
        public void onSpinnerChanged();
    }

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate;
    }

    public void setDelegate(MenuWindowDelegate delegate, GoogleMap mMap) {
        this.delegate = delegate;
        this.mMap = mMap;
    }

    public static MenuWindow inflateMenuWindow() {
        MenuWindow menuWindow = new MenuWindow();
        return menuWindow;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_window, container, false);

        category = (Spinner) view.findViewById(R.id.menu_category_dropdown);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(category.getItemAtPosition(position));
                /*
                I NEED TO FIGURE OUT HOW TO FILTER OUT MY POI ITEMS IN MY RECYCLER VIEW BASED
                ON THE CATEGORY THE USER SELECTED... BUT HOW?!?!?!?!?!
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemAdapter = new ItemAdapter();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_poi_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);




        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
