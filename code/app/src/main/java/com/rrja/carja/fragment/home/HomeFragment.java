package com.rrja.carja.fragment.home;

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
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.activity.LoginActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.activity.OnDoreWashActivity;
import com.rrja.carja.activity.RecommendDetialActivity;
import com.rrja.carja.activity.StoreReservationActivity;
import com.rrja.carja.activity.ViolationActivity;
import com.rrja.carja.adapter.RecommendAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.service.FileService;


public class HomeFragment extends Fragment implements RecommendAdapter.OnRecommendActionListener {

    private OnHomeInteractionListener mListener;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private RecommendAdapter discountAdapter;
    private RecommendDataReceiver mReceiver;


    public static HomeFragment getFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        discountAdapter = new RecommendAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.pull_refresh_grid_dicsount);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        discountAdapter.setRecommendActionListener(this);
        recyclerView.setAdapter(discountAdapter);

        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = ((MainActivity) activity).getHomeInteraction();
        if (CoreManager.getManager().getDiscounts() == null || CoreManager.getManager().getDiscounts().size() == 0) {
            mListener.requestRecommendData(1);
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        registBroadcast();

        if (CoreManager.getManager().getDiscounts() == null || CoreManager.getManager().getDiscounts().size() == 0) {
            mListener.requestRecommendData(1);
        }
    }


    @Override
    public void onStop() {
        unRegistBoradcast();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(RecommendGoods info) {
        Intent intent = new Intent(getActivity(), RecommendDetialActivity.class);
        intent.putExtra("recommend_info", info);
        getActivity().startActivity(intent);
    }

    @Override
    public void onHomeMaintenanceClick() {
        if (CoreManager.getManager().getCurrUser() != null) {
            Intent intent = new Intent(getActivity(), HomeMaintenanceActivity.class);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setAction(Constant.ACTION_LOGIN_AFTER_HOMEMAINTENANCE);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onOnDoreWashClick() {
        if (CoreManager.getManager().getCurrUser() != null) {
            Intent onDoreWash = new Intent(getActivity(), OnDoreWashActivity.class);
            getActivity().startActivity(onDoreWash);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setAction(Constant.ACTION_LOGIN_AFTER_ONDOREWASH);
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void onStoreReservationClick() {
        Intent maintenance = new Intent(getActivity(), StoreReservationActivity.class);
        getActivity().startActivity(maintenance);
    }

    @Override
    public void onViolationClicked() {
        if (CoreManager.getManager().getCurrUser() != null) {
            Intent violation = new Intent(getActivity(), ViolationActivity.class);
            getActivity().startActivity(violation);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setAction(Constant.ACTION_LOGIN_AFTER_VIOLATION);
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void onRequestRecommendPic(RecommendGoods recommendGoods) {
        Intent intent = new Intent(getActivity(), FileService.class);
        intent.setAction(FileService.ACTION_IMG_DISCOUNT);
        intent.putExtra("recommend_info", recommendGoods);
        getActivity().startService(intent);
    }


    public interface OnHomeInteractionListener {

        public void requestRecommendData(int page);
    }

    private class RecommendDataReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA.equals(action)) {
                discountAdapter.notifyDataSetChanged();
            }

            if (Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR.equals(action)) {
                String errMsg = "";
                if (intent.hasExtra("description")) {
                    errMsg = intent.getStringExtra("description");
                } else {
                    errMsg = context.getString(R.string.str_err_net);
                }
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT.equals(action)) {
                discountAdapter.notifyDataSetChanged();
            }
        }
    }

    private void registBroadcast() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR);
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_DISCOUNT);

            mReceiver = new RecommendDataReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }

    }

    private void unRegistBoradcast() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
