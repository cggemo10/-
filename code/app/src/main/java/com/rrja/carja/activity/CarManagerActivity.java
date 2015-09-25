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
import android.os.IBinder;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.CarBinder;
import com.rrja.carja.utils.DialogHelper;

import java.util.List;

public class CarManagerActivity extends BaseActivity {

    private static final int REQUEST_CODE_LOGIN = 100;

    private CarBinder carService;

    private AddCarReceiver mReceiver;

    private boolean isSelecte = false;

    private FragmentManager fragmentManager;

    private CarListFragment listFragment;
    private AddCarFragment addCarFragment;
    private CarSeriesFragment seriesFragment;
    private CarBrandFragment brandFragment;
    private CarModelFragment modelFragment;
    private CarNumberPrefixPickerFragment prefixPickerFragment;

    private Fragment currentFragment;

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
                onbackClicked();
            }
        });
        fragmentManager = getFragmentManager();

        listFragment = CarListFragment.newInstance();
        addCarFragment = AddCarFragment.newInstance();
        seriesFragment = CarSeriesFragment.newInstance();
        brandFragment = CarBrandFragment.newInstance();
        modelFragment = CarModelFragment.newInstance();
        prefixPickerFragment = CarNumberPrefixPickerFragment.newInstance();

        if (getIntent().hasExtra("select")) {
            this.isSelecte = true;
        }

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_CAR_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);
        registeReceiver();

        if (CoreManager.getManager().getCurrUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent,REQUEST_CODE_LOGIN);
        } else {
            if (CoreManager.getManager().getUserCars().size() == 0) {
                switchFragment(addCarFragment, null);
            } else {
                switchFragment(listFragment, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (carService != null) {
            unbindService(conn);
        }
        unRegistReceiver();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void switchFragment(Fragment fragmet, Boolean inFromLeft) {

        this.currentFragment = fragmet;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_addcar_content, fragmet);
        if (inFromLeft != null && inFromLeft) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (inFromLeft != null) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
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

    private class AddCarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Constant.ACTION_BROADCAST_ADD_CAR_ERR.equals(action)) {
                String errmsg = intent.getStringExtra("description");
                Toast.makeText(context, errmsg, Toast.LENGTH_LONG).show();
                if (CoreManager.getManager().getUserCars().size() == 0) {
                    Toast.makeText(context, "添加车辆失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                    if (isSelecte) {
                        setResultCode(RESULT_CANCELED);
                    }
                    finish();
                } else {
                    switchFragment(listFragment,true);
                }
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
                int size = CoreManager.getManager().getUserCars().size();
                if (size == 0) {
                    // TODO
                }

                if (size == 1 && isSelecte && currentFragment instanceof AddCarFragment) {
                    Intent result = new Intent();
                    result.putExtra("car", CoreManager.getManager().getUserCars().get(0));

                    Bundle data = new Bundle();
                    data.putParcelable("car", CoreManager.getManager().getUserCars().get(0));
                    setResultCode(RESULT_OK);
                    setResultExtras(data);
                    finish();
                } else {
                    switchFragment(listFragment, true);
                }
            }

            if (Constant.ACTION_BROADCAST_GET_USER_CARS_ERR.equals(action)) {
                Toast.makeText(context, "获取车辆信息失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                if (currentFragment instanceof AddCarFragment) {
                    if (CoreManager.getManager().getUserCars().size() == 0) {
                        if (isSelecte) {
                            setResultCode(RESULT_CANCELED);
                        }
                        finish();
                    } else {
                        switchFragment(listFragment, true);
                    }
                } else {
                    if (isSelecte) {
                        setResultCode(RESULT_CANCELED);
                    }
                    finish();
                }

            }

        }
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

    private boolean onbackClicked() {
        if (currentFragment == null) {
            onBackPressed();
            return true;
        } else {
            if (currentFragment instanceof CarModelFragment) {
                switchFragment(seriesFragment, true);
                return true;
            }

            if (currentFragment instanceof CarBrandFragment) {
                switchFragment(addCarFragment, true);
                return true;
            }

            if (currentFragment instanceof CarSeriesFragment) {
                switchFragment(brandFragment, true);
                return true;
            }

//            if (currentFragment instanceof CarDetalFragment) {
//                switchFragment(listFragment, true);
//                return true;
//            }

            if (currentFragment instanceof CarListFragment) {
                return false;
            }

            if (currentFragment instanceof AddCarFragment) {
                if (CoreManager.getManager().getUserCars().size() != 0) {
                    switchFragment(listFragment, true);
                    return true;
                } else {
                    return false;
                }
            }

            if (currentFragment instanceof CarNumberPrefixPickerFragment) {
                switchFragment(addCarFragment, true);
                return true;
            }


        }

        return false;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
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
                // TODO show Car detal
            }
        }

        @Override
        public void requestAddCar() {
            addCarFragment.clear();
            switchFragment(addCarFragment, false);
        }
    }

    // ---------------------------------------------------------------------------------------------
    public AddCarInteraction getAddCarInteraction() {
        return new AddCarInteraction();
    }

    private class AddCarInteraction implements AddCarFragment.OnAddCarFragmentInteractionListener {

        @Override
        public void onBrandClicked() {
            switchFragment(brandFragment, false);
        }

        @Override
        public void onPrefixClicked() {
            prefixPickerFragment.setPrefix1(addCarFragment.getPrefix1());
            prefixPickerFragment.setPrefix2(addCarFragment.getPrefix2());
            switchFragment(prefixPickerFragment, false);
        }

        @Override
        public void onCommit() {

            carService.addCarForUser(CoreManager.getManager().getCurrUser(), addCarFragment.getCarInfo());
            DialogHelper.getHelper().showWaitting();
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class BrandInteraction implements CarBrandFragment.OnBrandFragmentInteractionListener {

        @Override
        public void onBrandSelected(CarBrand brand) {

            addCarFragment.getCarInfo().setCarBrandId(brand.getId());
            addCarFragment.getCarInfo().setCarBrandName(brand.getName());

            List<CarSeries> seriesList = CoreManager.getManager().getCarSeriesByBrandId(brand.getId());
            seriesFragment.setSeriesData(seriesList);
            seriesFragment.setBrandId(brand.getId());
            switchFragment(seriesFragment, false);
        }
    }

    public BrandInteraction getBrandInteraction() {
        return new BrandInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class SeriesInteraction implements CarSeriesFragment.OnSeriesFragmentInteractionListener {

        @Override
        public void onSeriesSelected(CarSeries series) {

            addCarFragment.getCarInfo().setSeriesName(series.getSeriesName());
            addCarFragment.getCarInfo().setSeriesId(series.getId());

            List<CarModel> modelList = CoreManager.getManager().getCarModelsBySeriesId(series.getId());
            modelFragment.setSeriesId(series.getId());
            modelFragment.setModelData(modelList);
            switchFragment(modelFragment, false);
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
    private class ModelInteraction implements CarModelFragment.OnModelFragmentInteractionListener {

        @Override
        public void onModelSelected(CarModel model) {

            addCarFragment.getCarInfo().setModelName(model.getTypeName());
            addCarFragment.getCarInfo().setModelId(model.getId());

            switchFragment(addCarFragment, true);


        }

        @Override
        public void onModelDataRequest(String seriesID) {
            carService.getModelBySeriesId(seriesID);
        }
    }

    public ModelInteraction getModelInteraction() {
        return new ModelInteraction();
    }

    //----------------------------------------------------------------------------------------------
    private class PrefixInteraction implements CarNumberPrefixPickerFragment.OnPrefixFragmentInteractionListener {

        @Override
        public void onPrefix1Changed(String prefix1) {
            if (!TextUtils.isEmpty(prefix1) && prefix1.contains("(")) {
                prefix1 = prefix1.substring(0, prefix1.indexOf("("));
                addCarFragment.setPrefix1(prefix1);
            }
        }

        @Override
        public void onPrefix2Changed(String prefix2) {
            addCarFragment.setPrefix2(prefix2);
        }
    }

    public PrefixInteraction getPrefixFragmentInteraction() {
        return new PrefixInteraction();
    }
}
