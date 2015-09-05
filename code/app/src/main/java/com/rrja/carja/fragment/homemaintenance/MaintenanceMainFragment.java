package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.adapter.MaintenanceAdapter;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.TagableElement;
import com.rrja.carja.model.maintenance.MaintenanceOrder;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceMainFragment extends BaseElementFragment implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private LinearLayout llloc;
    private TextView txtLoc;
    private RecyclerView recyclerMaintenance;

    private MaintenanceAdapter maintanceAdapter;

    private OnMaintenancdMainFragmentionListener mListener;


    public static MaintenanceMainFragment newInstance() {
        MaintenanceMainFragment fragment = new MaintenanceMainFragment();
        return fragment;
    }

    public MaintenanceMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        maintanceAdapter = new MaintenanceAdapter();

        recyclerMaintenance = (RecyclerView) view.findViewById(R.id.recycler_main_maintenance);
        recyclerMaintenance.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMaintenance.setAdapter(maintanceAdapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity)activity).getMaintenanceMainListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.str_main_on_door);
        MaintenanceOrder order = ((HomeMaintenanceActivity) getActivity()).getOrderInfo();
        if (order != null) {
            maintanceAdapter.setOrder(order);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_loc) {
            // TODO switch service choise
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }


    public interface OnMaintenancdMainFragmentionListener {

    }

    public MaintenanceOrder getOrderContent() {
        if (getActivity() != null) {
            return ((HomeMaintenanceActivity) getActivity()).getmOrder();
        }
        return new MaintenanceOrder();
    }

}
