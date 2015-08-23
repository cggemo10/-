package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
 import android.app.Fragment;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.TextView;

 import com.rrja.carja.R;
 import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
 import com.rrja.carja.model.maintenance.MaintenanceService;

public class MaintenanceSubServiceFragment extends Fragment {

     private SubServiceActionListener mListener;
     private MaintenanceService maintService;
     private SubServiceAdapter adapter;
     private RecyclerView recyclerSub;

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

    }

    private void unregistReceiver() {

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

     public interface SubServiceActionListener {
         public void onSubServiceClicked(MaintenanceService service);

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
                     if (mListener != null) {
                         mListener.onSubServiceClicked(maintenanceService);
                     }
                 }
             });
         }

         @Override
         public int getItemCount() {
             return CoreManager.getManager().getMaintenanceService(maintService.getId()).size();
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
                if (intent.getExtras() != null || !intent.getExtras().containsKey(maintService.getId()) )
            }
        }
    }

 }
