package com.rrja.carja.activity;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.homemaintenance.BaseElementFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceMainFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceSubServiceFragment;
import com.rrja.carja.fragment.homemaintenance.MaintenanceTagServiceFragment;
import com.rrja.carja.fragment.homemaintenance.TagMaintenanceFragment;
import com.rrja.carja.fragment.homemaintenance.TagServiceActionListener;
import com.rrja.carja.model.TagableElement;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.MaintenanceService;

import java.util.ArrayList;
import java.util.List;

public class HomeMaintenanceActivity extends AppCompatActivity {

    private MaintenanceOrder mOrder;

    private RecyclerView recyclerView;
    private AppCompatButton btnAdd;
    private AppCompatButton btnCommitOrder;

    Fragment currFragment;
    private FragmentManager fm;

    private ArrayList<Fragment> tagFragmentList = new ArrayList<>();

    private MaintenancePagerAdapter maintenancePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_home_maintenance);

        initTags();

        fm = getSupportFragmentManager();

        mOrder = new MaintenanceOrder();
        mOrder.setUserInfo(CoreManager.getManager().getCurrUser());
        CoreManager.getManager().getUserCars();
        MaintenanceMainFragment fragment = MaintenanceMainFragment.newInstance();
        switchFragment(fragment, false);
    }

    private void switchFragment(BaseElementFragment fragment, boolean addToStack) {

        if (fragment == null) {
            return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_maintenance_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!fm.popBackStackImmediate()) {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public MaintenanceOrder getmOrder() {
        return mOrder;
    }

    // -----------------------------------------------------------------------------------------
    private void initTags() {
        Fragment tagMaintenance = TagMaintenanceFragment.newInstance("101");
        Fragment tagRepair = TagMaintenanceFragment.newInstance("102");
        Fragment tagCosmetology = TagMaintenanceFragment.newInstance("103");

        tagFragmentList.add(tagMaintenance);
        tagFragmentList.add(tagRepair);
        tagFragmentList.add(tagCosmetology);

        maintenancePagerAdapter = new MaintenancePagerAdapter(getSupportFragmentManager());
    }

    public MaintenancePagerAdapter getPagerAdapter() {
        return maintenancePagerAdapter;
    }


    public class MaintenancePagerAdapter extends FragmentPagerAdapter {


        public MaintenancePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tagFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return tagFragmentList.size();
        }
    }

    //-------------------------------------------------------------------------------------
    public TagServiceActionListener getTagServiceListener() {
        return new TagServiceListener();
    }

    private class TagServiceListener implements TagServiceActionListener {

        @Override
        public void requestService(String serviceId) {

        }

        @Override
        public void onServiceClicked(MaintenanceService service) {

        }
    }

    // -------------------------------------------------------------------------------
    public MaintenanceTagServiceFragment.MaintenanceTagActionListener getTagActionListener() {
        return new TagActionListener();
    }

    private class TagActionListener implements MaintenanceTagServiceFragment.MaintenanceTagActionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    //---------------------------------------------------------------------------------

    public MaintenanceSubServiceFragment.SubServiceActionListener getSubServiceListener() {
        return new SubServiceListener();
    }

    private class SubServiceListener implements MaintenanceSubServiceFragment.SubServiceActionListener {

        @Override
        public void onSubServiceClicked(MaintenanceService service) {

        }

        @Override
        public void requestSubService(String serviceId) {

        }
    }


}
