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
import com.rrja.carja.activity.CouponsDetalActivity;
import com.rrja.carja.activity.LoginActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.adapter.CouponsAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.service.FileService;


public class CouponsFragment extends Fragment implements CouponsAdapter.OnItemClickListener{

    RecyclerView couponGoodList;
    RecyclerView.LayoutManager mLayoutManager;
    private CouponsAdapter couponsAdapter;
    private CouponsDataReceiver mReceiver;

    private OnBigDiscountInteractionListener mListener;
    private static CouponsFragment fragment = new CouponsFragment();

    public static CouponsFragment getFragment() {
        return fragment;
    }

    public CouponsFragment() {
        couponsAdapter = new CouponsAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coupons, container, false);

        couponGoodList = (RecyclerView) root.findViewById(R.id.list_discount);
        mLayoutManager = new LinearLayoutManager(getActivity());
        couponGoodList.setLayoutManager(mLayoutManager);
        couponsAdapter.setItemClickListener(this);
        couponGoodList.setAdapter(couponsAdapter);

        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity)activity).getBigDiscountInteraction();
        if (CoreManager.getManager().getCoupons() == null || CoreManager.getManager().getCoupons().size() == 0) {
            mListener.requestCouponsData(1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        registBroadcast();

        if (CoreManager.getManager().getCoupons() == null || CoreManager.getManager().getCoupons().size() == 0) {
//            DialogHelper.getHelper().showWaitting();
            mListener.requestCouponsData(1);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        unRegistBoradcast();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCouponsGoodsClick(CouponGoods goods) {
        if (CoreManager.getManager().getCurrUser() != null) {
            Intent intent = new Intent(getActivity(), CouponsDetalActivity.class);
            intent.putExtra("coupons", goods);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestCouponsImg(CouponGoods goods) {
        Intent intent = new Intent(getActivity(), FileService.class);
        intent.setAction(FileService.ACTION_IMG_COUPONS);
        intent.putExtra("coupons_goods", goods);
        getActivity().startService(intent);
    }


    public interface OnBigDiscountInteractionListener {
        
        public void requestCouponsData(int page);
    }

    private class CouponsDataReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_COUPONS_DATA.equals(action)) {
                couponsAdapter.notifyDataSetChanged();
            }

            if (Constant.ACTION_BROADCAST_GET_COUPONS_DATA_ERR.equals(action)) {
                Toast.makeText(context, "coupons data error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registBroadcast() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_COUPONS_DATA);
            filter.addAction(Constant.ACTION_BROADCAST_GET_COUPONS_DATA_ERR);

            mReceiver = new CouponsDataReceiver();
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
