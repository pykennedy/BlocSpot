package com.example.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.peter.blocspot.R;
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.yelp.YelpAPI;

public class SearchWindow extends Fragment {

    private View view;
    private Button searchButton;

    public static SearchWindow inflateSearchWindow() {
        SearchWindow searchWindow = new SearchWindow();
        return searchWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_window, container, false);
        searchButton = (Button)view.findViewById(R.id.search_window_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YelpAPI yelpAPI = new YelpAPI();
                String result = yelpAPI.searchForBusinessesByLocation("Starbucks", MapsActivity.user);
                System.out.println(result);
            }
        });
        return view;
    }
}
