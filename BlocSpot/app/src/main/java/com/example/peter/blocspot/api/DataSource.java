package com.example.peter.blocspot.api;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.appcompat.BuildConfig;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.api.model.PoiItem;
import com.example.peter.blocspot.api.model.database.DatabaseOpenHelper;
import com.example.peter.blocspot.api.model.database.table.PoiItemTable;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private DatabaseOpenHelper databaseOpenHelper;
    private PoiItemTable poiItemTable;
    private List<PoiItem> poiItemList;

    public DataSource() {
        poiItemTable = new PoiItemTable();
        databaseOpenHelper = new DatabaseOpenHelper(BlocSpotApplication.getSharedInstance(),
                poiItemTable);
        poiItemList = new ArrayList<PoiItem>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (BuildConfig.DEBUG && true) {
                    BlocSpotApplication.getSharedInstance().deleteDatabase("blocspot_db");
                }
                SQLiteDatabase writableDatabase = databaseOpenHelper.getWritableDatabase();
            }
        }).start();
    }

    public List<PoiItem> getPoiItemList() {
        return poiItemList;
    }

  //  PoiItem(String titleID, String name, String category, String notes,
    //        int id, double longitude, double latitude, boolean viewed)
    static PoiItem itemFromCursor(Cursor cursor) {
        return new PoiItem(PoiItemTable.getTitleID(cursor), PoiItemTable.getName(cursor),
                PoiItemTable.getCategory(cursor), PoiItemTable.getNotes(cursor),
                (int)PoiItemTable.getID(cursor), Double.parseDouble(PoiItemTable.getLongitude(cursor)),
                Double.parseDouble(PoiItemTable.getLatitude(cursor)), PoiItemTable.getViewed(cursor));
    }
}
