package com.rrja.carja.service.impl;


import android.content.Context;
import android.os.Binder;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ProductBinder extends Binder{

    Context mContext;

    public ProductBinder(Context context) {
        this.mContext = context;
    }


}
