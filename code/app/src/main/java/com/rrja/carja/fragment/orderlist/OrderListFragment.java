package com.rrja.carja.fragment.orderlist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.OrderListActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.myorder.OrderRecord;

public class OrderListFragment extends BaseElementFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RecyclerView orderList;
    private TextView txtUnpay;
    private TextView txtPayed;
    private TextView txtFinished;
    private TextView txtCancel;

    private TextView txtEmpty;

    private OrderListAdapter payListAdapter;

    private OnOrderListListener mListener;
    private OrderRecordReceiver receiver;

    private String type;

    private static OrderListFragment instance;

    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance() {
        if (instance == null) {
            instance = new OrderListFragment();
        }
        return instance;
    }

    public OrderListFragment() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        payListAdapter = new OrderListAdapter();
        orderList = (RecyclerView) view.findViewById(R.id.list_order);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderList.setAdapter(payListAdapter);
//        orderList.setOnItemClickListener(this);

        txtUnpay = (TextView) view.findViewById(R.id.txt_label_unpay);
        txtUnpay.setOnClickListener(this);
        txtPayed = (TextView) view.findViewById(R.id.txt_label_payed);
        txtPayed.setOnClickListener(this);
        txtFinished = (TextView) view.findViewById(R.id.txt_label_finish_order);
        txtFinished.setOnClickListener(this);
        txtCancel = (TextView) view.findViewById(R.id.txt_label_cancel_order);
        txtCancel.setOnClickListener(this);

        txtEmpty = (TextView) view.findViewById(R.id.txt_order_empty);
        txtEmpty.setOnClickListener(this);
        txtEmpty.setVisibility(View.GONE);
        if (CoreManager.getManager().getMyOrders(type).size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
        }

        if ("11".equals(type)) {
            onSelectedType(0);
        }
        if ("22".equals(type)) {
            onSelectedType(1);
        }
        if ("33".equals(type)) {
            onSelectedType(2);
        }
        if ("44".equals(type)) {
            onSelectedType(3);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = ((OrderListActivity) activity).getOrderListListener();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        registReceiver();
        if (CoreManager.getManager().getMyOrders(type).size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
            payListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        unregistReceiver();
        super.onStop();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    private void onSelectedType(int status) {

        if (CoreManager.getManager().getMyOrders(type).size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
        }

        if (status == 0) {
            txtUnpay.setTextColor(getResources().getColor(R.color.c_style_red));
            txtPayed.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtFinished.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtCancel.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        if (status == 1) {
            txtUnpay.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtPayed.setTextColor(getResources().getColor(R.color.c_style_red));
            txtFinished.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtCancel.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        if (status == 2) {
            txtUnpay.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtPayed.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtFinished.setTextColor(getResources().getColor(R.color.c_style_red));
            txtCancel.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        if (status == 3) {
            txtUnpay.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtPayed.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtFinished.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
            txtCancel.setTextColor(getResources().getColor(R.color.c_style_red));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txt_label_unpay:
                this.type = "11";
                onSelectedType(0);
                payListAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_label_payed:
                this.type = "22";
                onSelectedType(1);
                payListAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_label_finish_order:
                this.type = "33";
                onSelectedType(2);
                payListAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_label_cancel_order:
                this.type = "44";
                onSelectedType(3);
                payListAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_order_empty:
                // TODO
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public interface OnOrderListListener {
        public void onOrderClicked(OrderRecord order);

        public void onPayRequest(OrderRecord orderRecord);

        public void onOrderDataRequest(String type);
    }

//    private class OrderListAdapter extends BaseAdapter {
//
//
//        @Override
//        public int getCount() {
//            return CoreManager.getManager().getMyOrders(type).size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return CoreManager.getManager().getMyOrders(type).get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            OrderHolder holder = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_order, null);
//                holder = new OrderHolder(convertView);
//                convertView.setTag(convertView);
//            } else {
//                holder = (OrderHolder) convertView.getTag();
//            }
//            OrderRecord orderRecord = CoreManager.getManager().getMyOrders(type).get(position);
//            holder.bindData(orderRecord);
//
//            return convertView;
//        }
//    }

    private class OrderListAdapter extends RecyclerView.Adapter<OrderHolder> {

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_order, parent, false);
            return new OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            final OrderRecord orderRecord = CoreManager.getManager().getMyOrders(type).get(position);
            holder.bindData(orderRecord);
            if ("11".equals(type)) {
                holder.btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onPayRequest(orderRecord);
                        }
                    }
                });
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onOrderClicked(orderRecord);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return CoreManager.getManager().getMyOrders(type).size();
        }
    }


    private class OrderHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderNm;
        private TextView txtOwner;
        private TextView txtCarPlat;
        private TextView txtSyncState;
        private ImageView imgLogo;
        private TextView txtOrderContent;
        private TextView txtAmount;
        private AppCompatButton btnPay;

        public OrderHolder(View itemView) {
            super(itemView);

            txtOrderNm = (TextView) itemView.findViewById(R.id.txt_order_nm_content);
            txtOwner = (TextView) itemView.findViewById(R.id.txt_order_owner_content);
            txtCarPlat = (TextView) itemView.findViewById(R.id.txt_order_car_content);
            txtSyncState = (TextView) itemView.findViewById(R.id.txt_sync_state);
            imgLogo = (ImageView) itemView.findViewById(R.id.img_order_logo);
            txtOrderContent = (TextView) itemView.findViewById(R.id.txt_order_content);
            txtAmount = (TextView) itemView.findViewById(R.id.txt_order_amount);
            btnPay = (AppCompatButton) itemView.findViewById(R.id.btn_pay_for);
            if ("11".equals(type)) {

            }
            if ("22".equals(type)) {
                btnPay.setVisibility(View.GONE);
            }
            if ("33".equals(type)) {
                btnPay.setVisibility(View.GONE);
            }
            if ("44".equals(type)) {
                btnPay.setVisibility(View.GONE);
            }
        }

        public void bindData(OrderRecord order) {

            txtOrderNm.setText(order.getOrderNumber());
            txtOwner.setText(CoreManager.getManager().getCurrUser().getTel());
            txtCarPlat.setText(order.getPlatNum());
            if ("11".equals(order.getOrderStatus())) {
                txtSyncState.setText("未支付");
            }
            if ("22".equals(order.getOrderStatus())) {
                txtSyncState.setText("已支付");
            }
            if ("33".equals(order.getOrderStatus())) {
                txtSyncState.setText("已完成");
            }
            if ("44".equals(order.getOrderStatus())) {
                txtSyncState.setText("已取消");
            }
            String orderContent = order.getServiceString();
            txtOrderContent.setText(orderContent);
            txtAmount.setText(order.getTotalAmount() + "");

        }
    }


    private void registReceiver() {
        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_MY_ORDER);
            filter.addAction(Constant.ACTION_BROADCAST_MY_ORDER_ERR);

            receiver = new OrderRecordReceiver();
            getActivity().registerReceiver(receiver, filter);
        }
    }

    private void unregistReceiver() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }


    private class OrderRecordReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!intent.hasExtra("order_type")) {
                return;
            }
            if (Constant.ACTION_BROADCAST_MY_ORDER.equals(action)) {
                String type = intent.getStringExtra("order_type");
                if (OrderListFragment.this.type.equals(type)) {

                    if (CoreManager.getManager().getMyOrders(type).size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        orderList.setVisibility(View.VISIBLE);
                        payListAdapter.notifyDataSetChanged();
                    }

                }
            }
            if (Constant.ACTION_BROADCAST_MY_ORDER_ERR.equals(type)) {
                String type = intent.getStringExtra("order_type");
                if (OrderListFragment.this.type.equals(type)){
                    if (CoreManager.getManager().getMyOrders(type).size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        orderList.setVisibility(View.VISIBLE);
                        payListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

}
