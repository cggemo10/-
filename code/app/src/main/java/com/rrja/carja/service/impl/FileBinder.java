package com.rrja.carja.service.impl;

import android.os.Binder;

import com.rrja.carja.model.FileRequest;
import com.rrja.carja.service.FileService;

/**
 * Created by Administrator on 2015/7/16.
 */
public class FileBinder extends Binder {

    FileService mService;

    public FileBinder(FileService fileService) {
        this.mService = fileService;
    }

    public String getFile(FileRequest request) {

//        String file = mService.findFile(request);
//        if (file != null) {
//            return file;
//        } else {
//            mService.downloadFile(request);
//        }

        return null;
    }
}
