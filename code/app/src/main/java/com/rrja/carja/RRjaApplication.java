package com.rrja.carja;

import android.app.Application;

import com.baidu.location.LocationClient;

/**
 * Created by Administrator on 2015/10/28.
 */
public class RRjaApplication extends Application {

    public LocationClient mLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationClient = new LocationClient(this.getApplicationContext());
    }
}
