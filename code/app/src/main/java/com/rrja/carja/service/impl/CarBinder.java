package com.rrja.carja.service.impl;

import android.content.Context;
import android.os.Binder;


public class CarBinder extends Binder{

    Context mContext;

    public CarBinder(Context context) {
        this.mContext = context;
    }




}
