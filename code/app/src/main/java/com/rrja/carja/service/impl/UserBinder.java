package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBinder extends Binder {

    private static String TAG = "rrja.UserBinder";

    DataCenterService mContext;

    public UserBinder(DataCenterService context) {
        this.mContext = context;
    }

    public void getSmsCode(final String mobileNum) {

        if (TextUtils.isEmpty(mobileNum)) {
            Intent intent = new Intent(Constant.ACTION_LOGIN_MOBILE_SMS_ERROR);
            mContext.sendBroadcast(intent);
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject smsCodeJson = HttpUtils.getSmsCode(mobileNum);
                try {
                    int code = smsCodeJson.getInt("code");
                    if (code == 0) {
                        Intent intent = new Intent(Constant.ACTION_LOGIN_MOBILE_SMS);
                        intent.putExtra("data", smsCodeJson.toString());
                        mContext.sendBroadcast(intent);
                        return;
                    } else {
                        Intent intent = new Intent(Constant.ACTION_LOGIN_MOBILE_SMS_ERROR);
                        String errMsg = null;
                        if (smsCodeJson.has("description")) {
                            errMsg = smsCodeJson.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_LOGIN_MOBILE_SMS_ERROR);
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void checkAuth(final String auth, final String tel) {
        if (TextUtils.isEmpty(auth) || TextUtils.isEmpty(tel)) {
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject authJson = HttpUtils.checkAuth(auth, tel);
                    int code = authJson.getInt("code");
                    if (code == 0) {
                        UserInfo info = UserInfo.parse(authJson.getJSONObject("data"));
                        if (info != null) {
                            CoreManager.getManager().setCurrUser(info);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_LOGIN_BY_AUTH);
                            mContext.sendBroadcast(intent);
                            return;
                        }
                    } else {
                        Intent intent = new Intent(Constant.ACTION_LOGIN_BY_AUTH_ERROR);
                        String errMsg = null;
                        if (authJson.has("description")) {
                            errMsg = authJson.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_LOGIN_BY_AUTH_ERROR);
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void registOrLogin(final String mobileNum, final String smsCode) {

        if (TextUtils.isEmpty(mobileNum) || TextUtils.isEmpty(smsCode)) {
            Intent intent = new Intent(Constant.ACTION_LOGIN_BY_PHONE_ERROR);
            mContext.sendBroadcast(intent);
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject loginJson = HttpUtils.login(mobileNum, smsCode);
                    int code = loginJson.getInt("code");
                    if (code == 0) {

                        UserInfo info = UserInfo.parse(loginJson.getJSONObject("data"));
                        if (info != null) {
                            CoreManager.getManager().setCurrUser(info);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_LOGIN_BY_PHONE);
                            mContext.sendBroadcast(intent);
                            return;
                        }

                    } else {
                        Intent intent = new Intent(Constant.ACTION_LOGIN_BY_PHONE_ERROR);
                        String errMsg = null;
                        if (loginJson.has("description")) {
                            errMsg = loginJson.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                Intent intent = new Intent(Constant.ACTION_LOGIN_BY_PHONE_ERROR);
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }


}
