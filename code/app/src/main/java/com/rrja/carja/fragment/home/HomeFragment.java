package com.rrja.carja.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rrja.carja.R;
import com.rrja.carja.activity.DiscountActivity;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.activity.StoreReservationActivity;
import com.rrja.carja.activity.OnDoreWashActivity;
import com.rrja.carja.activity.ViolationActivity;
import com.rrja.carja.adapter.DiscountAdapter;
import com.rrja.carja.adapter.GridAdapter;
import com.rrja.carja.core.CoreManager;


public class HomeFragment extends Fragment {

    private OnHomeInteractionListener mListener;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private DiscountAdapter discountAdapter;


    public static HomeFragment getFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for example

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.pull_refresh_grid_dicsount);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        discountAdapter = new DiscountAdapter(getActivity());

        recyclerView.setAdapter(discountAdapter);

        return root;
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


    public interface OnHomeInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
