package com.rrja.carja.fragment.store;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.StoreReservationDetalActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.BaseElementFragment;
import com.rrja.carja.model.CarStore;
import com.rrja.carja.service.FileService;

import java.io.File;

public class StoreMainFragment extends BaseElementFragment implements View.OnClickListener {

    private static final String TAG = "rrja.StoreMainFragment";

    private TextView txtStoreName;
    private ImageView imgStoreImg;
    private TextView txtAddress;
    private TextView txtStoreArea;
    private TextView txtStoreTel;
    private TextView txtStoreOpenTime;
    private TextView txtStorePayType;
    private TextView txtStoreService;
    private Button btnCommit;

    private OnStoreMainActionListener mListener;
    private CarStore store;

    private StoreImgReceiver mReceiver;

    public static StoreMainFragment newInstance() {
        StoreMainFragment fragment = new StoreMainFragment();
        return fragment;
    }

    public StoreMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        txtStoreName = (TextView) view.findViewById(R.id.txt_store_name_detal);
        imgStoreImg = (ImageView) view.findViewById(R.id.img_store_pic_detal);
        txtAddress = (TextView) view.findViewById(R.id.txt_store_address_detal_content);
        txtStoreArea = (TextView) view.findViewById(R.id.txt_store_area_detal);
        txtStoreTel = (TextView) view.findViewById(R.id.txt_store_tel_detal);
        txtStoreOpenTime = (TextView) view.findViewById(R.id.txt_store_opentime_detal_content);
        txtStorePayType = (TextView) view.findViewById(R.id.txt_store_paytype_content);
        txtStoreService = (TextView) view.findViewById(R.id.txt_store_service_content);
        btnCommit = (Button) view.findViewById(R.id.btn_gain_coupons);

        txtStoreName.setText(store.getStoreName());

        String picUrl = store.getStoreImg();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getStoreCacheDir(), fileName);
            if (img.exists()) {
                try {
                    imgStoreImg.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                onRequestStorePic(store);
            }
        }

        txtAddress.setText(store.getAddress());
        txtStoreArea.setText(store.getArea());

        txtStoreTel.setText(store.getTel());
        txtStoreOpenTime.setText(store.getOpenTime());
        txtStorePayType.setText(store.getPayType());
        txtStoreService.setText(store.getDesc());


        btnCommit.setOnClickListener(this);


    }

    private void onRequestStorePic(CarStore store) {
        Intent intent = new Intent(getActivity(), FileService.class);
        intent.setAction(FileService.ACTION_IMG_STORE);
        intent.putExtra("car_store", store);
        getActivity().startService(intent);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onAddBook();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((StoreReservationDetalActivity) activity).getStoreMainActionListener();
        this.store = ((StoreReservationDetalActivity) activity).getCurrentStore();
    }

    @Override
    public void onStart() {
        super.onStart();
        registReceiver();
    }

    @Override
    public void onStop() {
        unRegistReceiver();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mListener != null) {
            mListener.onBackClicked();
        }
        return true;
    }

    public interface OnStoreMainActionListener {

        public void onAddBook();
        public void onBackClicked();
    }

    // --------------------------------------------------------------------------------------

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE);

            mReceiver = new StoreImgReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    private void unRegistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private class StoreImgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_STORE.equals(action)) {
                String picUrl = store.getStoreImg();
                if (!TextUtils.isEmpty(picUrl)) {

                    String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

                    File img = new File(Constant.getStoreCacheDir(), fileName);
                    if (img.exists()) {
                        try {
                            imgStoreImg.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }


}
