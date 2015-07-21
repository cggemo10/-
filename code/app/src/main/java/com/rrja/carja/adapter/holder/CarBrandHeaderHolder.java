package com.rrja.carja.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.model.CarBrand;

/**
 * Created by Administrator on 2015/7/21.
 */
public class CarBrandHeaderHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;

    public CarBrandHeaderHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.txt_brand_header);
    }

    public void bindHeader(CarBrand brand) {
        mTextView.setText(brand.getFirstLetter()
        );
    }

    public String toString() {
        return mTextView.getText().toString();
    }
}
