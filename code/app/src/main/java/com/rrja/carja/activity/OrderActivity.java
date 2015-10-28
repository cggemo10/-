package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.order.OrderConformFragment;
import com.rrja.carja.fragment.order.OrderPayFragment;
import com.rrja.carja.model.PayInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.OrderBinder;

public class OrderActivity extends BaseActivity {


    private OrderBinder orderService;

    private MaintenanceOrder order;

    private String orderSubject;


    private PayInfo payInfo;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private OrderConformFragment conformFragment;
    private OrderPayFragment payFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("order") && extras.containsKey("subject")) {
            try {
                order = extras.getParcelable("order");
                orderSubject = extras.getString("subject");
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                finish();
            }

        } else if (extras.containsKey("payInfo") && extras.get("payInfo") != null) {
            // from orderlist
            setResult(RESULT_CANCELED);
            payInfo = getIntent().getParcelableExtra("payInfo");
            if (payInfo == null) {
                finish();
            }
        } else {
            finish();
        }

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_ORDER_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);

        fragmentManager = getFragmentManager();
        conformFragment = OrderConformFragment.newInstance();
        payFragment = OrderPayFragment.newInstance();

        switchFragment(conformFragment, false);


    }

    @Override
    protected void onDestroy() {
        if (orderService != null) {
            unbindService(conn);
        }
        super.onDestroy();
    }


    boolean firstStart = false;

    @Override
    protected void onResume() {
        super.onResume();
    }


    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            orderService = (OrderBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            orderService = null;
        }
    };


    public MaintenanceOrder getMaintenanceOrder() {
        return order;
    }

    public PayInfo getpayInfo() {
        return payInfo;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    private void switchFragment(Fragment fragmet, Boolean inFromLeft) {

        this.currentFragment = fragmet;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_order_content, fragmet);
        if (inFromLeft != null && inFromLeft) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (inFromLeft != null) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    // ---------------------------------------------------------------------------------------------
    public OrderConformListener getOrderConformListener() {
        return new OrderConformListener();
    }

    private class OrderConformListener implements OrderConformFragment.OnOrderConformInteractionListener {

        @Override
        public void onOrderConform(MaintenanceOrder order, Bundle data) {
            orderService.commitOrder(order, data);
        }

        @Override
        public void onPay(PayInfo payInfo) {
            OrderActivity.this.payInfo = payInfo;
            switchFragment(payFragment, false);
        }
    }

    public OrderPayListener getOrderPayListener() {
        return new OrderPayListener();
    }

    private class OrderPayListener implements OrderPayFragment.OnOrderPayInteractionListener {

        @Override
        public void syncPayment(String payStatus, String orderNum) {
            orderService.syncOrder(orderNum, payStatus);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (currentFragment == conformFragment) {
                finish();
                return true;
            } else if (currentFragment == payFragment) {
                switchFragment(conformFragment, true);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
