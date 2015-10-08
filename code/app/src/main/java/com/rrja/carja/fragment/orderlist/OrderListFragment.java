package com.rrja.carja.fragment.orderlist;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.OrderListActivity;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.maintenance.MaintenanceOrder;

public class OrderListFragment extends BaseElementFragment {

    private RecyclerView unpayRecycler;
    private RecyclerView payRecycler;
    private RecyclerView finishedRecycler;
    private RecyclerView cancelRecycler;

    private ViewPager vpOrderList;

    private OrderPagerAdapter pagerAdapter;

    private OrderListAdapter unPayAdapter;
    private OrderListAdapter payedAdapter;
    private OrderListAdapter finishedAdapter;
    private OrderListAdapter cancelAdapter;

    private OnOrderListListener mListener;

    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance() {
        OrderListFragment fragment = new OrderListFragment();
        return fragment;
    }

    public OrderListFragment() {
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

        unPayAdapter = new OrderListAdapter("11");
        payedAdapter = new OrderListAdapter("22");
        finishedAdapter = new OrderListAdapter("33");
        cancelAdapter = new OrderListAdapter("44");

        unpayRecycler = new RecyclerView(getActivity());
        LinearLayout.LayoutParams unPayParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        unpayRecycler.setLayoutParams(unPayParam);
        unpayRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        unpayRecycler.setAdapter(unPayAdapter);

        payRecycler = new RecyclerView(getActivity());
        LinearLayout.LayoutParams payedParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        payRecycler.setLayoutParams(payedParam);
        payRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        payRecycler.setAdapter(payedAdapter);

        finishedRecycler = new RecyclerView(getActivity());
        LinearLayout.LayoutParams finishedParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        finishedRecycler.setLayoutParams(finishedParam);
        finishedRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        finishedRecycler.setAdapter(finishedAdapter);

        cancelRecycler = new RecyclerView(getActivity());
        LinearLayout.LayoutParams cancelParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        cancelRecycler.setLayoutParams(cancelParam);
        cancelRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cancelRecycler.setAdapter(cancelAdapter);

        vpOrderList = (ViewPager) view.findViewById(R.id.vp_order_list);
        vpOrderList.setAdapter(pagerAdapter);
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

    }

    @Override
    public void onStop() {
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

    public interface OnOrderListListener {
        public void onOrderClicked(MaintenanceOrder order);
        public void onOrderDataRequest(String type);
    }

    private class OrderListAdapter extends RecyclerView.Adapter {

        private String orderType;

        public OrderListAdapter(String type) {
            this.orderType = type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_order, parent, false);
            return new OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return CoreManager.getManager().getMyOrders(orderType).size();
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
        }

        public void bindData(MaintenanceOrder order) {

        }
    }

    private class OrderPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = unpayRecycler;
                    break;
                case 1:
                    view = payRecycler;
                    break;
                case 2:
                    view = finishedRecycler;
                    break;
                case 3:
                    view = cancelRecycler;
                    break;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = null;
            switch (position) {
                case 0:
                    view = unpayRecycler;
                    break;
                case 1:
                    view = payRecycler;
                    break;
                case 2:
                    view = finishedRecycler;
                    break;
                case 3:
                    view = cancelRecycler;
                    break;
            }
            container.removeView(view);
        }
    }

}
