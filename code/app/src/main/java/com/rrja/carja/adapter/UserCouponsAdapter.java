package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.coupons.UserCoupons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCouponsAdapter extends RecyclerView.Adapter {

    private int couponsType;

    public UserCouponsAdapter(int type) {
        this.couponsType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_coupons,null);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        List<UserCoupons> couponses = CoreManager.getManager().getUserCouponsByStatus(couponsType + "");
        UserCoupons coupons = couponses.get(position);
        VH vh = (VH) holder;
        vh.bindData(coupons);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        List<UserCoupons> couponsList = CoreManager.getManager().getUserCouponsByStatus(couponsType+"");
        if (couponsList == null || couponsList.size() == 0) {
            return 0;
        } else return couponsList.size();
    }

    private class VH extends RecyclerView.ViewHolder {

        private TextView txtGetTime;
        private TextView txtDate;
        private TextView txtGoodsName;
        private TextView txtCouponsPrice;

        public VH(View itemView) {
            super(itemView);

            txtGoodsName = (TextView) itemView.findViewById(R.id.txt_uc_title);
            txtCouponsPrice = (TextView) itemView.findViewById(R.id.txt_uscop_price);
            txtGetTime = (TextView) itemView.findViewById(R.id.txt_uscop_time);
            txtDate = (TextView) itemView.findViewById(R.id.txt_uscop_date);
        }

        public void bindData(UserCoupons coupons) {
            txtGoodsName.setText(coupons.getGoodsName());
            txtCouponsPrice.setText(coupons.getCouponsPrice() + "元");

            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分");
            long getTime = coupons.getGetTime();
            if (getTime > 0) {
                txtGetTime.setText(df.format(new Date(getTime)));
            } else {
                txtGetTime.setText("");
            }

            long start = coupons.getStartTime();
            long end = coupons.getEndTime();
            if (start > 0 && end > 0) {
                String startDate = df.format(new Date(start));
                String endDate = df.format(new Date(end));

                txtDate.setText(startDate + " 至 " + endDate);
            }

        }
    }
}
