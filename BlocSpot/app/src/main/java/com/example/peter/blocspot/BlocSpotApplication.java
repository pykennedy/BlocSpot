package com.example.peter.blocspot;

import android.app.Application;

import com.example.peter.blocspot.api.DataSource;

public class BlocSpotApplication extends Application {
    public static BlocSpotApplication getSharedInstance() {
        return sharedInstance;
    }
    public static DataSource getSharedDataSource() {
        return BlocSpotApplication.getSharedInstance().getDataSource();
    }

    private static BlocSpotApplication sharedInstance;
    private DataSource dataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
        dataSource = new DataSource();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
