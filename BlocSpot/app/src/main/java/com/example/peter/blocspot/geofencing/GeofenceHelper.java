package com.example.peter.blocspot.geofencing;

import android.app.PendingIntent;
import android.content.Intent;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.api.DataSource;
import com.example.peter.blocspot.api.model.PoiItem;
import com.example.peter.blocspot.ui.activity.MapsActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class GeofenceHelper {
    public static void updateAllFences(GoogleApiClient apiClient, List<Geofence> mGeofenceList,
                                       PendingIntent pendingIntent, MapsActivity mapsActivity) {
        DataSource dataSource = BlocSpotApplication.getSharedDataSource();
        List<PoiItem> poiItemList = dataSource.getPoiItemList();

        if(poiItemList != null || !poiItemList.isEmpty()) {
            for(int i = 0; i<poiItemList.size(); i++) {
                GeofenceHelper.addFence(mGeofenceList, poiItemList.get(i));
            }
            LocationServices.GeofencingApi.addGeofences(apiClient,
                    GeofenceHelper.getGeofencingRequest(mGeofenceList),
                    GeofenceHelper.getGeofencePendingIntent(mapsActivity, pendingIntent))
                    .setResultCallback(mapsActivity);
        }
    }

    public static void addFence(List<Geofence> mGeofenceList, PoiItem poiItem) {
        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(poiItem.getTitleID())
                .setCircularRegion(poiItem.getLatitude(), poiItem.getLongitude(), 500)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .build());
    }

    public static GeofencingRequest getGeofencingRequest(List<Geofence> mGeofenceList) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofences(mGeofenceList)
                .build();
    }

    public static PendingIntent getGeofencePendingIntent(MapsActivity mapsActivity, PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(mapsActivity, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(mapsActivity, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }
}
