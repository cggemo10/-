package com.rrja.carja.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarStore;

import java.io.File;
import java.io.IOException;

public class StoreReservationAdapter extends RecyclerView.Adapter {

    private String TAG = "rrja.StoreReservationAdapter";
    private StoreActionListener mActionListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreViewHolder vh = (StoreViewHolder) holder;

        CarStore store = CoreManager.getManager().getStores().get(position);
        vh.bindData(store);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return CoreManager.getManager().getStores().size();
    }


    private class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storePic;
        TextView storeName;
        TextView storeAddress;
        TextView storeDistance;

        public StoreViewHolder(View itemView) {
            super(itemView);

            storePic = (ImageView) itemView.findViewById(R.id.img_store_pic);
            storeName = (TextView) itemView.findViewById(R.id.txt_store_name);
            storeAddress = (TextView) itemView.findViewById(R.id.txt_store_address);
            storeDistance = (TextView) itemView.findViewById(R.id.txt_store_distance);
        }

        public void bindData(CarStore store) {

            storeName.setText(store.getStoreName());
            storeAddress.setText(store.getAddress());
            storeDistance.setText(store.cacluteDistance());

            String picUrl = store.getStoreImg();
            if (!TextUtils.isEmpty(picUrl)) {

                String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

                File img = new File(Constant.getRecommendCacheDir(), fileName);
                if (img.exists()) {
                    try {
                        storePic.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                    if (mActionListener != null) {
                        mActionListener.onRequestStorePic(store);
                    }
                }
            }
        }
    }

    public StoreActionListener getActionListener() {
        return mActionListener;
    }

    public void setActionListener(StoreActionListener mActionListener) {
        this.mActionListener = mActionListener;
    }

    public interface StoreActionListener {

        void onRequestStorePic(CarStore carStore);
    }
}
