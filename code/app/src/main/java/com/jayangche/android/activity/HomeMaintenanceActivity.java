package com.jayangche.android.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;

public class HomeMaintenanceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }

//        toolbar.setNavigationIcon(R.drawable.);
//        findViewById(R.id.img_discount)
    }

}
