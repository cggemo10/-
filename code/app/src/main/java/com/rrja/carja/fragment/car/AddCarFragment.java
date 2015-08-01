package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.AddCarActivity;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarSeries;


public class AddCarFragment extends Fragment implements View.OnClickListener {

    private TextView txtPrefix;
    private String prefix1 = "京";
    private String prefix2 = "A";
    private TextView txtSeries;

    private TextView txtCarNum;

    private OnAddCarFragmentInteractionListener mListener;

    public AddCarFragment() {
        // Required empty public constructor
    }

    public static AddCarFragment newInstance() {
        AddCarFragment fragment = new AddCarFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addcar,container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        txtSeries = (TextView) v.findViewById(R.id.txt_lable_car_series_content);
        txtSeries.setOnClickListener(this);
        txtCarNum = (TextView) v.findViewById(R.id.txt_car_munber);
        txtCarNum.setOnClickListener(this);
        txtPrefix = (TextView) v.findViewById(R.id.txt_prefix_content);
        txtPrefix.setOnClickListener(this);
        AddCarActivity mActivity = (AddCarActivity)getActivity();
        String prefix = mActivity.getPrefix1() + mActivity.getPrefix2();
        txtPrefix.setText(prefix);
    }

    public void setPrefix(String prefix) {
        if (!TextUtils.isEmpty(prefix)) {
            txtPrefix.setText(prefix);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((AddCarActivity)activity).getAddCarInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_lable_car_series_content:
                if (mListener != null) {
                    mListener.onBrandClicked();
                }
                break;
            case R.id.txt_car_munber:
                break;
            case R.id.txt_prefix_content:
                if (mListener != null) {
                    mListener.onPrefixClicked();
                }
                break;
        }
    }


    public interface OnAddCarFragmentInteractionListener {

        public void onBrandClicked();
        public void onPrefixClicked();
        public void onCommit();
    }

}
