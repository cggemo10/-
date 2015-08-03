package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.model.CarModel;

import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;


public class CarModelAdapter extends RecyclerView.Adapter{

    private List<CarModel> modelData;

    private OnModelItemClickedListener mItemListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_model, parent, false);
        CarModelHolder modelHolder = new CarModelHolder(view);
        return modelHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CarModelHolder modelHolder = (CarModelHolder) holder;
        final CarModel model = modelData.get(position);;
        modelHolder.bindData(model);

        modelHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.onModelClicked(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if (modelData == null || modelData.size() == 0) {
            return 0;
        } else {
            return modelData.size();
        }
    }

    public void setOnItemListener(OnModelItemClickedListener listener) {
        this.mItemListener = listener;
    }

    public void setModelData(List<CarModel> models) {
        this.modelData = models;
    }

    private class CarModelHolder extends RecyclerView.ViewHolder {

        private TextView itemText;

        public CarModelHolder(View itemView) {
            super(itemView);

            itemText = (TextView) itemView.findViewById(R.id.txt_item_model);

        }

        public void bindData(CarModel model) {
            itemText.setText(model.getSeriesName());
        }
    }

    public interface OnModelItemClickedListener {
        public void onModelClicked(CarModel model);
    }
}
