package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.activity.CarInfoActivity;
import com.rrja.carja.adapter.CarBrandAdapter;
import com.rrja.carja.adapter.GridAdapter;
import com.rrja.carja.adapter.decoration.CarBrandDecoration;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarBrand;
import com.rrja.cja.view.recycler.stickyside.LayoutManager;

public class CarBrandFragment extends Fragment implements CarBrandAdapter.onItemClickListener {

    private int mHeaderDisplay;

    private OnBrandFragmentInteractionListener mListener;

    private RecyclerView brandRecycleList;
    private CarBrandAdapter mAdapter;
    private BrandRefreshReceiver receiver;


    public static CarBrandFragment newInstance() {
        return new CarBrandFragment();
    }

    public CarBrandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_brand, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);

        brandRecycleList = (RecyclerView) view.findViewById(R.id.recycler_car_brand);
        brandRecycleList.setLayoutManager(new LayoutManager(getActivity()));
        mAdapter = new CarBrandAdapter(getActivity());
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mAdapter.setItemListener(this);
        brandRecycleList.setAdapter(mAdapter);

    }

    public void smoothScrollToPosition(int position) {
        brandRecycleList.smoothScrollToPosition(position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((CarInfoActivity)activity).getBrandInteraction();
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_BROADCAST_REFRESH_CARBRAND);

        receiver = new BrandRefreshReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(CarBrand brand) {
        mListener.onBrandSelected(brand);
    }

    public interface OnBrandFragmentInteractionListener {

        public void onBrandSelected(CarBrand brand);
    }


    private class BrandRefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
