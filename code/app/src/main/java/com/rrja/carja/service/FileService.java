package com.rrja.carja.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rrja.carja.model.FileRequest;
import com.rrja.carja.service.impl.FileBinder;

public class FileService extends Service {



    public FileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new FileBinder(this);
    }

    public String findFile(FileRequest request) {

        if (request != null && request.isUseable()) {

        }

    }

    public void downloadFile(FileRequest request) {

    }
}
