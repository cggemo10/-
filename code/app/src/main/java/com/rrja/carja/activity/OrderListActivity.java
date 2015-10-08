package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.fragment.orderlist.OrderDetalFragment;
import com.rrja.carja.fragment.orderlist.OrderListFragment;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.OrderBinder;
import com.rrja.carja.utils.DialogHelper;

public class OrderListActivity extends BaseActivity {

    Fragment currFragment;
    private FragmentManager fm;

    OrderListFragment listFragment;
    OrderDetalFragment detalFragment;

    private OrderBinder orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

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

        fm = getFragmentManager();
        DialogHelper.getHelper().init(this);

        bindService();

        listFragment = OrderListFragment.newInstance();
        detalFragment = OrderDetalFragment.newInstance();
        switchFragment(listFragment, false);

    }

    @Override
    protected void onDestroy() {
        unbindService();
        super.onDestroy();
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
        transaction.replace(R.id.fl_my_order_content, fragment);
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

    private void bindService() {
        if (orderService == null) {
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_ORDER_SERVICE);
            bindService(intent, conn, BIND_AUTO_CREATE);
        }
    }

    private void unbindService() {
        if (orderService != null) {
            unbindService(conn);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    public OrderListFragment.OnOrderListListener getOrderListListener() {
        return new OrderListListener();
    }

    private class OrderListListener implements OrderListFragment.OnOrderListListener{

        @Override
        public void onOrderClicked(MaintenanceOrder order) {

        }

        @Override
        public void onOrderDataRequest(String type) {
            if (orderService != null) {
                orderService.getMyOrderList(CoreManager.getManager().getCurrUser(), type);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            orderService = (OrderBinder) service;

//            if (CoreManager.getManager().getMyOrders("11").size() == 0) {
//                orderService.getMyOrderList(CoreManager.getManager().getCurrUser(),"11");
//            }
            if (CoreManager.getManager().getMyOrders("22").size() == 0) {
                orderService.getMyOrderList(CoreManager.getManager().getCurrUser(),"22");
            }
//            if (CoreManager.getManager().getMyOrders("33").size() == 0) {
//                orderService.getMyOrderList(CoreManager.getManager().getCurrUser(),"33");
//            }
//            if (CoreManager.getManager().getMyOrders("44").size() == 0) {
//                orderService.getMyOrderList(CoreManager.getManager().getCurrUser(),"44");
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            orderService = null;
        }
    };


}
