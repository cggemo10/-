package com.rrja.carja.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.rrja.carja.R;
import com.rrja.carja.model.CouponGoods;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CouponsDetalActivity extends BaseActivity {

    CouponGoods coupons;
    ImageView imgCoupons;

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

        try {
            imgCoupons.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("juyouhui-img.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coupons_detal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
