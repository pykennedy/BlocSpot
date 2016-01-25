package com.kennedy.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.kennedy.peter.blocspot.R;
import com.kennedy.peter.blocspot.ui.delegates.SearchWindowDelegate;

public class SearchWindow extends Fragment {

    private View view;
    private EditText searchParams;
    private Button searchButton;
    private SearchWindowDelegate delegate;
    private GoogleMap mMap;

    public static interface Delegate {
        public void searchClicked(String searchParams, GoogleMap mMap);
    }

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate;
    }

    public void setDelegate(SearchWindowDelegate delegate, GoogleMap mMap) {
        this.delegate = delegate;
        this.mMap = mMap;
    }

    public static SearchWindow inflateSearchWindow() {
        SearchWindow searchWindow = new SearchWindow();
        return searchWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_window, container, false);
        searchParams = (EditText)view.findViewById(R.id.search_edit_text);
        searchButton = (Button)view.findViewById(R.id.search_window_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDelegate().searchClicked(searchParams.getText().toString(), mMap);
                System.out.println("GOT TO ON CLICK");
            }
        });
        return view;
    }
}
