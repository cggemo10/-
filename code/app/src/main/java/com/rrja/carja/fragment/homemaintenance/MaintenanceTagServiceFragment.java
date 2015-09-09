package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceTagServiceFragment extends BaseElementFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView tv1, tv2, tv3;
    private TextView tvLine;
    private ViewPager viewPager;

    private int lineWidth;

    private MaintenanceTagActionListener mListener;
    private List<TagListMaintenanceFragment> tagFragmentList = new ArrayList<>();
    private MaintenancePagerAdapter adapter;

    private static class FragmentHolder {
        private static MaintenanceTagServiceFragment fragment = new MaintenanceTagServiceFragment();
    }

    public static MaintenanceTagServiceFragment newInstance() {
        return FragmentHolder.fragment;
    }

    public MaintenanceTagServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_tag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv1 = (TextView) view.findViewById(R.id.txt_maintenance_tag1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) view.findViewById(R.id.txt_maintenance_tag2);
        tv2.setOnClickListener(this);
        tv3 = (TextView) view.findViewById(R.id.txt_maintenance_tag3);
        tv3.setOnClickListener(this);
        tvLine = (TextView) view.findViewById(R.id.tv_tag_line);

        viewPager = (ViewPager) view.findViewById(R.id.vp_maintenance);
        adapter = new MaintenancePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity) getActivity()).getTagActionListener();
        initTags();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeMaintenanceActivity) getActivity()).dismissAddIcon();
        int width = tv1.getWidth();
        if (width > 0) {
            lineWidth = width;
            ViewGroup.LayoutParams layoutParams = tvLine.getLayoutParams();
            layoutParams.width = lineWidth;
            tvLine.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_maintenance_tag1:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.txt_maintenance_tag2:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.txt_maintenance_tag3:
                viewPager.setCurrentItem(2, true);
                break;

            default:
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int s = position * lineWidth + (int) (positionOffset * lineWidth);
        Log.i("haha", s + "");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = lineWidth;
        layoutParams.setMargins(s, 0, 0, 0);
        tvLine.setLayoutParams(layoutParams);
    }


    @Override
    public void onPageSelected(int position) {
        tagFragmentList.get(position).refresh();
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    public interface MaintenanceTagActionListener {

        public void onFragmentInteraction(Uri uri);
    }


    // -----------------------------------------------------------------------------------------
    private void initTags() {
        TagListMaintenanceFragment tagMaintenance = TagListMaintenanceFragment.newInstance("101");
        TagListMaintenanceFragment tagRepair = TagListMaintenanceFragment.newInstance("102");
        TagListMaintenanceFragment tagCosmetology = TagListMaintenanceFragment.newInstance("103");

        tagFragmentList.add(tagMaintenance);
        tagFragmentList.add(tagRepair);
        tagFragmentList.add(tagCosmetology);

        adapter = new MaintenancePagerAdapter(getActivity().getSupportFragmentManager());
    }

    public class MaintenancePagerAdapter extends FragmentPagerAdapter {


        public MaintenancePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return tagFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return tagFragmentList.size();
        }
    }

}
