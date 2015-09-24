package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.CarInfoActivity;
import com.rrja.carja.adapter.decoration.CarBrandDecoration;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.FileService;
import com.rrja.carja.utils.DialogHelper;

import java.io.File;


public class CarListFragment extends BaseElementFragment {


    private CarListInteractionListener mListener;

    private AppCompatButton btnAdd;
    private RecyclerView mRecycler;
    private PrivateCarAdapter mAdapter;

    private AppCompatButton btn
    private UserCarReceiver mReceiver;

    public static CarListFragment newInstance() {
        CarListFragment fragment = new CarListFragment();
        return fragment;
    }

    public CarListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_my_car);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PrivateCarAdapter();
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new CarBrandDecoration(getActivity(),CarBrandDecoration.VERTICAL_LIST));

        btnAdd = (AppCompatButton) view.findViewById(R.id.btn_add_car);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.requestAddCar();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((CarInfoActivity) activity).getCarListListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.str_txt_setting_mycar);
        if (CoreManager.getManager().getUserCars() == null || CoreManager.getManager().getUserCars().size() == 0) {
            Intent intent = new Intent(getActivity(), DataCenterService.class);
            intent.setAction(Constant.ACTION_REQUEST_REFRESH_USER_CAR);
            getActivity().startService(intent);

            DialogHelper.getHelper().showWaitting();
        }

        mAdapter.notifyDataSetChanged();
        registReceiver();
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_GET_USER_CARS);
            filter.addAction(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);

            mReceiver = new UserCarReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onStop() {
        unregistReceiver();
        super.onStop();
    }

    private void unregistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onRequestCarLogo(CarInfo carInfo) {
        Intent intent = new Intent(getActivity(), FileService.class);
        intent.setAction(FileService.ACTION_IMG_CAR_LOGO);
        intent.putExtra("car", carInfo);
        getActivity().startService(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public interface CarListInteractionListener {
        public void onCarSelected(CarInfo car);
        public void requestAddCar();
    }

    private class PrivateCarAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_choise, null);
            CarVH vh = new CarVH(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final CarInfo carInfo = CoreManager.getManager().getUserCars().get(position);
            CarVH vh = (CarVH) holder;
            vh.bindData(carInfo);

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onCarSelected(carInfo);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return CoreManager.getManager().getUserCars().size();
        }
    }

    private class CarVH extends RecyclerView.ViewHolder {

        private ImageView imgLogo;
        private TextView txtCarPlat;
        private TextView txtDetal;

        public CarVH(View itemView) {
            super(itemView);

            imgLogo = (ImageView) itemView.findViewById(R.id.img_car_ic);
            txtCarPlat = (TextView) itemView.findViewById(R.id.txt_car_platnm);
            txtDetal = (TextView) itemView.findViewById(R.id.txt_car_detial);
        }

        public void bindData(CarInfo carInfo) {

            String picUrl = carInfo.getCarImg();
            try {
                if (!TextUtils.isEmpty(picUrl)) {

                    String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

                    File img = new File(Constant.getCarImageCacheDir(), fileName);
                    if (img.exists()) {
                        imgLogo.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                    } else {
                        onRequestCarLogo(carInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            txtCarPlat.setText(carInfo.getPlatNum());
            txtDetal.setText(carInfo.getSeriesName() + " " + carInfo.getModelName());
        }
    }

    private class UserCarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            DialogHelper.getHelper().dismissWatting();

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_GET_USER_CARS.equals(action)) {
                mAdapter.notifyDataSetChanged();
            }

            if (Constant.ACTION_BROADCAST_GET_USER_CARS_ERR.equals(action)) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
