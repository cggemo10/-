package com.jayangche.android.fragment.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayangche.android.R;
import com.jayangche.android.activity.MainActivity;


public class BigDiscountFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnBigDiscountInteractionListener mListener;
    private static BigDiscountFragment fragment = new BigDiscountFragment();

    public static BigDiscountFragment getFragment() {
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public BigDiscountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_big_discount, container, false);
        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity)activity).getBigDiscountInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    
    public interface OnBigDiscountInteractionListener {
        
        public void onFragmentInteraction(Uri uri);
    }

}
