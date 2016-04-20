package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarInfo;
import com.rrja.carja.model.coupons.CouponGoods;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.model.ViolationRecord;
import com.rrja.carja.model.coupons.RecommendGoods;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                            intent.putExtra("auth", info.getAuthToken());
                            intent.putExtra("tel", info.getTel());
                            mContext.sendBroadcast(intent);

                            mContext.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mContext.requestUserCars();
                                    mContext.requestUserCoupons();
                                }
                            });

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


                            mContext.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mContext.requestUserCars();
                                }
                            });

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

    public void updateAvatar(final File avatarFile) {

        if (CoreManager.getManager().getCurrUser() == null || !avatarFile.exists()) {

            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject avatarJs = HttpUtils.updateUserAvatar(CoreManager.getManager().getCurrUser(), avatarFile);
                    int code = avatarJs.getInt("code");
                    if (code == 0) {

                        CoreManager.getManager().getCurrUser().setAvatarPath(avatarFile.getAbsolutePath());

                        Intent intent = new Intent(Constant.ACTION_MODIFY_AVATAR);
                        intent.putExtra("avatar", avatarFile.getAbsoluteFile());
                        mContext.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(Constant.ACTION_MODIFY_AVATAR_ERR);
                        String errMsg = null;
                        if (avatarJs.has("description")) {
                            errMsg = avatarJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Intent intent = new Intent(Constant.ACTION_MODIFY_AVATAR_ERR);
                    String errMsg = "网络异常，请稍后再试。";
                    intent.putExtra("description", errMsg);
                    mContext.sendBroadcast(intent);

                    return;
                }

            }
        };
        mContext.execute(task);
    }

    //------------------------------------------------------------------------------------ goods or service
    public void getRecommendGoods(int page) {
        if (page <= 0) {
            page = 1;
        }

        final int finalPage = page;
        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject couponsJs = HttpUtils.getRecommendGoods(finalPage);
                    int code = couponsJs.getInt("code");
                    if (code == 0) {

                        List<RecommendGoods> info = ResponseUtils.parseDiscountList(couponsJs.getJSONArray("data"));
                        if (info != null || info.size() > 0) {
                            if (finalPage == 1) {
                                CoreManager.getManager().getDiscounts().clear();
                            }
                            CoreManager.getManager().getDiscounts().addAll(info);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA);
                            intent.putExtra("size", info.size());
                            mContext.sendBroadcast(intent);
                            return;
                        }

                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR);
                        String errMsg = null;
                        if (couponsJs.has("description")) {
                            errMsg = couponsJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = mContext.getString(R.string.str_err_net);
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_RECOMMEND_DATA_ERR);
                String errMsg = mContext.getString(R.string.str_err_net);
                intent.putExtra("description", errMsg);
                intent.putExtra("page", finalPage);
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void getCouponsGoods(int page) {

        if (page <= 0) {
            page = 1;
        }

        final int finalPage = page;
        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject couponsJs = HttpUtils.getCouponsGoods(finalPage);
                    int code = couponsJs.getInt("code");
                    if (code == 0) {

                        List<CouponGoods> info = ResponseUtils.parseCouponsList(couponsJs.getJSONArray("data"));
                        if (info != null || info.size() > 0) {
                            if (finalPage == 1) {
                                CoreManager.getManager().getCoupons().clear();
                            }
                            CoreManager.getManager().getCoupons().addAll(info);
                            // TODO save auth
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_COUPONS_DATA);
                            intent.putExtra("size", info.size());
                            mContext.sendBroadcast(intent);
                            return;
                        }

                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_COUPONS_DATA_ERR);
                        String errMsg = null;
                        if (couponsJs.has("description")) {
                            errMsg = couponsJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = mContext.getString(R.string.str_err_net);
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_COUPONS_DATA_ERR);
                String errMsg = mContext.getString(R.string.str_err_net);
                intent.putExtra("description", errMsg);
                intent.putExtra("page", finalPage);
                mContext.sendBroadcast(intent);
            }
        };

        mContext.execute(task);
    }

    public void getUserCars() {

        if (CoreManager.getManager().getCurrUser() == null) {

            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);
            intent.putExtra("usercars","usercars");
            mContext.sendBroadcast(intent);
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject userCarJs = HttpUtils.getPrivateCarList(CoreManager.getManager().getCurrUser());
                    int code = userCarJs.getInt("code");
                    if (code == 0) {

                        List<CarInfo> carInfoList = ResponseUtils.parseCarInfo(userCarJs.getJSONArray("data"));
                        if (carInfoList == null) {
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);
                            intent.putExtra("usercars","usercars");
                            mContext.sendBroadcast(intent);
                            return;
                        }
                        CoreManager.getManager().clearUserCars();
                        CoreManager.getManager().setUserCars(carInfoList);

                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_USER_CARS);
                        intent.putExtra("usercars", "usercars");
                        mContext.sendBroadcast(intent);
                        return;
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);
                        String errMg = null;
                        if (userCarJs.has("description")) {
                            errMg = userCarJs.getString("description");
                        } else {
                            errMg = mContext.getString(R.string.str_err_net);
                        }
                        intent.putExtra("description", errMg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Constant.ACTION_BROADCAST_GET_USER_CARS_ERR);
                    String errMg = mContext.getString(R.string.str_err_net);
                    intent.putExtra("description", errMg);
                    mContext.sendBroadcast(intent);
                }
            }
        };

        mContext.execute(task);
    }

    public void getIllegalRecord(final String carId) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject illegalJson = HttpUtils.queryIllegal(carId);
                    int code = illegalJson.getInt("code");
                    if (code == 0) {
                        List<ViolationRecord> carInfoList = ResponseUtils.parseViolation(illegalJson.getJSONArray("data"));
                        if (carInfoList == null) {
                            Intent intent = new Intent(Constant.ACTION_BROADCAST_VIOLATION_ERR);
                            intent.putExtra("usercars","usercars");
                            mContext.sendBroadcast(intent);
                            return;
                        }

                        Intent intent = new Intent(Constant.ACTION_BROADCAST_VIOLATION);
                        intent.putParcelableArrayListExtra("violation_record", (ArrayList) carInfoList);
                        mContext.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_VIOLATION_ERR);
                        String errMg = null;
                        if (illegalJson.has("description")) {
                            errMg = illegalJson.getString("description");
                        } else {
                            errMg = mContext.getString(R.string.str_err_net);
                        }
                        intent.putExtra("description", errMg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Constant.ACTION_BROADCAST_VIOLATION_ERR);
                    String errMg = mContext.getString(R.string.str_err_net);
                    intent.putExtra("description", errMg);
                    mContext.sendBroadcast(intent);
                }
            }
        };
        mContext.execute(task);
    }

    public void requestCoupons(final CouponGoods couponGoods) {

        if (CoreManager.getManager().getCurrUser() == null) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_GAIN_COUPONS_ERR);
            intent.putExtra("description", "请先登录");
            mContext.sendBroadcast(intent);
            return;
        }

        if (couponGoods == null || TextUtils.isEmpty(couponGoods.getServiceId())) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_GAIN_COUPONS_ERR);
            intent.putExtra("description", mContext.getString(R.string.str_err_net));
            mContext.sendBroadcast(intent);
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject gainJs = HttpUtils.gainCoupons(couponGoods);

                    int code = gainJs.getInt("code");
                    if (code == 0) {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GAIN_COUPONS);
                        intent.putExtra("number", "gainCoupons");
                        mContext.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_GAIN_COUPONS_ERR);
                        String errMg = null;
                        if (gainJs.has("description")) {
                            errMg = gainJs.getString("description");
                        } else {
                            errMg = mContext.getString(R.string.str_err_net);
                        }
                        intent.putExtra("description", errMg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Constant.ACTION_BROADCAST_GAIN_COUPONS_ERR);
                    String errMg = mContext.getString(R.string.str_err_net);
                    intent.putExtra("description", errMg);
                    mContext.sendBroadcast(intent);
                }
            }
        };

        mContext.execute(task);
    }

    public void getprivateCoupons() {

        if (CoreManager.getManager().getCurrUser() == null) {

            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {

                JSONObject userCouponsJs = HttpUtils.getPrivateCoupons(CoreManager.getManager().getCurrUser());
                try {
                    int code = userCouponsJs.getInt("code");
                    if (code == 0) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mContext.execute(task);
    }

    public void checkUpdate() {

        Runnable task = new Runnable() {
            @Override
            public void run() {

                HttpUtils.checkUpdate();
            }
        };

        mContext.execute(task);
    }
}
