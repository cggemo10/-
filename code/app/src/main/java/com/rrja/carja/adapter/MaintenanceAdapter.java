package com.rrja.carja.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.maintenance.TagableService;
import com.rrja.carja.model.maintenance.TagableSubService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MaintenanceAdapter extends RecyclerView.Adapter {

    private static final int TAG_HEADER = 10;
    private static final int TAG_ELEM = 11;
    private MaintenanceOrder orderInfo;
    private List<TagableService> serviceList;

    private MaintenanceListener mListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TAG_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_choise, null);
            return new CarVh(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_service, null);
            return new OrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TAG_HEADER) {
            CarVh vh = (CarVh) holder;
            if (orderInfo == null) {
                vh.bindData(null);
            } else {
                vh.bindData(orderInfo.getmCarInfo());
            }
        } else {

        }
    }

    @Override
    public int getItemCount() {
        if (orderInfo != null && serviceList != null) {
            return serviceList.size() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TAG_HEADER;
        } else {
            return TAG_ELEM;
        }
    }

    public void setOrder(MaintenanceOrder order) {
        this.orderInfo = order;
        this.serviceList = orderInfo.listOrderInfo();
        notifyDataSetChanged();
    }

    public void setListener(MaintenanceListener listener) {
        this.mListener = listener;
    }

    private class OrderVH extends RecyclerView.ViewHolder {

        TextView serviceName;
        LinearLayout orderContent;
        TextView serviceFee;
        TextView totalFee;

        public OrderVH(View itemView) {
            super(itemView);

            serviceName = (TextView) itemView.findViewById(R.id.txt_order_service_name);
            orderContent = (LinearLayout) itemView.findViewById(R.id.ll_order_content);
            serviceFee = (TextView) itemView.findViewById(R.id.txt_order_service_fee);
            totalFee = (TextView) itemView.findViewById(R.id.txt_order_total_fee);
        }

        public void bindData(TagableService service) {
            serviceName.setText(service.getService().getName());

            LayoutInflater inflate = LayoutInflater.from(itemView.getContext());

            orderContent.removeAllViews();
            ArrayList<TagableSubService> subServiceList = service.getSubServiceList();
            for (TagableSubService subService: subServiceList) {
                View view = inflate.inflate(R.layout.item_ordersubitem, null);
                TextView serviceName = (TextView) view.findViewById(R.id.txt_order_sub_name);
                serviceName.setText(subService.getServiceName());
                TextView goodsName = (TextView) view.findViewById(R.id.txt_order_goods_name);
                goodsName.setText(subService.getGoods().getName());
                TextView goodsFee = (TextView) view.findViewById(R.id.txt_order_goods_fee);
                goodsFee.setText(subService.getGoods().getPrice());
            }
            serviceFee.setText("��" + service.getService().getAmount());
            totalFee.setText("��" + service.calculateServiceFee());
        }
    }

    private class CarVh extends RecyclerView.ViewHolder {

        private ImageView imgLogo;
        private TextView txtCarPlat;
        private TextView txtDetal;

        public CarVh(View itemView) {
            super(itemView);

            imgLogo = (ImageView) itemView.findViewById(R.id.img_car_ic);
            txtCarPlat = (TextView) itemView.findViewById(R.id.txt_car_platnm);
            txtDetal = (TextView) itemView.findViewById(R.id.txt_car_detial);
        }

        public void bindData(CarInfo carInfo) {
            if (carInfo == null) {
                return;
            } else {
                String picUrl = carInfo.getCarImg();
                try {
                    if (!TextUtils.isEmpty(picUrl)) {

                        String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

                        File img = new File(Constant.getCarImageCacheDir(), fileName);
                        if (img.exists()) {
                            imgLogo.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                        } else if (mListener != null) {
                            mListener.onRequestCarLogo(carInfo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                txtCarPlat.setText(carInfo.getPlatNum());
                txtDetal.setText(carInfo.getSeriesName() + " " + carInfo.getModelName());
            }
        }
    }

    public interface MaintenanceListener {
        void onRequestCarLogo(CarInfo carInfo);
        void onCarClicked();
        void onMaintenanceServiceClicked(TagableService service, int position);
        void onMaintenanceServiceDelete(TagableService service, int position);
    }
}
