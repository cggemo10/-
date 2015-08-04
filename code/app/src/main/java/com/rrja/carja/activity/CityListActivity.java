package com.rrja.carja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rrja.carja.R;
import com.rrja.carja.adapter.CityAdapter;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Region;

public class CityListActivity extends BaseActivity implements CityAdapter.OnCityItemClickListener {

    RecyclerView recyclerCity;
    CityAdapter adapter = new CityAdapter();

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
        adapter.loadData(CoreManager.getManager().getRegions());
        recyclerCity.setAdapter(adapter);

    }


    @Override
    public void onCityClicked(Region region) {

        CoreManager.getManager().setCostumerRegion(region);
        setResult(RESULT_OK);
        finish();
    }
}
