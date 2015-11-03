package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.rrja.carja.R;
import com.rrja.carja.RRjaApplication;
import com.rrja.carja.adapter.CityAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Region;
import com.rrja.carja.utils.DialogHelper;

import java.util.List;

public class CityListActivity extends BaseActivity implements CityAdapter.OnCityItemClickListener, View.OnClickListener, RRjaApplication.OnLocationChangeListener {

    private static final String TAG = "rrja.CitylistAct";

    RecyclerView recyclerCity;
    CityAdapter adapter = new CityAdapter();

    private CityReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        if (llloc != null) {
//            llloc.setVisibility(View.GONE);
            txtLoc.setVisibility(View.GONE);
            imgLoc.setVisibility(View.VISIBLE);
            imgLoc.setOnClickListener(this);
        }

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CoreManager.getManager().getCostumerRegion() == null) {
                    CoreManager.getManager().setCustomeChange(false);
                } else {
                    CoreManager.getManager().setCustomeChange(true);
                }
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
        CoreManager.getManager().setCustomeChange(true);
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
        ((RRjaApplication)getApplication()).unRegistLocationChangeListener(TAG);

        findGpsRegion = false;

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == imgLoc.getId()) {

            if (CoreManager.getManager().getGpsRegion() != null) {
                Region gpsRegion = CoreManager.getManager().getGpsRegion();
                String cityName = gpsRegion.getName();
                Region regionByName = CoreManager.getManager().getRegionByName(cityName);
                if (regionByName != null) {

                    int cityIndex = adapter.getCityIndex(regionByName);
                    if (cityIndex != -1) {
                        adapter.setGpsIndex(cityIndex);
                        adapter.notifyItemChanged(cityIndex);
                    }
                    recyclerCity.scrollToPosition(cityIndex);
                }
            } else {
                Toast.makeText(this, "正在为您定位，请确保您的gps处于开启状态", Toast.LENGTH_LONG).show();
                findGpsRegion = false;
                ((RRjaApplication)getApplication()).registLocationChangeListener(TAG, this);
                // TODO show dialog
            }
        }
    }

    private boolean findGpsRegion = false;

    @Override
    public void onLocationChanged(BDLocation location) {
        if (!findGpsRegion) {

            findGpsRegion = true;

            String cityName = location.getCity();
            Region regionByName = CoreManager.getManager().getRegionByName(cityName);
            if (regionByName != null) {

                int cityIndex = adapter.getCityIndex(regionByName);
                if (cityIndex != -1) {
                    adapter.setGpsIndex(cityIndex);
                    adapter.notifyItemChanged(cityIndex);
                }
                recyclerCity.scrollToPosition(cityIndex);
            } else {
                findGpsRegion = false;
            }
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (CoreManager.getManager().getCostumerRegion() != null) {
                CoreManager.getManager().setCustomeChange(true);
            } else {
                CoreManager.getManager().setCustomeChange(false);
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
