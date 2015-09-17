package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rrja.carja.R;
import com.rrja.carja.adapter.StoreReservationAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.FileService;
import com.rrja.carja.service.impl.StoreReservationBinder;

public class StoreReservationActivity extends BaseActivity implements StoreReservationAdapter.StoreActionListener{

//    ListView listViewStore;
    private RecyclerView recyclerView;
    private StoreReservationAdapter mAdapter;

    private StoreReservationBinder storeService;
    private StoreReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_maintenance);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_store_reservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StoreReservationAdapter();
        mAdapter.setActionListener(this);
        recyclerView.setAdapter(mAdapter);

        registReceiver();

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_STORE_RESERVATION_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {

        if (storeService != null) {
            unbindService(conn);
        }
        unRegistReceiver();
        super.onDestroy();
    }

    @Override
    public void onRequestStorePic(CarStore carStore) {
        Intent intent = new Intent(this, FileService.class);
        intent.setAction(FileService.ACTION_IMG_STORE);
        intent.putExtra("car_store", carStore);
        startService(intent);
    }

    private void registReceiver() {

        if (mReceiver == null) {

            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BORADCAST_GET_STORE_LIST);
            filter.addAction(Constant.ACTION_BROADCAST_GET_STORE_LIST_ERROR);

            mReceiver = new StoreReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unRegistReceiver() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private class StoreReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            storeService = (StoreReservationBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            storeService = null;
        }
    };

}
