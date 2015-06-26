package com.jayangche.android.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.model.DiscountInfoToShow;

import java.io.IOException;

public class DiscountActivity extends BaseActivity {

    DiscountInfoToShow currDiscount;

    ImageView imgDiscount;
    TextView discountScop;
    TextView discountTime;
    TextView discountContent;
    TextView discountTel;
    TextView discountDetal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("discount_info")) {
            currDiscount = extras.getParcelable("discount_info");

        }

        if(currDiscount == null) {
            finish();
        }

        initView();
    }

    private void initView() {
        setTitle(currDiscount.getName());
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

        imgDiscount = (ImageView) findViewById(R.id.img_discount);
        try {
            imgDiscount.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("juyouhui-img.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO if show title

        discountScop = (TextView) findViewById(R.id.txt_item_discount_scope_content);
        discountTime = (TextView) findViewById(R.id.txt_item_discount_time_content);
        discountContent = (TextView) findViewById(R.id.txt_item_discount_content_content);
        discountTel = (TextView) findViewById(R.id.txt_item_discount_mobile_content);
        discountDetal = (TextView) findViewById(R.id.txt_item_discount_detial_content);

    }

}
