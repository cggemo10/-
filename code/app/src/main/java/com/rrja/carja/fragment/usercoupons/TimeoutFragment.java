package com.rrja.carja.fragment.usercoupons;

import android.app.Activity;
import android.net.Uri;
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


public class TimeoutFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserCouponsAdapter adapter;

    private OnTimeoutActionListener mListener;
    private static TimeoutFragment instance;

    public static TimeoutFragment newInstance() {

        if (instance == null) {
            instance = new TimeoutFragment();
        }
        return instance;
    }

    public TimeoutFragment() {
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
        adapter = new UserCouponsAdapter(Constant.TITLE_TAG_TIMEOUT);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((UserCouponsActivity) activity).getTimeoutListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTimeoutActionListener {

        public void onCouponsClicked(UserCoupons coupons);
    }

}
