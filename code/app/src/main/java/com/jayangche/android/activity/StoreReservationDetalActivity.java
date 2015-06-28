package com.jayangche.android.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jayangche.android.R;
import com.jayangche.android.model.CarStore;

import java.io.IOException;

/**
 * Created by chongge on 15/6/28.
 */
public class StoreReservationDetalActivity extends BaseActivity implements View.OnClickListener{

    CarStore store;

    private TextView txtStoreName;
    private ImageView imgStoreImg;
    private TextView txtAddress;
    private TextView txtStoreArea;
    private TextView txtStoreTel;
    private TextView txtStoreOpenTime;
    private TextView txtStorePayType;
    private TextView txtStoreService;
    private Button btnCommit;

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
        }

        if (store == null) {
            finish();
        }

        initView();

    }

    private void initView() {
        txtStoreName = (TextView) findViewById(R.id.txt_store_name_detal);
        imgStoreImg = (ImageView) findViewById(R.id.img_store_pic_detal);
        txtAddress = (TextView) findViewById(R.id.txt_store_address_detal_content);
        txtStoreArea = (TextView) findViewById(R.id.txt_store_area_detal);
        txtStoreTel = (TextView) findViewById(R.id.txt_store_tel_detal);
        txtStoreOpenTime = (TextView) findViewById(R.id.txt_store_opentime_detal_content);
        txtStorePayType = (TextView) findViewById(R.id.txt_store_paytype_content);
        txtStoreService = (TextView) findViewById(R.id.txt_store_service_content);
        btnCommit = (Button) findViewById(R.id.btn_commit_store_reservation);

        txtStoreName.setText(store.getStoreName());

        // TODO demo
        try {
            imgStoreImg.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("mendian.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtAddress.setText(store.getAddress());
        txtStoreArea.setText(store.getArea());
        txtStoreTel.setText(store.getTel());
        txtStoreOpenTime.setText(store.getOpenTime());
        txtStorePayType.setText(store.getPayType());
        txtStoreService.setText(store.getDesc());


        btnCommit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "已确认预约，您可以在用户中心中查看",Toast.LENGTH_LONG).show();
    }
}
