package com.rrja.carja.service.impl;

import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;

import org.json.JSONObject;

/**
 * Created by chongge on 15/10/21.
 */
public class FeedbackBinder extends Binder{

    private static final String TAG = "rrja.CarBinder";

    DataCenterService mContext;

    public FeedbackBinder(DataCenterService context) {
        this.mContext = context;
    }

    public void commitFeedback(final String title, final String content) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || CoreManager.getManager().getCurrUser() == null) {
            Intent intent = new Intent(Constant.ACTION_BROADCAST_FEEDBACK_ERR);
            String errMsg = "网络异常，请稍后再试。";
            intent.putExtra("description", errMsg);
            mContext.sendBroadcast(intent);
            return;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject feedbackJs = HttpUtils.commitFeedback(CoreManager.getManager().getCurrUser().getTel(), CoreManager.getManager().getCurrUser().getAuthToken(), title, content);
                    int code = feedbackJs.getInt("code");
                    if (code == 0) {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_FEEDBACK);
                        intent.putExtra("feedback", "feedback");
                        mContext.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(Constant.ACTION_BROADCAST_FEEDBACK_ERR);
                        String errMsg = null;
                        if (feedbackJs.has("description")) {
                            errMsg = feedbackJs.getString("description");
                        }

                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "网络异常，请稍后再试。";
                        }
                        intent.putExtra("description", errMsg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    Intent intent = new Intent(Constant.ACTION_BROADCAST_FEEDBACK_ERR);
                    String errMsg = "网络异常，请稍后再试。";
                    intent.putExtra("description", errMsg);
                    mContext.sendBroadcast(intent);
                }


            }
        };

        mContext.execute(task);
    }
}
