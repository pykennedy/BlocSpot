package com.example.peter.blocspot.api;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                if (false) {
                    BlocSpotApplication.getSharedInstance().deleteDatabase("blocspot_db");
                }
                SQLiteDatabase writableDatabase = databaseOpenHelper.getWritableDatabase();
            }
        }).start();
    }

    public PoiItemTable getPoiItemTable() {
        return poiItemTable;
    }

    public DatabaseOpenHelper getDatabaseOpenHelper() {
        return databaseOpenHelper;
    }

  //  PoiItem(String titleID, String name, String category, String notes,
    //        int id, double longitude, double latitude, boolean viewed)
    public static PoiItem itemFromCursor(Cursor cursor) {
        return new PoiItem(PoiItemTable.getTitleID(cursor), PoiItemTable.getName(cursor),
                PoiItemTable.getCategory(cursor), PoiItemTable.getNotes(cursor),
                PoiItemTable.getID(cursor), Double.parseDouble(PoiItemTable.getLongitude(cursor)),
                Double.parseDouble(PoiItemTable.getLatitude(cursor)), PoiItemTable.getViewed(cursor));
    }

    public List<PoiItem> getPoiItemList() {
        ArrayList<PoiItem> poiItems = new ArrayList<>();
        DataSource dataSource = BlocSpotApplication.getSharedDataSource();
        Cursor cursor = dataSource.getPoiItemTable().fetchAllItems(
                dataSource.getDatabaseOpenHelper().getReadableDatabase());
        if (cursor.moveToFirst()) {
            do {
                poiItems.add(itemFromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return poiItems;
    }

    public void savePOI(PoiItem poiItem) {
        PoiItemTable.Builder builder = new PoiItemTable.Builder()
                .setCategory(poiItem.getCategory())
                .setName(poiItem.getName())
                .setNotes(poiItem.getNotes())
                .setViewed(poiItem.isViewed())
                .setLatitude(poiItem.getLatitude())
                .setLongitude(poiItem.getLongitude())
                .setTitleID(poiItem.getTitleID());
        if(poiItem.getId() != -1) {
            builder.update(databaseOpenHelper.getWritableDatabase(), poiItem.getId());
            System.out.println("update");
        }
        else {
            poiItem.setId(builder.insert(databaseOpenHelper.getWritableDatabase()));
            System.out.println("insert");
        }
    }
    public void removePOI(long id) {
        PoiItemTable.Builder builder = new PoiItemTable.Builder();
        builder.remove(databaseOpenHelper.getWritableDatabase(), id);
        System.out.println("remove");
    }
}
