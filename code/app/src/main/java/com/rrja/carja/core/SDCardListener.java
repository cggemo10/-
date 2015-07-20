package com.rrja.carja.core;

import android.os.FileObserver;

/**
 * Created by Administrator on 2015/7/20.
 */
public class SDCardListener extends FileObserver {

    public SDCardListener(String path) {
        super(path);
    }

    @Override
    public void onEvent(int event, String path) {

        switch (event) {
            case FileObserver.DELETE:
            case FileObserver.DELETE_SELF:
        }
    }
}
