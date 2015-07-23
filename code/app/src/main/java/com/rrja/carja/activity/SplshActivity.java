package com.rrja.carja.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.service.DataCenterService;

public class SplshActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CoreManager.getManager().init(this);

        Intent service = new Intent(this, DataCenterService.class);
        service.setAction(Constant.ACTION_INIT_SERVICE);
        startService(service);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splsh);
    }

}
