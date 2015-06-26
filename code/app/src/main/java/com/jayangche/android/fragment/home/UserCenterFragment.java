package com.jayangche.android.fragment.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.activity.MainActivity;
import com.jayangche.android.constant.Constant;
import com.jayangche.android.core.CoreManager;
import com.jayangche.android.model.UserInfo;

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
    ListView listSetting;


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
        View view = inflater.inflate(R.layout.fragment_user_center,null);
        initView(view);
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

        checkLogin();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity)mListener).getUserCenterInteraction();
        registUserReceiver();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        unregistUserReceiver();
        mListener = null;
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
            imgAvatar.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open("user.jpg")));
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

        switch (v.getId()) {
            case R.id.btn_modify_nick_name:
                mListener.modifyNickname();
                break;
            case R.id.btn_show_login:
                mListener.loginInteraction();
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
        filter.addAction(Constant.ACTION_LOGIN);
        filter.addAction(Constant.ACTION_MODIFY_NICK_NAME);
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
            if (Constant.ACTION_LOGIN.equals(action)) {
                mHandler.sendEmptyMessage(0);
            }

            if (Constant.ACTION_MODIFY_NICK_NAME.equals(action)) {
                mHandler.sendEmptyMessage(0);
            }
        }
    }


}
