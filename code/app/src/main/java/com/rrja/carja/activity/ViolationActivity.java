package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.model.CarInfo;

import static com.rrja.carja.R.id.recycler_violation;

public class ViolationActivity extends BaseActivity implements View.OnClickListener{

    View carView;
    ImageView imgLogo;
    TextView txtPlat;
    TextView txtCarDetal;

    RecyclerView recyclerViolate;

    private CarInfo carInfo;
    private ViolateAdapter adapter;
    private ViolateReceiver receiver;

    /*
    {"code":0,"data":[
    {"act":"未按尾号限制通行的",
    "area":"【北京】北京市海淀区学清路北口 北向南",
    "code":"70064","date":"2015-05-18 10:05:00",
    "fen":"0","handled":"0","money":"100"},
    {"act":"机动车违反规定停车","area":"【北京】北京市海淀区北洼路七贤村路西口",
    "code":"10391","date":"2014-08-11 10:04:00",
    "fen":"0","handled":"0","money":"200"}
    ],"description":"OK","total":"02"}
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation);

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

        initView();
    }

    private void initView() {
        carView = findViewById(R.id.view_car);
        carView.setOnClickListener(this);
        imgLogo = (ImageView) findViewById(R.id.img_car_ic);
        txtPlat = (TextView) findViewById(R.id.txt_car_platnm);
        txtCarDetal = (TextView) findViewById(R.id.txt_car_detial);

        recyclerViolate = (RecyclerView) findViewById(recycler_violation);
        recyclerViolate.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ViolateAdapter();
        recyclerViolate.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        loadCarInfo();
        super.onStart();
        registReceiver();
    }

    @Override
    protected void onStop() {
        unregistReceiver();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO
    }

    private void registReceiver() {
        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            // TODO

            receiver = new ViolateReceiver();
            registerReceiver(receiver, filter);
        }
    }

    private void unregistReceiver() {
        if(receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void loadCarInfo() {
        if (carInfo == null) {
            imgLogo.setVisibility(View.GONE);
            txtPlat.setText("");
            txtCarDetal.setText("点击选择车辆");
        } else {
            if (!TextUtils.isEmpty(carInfo.getPlatNum())) {
                txtPlat.setText(carInfo.getPlatNum());
            } else {
                // TODO
            }

            if (TextUtils.isEmpty(carInfo.getEngineNo())) {
                // TODO
            }

            if (TextUtils.isEmpty(carInfo.getFrameNo6())) {
                // TODO
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view_car) {
            // TODO
        }
    }

    private class ViolateAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class ViolateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
        }
    }



}
