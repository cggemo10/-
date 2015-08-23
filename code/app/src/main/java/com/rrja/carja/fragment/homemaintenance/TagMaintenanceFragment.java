package com.rrja.carja.fragment.homemaintenance;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class TagMaintenanceFragment extends Fragment {

    private String serviceTag = "101";

    private RecyclerView maintenanceRv;
    private TagAdapter tagAdapter;

    private TagServiceActionListener mListener;
    private TagServiceReceiver mReceiver;

    public TagMaintenanceFragment() {
    }

    public static TagMaintenanceFragment newInstance(String serviceTag) {
        TagMaintenanceFragment fragment = new TagMaintenanceFragment();
        fragment.setServiceTag(serviceTag);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_pagerv_maintenance, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tagAdapter = new TagAdapter();

        maintenanceRv = (RecyclerView) view.findViewById(R.id.recycler_item_pv_maintenance);
        maintenanceRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        maintenanceRv.setAdapter(tagAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = ((HomeMaintenanceActivity) activity).getTagServiceListener();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (CoreManager.getManager().getMaintenanceService(serviceTag).size() == 0) {
            mListener.requestService(serviceTag);
        }

        registReceiver();
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR);

            mReceiver = new TagServiceReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }


    @Override
         public void onStop() {
             unregistReceiver();
             super.onStop();
         }

    private void unregistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
         public void onDetach() {

             mListener = null;
             super.onDetach();
         }

    public String getServiceTag() {
        return this.serviceTag;
    }

    public void setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
    }

    private class TagAdapter extends RecyclerView.Adapter {

             @Override
             public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagerv_maintenance, null);

                 return new TagVH(view);
             }

             @Override
             public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                 final TagVH vh = (TagVH) holder;
                 final MaintenanceService service = CoreManager.getManager().getMaintenanceService(serviceTag).get(position);
                 vh.bindData(service.getName());

                 vh.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         // TODO lock UI
                         vh.textView.setBackgroundColor(getResources().getColor(R.color.c_style_red));
                         notifyItemChanged(position);
                         mListener.onServiceClicked(service);
                     }
                 });
             }

             @Override
             public int getItemCount() {
                 return CoreManager.getManager().getMaintenanceService(serviceTag).size();
             }
         }

    private class TagVH extends RecyclerView.ViewHolder {

             TextView textView;

             public TagVH(View itemView) {
                 super(itemView);

                 textView = (TextView) itemView.findViewById(R.id.txt_item_maintenance_ele);
             }

             public void bindData(String serviceName) {
                 textView.setText(serviceName);
             }
         }

    private class TagServiceReceiver extends BroadcastReceiver {

             @Override
             public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                 if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA.equals(action)) {
                     Bundle extras = intent.getExtras();
                     if (extras.containsKey("serviceId") && extras.getString("serviceId").equals(serviceTag)) {
                         tagAdapter.notifyDataSetChanged();
                     }
                 }
                 if (Constant.ACTION_BROADCAST_MAINTENANCE_SERVICE_DATA_ERR.equals(action)) {
                     // TODO
                 }
             }
         }
}
