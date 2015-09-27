package com.rrja.carja.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;

public class OnDoreWashActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_SELECT_CAR = 10;

    AppCompatButton btnAddCar;
    MaintenanceOrder order;

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

        btnAddCar = (AppCompatButton) findViewById(R.id.btn_add_car);
        btnAddCar.setOnClickListener(this);

        order = new MaintenanceOrder();
        order.setUserInfo(CoreManager.getManager().getCurrUser());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CarManagerActivity.class);
        intent.putExtra("select", true);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_CAR) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("car")) {
                    CarInfo carInfo = data.getParcelableExtra("car");
                    MaintenanceOrder order = new MaintenanceOrder();
                    order.setmCarInfo(carInfo);
                    order.setUserInfo(CoreManager.getManager().getCurrUser());
//                    order.addGoods();
                } else {
                    Toast.makeText(this, "获取车辆信息失败，请稍后再试。", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "上门洗车已取消。", Toast.LENGTH_LONG).show();
            }
        }

    }
}
