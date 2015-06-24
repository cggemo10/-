package com.jayangche.android.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.model.DiscountInfoToShow;

public class DiscountActivity extends BaseActivity {

    DiscountInfoToShow currDiscount;

    ImageView imgDiscount;
    TextView discountTitle;
    TextView discountScop;
    TextView discountTime;
    TextView discountNo;
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
        llloc.setVisibility(View.GONE);
    }

}
