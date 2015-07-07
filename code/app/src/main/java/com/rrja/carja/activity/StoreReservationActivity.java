package com.rrja.carja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rrja.carja.R;
import com.rrja.carja.adapter.StoreReservationAdapter;
import com.rrja.carja.core.CoreManager;

public class StoreReservationActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    ListView listViewStore;

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

        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_stroe_reservation);
        listViewStore = pullToRefreshListView.getRefreshableView();
        listViewStore.setOnItemClickListener(this);
        listViewStore.setAdapter(new StoreReservationAdapter(this));

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, StoreReservationDetalActivity.class);
        intent.putExtra("curr_store", CoreManager.getManager().getStores().get(position));
        startActivity(intent);
    }
}
