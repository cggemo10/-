package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.activity.AddCarActivity;
import com.rrja.carja.adapter.CarModelAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarModel;
import com.rrja.carja.model.CarSeries;
import com.rrja.carja.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;


public class CarModelFragment extends Fragment implements CarModelAdapter.OnModelItemClickedListener{


    private OnModelFragmentInteractionListener mListener;
    private List<CarModel> modelData = new ArrayList<>();

    private RecyclerView recyclerView;
    private CarModelAdapter adapter;
    private String seriesId;

    private CarModelReceiver receiver;

    public static CarModelFragment newInstance() {
        CarModelFragment fragment = new CarModelFragment();
        return fragment;
    }

    public CarModelFragment() {
        adapter = new CarModelAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_model, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_car_model);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(this);
        adapter.setModelData(CoreManager.getManager().getCarModelsBySeriesId(seriesId));
    }

    public void setModelData(List<CarModel> modelList) {

        modelData.clear();
        modelData = modelList;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (CoreManager.getManager().getCarModelsBySeriesId(seriesId) == null || CoreManager.getManager().getCarModelsBySeriesId(seriesId).size() == 0) {
            if (mListener != null) {
                mListener.onModelDataRequest(seriesId);
                DialogHelper.getHelper().showWaitting();
            }
        }

        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_CAR_MODEL);
            filter.addAction(Constant.ACTION_BROADCAST_GET_CAR_MODEL_ERR);

            receiver = new CarModelReceiver();
            getActivity().registerReceiver(receiver, filter);
        }
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((AddCarActivity)activity).getModelInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public void onModelClicked(CarModel model) {
        if (mListener != null) {
            mListener.onModelSelected(model);
        }
    }


    public interface OnModelFragmentInteractionListener {
        public void onModelSelected(CarModel model);
        public void onModelDataRequest(String seriesID);
    }

    private class CarModelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_CAR_MODEL.equals(action) || Constant.ACTION_BROADCAST_GET_CAR_MODEL_ERR.equals(action)) {
                if (intent.getExtras() != null && intent.getExtras().containsKey("series_id")) {
                    String seriesID = intent.getExtras().getString("series_id");
                    if (seriesId.equals(seriesID)) {
                        DialogHelper.getHelper().dismissWatting();
                        List<CarModel> carModelList = CoreManager.getManager().getCarModelsBySeriesId(seriesId);
                        setModelData(carModelList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

}
