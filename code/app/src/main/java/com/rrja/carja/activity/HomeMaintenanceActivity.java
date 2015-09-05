package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.car.CarListFragment;
import com.rrja.carja.fragment.homemaintenance.BaseElementFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceGoodsFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceMainFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceSubServiceFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceTagServiceFragment;
import com.rrja.carja.fragment.homemaintenance.TagMaintenanceFragment;
import com.rrja.carja.fragment.homemaintenance.TagServiceActionListener;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableSubService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.MaintenanceBinder;
import com.rrja.carja.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeMaintenanceActivity extends BaseActivity {

    private static final int ACTION_REQUEST_CAR = 11;

    private MaintenanceOrder mOrder;

    Fragment currFragment;
    private FragmentManager fm;

    private ArrayList<Fragment> tagFragmentList = new ArrayList<>();

    private MaintenancePagerAdapter maintenancePagerAdapter;

    private MaintenanceBinder mBinder;
    private MaintenanceReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_maintenance);

        initTags();

        fm = getSupportFragmentManager();

        mOrder = new MaintenanceOrder();
        mOrder.setUserInfo(CoreManager.getManager().getCurrUser());

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_MAINTENANCE_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);

        DialogHelper.getHelper().init(this);

        registeReceiver();

        MaintenanceMainFragment fragment = MaintenanceMainFragment.newInstance();
        switchFragment(fragment, false);

        List<CarInfo> carInfos = CoreManager.getManager().getUserCars();
        if (carInfos != null && carInfos.size() == 1) {
            mOrder.setmCarInfo(carInfos.get(0));
        } else {
            Intent intentCarInfo = new Intent(this, CarInfoActivity.class);
            startActivityForResult(intentCarInfo,ACTION_REQUEST_CAR);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_REQUEST_CAR && resultCode == RESULT_OK) {
            DialogHelper.getHelper().init(this);
            DialogHelper.getHelper().showWaitting();

            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_REQUEST_REFRESH_USER_CAR);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            unbindService(conn);
        }
        unregistReceiver();
        super.onDestroy();
    }

    private void registeReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_USER_CARS);
            filter.addAction(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);

            mReceiver = new MaintenanceReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unregistReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private void switchFragment(BaseElementFragment fragment, boolean addToStack) {

        if (fragment == null) {
            return;
        }
        currFragment = fragment;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_maintenance_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setCarInfo(CarInfo carInfo) {
        if (mOrder != null) {
            mOrder.setmCarInfo(carInfo);
        }
    }


    public void addOrderGoods(MaintenanceService service, MaintenanceService subService,
                              MaintenanceService feeService, MaintenanceGoods goods) {
        if (subService != null && feeService != null && service != null && goods != null && mOrder != null) {
            TagableSubService tagService = new TagableSubService();
            tagService.setSubService(subService);
            tagService.setGoods(goods);

            service.setAmount(feeService.getAmount());

            mOrder.addGoods(service.getId(), service, tagService);
        }
    }

    public MaintenanceOrder getOrderInfo() {
        return mOrder;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!fm.popBackStackImmediate()) {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public MaintenanceOrder getmOrder() {
        return mOrder;
    }

    // -----------------------------------------------------------------------------------------
    private void initTags() {
        Fragment tagMaintenance = TagMaintenanceFragment.newInstance("101");
        Fragment tagRepair = TagMaintenanceFragment.newInstance("102");
        Fragment tagCosmetology = TagMaintenanceFragment.newInstance("103");

        tagFragmentList.add(tagMaintenance);
        tagFragmentList.add(tagRepair);
        tagFragmentList.add(tagCosmetology);

        maintenancePagerAdapter = new MaintenancePagerAdapter(getSupportFragmentManager());
    }

    public MaintenancePagerAdapter getPagerAdapter() {
        return maintenancePagerAdapter;
    }


    public class MaintenancePagerAdapter extends FragmentPagerAdapter {


        public MaintenancePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tagFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return tagFragmentList.size();
        }
    }

    //-------------------------------------------------------------------------------------
    public TagServiceActionListener getTagServiceListener() {
        return new TagServiceListener();
    }

    private class TagServiceListener implements TagServiceActionListener {

        @Override
        public void requestService(String serviceId) {
            if (mBinder != null) {
                mBinder.getService(serviceId);
            }
        }

        @Override
        public void onServiceClicked(MaintenanceService service) {
            MaintenanceSubServiceFragment subServiceFm = MaintenanceSubServiceFragment.newInstance();
            subServiceFm.setService(service);
            switchFragment(subServiceFm,true);
        }
    }

    // -------------------------------------------------------------------------------
    public MaintenanceTagServiceFragment.MaintenanceTagActionListener getTagActionListener() {
        return new TagActionListener();
    }

    private class TagActionListener implements MaintenanceTagServiceFragment.MaintenanceTagActionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    //---------------------------------------------------------------------------------

    public MaintenanceSubServiceFragment.SubServiceActionListener getSubServiceListener() {
        return new SubServiceListener();
    }

    private class SubServiceListener implements MaintenanceSubServiceFragment.SubServiceActionListener {

        @Override
        public void onSubServiceClicked(MaintenanceService service, MaintenanceService feeService) {
            if (service == null) {
                return;
            }

            if (feeService == null) {
                // TODO
            }

            MaintenanceGoodsFragment fragment = MaintenanceGoodsFragment.newInstance();
            fragment.setSubService(service);
            fragment.setFeeService(feeService);
            switchFragment(fragment, true);
        }

        @Override
        public void requestSubService(String serviceId) {
            if (mBinder != null) {
                mBinder.getSubService(serviceId);
            }
        }
    }

    // ----------------------------------------------------------------------------------
    public MaintenanceMainFragment.OnMaintenancdMainFragmentionListener getMaintenanceMainListener() {

        return new MaintenActionListener();
    }

    private class MaintenActionListener implements MaintenanceMainFragment.OnMaintenancdMainFragmentionListener {

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MaintenanceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    //--------------------------------------------------------------------------------------
    public MaintenanceGoodsFragment.OnMaintenanceGoodsListener getMaintenGoodsListener() {
        return new MaintenanceGoodsListener();
    }

    private class MaintenanceGoodsListener implements MaintenanceGoodsFragment.OnMaintenanceGoodsListener {

        @Override
        public void onGoodsCommit(TagableSubService tagableSubService) {
//            mOrder.addGoods();
        }

        @Override
        public void requestGoods(String serviceId, int page) {

            if (mBinder != null) {
                mBinder.getServiceGoods(serviceId, page);
            }
        }

        @Override
        public void refreshGoods() {

//            CoreManager.getManager().
        }
    }

    private class MaintenanceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            DialogHelper.getHelper().dismissWatting();
            if (Constant.ACTION_BROADCAST_GET_USER_CARS.equals(action)) {

                List<CarInfo> userCars = CoreManager.getManager().getUserCars();
                if (userCars.size() != 0) {

                }
            }

            if (Constant.ACTION_BROADCAST_GET_USER_CARS_ERR.equals(action)) {
                Toast.makeText(context, "获取车辆信息失败，请稍后再试！", Toast.LENGTH_LONG).show();
            }
        }
    }
}
