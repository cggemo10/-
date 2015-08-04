package com.rrja.carja.service.impl;


import android.content.Context;
import android.os.Binder;

import com.rrja.carja.service.DataCenterService;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ProductBinder extends Binder{

    DataCenterService mContext;

    public ProductBinder(DataCenterService context) {
        this.mContext = context;
    }

    public void requestDiscountList() {

    }

    public void requestCouponsList() {

    }

    public void requestCarStoreList() {

    }


}
