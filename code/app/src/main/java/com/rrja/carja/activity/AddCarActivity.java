package com.rrja.carja.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.fragment.car.AddCarFragment;
import com.rrja.carja.fragment.car.CarBrandFragment;
import com.rrja.carja.fragment.car.CarModelFragment;
import com.rrja.carja.fragment.car.CarNumberPrefixPickerFragment;
import com.rrja.carja.fragment.car.CarSeriesFragment;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;

public class AddCarActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private String prefix1 = "京";
    private String prefix2 = "A";

    private CarInfo carInfo;

    private FragmentManager fragmentManager;

    private AddCarFragment addCarFragment;
    private CarSeriesFragment seriesFragment;
    private CarBrandFragment brandFragment;
    private CarModelFragment modelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fragmentManager = getSupportFragmentManager();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("car_info")) {
            carInfo = extras.getParcelable("car_info");
        }
        if (carInfo == null) {
            carInfo = new CarInfo();
        }

        addCarFragment = AddCarFragment.newInstance();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_addcar_content, addCarFragment);
        transaction.commitAllowingStateLoss();

        seriesFragment = CarSeriesFragment.newInstance();

        brandFragment = CarBrandFragment.newInstance();

        modelFragment = CarModelFragment.newInstance();
    }

    private void switchFragment(Fragment addCarFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_addcar_content, addCarFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentManager.getBackStackEntryCount() <= 1) {
                finish();
                return true;
            }
        }

        return false;
    }

    private class AddCarInteraction implements AddCarFragment.OnAddCarFragmentInteractionListener {

        @Override
        public void onSeriesClicked() {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            CarSeriesFragment fragment = CarSeriesFragment.newInstance();
            switchFragment(fragment);
        }

        @Override
        public void onPrefixClicked() {
            CarNumberPrefixPickerFragment fragment = CarNumberPrefixPickerFragment.newInstance(prefix1, prefix2);
            switchFragment(fragment);
        }

        @Override
        public void onCommit() {

        }
    }

    public String getPrefix1() {
        return prefix1;
    }


    public String getPrefix2() {
        return prefix2;
    }


    public AddCarInteraction getAddCarInteraction() {
        return new AddCarInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class PrefixInteraction implements CarNumberPrefixPickerFragment.OnPrefixFragmentInteractionListener {

        @Override
        public void onPrefix1Changed(String prefix1) {
            if (!TextUtils.isEmpty(prefix1) && prefix1.contains("(")) {
                prefix1 = prefix1.substring(0, prefix1.indexOf("("));
                AddCarActivity.this.prefix1 = prefix1;
                addCarFragment.setPrefix(AddCarActivity.this.prefix1 + AddCarActivity.this.prefix2);
            }
        }

        @Override
        public void onPrefix2Changed(String prefix2) {
            AddCarActivity.this.prefix2 = prefix2;
            addCarFragment.setPrefix(AddCarActivity.this.prefix1 + AddCarActivity.this.prefix2);
        }
    }

    public PrefixInteraction getPrefixFragmentInteraction() {
        return new PrefixInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class SeriesInteraction implements CarSeriesFragment.OnSeriesFragmentInteractionListener {

        @Override
        public void onSeriesSelected(CarSeries series) {

        }
    }

    public SeriesInteraction getSeriesInteraction() {
        return new SeriesInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class BrandInteraction implements CarBrandFragment.OnBrandFragmentInteractionListener {

        @Override
        public void onBrandSelected(CarBrand brand) {

        }
    }

    public BrandInteraction getBrandInteraction() {
        return new BrandInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class ModelInteraction implements CarModelFragment.OnModelFragmentInteractionListener {

        @Override
        public void onModelSelected(CarModel model) {
            if (model != null) {
            }
        }
    }

    public ModelInteraction getModelInteraction() {
        return new ModelInteraction();
    }
}
