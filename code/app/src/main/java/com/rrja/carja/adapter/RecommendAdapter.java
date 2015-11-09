package com.rrja.carja.adapter;

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
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.coupons.CouponGoods;
import com.rrja.carja.model.coupons.RecommendGoods;

import java.io.File;
import java.text.SimpleDateFormat;

public class RecommendAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final String TAG = RecommendAdapter.class.getName();

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_ITEM = 12;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    private OnRecommendActionListener mActionListener;

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
        final RecommendGoods goods = CoreManager.getManager().getDiscounts().get(position - 1);

        DiscountHolder discountHolder = holder;
        discountHolder.title.setText(goods.getName());
        discountHolder.discountScope.setText(goods.getScope());

        long startTime = goods.getStartTime();
        long endTime = goods.getEndTime();
        String time = dateFormat.format(startTime) + " 至\n " + dateFormat.format(endTime);

        discountHolder.discountTime.setText(time);
        discountHolder.discountContent.setText(goods.getContent());

        // TODO later
        String picUrl = goods.getImgUrl();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getRecommendCacheDir(), fileName);
            if (img.exists()) {
                try {
                    discountHolder.pic.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {
                if (mActionListener != null) {
                    mActionListener.onRequestRecommendPic(goods);
                }
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onItemClick(goods);
                }
            }
        });
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
             if (mActionListener != null) {
                 switch (v.getId()){
                     case R.id.ll_home_maintenance:
                         mActionListener.onHomeMaintenanceClick();
                         break;
                     case R.id.ll_on_dore_wash:
                         mActionListener.onOnDoreWashClick();
                         break;
                     case R.id.ll_store_reservation:
                         mActionListener.onStoreReservationClick();
                         break;
                     case R.id.ll_violation:
                         mActionListener.onViolationClicked();
                         break;
                 }
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
                      discountScope = (TextView) itemView.findViewById(R.id.txt_item_coupons_scope_content);
                      discountTime = (TextView) itemView.findViewById(R.id.txt_item_discount_time_content);
                      discountContent = (TextView) itemView.findViewById(R.id.txt_item_discount_detial_content);
                      pic = (ImageView) itemView.findViewById(R.id.img_item_discount);
                  }
              }

    public void setRecommendActionListener(OnRecommendActionListener listener) {
        this.mActionListener = listener;
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

    public interface OnRecommendActionListener {
        public void onItemClick(RecommendGoods info);
        public void onHomeMaintenanceClick();
        public void onOnDoreWashClick();
        public void onStoreReservationClick();
        public void onViolationClicked();
        public void onRequestRecommendPic(RecommendGoods recommendGoods);
    }
}
