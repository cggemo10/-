package com.rrja.carja.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableService;
import com.rrja.carja.model.maintenance.TagableSubService;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.FileService;
import com.rrja.carja.service.impl.MaintenanceBinder;
import com.rrja.carja.utils.DialogHelper;

import java.io.File;
import java.util.List;

public class OnDoreWashActivity extends BaseActivity implements View.OnClickListener, Handler.Callback{

    private static final int REQUEST_SELECT_CAR = 10;

    AppCompatButton btnCommit;
    MaintenanceOrder order;

    View carChoise;
    ImageView carLogo;
    TextView txtPlatNum;
    TextView tvCarDetal;

    MaintenanceBinder orderService;
    OrderReceiver orderReceiver;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_dore_wash);

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

        btnCommit = (AppCompatButton) findViewById(R.id.btn_on_dore_wash_commit);
        btnCommit.setOnClickListener(this);

        carChoise = findViewById(R.id.v_chose_car);
        carChoise.setOnClickListener(this);

        carLogo = (ImageView) findViewById(R.id.img_car_ic);
        txtPlatNum = (TextView) findViewById(R.id.txt_car_platnm);
        tvCarDetal = (TextView) findViewById(R.id.txt_car_detial);

        order = new MaintenanceOrder();
        order.setUserInfo(CoreManager.getManager().getCurrUser());

        mHandler = new Handler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService();
        registReceiver();

        if (order.getmCarInfo() == null) {
            tvCarDetal.setText("请选择您的车辆");
            txtPlatNum.setText("");
            carLogo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegistReceiver();
        unBindService();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.v_chose_car) {
            Intent intent = new Intent(this, CarManagerActivity.class);
            intent.putExtra("select", true);
            startActivityForResult(intent, REQUEST_SELECT_CAR);
        }

        if (id == R.id.btn_on_dore_wash_commit) {
            btnCommit.setEnabled(false);
            DialogHelper.getHelper().showWaitting();
            mHandler.sendEmptyMessage(1);

        }
    }

    private void checkOrder() {
        if (CoreManager.getManager().getMaintenanceService("104").size() != 0) {
            List<MaintenanceService> services = CoreManager.getManager().getMaintenanceService("104");
            MaintenanceService service = services.get(0);
            if (CoreManager.getManager().getMaintenanceService(service.getId()).size() != 0) {
                List<MaintenanceService> subServices = CoreManager.getManager().getMaintenanceService(service.getId());
                MaintenanceService subService = subServices.get(0);
                TagableSubService tagableSubService = new TagableSubService();
                tagableSubService.setSubService(subService);

                MaintenanceGoods goods = new MaintenanceGoods();
                goods.setId("1000026");
                goods.setPrice(0);

                tagableSubService.setGoods(goods);
                // TODO check coupons

                TagableService tagableService = new TagableService();
                tagableService.setService(service);

                order.addGoods(service.getId(), service, tagableSubService);

                DialogHelper.getHelper().dismissWatting();

                Intent intent = new Intent(this,OrderActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("subject", "上门洗车");
                startActivity(intent);

                finish();
            } else {
                orderService.getService(service.getId());
            }
        } else {
            orderService.getService("104");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_CAR) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("car")) {
                    CarInfo carInfo = data.getParcelableExtra("car");
                    order.setmCarInfo(carInfo);
                    order.setUserInfo(CoreManager.getManager().getCurrUser());

                    reloadCarInfo();
                } else {
                    Toast.makeText(this, "获取车辆信息失败，请稍后再试。", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "上门洗车已取消。", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void reloadCarInfo() {

        if (order.getmCarInfo() != null) {
            CarInfo carInfo = order.getmCarInfo();
            txtPlatNum.setText(carInfo.getPlatNum());
            tvCarDetal.setText(carInfo.getSeriesName() + " " + carInfo.getModelName());
            carLogo.setVisibility(View.VISIBLE);

            String picUrl = carInfo.getCarImg();
            try {
                if (!TextUtils.isEmpty(picUrl)) {

                    String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

                    File img = new File(Constant.getCarImageCacheDir(), fileName);
                    if (img.exists()) {
                        carLogo.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                    } else {
                        Intent intent = new Intent(this, FileService.class);
                        intent.setAction(FileService.ACTION_IMG_CAR_LOGO);
                        intent.putExtra("car", carInfo);
                        startService(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvCarDetal.setText("请选择您的车辆");
            txtPlatNum.setText("");
            carLogo.setVisibility(View.GONE);
        }
    }

    private void bindService() {

        if (orderService == null) {
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_ORDER_SERVICE);
            bindService(intent, conn, BIND_AUTO_CREATE);
        }
    }

    private void unBindService() {
        if (orderService != null) {
            unbindService(conn);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            orderService = (MaintenanceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            orderService = null;
        }
    };

    private void registReceiver() {

        if (orderReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_CARLOGO);

            orderReceiver = new OrderReceiver();
            registerReceiver(orderReceiver, filter);
        }
    }

    private void unRegistReceiver() {

        if (orderReceiver != null) {
            unregisterReceiver(orderReceiver);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        checkOrder();
        return false;
    }

    private class OrderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_CARLOGO.equals(action)) {
                reloadCarInfo();
            }

            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA.equals(action)) {
                mHandler.sendEmptyMessage(1);
            }

            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR.equals(action)) {
                Toast.makeText(context, "获取订单失败，请稍后再试！", Toast.LENGTH_LONG).show();
                DialogHelper.getHelper().dismissWatting();
                btnCommit.setEnabled(true);
            }
        }
    }
}
