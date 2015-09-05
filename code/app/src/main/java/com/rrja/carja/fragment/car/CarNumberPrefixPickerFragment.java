package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.CarInfoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;


public class CarNumberPrefixPickerFragment extends Fragment implements View.OnClickListener {

    public static final int FLAG_PREFIX_1 = 10;
    public static final int FLAG_PREFIX_2 = 20;

    private int flag;

    private int prefix1Index;
    private int prefix2Index;

    private String prefix1;
    private String prefix2;

    private OnPrefixFragmentInteractionListener mListener;

    private TextView txtPrefix1;
    private TextView txtPrefix2;
    private RecyclerView recyclerView;
    private Handler mHandler;

    PrefixAdapter prefixAdapter;
    String[] prefix1Array;
    String[] prefix2Array = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static CarNumberPrefixPickerFragment newInstance(String prefix1_, String prefix2_) {
        CarNumberPrefixPickerFragment fragment = new CarNumberPrefixPickerFragment();
        fragment.setPrefix1(prefix1_);
        fragment.setPrefix2(prefix2_);
        return fragment;
    }

    public CarNumberPrefixPickerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefix1Array = getResources().getStringArray(R.array.strs_car_prefix_1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_num_prefix_picker, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {

        txtPrefix1 = (TextView) v.findViewById(R.id.txt_car_prefix_1);
        txtPrefix1.setText(prefix1);

        txtPrefix1.setOnClickListener(this);
        txtPrefix2 = (TextView) v.findViewById(R.id.txt_car_prefix_2);
        txtPrefix2.setText(prefix2);

        txtPrefix2.setOnClickListener(this);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_car_prefix_picker);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        prefixAdapter = new PrefixAdapter();
        prefixAdapter.setData(prefix1Array);
        this.flag = FLAG_PREFIX_1;
        recyclerView.setAdapter(prefixAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = 0; i< prefix1Array.length; i++) {
            if (prefix1Array[i].startsWith(prefix1)) {
                prefix1Index = i;
            }
        }
        for (int i = 0; i< prefix2Array.length; i++) {
            if (prefix2Array[i].equals(prefix2)) {
                prefix2Index = i;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((CarInfoActivity)activity).getPrefixFragmentInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_car_prefix_1:
                if (flag == FLAG_PREFIX_2) {
                    flag = FLAG_PREFIX_1;
                    prefixAdapter.setData(prefix1Array);
                }
                break;
            case R.id.txt_car_prefix_2:
                if (flag == FLAG_PREFIX_1) {
                    flag = FLAG_PREFIX_2;
                    prefixAdapter.setData(prefix2Array);
                }
                break;
        }
    }

    public void setPrefix1(String prefix1) {
        this.prefix1 = prefix1;
    }

    public void setPrefix2(String prefix2) {
        this.prefix2 = prefix2;
    }

    public interface OnPrefixFragmentInteractionListener {
        public void onPrefix1Changed(String prefix1);
        public void onPrefix2Changed(String prefix2);
    }

    // ---------------------------------------------------------------------------------------------
    class PrefixAdapter extends RecyclerView.Adapter {



        List<String> data = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_prefix, parent, false);
            return new PrefixHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            String prefixData = data.get(position);
            final PrefixHolder prefixHolder = (PrefixHolder) holder;
            prefixHolder.bindData(prefixData);
            if (flag == FLAG_PREFIX_1) {
                if (!TextUtils.isEmpty(prefixData)) {
                    String prefixShower = prefixData.substring(0, prefixData.indexOf("("));
                    if (!TextUtils.isEmpty(prefixShower) && prefix1.startsWith(prefixShower)) {
                        prefixHolder.setSelected(true);
                    } else {
                        prefixHolder.setSelected(false);
                    }
                } else {
                    prefixHolder.setSelected(false);
                }

                prefixHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefix1 = data.get(position);
                        String prefixShow = prefix1.substring(0, prefix1.indexOf("("));
                        txtPrefix1.setText(prefixShow);
                        if (mListener != null) {
                            mListener.onPrefix1Changed(prefix1);
                        }

                        PrefixAdapter.this.notifyItemChanged(position);
                        PrefixAdapter.this.notifyItemChanged(prefix1Index);
                        prefix1Index = position;
                    }
                });
            } else {
                if (!TextUtils.isEmpty(prefix2) && !TextUtils.isEmpty(prefixData) && prefixData.contains(prefix2)) {
                    prefixHolder.setSelected(true);
                } else {
                    prefixHolder.setSelected(false);
                }

                prefixHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefix2 = data.get(position);
                        txtPrefix2.setText(prefix2);
                        if (mListener != null) {
                            mListener.onPrefix2Changed(prefix2);
                        }
                        PrefixAdapter.this.notifyItemChanged(prefix2Index);
                        prefix2Index = position;
                        PrefixAdapter.this.notifyItemChanged(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(String[] dataSource) {
            if (dataSource != null && dataSource.length != 0) {
                data.clear();
                data.addAll(Arrays.asList(dataSource));
                notifyDataSetChanged();
            }
        }
    }

    private class PrefixHolder extends RecyclerView.ViewHolder {

        TextView txtItme;
        LinearLayout llBg;
        String prefixData;

        public PrefixHolder(View itemView) {
            super(itemView);

            this.txtItme = (TextView) itemView.findViewById(R.id.txt_item_prefix);
            llBg = (LinearLayout) itemView.findViewById(R.id.ll_item_prefix);
        }

        public void setSelected(boolean isSelected) {
            if (isSelected) {
                llBg.setBackgroundColor(itemView.getResources().getColor(R.color.c_style_red));
                txtItme.setTextColor(Color.WHITE);
            } else {
                llBg.setBackgroundColor(Color.WHITE);
                txtItme.setTextColor(itemView.getResources().getColor(R.color.c_style_red));
            }
        }

        public void bindData(String data) {
            if (!TextUtils.isEmpty(data)) {
                txtItme.setText(data);
            }
        }
    }
}
