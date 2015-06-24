package com.jayangche.android.fragment.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jayangche.android.R;
import com.jayangche.android.activity.MainActivity;
import com.jayangche.android.adapter.DiscountAdapter;
import com.jayangche.android.adapter.GridAdapter;


public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnHomeInteractionListener mListener;

    private PullToRefreshListView refreshListDiscount;
    private ListView listDiscount;
    private GridAdapter gridAdapter;
    private DiscountAdapter discountAdapter;

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
        gridAdapter = new GridAdapter(getActivity(),discountAdapter);

        gridAdapter.setNumColumns(2);


        View header = inflater.inflate(R.layout.view_fg_home_header, null);
        initHeader(header);

        listDiscount.addHeaderView(header);
        listDiscount.setAdapter(discountAdapter);
//        GridView
        return root;
    }

    private void initHeader(View header) {

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
