package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.maintenance.MaintenanceService;

import android.content.IntentFilter;

import java.util.List;

public class MaintenanceSubServiceFragment extends BaseElementFragment {

    private SubServiceActionListener mListener;
    private MaintenanceService maintService;
    private SubServiceAdapter adapter;
    private RecyclerView recyclerSub;

    private SubServiceReceiver mReceiver;

    public static MaintenanceSubServiceFragment newInstance() {
        MaintenanceSubServiceFragment fragment = new MaintenanceSubServiceFragment();
        return fragment;
    }

    public MaintenanceSubServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_sub, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {


        recyclerSub = (RecyclerView) view.findViewById(R.id.recycler_maintenance_subservice);
        recyclerSub.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SubServiceAdapter();
        recyclerSub.setAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity) activity).getSubServiceListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        registReceiver();
        if (CoreManager.getManager().getMaintenanceService(maintService.getId()) == null ||
                CoreManager.getManager().getMaintenanceService(maintService.getId()).size() == 0) {

        }

    }

    private void registReceiver() {

        if (mReceiver == null) {

            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);

            mReceiver = new SubServiceReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    private void unregistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    public void setService(MaintenanceService service) {
        this.maintService = service;
    }

    @Override
    public void onStop() {
        unregistReceiver();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public MaintenanceService getMaintService() {
        return maintService;
    }

    public void setMaintService(MaintenanceService maintService) {
        this.maintService = maintService;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public interface SubServiceActionListener {

        public void onSubServiceClicked(MaintenanceService service, MaintenanceService feeService);

        public void requestSubService(String serviceId);
    }

    private class SubServiceAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagerv_maintenance, null);
            return new ServiceTV(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final MaintenanceService maintenanceService = CoreManager.getManager().getMaintenanceService(maintService.getId()).get(position);

            final ServiceTV tv = (ServiceTV) holder;
            tv.bindData(maintenanceService.getName());

            tv.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO lock ui
                    tv.txt.setBackgroundColor(getResources().getColor(R.color.c_style_red));
                    notifyItemChanged(position);

                    MaintenanceService feeService = adapter.getFeeService();
                    if (mListener != null) {
                        mListener.onSubServiceClicked(maintenanceService, feeService);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return CoreManager.getManager().getMaintenanceService(maintService.getId()).size();
        }

        private MaintenanceService getFeeService() {
            if (CoreManager.getManager().getMaintenanceService(maintService.getId()) != null &&
                    CoreManager.getManager().getMaintenanceService(maintService.getId()).size() != 0) {
                List<MaintenanceService> serviceList = CoreManager.getManager().getMaintenanceService(maintService.getId());
                for (MaintenanceService maintSe : serviceList) {
                    if ("�����".equals(maintSe.getName())) {
                        return maintSe;
                    }
                }
            }

            return null;
        }
    }

    private class ServiceTV extends RecyclerView.ViewHolder {

        TextView txt;

        public ServiceTV(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt_item_maintenance_ele);
        }

        public void bindData(String serviceName) {
            txt.setText(serviceName);
        }
    }


    private class SubServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA.equals(action)) {
                if (intent.getExtras() != null && intent.getExtras().containsKey(maintService.getId())) {
                    adapter.notifyDataSetChanged();
                }
            }
            if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR.equals(action)) {
                if (intent.getExtras() != null || !intent.getExtras().containsKey(maintService.getId())) {
                    // TODO
                }
            }
        }
    }

}
