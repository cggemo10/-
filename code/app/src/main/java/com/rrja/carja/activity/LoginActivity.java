package com.rrja.carja.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.UserBinder;

import static android.os.Handler.Callback;

public class LoginActivity extends BaseActivity implements View.OnClickListener, Callback {

    private static final String TAG = LoginActivity.class.getName();

    private EditText mobileEd;
    private EditText smsEd;
    private AppCompatButton btnRequestSms;
    private AppCompatButton btnRegistOrLogin;
    private ImageView imgMobileClear;
    private ImageView imgSmsClear;

    private Handler mHandler;
    private UserBinder userService;

    private UserReceiver userReceiver;

    private static final int WHAT_WAIT_SMS_RECEIVE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initView();

        mHandler = new Handler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_USER_SERVICE);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_LOGIN_BY_PHONE);
        filter.addAction(Constant.ACTION_LOGIN_BY_PHONE_ERROR);
        filter.addAction(Constant.ACTION_LOGIN_MOBILE_SMS);
        filter.addAction(Constant.ACTION_LOGIN_MOBILE_SMS_ERROR);

        userReceiver = new UserReceiver();

        registerReceiver(userReceiver, filter);
    }

    @Override
    protected void onStop() {
        unbindService(connection);

        unregisterReceiver(userReceiver);
        super.onStop();
    }

    private void initView() {
        imgMobileClear = (ImageView) findViewById(R.id.img_mobel_clear);
        imgMobileClear.setOnClickListener(this);
        imgSmsClear = (ImageView) findViewById(R.id.img_sms_code_clear);
        imgSmsClear.setOnClickListener(this);
        mobileEd = (EditText) findViewById(R.id.ed_user_mobile);
        mobileEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    imgMobileClear.setVisibility(View.GONE);
                } else {
                    imgMobileClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        smsEd = (EditText) findViewById(R.id.ed_mobile_sms);
        smsEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    imgSmsClear.setVisibility(View.GONE);
                } else {
                    imgSmsClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnRequestSms = (AppCompatButton) findViewById(R.id.btn_get_sms_code);
        btnRequestSms.setOnClickListener(this);
        btnRegistOrLogin = (AppCompatButton) findViewById(R.id.btn_user_login);
        btnRegistOrLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_mobel_clear:
                mobileEd.setText("");
                break;
            case R.id.img_sms_code_clear:
                smsEd.setText("");
                break;
            case R.id.btn_get_sms_code:

                if (btnRequestSms.isEnabled()) {
                    btnRequestSms.setEnabled(false);


                    String mobileNm = mobileEd.getText().toString();
                    if (checkMobileNumber(mobileNm)) {
                        userService.getSmsCode(mobileNm);
                    } else {
                        Toast.makeText(LoginActivity.this, "手机号码不正确，请重新输入。", Toast.LENGTH_LONG).show();
                        btnRequestSms.setEnabled(true);
                    }


                }
                break;

            case R.id.btn_user_login:

                if (!btnRegistOrLogin.isEnabled()) {
                    return;
                }

                btnRegistOrLogin.setEnabled(false);

                String mobileNm = mobileEd.getText().toString();
                if (!checkMobileNumber(mobileNm)) {
                    Toast.makeText(LoginActivity.this, "手机号码不正确，请重新输入。", Toast.LENGTH_LONG).show();
                    btnRegistOrLogin.setEnabled(true);
                    return;
                }

                String smsCode = smsEd.getText().toString();
                if (TextUtils.isEmpty(smsCode) || !TextUtils.isDigitsOnly(smsCode)) {
                    Toast.makeText(LoginActivity.this, "验证码格式错误，请重新输入。", Toast.LENGTH_LONG).show();
                    btnRegistOrLogin.setEnabled(true);
                    return;
                }

                userService.registOrLogin(mobileNm, smsCode);
                break;
        }

    }

    private boolean checkMobileNumber(String mobileNm) {

        if (TextUtils.isEmpty(mobileNm)) {
            return false;
        }

        String phoneFormat = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
        if (mobileNm.matches(phoneFormat)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == WHAT_WAIT_SMS_RECEIVE) {

            int lastSec = (int) msg.obj;
            if (lastSec <= 0) {
                try {
                    btnRequestSms.setEnabled(true);
                    btnRequestSms.setText(R.string.hint_login_verify_mn);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {

                try {
                    btnRequestSms.setText(String.format("(%d)", lastSec));

                    Message msg2 = mHandler.obtainMessage(msg.what);
                    msg2.obj = lastSec - 1;
                    mHandler.sendMessageDelayed(msg2, 1000);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return false;
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            userService = (UserBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            userService = null;
        }
    };

    private class UserReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_LOGIN_BY_PHONE.equals(action)) {
                Toast.makeText(context, "登录成功。", Toast.LENGTH_LONG).show();
                LoginActivity.this.onBackPressed();
            }

            if (Constant.ACTION_LOGIN_BY_PHONE_ERROR.equals(action)) {
                btnRegistOrLogin.setEnabled(true);
                String description = intent.getStringExtra("description");
                Toast.makeText(context, description, Toast.LENGTH_LONG).show();
            }

            if (Constant.ACTION_LOGIN_MOBILE_SMS.equals(action)) {
                Message msg = mHandler.obtainMessage(WHAT_WAIT_SMS_RECEIVE);
                msg.obj = 30;
                msg.sendToTarget();
            }

            if (Constant.ACTION_LOGIN_MOBILE_SMS_ERROR.equals(action)) {
                btnRequestSms.setEnabled(true);
                String description = intent.getStringExtra("description");
                Toast.makeText(context, description, Toast.LENGTH_LONG).show();
            }
        }
    }
}
