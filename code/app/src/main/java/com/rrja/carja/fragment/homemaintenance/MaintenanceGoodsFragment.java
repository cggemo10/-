package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.maintenance.MaintenanceGoods;
import com.rrja.carja.model.maintenance.MaintenanceService;
import com.rrja.carja.model.maintenance.TagableSubService;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceGoodsFragment extends BaseElementFragment {


    private OnMaintenanceGoodsListener mListener;

    private MaintenanceService service;
    private MaintenanceService subService;
    private MaintenanceService feeService;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerGoods;
    private GoodsAdapter mAdapter;

    private GoodsReceiver mReceiver;
    private int currentPage;

    private List<MaintenanceGoods> goodList = new ArrayList<>();

    public static MaintenanceGoodsFragment newInstance() {
        MaintenanceGoodsFragment fragment = new MaintenanceGoodsFragment();
        return fragment;
    }

    public MaintenanceGoodsFragment() {
        // Required empty public constructor
        mAdapter = new GoodsAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maintenance_goods, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        recyclerGoods = (RecyclerView) v.findViewById(R.id.recycler_maintenance_good);
        recyclerGoods.setLayoutManager(new LinearLayoutManager(getActivity()));


        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_maintenance_good);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        recyclerGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

        });

        recyclerGoods.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerGoods.setLayoutManager(mLayoutManager);
        recyclerGoods.setItemAnimator(new DefaultItemAnimator());

        recyclerGoods.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((HomeMaintenanceActivity) activity).getMaintenGoodsListener();
        currentPage = 0;
        goodList.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        registReceiver();
        if (goodList.size() == 0) {
            if (CoreManager.getManager().getMaintenanceGoods(getServiceKey(1)).size() == 0) {
                mListener.requestGoods(subService.getId(), 1);
            } else {
                goodList.addAll(CoreManager.getManager().getMaintenanceGoods(getServiceKey(1)));
                currentPage = 1;
            }
        }
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR);

            mReceiver = new GoodsReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    private void unRegistReciever() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onStop() {
        unRegistReciever();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String getServiceKey(int page) {
        return subService.getId() + "_" + page;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    public interface OnMaintenanceGoodsListener {

        public void onGoodsCommit(MaintenanceService service, TagableSubService tagableSubService, TagableSubService feeService);

        public void requestGoods(String serviceId, int page);

        public void refreshGoods();
    }

    public void setService(MaintenanceService service) {
        this.service = service;
    }

    public void setSubService(MaintenanceService service) {
        this.subService = service;
    }

    public void setFeeService(MaintenanceService feeService) {
        this.feeService = feeService;
    }

    private class GoodsAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance_multi_checkable, parent, false);
            return new GoodsVH(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GoodsVH vh = (GoodsVH) holder;
            final MaintenanceGoods goods = goodList.get(position);
            vh.bindData(goods);
            vh.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mListener != null) {

                        TagableSubService commitSubService = new TagableSubService();
                        commitSubService.setSubService(subService);
                        commitSubService.setGoods(goods);

                        TagableSubService commitFeeService = new TagableSubService();
                        commitFeeService.setSubService(feeService);
                        mListener.onGoodsCommit(service, commitSubService, commitFeeService);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return goodList.size();
        }
    }

    private class GoodsVH extends RecyclerView.ViewHolder {

        TextView goods;

        public GoodsVH(View itemView) {
            super(itemView);

            goods = (TextView) itemView.findViewById(R.id.txt_item_maintenance_ele);
        }

        private void bindData(MaintenanceGoods modGoods) {
            goods.setText(modGoods.getName());
        }
    }

    private class GoodsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA.equals(action)) {
                List<MaintenanceGoods> requestGoods = CoreManager.getManager().getMaintenanceGoods(getServiceKey(currentPage + 1));
                if (requestGoods.size() != 0) {
                    goodList.addAll(requestGoods);
                    currentPage += 1;
                }
                mAdapter.notifyDataSetChanged();
            }

            if (Constant.ACTION_BROADCAST_MAINTENANCE_GOODS_DATA_ERR.equals(action)) {

            }
        }
    }

}
