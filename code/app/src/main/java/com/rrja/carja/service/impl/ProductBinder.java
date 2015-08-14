package com.rrja.carja.service.impl;


import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.text.TextUtils;

import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.transaction.HttpUtils;
import com.rrja.carja.utils.ResponseUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ProductBinder extends Binder{

    DataCenterService mContext;

    public ProductBinder(DataCenterService context) {
        this.mContext = context;
    }



    public void requestCarStoreList() {

    }


}
