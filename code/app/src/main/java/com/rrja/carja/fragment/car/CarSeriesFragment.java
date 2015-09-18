package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rrja.carja.R;
import com.rrja.carja.activity.CarInfoActivity;
import com.rrja.carja.adapter.CarSeriesAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;


public class CarSeriesFragment extends Fragment implements CarSeriesAdapter.OnSeriesItemClickListener{


    private OnSeriesFragmentInteractionListener mListener;
    private List<CarSeries> seriesData = new ArrayList<>();

    private RecyclerView recyclerView;
    private CarSeriesAdapter adapter;

    private CarSeriesReceiver receiver;

    private String brandId;

    public static CarSeriesFragment newInstance() {
        CarSeriesFragment fragment = new CarSeriesFragment();
        return fragment;
    }

    public CarSeriesFragment() {
        adapter = new CarSeriesAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_series, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_car_series);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter.setItemClickListener(this);
        adapter.setData(seriesData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((CarInfoActivity)activity).getSeriesInteraction();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter.getSeriesData() == null || adapter.getSeriesData().size() == 0) {

            if (mListener != null) {
                DialogHelper.getHelper().showWaitting();
                mListener.onRequestSeriesData(brandId);
            }
        }

        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_CAR_SERIES);
            filter.addAction(Constant.ACTION_BROADCAST_GET_CAR_SERIES_ERR);

            receiver = new CarSeriesReceiver();
            getActivity().registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onStop() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        DialogHelper.getHelper().dismissWatting();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setSeriesData(List<CarSeries> series) {
        adapter.setData(series);
    }

    @Override
    public void onItemClick(CarSeries carSeries) {
        if (mListener != null) {
            mListener.onSeriesSelected(carSeries);
        }
    }


    public interface OnSeriesFragmentInteractionListener {

        public void onSeriesSelected(CarSeries series);
        public void onRequestSeriesData(String brandId);
    }

    private class CarSeriesReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_CAR_SERIES.equals(action) || Constant.ACTION_BROADCAST_GET_CAR_SERIES_ERR.equals(action)) {
                if (intent.getExtras() != null && intent.getExtras().containsKey("brand_id")) {
                    String brandID = intent.getExtras().getString("brand_id");
                    if (brandId.equals(brandID)) {
                        DialogHelper.getHelper().dismissWatting();
                        List<CarSeries> carSeriesList = CoreManager.getManager().getCarSeriesByBrandId(brandId);
                        setSeriesData(carSeriesList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }


}
