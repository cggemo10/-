package com.rrja.carja.service.impl;


import android.content.Context;
import android.os.RemoteException;

import com.rrja.carja.service.IProductAidlInterface;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ProductBinder extends IProductAidlInterface.Stub{

    Context mContext;

    public ProductBinder(Context context) {
        this.mContext = context;
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }
}
