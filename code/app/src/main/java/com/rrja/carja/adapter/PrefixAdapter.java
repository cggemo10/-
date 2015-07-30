package com.rrja.carja.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chongge on 15/7/29.
 */
public class PrefixAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    public static final int FLAG_PREFIX_1 = 10;
    public static final int FLAG_PREFIX_2 = 20;

    List<String> data = new ArrayList<>();
    private int flag;
    private String prefix1;
    private String prefix2;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_prefix, parent, false);
        view.setOnClickListener(this);
        return new PrefixHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String prefixData = data.get(position);
        PrefixHolder prefixHolder = (PrefixHolder) holder;
        prefixHolder.bindData(prefixData);
        if (flag == FLAG_PREFIX_1) {
            if (!TextUtils.isEmpty(prefix1) && !TextUtils.isEmpty(prefixData) && prefixData.contains(prefix1)) {
                prefixHolder.setSelected(true);
            } else {
                prefixHolder.setSelected(false);
            }
        } else {
            if (!TextUtils.isEmpty(prefix2) && !TextUtils.isEmpty(prefixData) && prefixData.contains(prefix2)) {
                prefixHolder.setSelected(true);
            } else {
                prefixHolder.setSelected(false);
            }
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(String[] dataSource, int flag) {
        if (dataSource != null && dataSource.length != 0) {
            data.clear();
            data.addAll(Arrays.asList(dataSource));
            this.flag = flag;
            notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_item_prefix) {
            PrefixHolder holder = new PrefixHolder(v);
            if (flag == FLAG_PREFIX_1) {
                this.prefix1 = holder.prefixData;
            } else if (flag == FLAG_PREFIX_2) {
                this.prefix2 = holder.prefixData;
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
//            itemView.setOnClickListener(this);
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

//        @Override
//        public void onClick(View v) {
//            String s = txtItme.getText().toString();
//            if (!TextUtils.isEmpty(s)) {
//                PrefixAdapter.this.selectedPrefix = s;
//                PrefixAdapter.this.notifyDataSetChanged();
//            }
//        }
    }
}
