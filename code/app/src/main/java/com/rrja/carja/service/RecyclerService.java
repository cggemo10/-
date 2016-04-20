package com.rrja.carja.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.rrja.carja.constant.Constant;

public class RecyclerService extends Service {

    RecyclerReceiver mReceiver;

    public RecyclerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registReceiver();

        return super.onStartCommand(intent, flags, startId);

    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BORADCAST_APK_DOWNLOAD_PROGRESS);
            filter.addAction(Constant.ACTION_BORADCAST_APK_DOWNLOAD_FAILED);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_FORUM);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_COUPONS);

            mReceiver = new RecyclerReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class RecyclerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
