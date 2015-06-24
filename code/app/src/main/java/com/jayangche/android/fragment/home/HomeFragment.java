package com.jayangche.android.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jayangche.android.R;
import com.jayangche.android.activity.DiscountActivity;
import com.jayangche.android.activity.HomeMaintenanceActivity;
import com.jayangche.android.activity.MainActivity;
import com.jayangche.android.activity.StoreReservationActivity;
import com.jayangche.android.activity.OnDoreWashActivity;
import com.jayangche.android.activity.ViolationActivity;
import com.jayangche.android.adapter.DiscountAdapter;
import com.jayangche.android.adapter.GridAdapter;
import com.jayangche.android.core.CoreManager;


public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnHomeInteractionListener mListener;

    private PullToRefreshListView refreshListDiscount;
    private ListView listDiscount;
    private GridAdapter gridAdapter;
    private DiscountAdapter discountAdapter;

    // header
    private ViewPager mPager;
    private LinearLayout llHomeMaintenance;
    private LinearLayout llOnDoreWash;
    private LinearLayout llStoreReservation;
    private LinearLayout llViolation;

    public static HomeFragment getFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        refreshListDiscount = (PullToRefreshListView) root.findViewById(R.id.pull_refresh_grid_dicsount);
        listDiscount = refreshListDiscount.getRefreshableView();

        discountAdapter = new DiscountAdapter(getActivity());

        View header = inflater.inflate(R.layout.view_fg_home_header, null);
        initHeader(header);

        listDiscount.addHeaderView(header);
        listDiscount.setAdapter(discountAdapter);
        listDiscount.setOnItemClickListener(this);
//        GridView
        return root;
    }

    private void initHeader(View header) {

        // for demo
        CoreManager.getManager().initCompanyInfo(getActivity());

        mPager = (ViewPager) header.findViewById(R.id.pager_main_ad);
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return CoreManager.getManager().getCompanyInfo().size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(CoreManager.getManager().getCompanyInfo().get(position));
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView ad = CoreManager.getManager().getCompanyInfo().get(position);
                container.addView(ad);
                return ad;
            }
        });
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        llHomeMaintenance = (LinearLayout) header.findViewById(R.id.ll_home_maintenance);
        llHomeMaintenance.setOnClickListener(this);
        llOnDoreWash = (LinearLayout) header.findViewById(R.id.ll_on_dore_wash);
        llOnDoreWash.setOnClickListener(this);
        llStoreReservation = (LinearLayout) header.findViewById(R.id.ll_store_reservation);
        llStoreReservation.setOnClickListener(this);
        llViolation = (LinearLayout) header.findViewById(R.id.ll_violation);
        llViolation.setOnClickListener(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity) activity).getHomeInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home_maintenance:
                Intent intent = new Intent(getActivity(), HomeMaintenanceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_on_dore_wash:
                Intent onDoreWash = new Intent(getActivity(), OnDoreWashActivity.class);
                startActivity(onDoreWash);
                break;
            case R.id.ll_store_reservation:
                Intent maintenance = new Intent(getActivity(), StoreReservationActivity.class);
                startActivity(maintenance);
                break;
            case R.id.ll_violation:
                Intent violation = new Intent(getActivity(), ViolationActivity.class);
                startActivity(violation);
                break;
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DiscountActivity.class);
        intent.putExtra("discount_info", CoreManager.getManager().getDiscounts().get(position));
        startActivity(intent);
    }


    public interface OnHomeInteractionListener {
        
        public void onFragmentInteraction(Uri uri);
    }

}
