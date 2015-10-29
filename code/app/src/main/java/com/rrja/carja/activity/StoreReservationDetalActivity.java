package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.location.BDLocation;
import com.rrja.carja.R;
import com.rrja.carja.RRjaApplication;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.fragment.store.StoreBookingFragment;
import com.rrja.carja.fragment.store.StoreMainFragment;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.StoreReservationBinder;

/**
 * Created by chongge on 15/6/28.
 */
public class StoreReservationDetalActivity extends BaseActivity{

    CarStore store;
    Fragment currFragment;
    private FragmentManager fm;

    BaseElementFragment storeMainFragment;
    BaseElementFragment storeBookFragment;

    StoreReservationBinder mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_maintenance_detal);
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

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("curr_store")) {
            store = extras.getParcelable("curr_store");
        }

        if (store == null) {
            finish();
        }

        if (!TextUtils.isEmpty(store.getStoreName())) {
            setTitle(store.getStoreName());
        }

        if (mService == null) {
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_STORE_RESERVATION_SERVICE);
            bindService(intent, conn, BIND_AUTO_CREATE);
        }

        fm = getFragmentManager();
        storeMainFragment = StoreMainFragment.newInstance();
        storeBookFragment = StoreBookingFragment.newInstance();

        switchFragment(storeMainFragment, false);
    }

    @Override
    protected void onDestroy() {
        if (mService != null) {
            unbindService(conn);
        }
        super.onDestroy();
    }

    public CarStore getCurrentStore() {
        return store;
    }

    private void switchFragment(BaseElementFragment fragment, boolean leftIn) {

        if (fragment == null) {
            return;
        }
        currFragment = fragment;
        FragmentTransaction transaction = fm.beginTransaction();
        if (leftIn) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.replace(R.id.fl_store_content, fragment);
        transaction.commit();
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (StoreReservationBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    // -----------------------------------------------------------------------------
    public StoreBookingFragment.OnBookActionListener getBookActionListener() {
        return new BookActionListener();
    }

    private class BookActionListener implements StoreBookingFragment.OnBookActionListener {

        @Override
        public void onBookCommit(String name, String tel, String time, String loc) {
            if (mService != null) {
                mService.commitBookStore(store.getStoreId(), time);
            }
        }

        @Override
        public void onBackClicked() {

            switchFragment(storeMainFragment, true);
        }

        @Override
        public void onCommitResult(boolean result) {
            if (result) {
                finish();
            } else {
                switchFragment(storeMainFragment, true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currFragment instanceof StoreBookingFragment) {
                switchFragment(storeMainFragment, true);
                return true;
            }

            if (currFragment instanceof StoreMainFragment) {
                finish();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    // ------------------------------------------------------------------------------------------
    public StoreMainFragment.OnStoreMainActionListener getStoreMainActionListener () {
        return new StoreMainActionListener();
    }

    private class StoreMainActionListener implements StoreMainFragment.OnStoreMainActionListener {

        @Override
        public void onAddBook() {
            switchFragment(storeBookFragment, false);
        }

        @Override
        public void onBackClicked() {
            finish();
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class LocationChangeListener implements RRjaApplication.OnLocationChangeListener {

        @Override
        public void onLocationChanged(BDLocation location) {
            // TODO
        }
    }
}
