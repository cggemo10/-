package com.jayangche.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.core.CoreManager;
import com.jayangche.android.model.CarStore;

public class StoreReservationAdapter extends BaseAdapter {

    Context mContext;

    public StoreReservationAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return CoreManager.getManager().getStores().size();
    }

    @Override
    public Object getItem(int position) {
        return CoreManager.getManager().getStores().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_store, null);
            holder = new ViewHolder();
            holder.storePic = (ImageView) convertView.findViewById(R.id.img_store_pic);
            holder.storeName = (TextView) convertView.findViewById(R.id.txt_store_name);
            holder.storeAddress = (TextView) convertView.findViewById(R.id.txt_store_address);
            holder.storeDistance = (TextView) convertView.findViewById(R.id.txt_store_distance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarStore store = CoreManager.getManager().getStores().get(position);
        String storeImg = store.getStoreImg();
        if (!TextUtils.isEmpty(storeImg)) {
            if (storeImg.contains("http")) {
                // TODO download
            } else {
                // TODO
            }
        } else {
            // TODO
        }

        try{
            Bitmap pic = BitmapFactory.decodeStream(mContext.getAssets().open("mendian.jpg"));
            holder.storePic.setImageBitmap(pic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView storePic;
        TextView storeName;
        TextView storeAddress;
        TextView storeDistance;
    }
}
