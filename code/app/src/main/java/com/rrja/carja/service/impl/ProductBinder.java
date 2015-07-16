package com.rrja.carja.service.impl;


import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;

import com.rrja.carja.service.IProductAidlInterface;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ProductBinder extends Binder{

    Context mContext;

    public ProductBinder(Context context) {
        this.mContext = context;
    }


}
