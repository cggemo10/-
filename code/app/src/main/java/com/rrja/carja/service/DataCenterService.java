package com.rrja.carja.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.service.impl.CarBinder;
import com.rrja.carja.service.impl.ForumBinder;
import com.rrja.carja.service.impl.ProductBinder;
import com.rrja.carja.service.impl.UserBinder;

public class DataCenterService extends Service {

    public DataCenterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        String action = intent.getAction();
        if (Constant.ACTION_USER_SERVICE.equals(action)) {
            return new UserBinder(this);
        }
        if (Constant.ACTION_CAR_SERVICE.equals(action)) {
            return new CarBinder(this);
        }
        if (Constant.ACTION_FORUM_SERVICE.equals(action)) {
            return new ForumBinder(this);
        }
        if (Constant.ACTION_PRODUCT_SERVICE.equals(action)) {
            return new ProductBinder(this);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
