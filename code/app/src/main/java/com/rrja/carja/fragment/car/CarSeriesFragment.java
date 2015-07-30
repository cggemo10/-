package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.activity.AddCarActivity;
import com.rrja.carja.model.CarSeries;


public class CarSeriesFragment extends Fragment {


    private OnSeriesFragmentInteractionListener mListener;


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

        return inflater.inflate(R.layout.fragment_car_series, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((AddCarActivity)activity).getSeriesInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSeriesFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSeriesSelected(CarSeries series);
    }

}
