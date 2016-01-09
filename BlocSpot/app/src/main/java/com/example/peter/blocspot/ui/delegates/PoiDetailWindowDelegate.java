package com.example.peter.blocspot.ui.delegates;

import android.database.Cursor;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.api.DataSource;
import com.example.peter.blocspot.api.model.PoiItem;
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.example.peter.blocspot.ui.animations.BlocSpotAnimator;
import com.example.peter.blocspot.ui.fragment.PoiDetailWindow;
import com.google.android.gms.maps.model.Marker;

public class PoiDetailWindowDelegate implements PoiDetailWindow.Delegate {

    private DataSource dataSource = BlocSpotApplication.getSharedDataSource();

    @Override
    public void onDeleteClicked(Marker marker) {
        Cursor itemCursor = dataSource.getPoiItemTable().fetchRowFromMarkerID(
                dataSource.getDatabaseOpenHelper().getReadableDatabase(), marker.getId());
        if(itemCursor.moveToFirst()) {
            System.out.println(DataSource.itemFromCursor(itemCursor).getId());
        }

        dataSource.removePOI(DataSource.itemFromCursor(itemCursor).getId());
        itemCursor.close();

        itemCursor = dataSource.getPoiItemTable().fetchRowFromMarkerID(
                dataSource.getDatabaseOpenHelper().getReadableDatabase(), marker.getId());
        if(itemCursor.moveToFirst())
            System.out.println(DataSource.itemFromCursor(itemCursor).getId());
        itemCursor.close();
        marker.remove();
    }

    @Override
    public void onSaveClicked(Marker marker, PoiItem poiItem) {
        Cursor itemCursor = dataSource.getPoiItemTable().fetchRowFromMarkerID(
                dataSource.getDatabaseOpenHelper().getReadableDatabase(), poiItem.getTitleID());
        if(itemCursor.moveToFirst())
            poiItem.setId(DataSource.itemFromCursor(itemCursor).getId());
        itemCursor.close();
        // not sure if i ever use this??? keeping it until confirmed useless
        //dataSource.getPoiItemList().add(poiItem);

        dataSource.savePOI(poiItem);
        itemCursor = dataSource.getPoiItemTable().fetchRow(
                dataSource.getDatabaseOpenHelper().getReadableDatabase(), poiItem.getId());
        itemCursor.moveToFirst();
        System.out.println(DataSource.itemFromCursor(itemCursor).getId());
        itemCursor.close();
    }

    @Override
    public void onCancelClicked(Marker marker) {
        marker.remove();
        BlocSpotAnimator.collapse(MapsActivity.getCurrentWindow());
    }
}
