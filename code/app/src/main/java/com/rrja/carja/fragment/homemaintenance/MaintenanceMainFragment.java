package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.rrja.carja.activity.CarInfoActivity;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.adapter.MaintenanceAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.TagableElement;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.TagableService;
import com.rrja.carja.model.maintenance.TagableSubService;
import com.rrja.carja.service.FileService;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceMainFragment extends BaseElementFragment implements View.OnClickListener, MaintenanceAdapter.MaintenanceListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private LinearLayout llloc;
    private TextView txtLoc;
    private RecyclerView recyclerMaintenance;

    private MaintenanceAdapter maintanceAdapter;

    private OnMaintenancdMainFragmentionListener mListener;

    private MaintenanceReceiver mReceiver;


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
        maintanceAdapter.setListener(this);

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
        registReceiver();
        getActivity().setTitle(R.string.str_main_on_door);
        ((HomeMaintenanceActivity)getActivity()).showAddIcon();
        MaintenanceOrder order = ((HomeMaintenanceActivity) getActivity()).getOrderInfo();
        if (order != null) {
            maintanceAdapter.setOrder(order);
        }
    }

    @Override
    public void onStop() {
        unregistReceiver();
        super.onStop();
    }

    private void registReceiver() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_CARLOGO);

            mReceiver = new MaintenanceReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    private void unregistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
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

        void removeService(MaintenanceMainFragment fragment, TagableService service);
        void removeSubService(MaintenanceMainFragment fragment, TagableService service, TagableSubService subService);
        void onCarClicked();
    }

    public MaintenanceOrder getOrderContent() {
        if (getActivity() != null) {
            return ((HomeMaintenanceActivity) getActivity()).getmOrder();
        }
        return new MaintenanceOrder();
    }

    private class MaintenanceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_CARLOGO.equals(action)) {
                maintanceAdapter.notifyItemChanged(0);
            }
        }
    }

    // ----------------------------------------------------------------------------------
    @Override
    public void onRequestCarLogo(CarInfo carInfo) {
        Intent intent = new Intent(getActivity(), FileService.class);
        intent.setAction(FileService.ACTION_IMG_CAR_LOGO);
        intent.putExtra("car", carInfo);
        getActivity().startService(intent);
    }

    @Override
    public void onCarClicked() {
        if (mListener != null) {
            mListener.onCarClicked();
        }
    }

    @Override
    public void onMaintenanceServiceDelete(TagableService service, int position) {
        if (mListener != null) {
            mListener.removeService(this, service);
            maintanceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMaintenanceSubServiceDelete(TagableService service, TagableSubService subService, int position) {
        if (mListener != null) {
            mListener.removeSubService(this, service, subService);
            maintanceAdapter.notifyItemChanged(position);
        }
    }

}
