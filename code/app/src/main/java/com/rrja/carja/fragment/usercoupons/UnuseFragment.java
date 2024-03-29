package com.rrja.carja.fragment.usercoupons;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.activity.UserCouponsActivity;
import com.rrja.carja.adapter.UserCouponsAdapter;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.coupons.UserCoupons;


public class UnuseFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserCouponsAdapter adapter;

    private OnUnuseActionListener mListener;

    private static UnuseFragment instance;

    public static UnuseFragment newInstance() {

        if (instance == null) {
            instance = new UnuseFragment();
        }

        return instance;
    }

    public UnuseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_coupons, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_coupons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserCouponsAdapter(Constant.TITLE_TAG_UNUSE);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = ((UserCouponsActivity) activity).getUnuseListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUnuseActionListener {

        public void onCouponsClicked(UserCoupons coupons);
    }

}
