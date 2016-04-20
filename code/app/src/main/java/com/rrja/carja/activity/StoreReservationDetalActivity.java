package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.rrja.carja.R;
import com.rrja.carja.RRjaApplication;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.fragment.store.StoreBookingFragment;
import com.rrja.carja.fragment.store.StoreMainFragment;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.StoreReservationBinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chongge on 15/6/28.
 */
public class StoreReservationDetalActivity extends BaseActivity {

    public static final String ROUTE_START_NODE = "route_Start_Node";
    public static final String ROUTE_END_NODE = "route_End_Node";

    CarStore store;
    Fragment currFragment;
    private FragmentManager fm;

    BaseElementFragment storeMainFragment;
    BaseElementFragment storeBookFragment;

    StoreReservationBinder mService;
    LocationChangeListener locationListener;

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
            // just for test TODO　delete
            store.setLat(30.3227);
            store.setLng(104.3605);
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
        storeMainFragment = StoreMainFragment.newInstance(store);
        storeBookFragment = StoreBookingFragment.newInstance(store);

        switchFragment(storeMainFragment, false);

        locationListener = new LocationChangeListener();
        ((RRjaApplication) getApplication()).registLocationChangeListener(StoreReservationDetalActivity.class.getName(), locationListener);
    }

    // 百度导航
    private void startNav() {
        String sdcardDir = getSdcardDir();
        if (TextUtils.isEmpty(sdcardDir)) {
            Toast.makeText(this, "未找到SDK卡", Toast.LENGTH_LONG).show();
        }
        String packageName = this.getPackageName();
        File dir = new File(sdcardDir, packageName);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        BaiduNaviManager.getInstance().init(this, getSdcardDir(), this.getPackageName(),
                new BaiduNaviManager.NaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 != status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(StoreReservationDetalActivity.this, "认证失败，请在官网下载App", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void initStart() {

                    }

                    @Override
                    public void initSuccess() {
                        routeplanToNavi();
                    }

                    @Override
                    public void initFailed() {
                        Log.e(StoreReservationDetalActivity.this.getClass().getName(), "nav init failed");
                    }
                }, null);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi() {
        if (locationListener == null || locationListener.getLocation() == null) {
            if (locationListener == null) {

                return;
            }
            if (locationListener.getLocation() == null) {
                return;
            }
        }
        final BNRoutePlanNode sNode = new BNRoutePlanNode(locationListener.getLocation().getLongitude(), locationListener.getLocation().getLatitude(),  "当前位置", null, BNRoutePlanNode.CoordinateType.BD09LL);
        final BNRoutePlanNode eNode = new BNRoutePlanNode( Double.valueOf(store.getLng()),Double.valueOf(store.getLat()), store.getStoreName(), null, BNRoutePlanNode.CoordinateType.BD09LL);

        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManager.getInstance().launchNavigator(this, list, BaiduNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_AVOID_TAFFICJAM, false, new BaiduNaviManager.RoutePlanListener() {
            @Override
            public void onJumpToNavigator() {
// TODO
                Intent intent = new Intent(StoreReservationDetalActivity.this, NavActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ROUTE_START_NODE, sNode);
                bundle.putSerializable(ROUTE_END_NODE, eNode);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFailed() {
                Toast.makeText(StoreReservationDetalActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (mService != null) {
            unbindService(conn);
        }
        ((RRjaApplication) getApplication()).unRegistLocationChangeListener(StoreReservationDetalActivity.class.getName());
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
    public StoreMainFragment.OnStoreMainActionListener getStoreMainActionListener() {
        return new StoreMainActionListener();
    }

    private class StoreMainActionListener implements StoreMainFragment.OnStoreMainActionListener {

        @Override
        public void onAddBook() {
            switchFragment(storeBookFragment, false);
        }

        @Override
        public void onNavClicked() {
            startNav();
        }

        @Override
        public void onBackClicked() {
            finish();
        }
    }


    // ---------------------------------------------------------------------------------------------
    private class LocationChangeListener implements RRjaApplication.OnLocationChangeListener {

        BDLocation mLocation;

        @Override
        public void onLocationChanged(BDLocation location) {
            mLocation = location;
        }

        public BDLocation getLocation() {
            return mLocation;
        }
    }
}
