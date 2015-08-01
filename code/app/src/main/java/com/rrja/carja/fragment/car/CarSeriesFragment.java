package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rrja.carja.R;
import com.rrja.carja.activity.AddCarActivity;
import com.rrja.carja.adapter.CarSeriesAdapter;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;


public class CarSeriesFragment extends Fragment {


    private OnSeriesFragmentInteractionListener mListener;
    private List<CarSeries> seriesData = new ArrayList<>();

    private RecyclerView recyclerView;
    private CarSeriesAdapter adapter;

    public static CarSeriesFragment newInstance() {
        CarSeriesFragment fragment = new CarSeriesFragment();
        return fragment;
    }

    public CarSeriesFragment() {

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
        adapter = new CarSeriesAdapter();
        adapter.setData(seriesData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((AddCarActivity)activity).getSeriesInteraction();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (seriesData== null || seriesData.size() == 0) {
            DialogHelper.getHelper().showWaitting();
            if (mListener != null) {
                mListener.onRequestSeriesData();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSeriesData(List<CarSeries> series) {
        seriesData.clear();
        if (series != null && series.size() != 0) {
            this.seriesData = series;
        }
    }


    public interface OnSeriesFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSeriesSelected(CarSeries series);
        public void onRequestSeriesData();
    }

    private class

}
