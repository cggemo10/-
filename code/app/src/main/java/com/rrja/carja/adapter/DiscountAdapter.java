package com.rrja.carja.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.activity.OnDoreWashActivity;
import com.rrja.carja.activity.StoreReservationActivity;
import com.rrja.carja.activity.ViolationActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.service.FileService;

import java.io.File;
import java.io.IOException;

public class DiscountAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final String TAG = DiscountAdapter.class.getName();

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_ITEM = 12;

    private Context mContext;

    private OnDiscountItemClickListener itemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fg_home_header, parent, false);
            DiscountHeaderHolder headerHolder = new DiscountHeaderHolder(headerView);
            return headerHolder;
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount, parent, false);
            DiscountHolder discountHolder = new DiscountHolder(itemView);
            return discountHolder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            bindHeaderHolder((DiscountHeaderHolder) holder);
        } else {
            bindItemHolder((DiscountHolder) holder, position);
        }


    }

    private void bindHeaderHolder(DiscountHeaderHolder holder) {
        holder.mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return CoreManager.getManager().getCompanyInfo().size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(CoreManager.getManager().getCompanyInfo().get(position));
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView ad = CoreManager.getManager().getCompanyInfo().get(position);
                container.addView(ad);
                return ad;
            }
        });

        holder.llHomeMaintenance.setOnClickListener(this);
        holder.llOnDoreWash.setOnClickListener(this);
        holder.llStoreReservation.setOnClickListener(this);
        holder.llViolation.setOnClickListener(this);
    }

    private void bindItemHolder(DiscountHolder holder, int position) {
        final RecommendGoods discount = CoreManager.getManager().getDiscounts().get(position - 1);

        DiscountHolder discountHolder = holder;
        discountHolder.title.setText(discount.getName());
        discountHolder.discountScope.setText(discount.getScope());
        discountHolder.discountTime.setText(discount.getTime());
        discountHolder.discountContent.setText(discount.getContent());

        // TODO later
        String picUrl = discount.getImgUrl();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getRecommendCacheDir(), fileName);
            if (img.exists()) {
                try {
                    discountHolder.pic.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    try {
                        discountHolder.pic.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("juyouhui-img.jpg")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {
                Intent intent = new Intent(mContext, FileService.class);
                intent.setAction(FileService.ACTION_IMG_DISCOUNT);
                intent.putExtra("discount_info", discount);
                mContext.startService(intent);
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(discount);
                }
            }
        });

        try {
            discountHolder.pic.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("juyouhui-img.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return CoreManager.getManager().getDiscounts().size() + 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home_maintenance:
                Intent intent = new Intent(mContext, HomeMaintenanceActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.ll_on_dore_wash:
                Intent onDoreWash = new Intent(mContext, OnDoreWashActivity.class);
                mContext.startActivity(onDoreWash);
                break;
            case R.id.ll_store_reservation:
                Intent maintenance = new Intent(mContext, StoreReservationActivity.class);
                mContext.startActivity(maintenance);
                break;
            case R.id.ll_violation:
                Intent violation = new Intent(mContext, ViolationActivity.class);
                mContext.startActivity(violation);
                break;
        }
    }


    private class DiscountHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView discountScope;
        TextView discountTime;
        TextView discountContent;
        ImageView pic;

        public DiscountHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_item_discount_title);
            discountScope = (TextView) itemView.findViewById(R.id.txt_item_discount_scope_content);
            discountTime = (TextView) itemView.findViewById(R.id.txt_item_discount_time_content);
            discountContent = (TextView) itemView.findViewById(R.id.txt_item_discount_detial_content);
            pic = (ImageView) itemView.findViewById(R.id.img_item_discount);
        }
    }

    public void setItemClickListener(OnDiscountItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class DiscountHeaderHolder extends RecyclerView.ViewHolder {

        ViewPager mPager;
        LinearLayout llHomeMaintenance;
        LinearLayout llOnDoreWash;
        LinearLayout llStoreReservation;
        LinearLayout llViolation;

        public DiscountHeaderHolder(View itemView) {
            super(itemView);
            mPager = (ViewPager) itemView.findViewById(R.id.pager_main_ad);
            llHomeMaintenance = (LinearLayout) itemView.findViewById(R.id.ll_home_maintenance);
            llOnDoreWash = (LinearLayout) itemView.findViewById(R.id.ll_on_dore_wash);
            llStoreReservation = (LinearLayout) itemView.findViewById(R.id.ll_store_reservation);
            llViolation = (LinearLayout) itemView.findViewById(R.id.ll_violation);
        }
    }

    private class DiscountClickListener  {

        RecommendGoods mInfo;

        DiscountClickListener(Context context, RecommendGoods info) {
            this.mInfo = info;
        }

    }

    public interface OnDiscountItemClickListener {
        public void onItemClick(RecommendGoods info);
    }
}
