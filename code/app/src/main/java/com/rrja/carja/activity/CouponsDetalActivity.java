package com.rrja.carja.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.FileService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class CouponsDetalActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "rrja.CouponsDetal";

    CouponGoods coupons;
    private ImageView imgCoupons;
    private TextView txtScope;
    private TextView txtTime;
    private TextView txtContent;
    private TextView txtTel;
    private TextView txtDetal;

    private AppCompatButton btnGainCoupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_detal);

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
        if (extras.containsKey("coupons")) {
            coupons = extras.getParcelable("coupons");
        } else {
            finish();
        }

        setTitle(coupons.getName());

        initView();
    }

    private void initView() {
        // TODO

        imgCoupons = (ImageView) findViewById(R.id.img_coupons);

        String picUrl = coupons.getPicUrl();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getCouponsCacheDir(), fileName);
            if (img.exists()) {
                try {
                    imgCoupons.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {
                Intent intent = new Intent(this, FileService.class);
                intent.setAction(FileService.ACTION_IMG_COUPONS);
                intent.putExtra("coupons_goods", coupons);
                startService(intent);
            }

        }

        txtScope = (TextView) findViewById(R.id.txt_item_coupons_scope_content);
        txtScope.setText(coupons.getScope());

        long startTime = coupons.getStartTime();
        long endTime = coupons.getEndTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String time = format.format(startTime) + " 至\n " + format.format(endTime);
        txtTime = (TextView) findViewById(R.id.txt_item_coupons_time_content);
        txtTime.setText(time);

        txtContent = (TextView) findViewById(R.id.txt_item_coupons_content_content);
        txtContent.setText(coupons.getContent());

        txtTel = (TextView) findViewById(R.id.txt_item_coupons_tel_content);
        txtTel.setText(coupons.getTelNumber());

        txtDetal = (TextView) findViewById(R.id.txt_coupons_content);
        txtDetal.setText(coupons.getDetal());

        btnGainCoupons = (AppCompatButton) findViewById(R.id.btn_gain_coupons);
        btnGainCoupons.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (CoreManager.getManager().getCurrUser() != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            btnGainCoupons.setEnabled(false);
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_GAIN_COUPONS);
            intent.putExtra("coupons", coupons);
            startService(intent);
        }

    }
}
