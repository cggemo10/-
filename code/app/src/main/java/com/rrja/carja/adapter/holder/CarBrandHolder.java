package com.rrja.carja.adapter.holder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.model.CarBrand;

/**
 * Created by Administrator on 2015/7/21.
 */
public class CarBrandHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "rrja.CarBrandHolder";

    private ImageView mImgView;
    private TextView mTextView;

    public CarBrandHolder(View itemView) {
        super(itemView);

        mImgView = (ImageView) itemView.findViewById(R.id.img_car_brand_ic);
        mTextView = (TextView) itemView.findViewById(R.id.txt_car_brand_name);
    }

    public void bindItem(CarBrand brand) {
        mTextView.setText(brand.getName());
        try {
            Bitmap b = BitmapFactory.decodeFile(brand.getLogo());
            mImgView.setImageBitmap(b);
        } catch (Exception e) {
            // TODO set default img
            Log.e(TAG, e.getCause().toString() + e.getMessage());
        }
    }
}
