package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.model.CarSeries;

import java.util.Collection;
import java.util.List;


public class CarSeriesAdapter extends RecyclerView.Adapter {

    private List<CarSeries> data;
    private OnSeriesItemClickListener itemClickListener;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_series, parent, false);
        SeriesHolder holder = new SeriesHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SeriesHolder seriesHolder = (SeriesHolder) holder;
        final CarSeries carSeries = data.get(position);
        seriesHolder.bindData(carSeries);
        seriesHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(carSeries);
                }
            }
        });

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

    public void setItemClickListener(OnSeriesItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public List<CarSeries> getSeriesData() {
        return data;
    }

    private class SeriesHolder extends RecyclerView.ViewHolder {

        private TextView txtSeriesName;

        public SeriesHolder(View itemView) {
            super(itemView);

            txtSeriesName = (TextView) itemView.findViewById(R.id.txt_item_series);
        }

        public void bindData(CarSeries carSeries) {
            txtSeriesName.setText(carSeries.getSeriesName());
        }
    }

    public interface OnSeriesItemClickListener {
        public void onItemClick(CarSeries carSeries);
    }

}
