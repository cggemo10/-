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
import com.rrja.carja.activity.RecommendDetialctivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.adapter.DiscountAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.utils.DialogHelper;


public class HomeFragment extends Fragment implements DiscountAdapter.OnDiscountItemClickListener{

    private OnHomeInteractionListener mListener;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private DiscountAdapter discountAdapter;
    private DiscountDataReceiver mReceiver;


    public static HomeFragment getFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        discountAdapter = new DiscountAdapter();
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
        discountAdapter.setItemClickListener(this);
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

        registBroadcast();

        if (CoreManager.getManager().getDiscounts() == null || CoreManager.getManager().getDiscounts().size() == 0) {
            DialogHelper.getHelper().showWaitting();
            mListener.requestRecommendData(1);
        }
    }



    @Override
    public void onStop() {
        unRegistBoradcast();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(RecommendGoods info) {
        Intent intent = new Intent(getActivity(), RecommendDetialctivity.class);
        intent.putExtra("recommend_info", info);
        getActivity().startActivity(intent);
    }


    public interface OnHomeInteractionListener {

        public void requestRecommendData(int page);
    }

    private class DiscountDataReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            DialogHelper.getHelper().dismissWatting();
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA.equals(action)) {
                discountAdapter.notifyDataSetChanged();
            }

            if (Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR.equals(action)) {
                Toast.makeText(context,getString(R.string.str_err_net),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registBroadcast() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR);

            mReceiver = new DiscountDataReceiver();
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
