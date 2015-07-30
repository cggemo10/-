package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.adapter.PrefixAdapter;


public class CarNumberPrefixPickerFragment extends Fragment implements View.OnClickListener{


    private OnFragmentInteractionListener mListener;

    private TextView txtPrefix1;
    private TextView txtPrefix2;
    private RecyclerView recyclerView;

    PrefixAdapter prefixAdapter;

    public static CarNumberPrefixPickerFragment newInstance(String param1, String param2) {
        CarNumberPrefixPickerFragment fragment = new CarNumberPrefixPickerFragment();

        return fragment;
    }

    public CarNumberPrefixPickerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_num_prefix_picker, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {

        txtPrefix1 = (TextView) v.findViewById(R.id.txt_car_prefix_1);
        txtPrefix2 = (TextView) v.findViewById(R.id.txt_car_prefix_2);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_car_prefix_picker);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(manager);
        prefixAdapter = new PrefixAdapter();
        recyclerView.setAdapter(prefixAdapter);

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
