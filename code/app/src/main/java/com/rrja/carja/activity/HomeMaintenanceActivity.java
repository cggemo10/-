package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.homemaintenance.BaseElementFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceGoodsFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceMainFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceSubServiceFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceTagListServiceFragment;
import com.rrja.carja.fragment.homemaintenance.TagServiceActionListener;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableService;
import com.rrja.carja.model.maintenance.TagableSubService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.MaintenanceBinder;
import com.rrja.carja.utils.DialogHelper;

import java.util.List;

public class HomeMaintenanceActivity extends BaseActivity {

    public static final int ACTION_REQUEST_CAR = 11;

    private MaintenanceOrder mOrder;

    Fragment currFragment;
    private FragmentManager fm;

    private MaintenanceBinder mBinder;
    private MaintenanceReceiver mReceiver;

    private ImageView imgAdd;

    private MaintenanceMainFragment mainFragment;
    private MaintenanceTagListServiceFragment tagListMaintenanceFragment;
    private MaintenanceSubServiceFragment subServiceFragment;
    private MaintenanceGoodsFragment goodsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_maintenance);

        if (llloc != null) {
            imgAdd = (ImageView) llloc.findViewById(R.id.img_loc);
            imgAdd.setImageResource(R.drawable.icon_add);
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchFragment(tagListMaintenanceFragment, true);
                }
            });

            TextView txt = (TextView) llloc.findViewById(R.id.txt_location);
            txt.setVisibility(View.GONE);
        }


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fm = getFragmentManager();

        mOrder = new MaintenanceOrder();
        mOrder.setUserInfo(CoreManager.getManager().getCurrUser());

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_MAINTENANCE_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);

        DialogHelper.getHelper().init(this);

        registeReceiver();

        mainFragment = MaintenanceMainFragment.newInstance();
        tagListMaintenanceFragment = MaintenanceTagListServiceFragment.newInstance();
        subServiceFragment = MaintenanceSubServiceFragment.newInstance();
        goodsFragment = MaintenanceGoodsFragment.newInstance();

        switchFragment(mainFragment, false);

        List<CarInfo> carInfos = CoreManager.getManager().getUserCars();
        if (carInfos != null && carInfos.size() == 1) {
            mOrder.setmCarInfo(carInfos.get(0));
        } else {
            Intent intentCarInfo = new Intent(this, CarInfoActivity.class);
            startActivityForResult(intentCarInfo, ACTION_REQUEST_CAR);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_REQUEST_CAR && resultCode == RESULT_OK) {
            DialogHelper.getHelper().init(this);

            CarInfo carInfo = data.getParcelableExtra("car");
            mOrder.setmCarInfo(carInfo);
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

    private void switchFragment(BaseElementFragment fragment, boolean leftIn) {

        if (fragment == null) {
            return;
        }
        currFragment = fragment;
        FragmentTransaction transaction = fm.beginTransaction();
        if (leftIn) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.fl_maintenance_content, fragment);
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

            subServiceFragment.setService(service);
            switchFragment(subServiceFragment, true);
        }
    }

    // -------------------------------------------------------------------------------
    public MaintenanceTagListServiceFragment.MaintenanceTagActionListener getTagListActionListener() {
        return new TagListActionListener();
    }

    private class TagListActionListener implements MaintenanceTagListServiceFragment.MaintenanceTagActionListener {


        @Override
        public void requestService(String serviceId) {
            if (mBinder != null) {
                mBinder.getService(serviceId);
            }
        }

        @Override
        public void onServiceClicked(MaintenanceService service) {

            subServiceFragment.setService(service);
            switchFragment(subServiceFragment, true);
        }
    }

    //---------------------------------------------------------------------------------

    public MaintenanceSubServiceFragment.SubServiceActionListener getSubServiceListener() {
        return new SubServiceListener();
    }

    private class SubServiceListener implements MaintenanceSubServiceFragment.SubServiceActionListener {

        @Override
        public void onSubServiceClicked(MaintenanceService service, MaintenanceService subService, MaintenanceService feeService) {
            if (service == null) {
                return;
            }

            if (service == feeService) {
                return;
            }

            if (feeService == null) {
                // TODO
            }

            goodsFragment.setService(service);
            goodsFragment.setSubService(subService);
            goodsFragment.setFeeService(feeService);
            switchFragment(goodsFragment, true);
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

        @Override
        public void removeService(MaintenanceMainFragment fragment, TagableService service) {
            String serviceId = service.getService().getId();
            mOrder.removeService(serviceId);
        }

        @Override
        public void removeSubService(MaintenanceMainFragment fragment, TagableService service, TagableSubService subService) {

            String serviceId = service.getService().getId();
            mOrder.removeSubService(serviceId, subService);
        }

        @Override
        public void onCarClicked() {
            Intent intentCarInfo = new Intent(HomeMaintenanceActivity.this, CarInfoActivity.class);
            startActivityForResult(intentCarInfo, ACTION_REQUEST_CAR);

        }

        public void onOrderCommit() {

            if (mOrder.getmCarInfo() == null) {
                Toast.makeText(HomeMaintenanceActivity.this, getString(R.string.str_order_no_carinfo),Toast.LENGTH_LONG).show();
                return;
            }

            if (mOrder.isOrderEmpty()) {
                Toast.makeText(HomeMaintenanceActivity.this, getString(R.string.str_order_content_empty), Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(HomeMaintenanceActivity.this, OrderActivity.class);
            intent.putExtra("order", mOrder);
            intent.putExtra("subject", "汽车保养");
            startActivity(intent);
        }
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
        public void onGoodsCommit(MaintenanceService service, TagableSubService tagableSubService, TagableSubService feeService) {
            if (tagableSubService != null) {
                mOrder.addGoods(service.getId(), service, tagableSubService);
                mOrder.addGoods(service.getId(), service, feeService);
            }

            switchFragment(mainFragment, false);

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

    public void showAddIcon() {
        if (imgAdd != null) {
            imgAdd.setVisibility(View.VISIBLE);
        }
    }

    public void dismissAddIcon() {
        if (imgAdd != null) {
            imgAdd.setVisibility(View.GONE);
        }
    }
}
