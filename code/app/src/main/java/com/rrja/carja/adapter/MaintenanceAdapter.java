package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MaintenanceAdapter extends RecyclerView.Adapter {

    private static final int TAG_FOOTER = 10;
    private static final int TAG_ELEM = 11;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == )
        return super.getItemViewType(position);
    }
}
