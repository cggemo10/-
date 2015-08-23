package com.rrja.carja.activity;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;

import com.rrja.carja.R;
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

    public List<TagableElement> getOrderContent() {

        return mOrder.listOrderInfo();

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
    }


}
