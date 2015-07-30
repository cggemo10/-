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
import com.rrja.carja.model.CarModel;


public class CarModelFragment extends Fragment {


    private OnModelFragmentInteractionListener mListener;


    public static CarModelFragment newInstance() {
        CarModelFragment fragment = new CarModelFragment();
        return fragment;
    }

    public CarModelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_car_model, container, false);
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


    public interface OnModelFragmentInteractionListener {
        public void onModelSelected(CarModel model);
    }

}
