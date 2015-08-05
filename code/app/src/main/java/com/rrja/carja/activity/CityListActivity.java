package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.adapter.CityAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Region;
import com.rrja.carja.utils.DialogHelper;

import java.util.List;

public class CityListActivity extends BaseActivity implements CityAdapter.OnCityItemClickListener {

    RecyclerView recyclerCity;
    CityAdapter adapter = new CityAdapter();

    private CityReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

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

        recyclerCity = (RecyclerView) findViewById(R.id.recycler_city);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerCity.setLayoutManager(manager);
        adapter.setOnitemClickListener(this);
        recyclerCity.setAdapter(adapter);

        DialogHelper.getHelper().init(this);
    }

    @Override
    public void onCityClicked(Region region) {

        CoreManager.getManager().setCostumerRegion(region);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registCityReceiver();
        if (CoreManager.getManager().getRegions().size() == 0) {
            DialogHelper.getHelper().showWaitting();
        } else {
            adapter.loadData(CoreManager.getManager().getRegions());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onStop() {
        unregistCityReceiver();
        super.onStop();
    }

    private void registCityReceiver() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_REFRESH_REGION);
            filter.addAction(Constant.ACTION_BROADCAST_REFRESH_REGION_ERROR);

            mReceiver = new CityReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unregistCityReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private class CityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_REFRESH_REGION.equals(action)) {
                List<Region> regions = CoreManager.getManager().getRegions();
                adapter.loadData(regions);
                adapter.notifyDataSetChanged();
                DialogHelper.getHelper().dismissWatting();
            }

            if (Constant.ACTION_BROADCAST_REFRESH_REGION_ERROR.equals(action)) {
                List<Region> regions = CoreManager.getManager().getRegions();
                DialogHelper.getHelper().dismissWatting();
                if (regions == null || regions.size() == 0)
                    Toast.makeText(context, "获取城市列表失败，请检查您的网络。", Toast.LENGTH_LONG).show();
            }
        }
    }
}
