package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.maintenance.MaintenanceService;



public class MaintenanceTagListServiceFragment extends BaseElementFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView tv1, tv2, tv3;
    private TextView tvLine;
    private ViewPager viewPager;

    private int lineWidth;

    private MaintenanceTagActionListener mListener;
    private TagServiceReceiver mReceiver;

    private ListView maintList, repareList, cosmetoList;
    private MaintenancePagerAdapter adapter;
    private ServiceAdapter maintAdapter, repareAdapter, cosmetoAdapter;

    private static class FragmentHolder {
        private static MaintenanceTagListServiceFragment fragment = new MaintenanceTagListServiceFragment();
    }

    public static MaintenanceTagListServiceFragment newInstance() {
        return FragmentHolder.fragment;
    }

    public MaintenanceTagListServiceFragment() {
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
        adapter = new MaintenancePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity) getActivity()).getTagListActionListener();
        initData();
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

        registReceiver();
    }

    @Override
    public void onStop() {
        unregistReceiver();
        super.onStop();
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);

            mReceiver = new TagServiceReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    private void unregistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
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
//        switch (position) {
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    public interface MaintenanceTagActionListener {

        public void requestService(String serviceId);

        public void onServiceClicked(MaintenanceService service);
    }


    // -----------------------------------------------------------------------------------------
    private void initData() {
        if (CoreManager.getManager().getMaintenanceService("101").size() == 0) {
            if (mListener != null) {
                mListener.requestService("101");
            }
        }

        if (CoreManager.getManager().getMaintenanceService("102").size() == 0) {
            if (mListener != null) {
                mListener.requestService("102");
            }
        }

        if (CoreManager.getManager().getMaintenanceService("103").size() == 0) {
            if (mListener != null) {
                mListener.requestService("103");
            }
        }

    }

    private void initTags() {
//        TagListMaintenanceFragment tagMaintenance = TagListMaintenanceFragment.newInstance("101");
//        TagListMaintenanceFragment tagRepair = TagListMaintenanceFragment.newInstance("102");
//        TagListMaintenanceFragment tagCosmetology = TagListMaintenanceFragment.newInstance("103");
//
//        pageList.add(tagMaintenance);
//        pageList.add(tagRepair);
//        pageList.add(tagCosmetology);
        if (maintAdapter == null) {
            maintAdapter = new ServiceAdapter("101");
        }
        if (maintList == null) {
            maintList = new ListView(getActivity());
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            maintList.setLayoutParams(params1);
            maintList.setAdapter(maintAdapter);
            maintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view.setBackgroundColor(getResources().getColor(R.color.c_style_red));
                    MaintenanceService service = CoreManager.getManager().getMaintenanceService("101").get(position);
                    mListener.onServiceClicked(service);
                }
            });
        }

        if (repareAdapter == null) {
            repareAdapter = new ServiceAdapter("102");
        }

        if (repareList == null) {
            repareList = new ListView(getActivity());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            repareList.setLayoutParams(params2);
            repareList.setAdapter(repareAdapter);
            repareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view.setBackgroundColor(getResources().getColor(R.color.c_style_red));
                    MaintenanceService service = CoreManager.getManager().getMaintenanceService("102").get(position);
                    mListener.onServiceClicked(service);
                }
            });
        }

        if (cosmetoAdapter == null) {
            cosmetoAdapter = new ServiceAdapter("103");
        }

        if (cosmetoList == null) {
            cosmetoList = new ListView(getActivity());
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            cosmetoList.setLayoutParams(params3);
            cosmetoList.setAdapter(cosmetoAdapter);
            cosmetoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view.setBackgroundColor(getResources().getColor(R.color.c_style_red));
                    MaintenanceService service = CoreManager.getManager().getMaintenanceService("103").get(position);
                    mListener.onServiceClicked(service);
                }
            });
        }

        if (adapter == null) {
            adapter = new MaintenancePagerAdapter();
        }
    }

    public class MaintenancePagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = maintList;
                    break;
                case 1:
                    view = repareList;
                    break;
                case 2:
                    view = cosmetoList;
                    break;
            }
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = null;
            switch (position) {
                case 0:
                    view = maintList;
                    break;
                case 1:
                    view = repareList;
                    break;
                case 2:
                    view = cosmetoList;
                    break;
            }
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class ServiceAdapter extends BaseAdapter {

        private String serviceTag;

        ServiceAdapter(String tag) {
            this.serviceTag = tag;
        }

        @Override
        public int getCount() {
            return CoreManager.getManager().getMaintenanceService(serviceTag).size();
        }


        @Override
        public Object getItem(int position) {
            return CoreManager.getManager().getMaintenanceService(serviceTag).get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListHolder holder = null;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_maintenance_multi_checkable, null);
                holder = new ListHolder();
                holder.txt = (TextView) convertView.findViewById(R.id.txt_item_maintenance_ele);
                convertView.setTag(holder);
            } else {
                holder = (ListHolder) convertView.getTag();
            }

            MaintenanceService service = CoreManager.getManager().getMaintenanceService(serviceTag).get(position);
            holder.txt.setText(service.getName());
            return convertView;
        }

        private class ListHolder {
            TextView txt;
        }
    }

    private class TagServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras.containsKey("serviceId")) {
                    if (extras.getString("serviceId").equals("101")) {
                        maintAdapter.notifyDataSetChanged();
                    }
                    if (extras.getString("serviceId").equals("102")) {
                        repareAdapter.notifyDataSetChanged();
                    }
                    if (extras.getString("serviceId").equals("103")) {
                        cosmetoAdapter.notifyDataSetChanged();
                    }
                }
            }
            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR.equals(action)) {
                // TODO
            }
        }
    }

}
