package com.rrja.carja.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.car.AddCarFragment;
import com.rrja.carja.fragment.car.CarBrandFragment;
import com.rrja.carja.fragment.car.CarListFragment;
import com.rrja.carja.fragment.car.CarModelFragment;
import com.rrja.carja.fragment.car.CarNumberPrefixPickerFragment;
import com.rrja.carja.fragment.car.CarSeriesFragment;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.CarBinder;
import com.rrja.carja.utils.DialogHelper;

import java.util.List;

import static com.rrja.carja.fragment.car.CarModelFragment.*;

public class CarInfoActivity extends BaseActivity implements View.OnClickListener {

    private String prefix1 = "京";
    private String prefix2 = "A";

    private CarInfo carInfo;

    private FragmentManager fragmentManager;
    private CarListFragment listFragment;
    private AddCarFragment addCarFragment;
    private CarSeriesFragment seriesFragment;
    private CarBrandFragment brandFragment;
    private CarModelFragment modelFragment;

    private CarBinder carService;

    private AddCarReceiver mReceiver;

    private boolean isSelecte = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);

        DialogHelper.getHelper().init(this);

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

        fragmentManager = getFragmentManager();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("car_info")) {
            carInfo = extras.getParcelable("car_info");
        }
        if (carInfo == null) {
            carInfo = new CarInfo();
        }

        listFragment = CarListFragment.newInstance();

        switchFragment(listFragment);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fl_addcar_content, addCarFragment);
//        transaction.addToBackStack(null);
//        transaction.commitAllowingStateLoss();

        addCarFragment = AddCarFragment.newInstance();
        seriesFragment = CarSeriesFragment.newInstance();
        brandFragment = CarBrandFragment.newInstance();
        modelFragment = CarModelFragment.newInstance();

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_CAR_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);
        registeReceiver();
    }



    @Override
    protected void onDestroy() {
        unbindService(conn);
        unRegistReceiver();
        super.onDestroy();
    }

    private void registeReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_ADD_CAR);
            filter.addAction(Constant.ACTION_BROADCAST_ADD_CAR_ERR);

            mReceiver = new AddCarReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unRegistReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentManager.getBackStackEntryCount() <= 1) {
                finish();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private class AddCarInteraction implements AddCarFragment.OnAddCarFragmentInteractionListener {

        @Override
        public void onBrandClicked() {
            switchFragment(brandFragment);
        }

        @Override
        public void onPrefixClicked() {
            CarNumberPrefixPickerFragment fragment = CarNumberPrefixPickerFragment.newInstance(prefix1, prefix2);
            switchFragment(fragment);
        }

        @Override
        public void onCommit() {
            UserInfo info = CoreManager.getManager().getCurrUser();
            if (info == null) {
                Intent intent = new Intent(CarInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }

            carService.addCarForUser(CoreManager.getManager().getCurrUser(), carInfo);
            DialogHelper.getHelper().showWaitting();
        }
    }

    public CarInfo getCarInfo() {
        return carInfo;
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

    private ServiceConnection conn  = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            carService = (CarBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            carService = null;
        }
    };

    //----------------------------------------------------------------------------------------------
    private class PrefixInteraction implements CarNumberPrefixPickerFragment.OnPrefixFragmentInteractionListener {

        @Override
        public void onPrefix1Changed(String prefix1) {
            if (!TextUtils.isEmpty(prefix1) && prefix1.contains("(")) {
                prefix1 = prefix1.substring(0, prefix1.indexOf("("));
                CarInfoActivity.this.prefix1 = prefix1;
                addCarFragment.setPrefix(CarInfoActivity.this.prefix1 + CarInfoActivity.this.prefix2);
            }
        }

        @Override
        public void onPrefix2Changed(String prefix2) {
            CarInfoActivity.this.prefix2 = prefix2;
            addCarFragment.setPrefix(CarInfoActivity.this.prefix1 + CarInfoActivity.this.prefix2);
        }
    }

    public PrefixInteraction getPrefixFragmentInteraction() {
        return new PrefixInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class SeriesInteraction implements CarSeriesFragment.OnSeriesFragmentInteractionListener {

        @Override
        public void onSeriesSelected(CarSeries series) {

            carInfo.setSeriesName(series.getSeriesName());
            carInfo.setSeriesId(series.getId());

            List<CarModel> modelList = CoreManager.getManager().getCarModelsBySeriesId(series.getId());
            modelFragment.setSeriesId(series.getId());
            modelFragment.setModelData(modelList);
            switchFragment(modelFragment);
        }

        @Override
        public void onRequestSeriesData(String brandId) {
            carService.getSeriesByBrandId(brandId);
        }
    }

    public SeriesInteraction getSeriesInteraction() {
        return new SeriesInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class BrandInteraction implements CarBrandFragment.OnBrandFragmentInteractionListener {

        @Override
        public void onBrandSelected(CarBrand brand) {

            carInfo.setCarBrandId(brand.getId());
            carInfo.setCarBrandName(brand.getName());

            List<CarSeries> seriesList = CoreManager.getManager().getCarSeriesByBrandId(brand.getId());
            seriesFragment.setSeriesData(seriesList);
            seriesFragment.setBrandId(brand.getId());
            switchFragment(seriesFragment);
        }
    }

    public BrandInteraction getBrandInteraction() {
        return new BrandInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class ModelInteraction implements OnModelFragmentInteractionListener {

        @Override
        public void onModelSelected(CarModel model) {

            carInfo.setModelName(model.getTypeName());
            carInfo.setModelId(model.getId());

            for(int i = 0; i < fragmentManager.getBackStackEntryCount() - 1; ++i) {
                fragmentManager.popBackStack();
            }
//            switchFragment(addCarFragment);


//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fl_addcar_content,addCarFragment);
//            fragmentManager.popBackStackImmediate(CarModelFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            fragmentTransaction.commitAllowingStateLoss();


        }

        @Override
        public void onModelDataRequest(String seriesID) {
            carService.getModelBySeriesId(seriesID);
        }
    }

    public ModelInteraction getModelInteraction() {
        return new ModelInteraction();
    }

    private class AddCarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Constant.ACTION_BROADCAST_ADD_CAR_ERR.equals(action)) {
                String errmsg = intent.getStringExtra("description");
                Toast.makeText(context, errmsg, Toast.LENGTH_LONG).show();
                return;
            }
            if (Constant.ACTION_BROADCAST_ADD_CAR.equals(action)) {
                Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
                DialogHelper.getHelper().showWaitting();
                Intent refreshIntent = new Intent(context, DataCenterService.class);
                refreshIntent.setAction(Constant.ACTION_REQUEST_REFRESH_USER_CAR);
                startService(refreshIntent);
            }

            if (Constant.ACTION_BROADCAST_GET_USER_CARS.equals(action)) {

            }

            if (Constant.ACTION_BROADCAST_GET_USER_CARS_ERR.equals(action)) {
                switchFragment(listFragment);
            }

        }
    }

    public CarListFragment.CarListInteractionListener getCarListListener() {
        return new CarListListener();
    }

    private class CarListListener implements CarListFragment.CarListInteractionListener {

        @Override
        public void onCarSelected(CarInfo car) {

            if (isSelecte) {
                Intent result = new Intent();
                result.putExtra("car", car);
                setResult(RESULT_OK, result);

                finish();
            } else {
                // TODO
            }
        }

        @Override
        public void requestAddCar() {
            switchFragment(addCarFragment);
        }
    }
}
