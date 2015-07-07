package com.rrja.carja.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rrja.carja.R;
import com.rrja.carja.activity.CouponsDetalActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.adapter.CouponsAdapter;
import com.rrja.carja.core.CoreManager;


public class CouponsFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView couponGoodList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnBigDiscountInteractionListener mListener;
    private static CouponsFragment fragment = new CouponsFragment();

    public static CouponsFragment getFragment() {
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public CouponsFragment() {

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
        View root = inflater.inflate(R.layout.fragment_coupons, container, false);

        couponGoodList = (ListView) root.findViewById(R.id.list_discount);
        couponGoodList.setAdapter(new CouponsAdapter(getActivity()));
        couponGoodList.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CouponsDetalActivity.class);
        intent.putExtra("coupons", CoreManager.getManager().getCoupons().get(position));
        startActivity(intent);
    }


    public interface OnBigDiscountInteractionListener {
        
        public void onFragmentInteraction(Uri uri);
    }

}
