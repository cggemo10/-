package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rrja.carja.model.CarSeries;

import java.util.List;


public class CarSeriesAdapter extends RecyclerView.Adapter {

    private List<CarSeries> data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (data == null || data.size() == 0) {
            return 0;
        } else {
            return data.size();
        }
    }

    public void setData(List<CarSeries> data) {
        this.data = data;
    }
}
