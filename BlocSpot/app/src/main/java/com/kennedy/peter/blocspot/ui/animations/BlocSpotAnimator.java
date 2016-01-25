package com.kennedy.peter.blocspot.ui.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.kennedy.peter.blocspot.ui.activity.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class BlocSpotAnimator {
    public static void expand(final View v, int targetHeight) {
        final int tempHeight = targetHeight;
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int height = (int)(tempHeight * interpolatedTime);
                v.getLayoutParams().height = height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        float density =  v.getContext().getResources().getDisplayMetrics().density;
        a.setDuration(250);
        a.setInterpolator(new LinearInterpolator());
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(250);
        v.startAnimation(a);
    }
    public static void centerMapOnPoint(LatLng location, int speed, GoogleMap mMap) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
    }
    public static void offsetCenterMapOnPoint(LatLng location, int speed, GoogleMap mMap) {
        double offset = (location == MapsActivity.user) ? 0.0014 : 0.0012;
        LatLng temp = new LatLng(location.latitude - offset, location.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 17), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {}

            @Override
            public void onCancel() {}
        });
    }
    public static void centerMapOnPointZoomedIn(LatLng location, int speed, GoogleMap mMap) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
    }
    public static void centerMapOnPointZoomedOut(LatLng location, int speed, GoogleMap mMap) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15), speed, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
