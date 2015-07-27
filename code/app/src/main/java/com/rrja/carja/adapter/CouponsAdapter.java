package com.rrja.carja.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.CouponsDetalActivity;
import com.rrja.carja.activity.DiscountActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;
import com.rrja.carja.service.FileService;

import java.io.File;
import java.io.IOException;
import java.util.zip.Inflater;


public class CouponsAdapter extends RecyclerView.Adapter {

    private static final String TAG = CouponsAdapter.class.getName();

    Context mContext;

    public CouponsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupons, parent, false);
        CouponsHolder holder = new CouponsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Coupons coupon = CoreManager.getManager().getCoupons().get(position);

        CouponsHolder couponsHolder = (CouponsHolder) holder;

        couponsHolder.title.setText(coupon.getName());
        couponsHolder.couponsScope.setText(coupon.getAddress());
        couponsHolder.couponsTime.setText(coupon.getTime());
        couponsHolder.couponsContent.setText(coupon.getContent());
        // TODO later
        String picUrl = coupon.getPicUrl();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getCouponsCacheDir(), fileName);
            if (img.exists()) {
                try {
                    couponsHolder.pic.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    try {
                        couponsHolder.pic.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("juyouhui-img.jpg")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {
                Intent intent = new Intent(mContext, FileService.class);
                intent.setAction(FileService.ACTION_IMG_COUPONS);
                intent.putExtra("coupons", coupon);
                mContext.startService(intent);
            }
        }

        holder.itemView.setOnClickListener(new CouponsClickListener(mContext, coupon));

        // for example
        try {
            couponsHolder.pic.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("juyouhui-img.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return   CoreManager.getManager().getCoupons().size();
    }


    private class CouponsHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView couponsScope;
        TextView couponsTime;
        TextView couponsContent;
        ImageView pic;

        public CouponsHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_item_coupons_title);
            couponsScope = (TextView) itemView.findViewById(R.id.txt_item_coupons_scope_content);
            couponsTime = (TextView) itemView.findViewById(R.id.txt_item_coupons_time_content);
            couponsContent = (TextView) itemView.findViewById(R.id.txt_item_coupons_detial_content);
            pic = (ImageView) itemView.findViewById(R.id.img_item_coupons);
        }
    }

    private class CouponsClickListener implements View.OnClickListener {

        Coupons mInfo;

        CouponsClickListener(Context context, Coupons info) {
            this.mInfo = info;
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CouponsDetalActivity.class);
            intent.putExtra("coupons", mInfo);
            mContext.startActivity(intent);
        }
    }
}
