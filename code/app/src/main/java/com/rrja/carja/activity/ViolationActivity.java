package com.rrja.carja.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.ViolationRecord;
import com.rrja.carja.service.DataCenterService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rrja.carja.R.id.recycler_violation;
import static com.rrja.carja.R.id.txt_loc;

public class ViolationActivity extends BaseActivity implements View.OnClickListener{

    public static final int REQUEST_SELECT_CAR = 11;

    View carView;
    ImageView imgLogo;
    TextView txtPlat;
    TextView txtCarDetal;

    RecyclerView recyclerViolate;

    List<ViolationRecord> recordList = new ArrayList<>();

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

        if (requestCode == REQUEST_SELECT_CAR) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("car")) {
                    carInfo = data.getParcelableExtra("car");
                    loadCarInfo();
                } else {
                    Toast.makeText(this, "获取车辆信息失败，请稍后再试。", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "上门洗车已取消。", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registReceiver() {
        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_VIOLATION);
            filter.addAction(Constant.ACTION_BROADCAST_VIOLATION_ERR);

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
            Intent intent = new Intent(this, CarManagerActivity.class);
            intent.putExtra("select", true);
            startActivityForResult(intent, REQUEST_SELECT_CAR);
        }

        if (id == R.id.btn_commit_violation) {
            if (carInfo == null) {
                Toast.makeText(this, "请选择您的车辆！", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_REQUEST_VIOLATION);
            intent.putExtra("carId", carInfo.getId());
        }
    }

    private class ViolateAdapter extends RecyclerView.Adapter<ViolateHolder> {

        @Override
        public ViolateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViolateHolder holder, int position) {
            ViolationRecord record = recordList.get(position);
            holder.bindData(record);
        }

        @Override
        public int getItemCount() {
            return recordList.size();
        }
    }

    private class ViolateHolder extends RecyclerView.ViewHolder {

        private TextView txtPlat;
        private TextView txtLoc;
        private TextView txtAct;
        private TextView txtArea;
        private TextView txtTime;
        private TextView txtFen;
        private TextView txtMoney;

        public ViolateHolder(View itemView) {
            super(itemView);

            txtPlat = (TextView) itemView.findViewById(R.id.txt_violation_platnm);
            txtLoc = (TextView) itemView.findViewById(R.id.txt_violation_region);
            txtAct = (TextView) itemView.findViewById(R.id.txt_action_content);
            txtArea = (TextView) itemView.findViewById(R.id.txt_location_content);
            txtTime = (TextView) itemView.findViewById(R.id.txt_time_content);
            txtFen = (TextView) itemView.findViewById(R.id.txt_fen_content);
            txtMoney = (TextView) itemView.findViewById(R.id.txt_money_content);
        }

        public void bindData(ViolationRecord record) {
            txtPlat.setText(carInfo.getPlatNum());
//            txtPlat.setText();
            txtAct.setText(record.getAct());
            txtArea.setText(record.getArea());
            txtTime.setText(record.getDate());
            txtFen.setText(record.getFen());
            txtMoney.setText(record.getMoney());
        }
    }

    private class ViolateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_VIOLATION.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras.containsKey("violation_record")) {
                    recordList.clear();
                    ViolationRecord[] violation_records = (ViolationRecord[]) extras.getParcelableArray("violation_record");
                    Collections.addAll(recordList, violation_records);
                    adapter.notifyDataSetChanged();
                }
            }
            if (Constant.ACTION_BROADCAST_VIOLATION_ERR.equals(action)) {
                recordList.clear();
                adapter.notifyDataSetChanged();
            }

        }
    }



}
