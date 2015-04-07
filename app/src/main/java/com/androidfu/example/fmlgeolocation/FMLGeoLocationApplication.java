package com.androidfu.example.fmlgeolocation;

import android.app.Application;
import android.util.Log;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

/**
 * Created by bmote on 4/6/15.
 */
public class FMLGeoLocationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ETPush.setLogLevel(Log.VERBOSE);
        try {
            ETPush.readyAimFire(this, getString(R.string.et_app_id), getString(R.string.access_token), getString(R.string.gcm_sender_id), true, true, true, true);
        } catch (ETException e) {
            e.printStackTrace();
        }
    }
}