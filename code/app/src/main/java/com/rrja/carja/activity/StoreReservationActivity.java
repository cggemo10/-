package com.rrja.carja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rrja.carja.R;
import com.rrja.carja.adapter.StoreReservationAdapter;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.FileService;

public class StoreReservationActivity extends BaseActivity implements StoreReservationAdapter.StoreActionListener{

//    ListView listViewStore;
    private RecyclerView recyclerView;
    private StoreReservationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_maintenance);

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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_store_reservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StoreReservationAdapter();
        mAdapter.setActionListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRequestStorePic(CarStore carStore) {
        Intent intent = new Intent(this, FileService.class);
        intent.setAction(FileService.ACTION_IMG_STORE);
        intent.putExtra("car_store", carStore);
        startService(intent);
    }


}
