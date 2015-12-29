package com.example.peter.blocspot;

import android.test.SingleLaunchActivityTestCase;

import com.example.peter.blocspot.ui.activity.MapsActivity;

/**
 * Created by Peter on 12/27/2015.
 */
public class ActivityTest extends SingleLaunchActivityTestCase<MapsActivity> {

    public ActivityTest() {
        super("com.example.peter.blocspot", MapsActivity.class);
    }

    private MapsActivity activity;
    private boolean menuShouldBeOpen;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        menuShouldBeOpen = false;
        activity = getActivity();
    }

    /*
    public void testMenuOpenClose() throws Exception {
        menuShouldBeOpen = activity.setLights("menu");

        if(menuShouldBeOpen) {
            assertEquals(activity.mMenu.getItem(0).getIcon(), R.drawable.menu_light);
        }
        else {
            assertEquals(activity.mMenu.getItem(0).getIcon(), R.drawable.menu_dark);
        }
    }
    */
}
