package com.rrja.carja.service.impl;

import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;

import com.rrja.carja.service.IForumAidlInterface;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ForumBinder extends Binder {

    Context mContext;

    public ForumBinder(Context context) {
        this.mContext = context;
    }


}
