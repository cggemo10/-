package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableSubService;

public class MaintenanceGoodsFragment extends BaseElementFragment {


    private OnMaintenanceGoodsListener mListener;

    private MaintenanceService service;
    private MaintenanceService feeService;


    public static MaintenanceGoodsFragment newInstance() {
        MaintenanceGoodsFragment fragment = new MaintenanceGoodsFragment();
        return fragment;
    }

    public MaintenanceGoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maintenance_goods, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity) activity).getMaintenGoodsListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    public interface OnMaintenanceGoodsListener {

        public void onGoodsCommit(TagableSubService tagableSubService);

        public void requestGoods(String serviceId, int page);

        public void refreshGoods();
    }

    public void setSubService(MaintenanceService service) {
        this.service = service;
    }

    public void setFeeService(MaintenanceService feeService) {
        this.feeService = feeService;
    }

}
