package com.example.peter.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.blocspot.R;

public class SettingsWindow extends Fragment {

    private View view;

    public static SettingsWindow inflateSettingsWindow() {
        SettingsWindow settingsWindow = new SettingsWindow();
        return settingsWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_window, container, false);
        return view;
    }
}
