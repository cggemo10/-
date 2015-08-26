package com.rrja.carja.activity;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.homemaintenance.BaseElementFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceGoodsFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceMainFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceSubServiceFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceTagServiceFragment;
import com.rrja.carja.fragment.homemaintenance.TagMaintenanceFragment;
import com.rrja.carja.fragment.homemaintenance.TagServiceActionListener;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableSubService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.MaintenanceBinder;

import java.util.ArrayList;
import java.util.List;

public class HomeMaintenanceActivity extends AppCompatActivity {

    private MaintenanceOrder mOrder;

    Fragment currFragment;
    private FragmentManager fm;

    private ArrayList<Fragment> tagFragmentList = new ArrayList<>();

    private MaintenancePagerAdapter maintenancePagerAdapter;

    private MaintenanceBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_home_maintenance);

        initTags();

        fm = getSupportFragmentManager();

        mOrder = new MaintenanceOrder();
        mOrder.setUserInfo(CoreManager.getManager().getCurrUser());
        List<CarInfo> carInfos = CoreManager.getManager().getUserCars();
        if (carInfos != null && carInfos.size() != 0) {
            mOrder.setmCarInfo(carInfos.get(0));
        }
        MaintenanceMainFragment fragment = MaintenanceMainFragment.newInstance();
        switchFragment(fragment, false);

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_MAINTENANCE_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            unbindService(conn);
        }
        super.onDestroy();
    }

    private void switchFragment(BaseElementFragment fragment, boolean addToStack) {

        if (fragment == null) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_maintenance_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
            mOrder.addGoods();
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
}
