package com.rrja.carja.fragment.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.FeedbackActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.utils.ImageUtil;

import java.io.IOException;


public class UserCenterFragment extends Fragment implements View.OnClickListener, Handler.Callback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ImageView imgAvatar;
    TextView txtAccount;
    TextView txtNickName;
    Button btnModifyNick;
    Button btnLogin;

    RelativeLayout rlOrder;
    LinearLayout llOrderContent;

    LinearLayout llOrderPayed;
    LinearLayout llOrderUnpay;
    LinearLayout llOrderFinished;
    LinearLayout llOrderCancel;

    RelativeLayout rlMyCar;
    RelativeLayout rlCoupons;
    RelativeLayout rlFeedback;


    private UserReceiver userReceiver;
    private OnUserCenterInteractionListener mListener;

    private Handler mHandler;


    public static UserCenterFragment newInstance(String param1, String param2) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserCenterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mHandler = new Handler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, null);
        initView(view);
        view.setOnClickListener(this);
        return view;
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        txtAccount = (TextView) view.findViewById(R.id.txt_account);
        txtNickName = (TextView) view.findViewById(R.id.txt_nick_name);
        btnModifyNick = (Button) view.findViewById(R.id.btn_modify_nick_name);
        btnModifyNick.setOnClickListener(this);

        btnLogin = (Button) view.findViewById(R.id.btn_show_login);
        btnLogin.setOnClickListener(this);

        rlOrder = (RelativeLayout) view.findViewById(R.id.rl_setting_order);
        rlOrder.setOnClickListener(this);
        llOrderContent = (LinearLayout) view.findViewById(R.id.ll_setting_order_content);

        llOrderPayed = (LinearLayout) view.findViewById(R.id.ll_setting_order_payed);
        llOrderPayed.setOnClickListener(this);
        llOrderUnpay = (LinearLayout) view.findViewById(R.id.ll_setting_order_unpay);
        llOrderUnpay.setOnClickListener(this);
        llOrderFinished = (LinearLayout) view.findViewById(R.id.ll_setting_order_finished);
        llOrderFinished.setOnClickListener(this);
        llOrderCancel = (LinearLayout) view.findViewById(R.id.ll_setting_order_cancel);
        llOrderCancel.setOnClickListener(this);

        rlMyCar = (RelativeLayout) view.findViewById(R.id.rl_setting_mycar);
        rlMyCar.setOnClickListener(this);
        rlCoupons = (RelativeLayout) view.findViewById(R.id.rl_setting_coupons);
        rlCoupons.setOnClickListener(this);
        rlFeedback = (RelativeLayout) view.findViewById(R.id.rl_setting_feedback);
        rlFeedback.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity) activity).getUserCenterInteraction();
        registUserReceiver();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        unregistUserReceiver();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLogin();
    }

    private void checkLogin() {
        UserInfo userInfo = CoreManager.getManager().getCurrUser();
        if (userInfo == null) {
            onLogout();
        } else {
            onLogin(userInfo);
        }
    }

    public void onLogout() {

        imgAvatar.setVisibility(View.GONE);
        txtAccount.setVisibility(View.GONE);
        txtNickName.setVisibility(View.GONE);
        btnModifyNick.setVisibility(View.GONE);

        btnLogin.setVisibility(View.VISIBLE);
    }

    public void onLogin(UserInfo userInfo) {

        // TODO show avatar from path
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("user.jpg"));
            Bitmap roundedAvatar = ImageUtil.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 3.14f);
            imgAvatar.setImageBitmap(roundedAvatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtAccount.setText(userInfo.getName());
        txtNickName.setText(userInfo.getNikeName());
        imgAvatar.setVisibility(View.VISIBLE);
        txtAccount.setVisibility(View.VISIBLE);
        txtNickName.setVisibility(View.VISIBLE);
        btnModifyNick.setVisibility(View.VISIBLE);

        btnLogin.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        UserInfo currUser = CoreManager.getManager().getCurrUser();
        switch (v.getId()) {
            case R.id.btn_modify_nick_name:
                mListener.modifyNickname();
                break;
            case R.id.btn_show_login:
                mListener.loginInteraction();
                break;
            case R.id.rl_setting_order: {
                if (currUser != null) {
                    if (llOrderContent.getVisibility() == View.VISIBLE) {
                        llOrderContent.setVisibility(View.GONE);
                    } else {
                        llOrderContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    mListener.loginInteraction();
                }
            }
            break;
            case R.id.ll_setting_order_payed:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }

                break;
            case R.id.ll_setting_order_unpay:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }
                break;
            case R.id.ll_setting_order_finished:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }
                break;
            case R.id.ll_setting_order_cancel:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }
                break;
            case R.id.rl_setting_mycar:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }
                break;
            case R.id.rl_setting_coupons:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {

                }
                break;
            case R.id.rl_setting_feedback:
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        checkLogin();
        return false;
    }


    public interface OnUserCenterInteractionListener {

        public void loginInteraction();

        public void modifyNickname();
    }

    private void registUserReceiver() {
        if (userReceiver == null) {
            userReceiver = new UserReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_LOGIN_BY_AUTH);
        filter.addAction(Constant.ACTION_LOGIN_BY_AUTH_ERROR);
        filter.addAction(Constant.ACTION_MODIFY_NICK_NAME);
        filter.addAction(Constant.ACTION_MODIFY_NICK_NAME_ERROR);

        getActivity().registerReceiver(userReceiver, filter);
    }

    private void unregistUserReceiver() {
        if (userReceiver == null) {
            return;
        }
        getActivity().unregisterReceiver(userReceiver);
        userReceiver = null;
    }

    private class UserReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_LOGIN_BY_AUTH.equals(action)) {

                SharedPreferences sp = context.getSharedPreferences("authsp", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("auth", intent.getStringExtra("auth"));
                edit.putString("tel", intent.getStringExtra("tel"));
                edit.commit();

                mHandler.sendEmptyMessage(0);
            }

            if (Constant.ACTION_MODIFY_NICK_NAME.equals(action)) {
                mHandler.sendEmptyMessage(0);
            }

            if (Constant.ACTION_MODIFY_NICK_NAME_ERROR.equals(action)) {
                // TODO
            }

            if (Constant.ACTION_LOGIN_BY_AUTH_ERROR.equals(action)) {
                // TODO
            }
        }
    }


}
