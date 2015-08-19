package com.rrja.carja.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rrja.carja.R;

public class HomeMaintenanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppCompatButton btnAdd;
    private AppCompatButton btnCommitOrder;

    Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_home_maintenance);
        
    }

}
