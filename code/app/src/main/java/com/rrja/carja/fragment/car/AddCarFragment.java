package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.activity.AddCarActivity;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.CarSeries;

import org.w3c.dom.Text;


public class AddCarFragment extends Fragment implements View.OnClickListener {

    private TextView txtPrefix;
    private String prefix1 = "京";
    private String prefix2 = "A";

    private EditText txtSeries;

    private EditText txtCarNum;
    private EditText edFrame;
    private EditText edEngine;

    private AppCompatButton btnSave;
    private AppCompatButton btnDel;

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
        AddCarActivity mActivity = (AddCarActivity)getActivity();
        txtSeries = (EditText) v.findViewById(R.id.txt_lable_car_series_content);
        txtSeries.setOnClickListener(this);
        txtCarNum = (EditText) v.findViewById(R.id.txt_car_munber);
        txtCarNum.setOnClickListener(this);
        txtPrefix = (TextView) v.findViewById(R.id.txt_prefix_content);
        txtPrefix.setOnClickListener(this);
        String prefix = mActivity.getPrefix1() + mActivity.getPrefix2();
        txtPrefix.setText(prefix);

        edFrame = (EditText) v.findViewById(R.id.ed_car_frame_munber);
        edEngine = (EditText) v.findViewById(R.id.ed_car_engine_munber);

        btnSave = (AppCompatButton) v.findViewById(R.id.btn_save_car);
        btnSave.setOnClickListener(this);
        btnDel = (AppCompatButton) v.findViewById(R.id.btn_del_car);
        btnDel.setOnClickListener(this);
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
    public void onStart() {
        super.onStart();
        AddCarActivity mActivity = (AddCarActivity)getActivity();
        CarInfo carInfo = mActivity.getCarInfo();
        if (carInfo.getSeries() != null && carInfo.getCarBrand() != null && carInfo.getCarModel() != null) {
            String seriesInfo = carInfo.getCarBrand().getName() + " " + carInfo.getCarModel().getSeriesName();
            txtSeries.setText(seriesInfo);
        }
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
            case R.id.btn_save_car:
                AddCarActivity mActivity = (AddCarActivity)getActivity();
                CarInfo carInfo = mActivity.getCarInfo();
                if (carInfo.getCarBrand() == null || carInfo.getSeries() ==null || carInfo.getCarModel() == null) {
                    Toast.makeText(getActivity(), "请选择您的车型。", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(txtCarNum.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写您的车牌号。", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!TextUtils.isEmpty(edEngine.getText().toString())) {
                    carInfo.setEngineNo(edEngine.getText().toString());
                }

                if (!TextUtils.isEmpty(edFrame.getText().toString())) {
                    carInfo.setFrameNo6(edFrame.getText().toString());
                }

                String carNum = prefix1 + prefix2 + txtCarNum.getText().toString();
                if (mListener != null) {
                    mListener.onCommit(carNum);
                }
                break;
            case R.id.btn_del_car:
                getActivity().finish();
                break;
        }
    }


    public interface OnAddCarFragmentInteractionListener {

        public void onBrandClicked();
        public void onPrefixClicked();
        public void onCommit(String carNumber);
    }

}
