package com.rrja.carja.service.impl;

import android.content.Context;
import android.os.RemoteException;

import com.rrja.carja.service.IForumAidlInterface;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ForumBinder extends IForumAidlInterface.Stub {

    Context mContext;

    public ForumBinder(Context context) {
        this.mContext = context;
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }
}
