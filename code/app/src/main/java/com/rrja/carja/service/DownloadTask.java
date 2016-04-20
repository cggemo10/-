package com.rrja.carja.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.rrja.carja.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable {

    public static final int MSG_DOWNLOAD_PROGRESS = 31;
    public static final int MSG_DOWNLOAD_SUCCESS = 32;
    public static final int MSG_DOWNLOAD_CANCEL = 33;
    public static final int MSG_DOWNLOAD_FAILED = 34;

    static final int PREPARE = 3;
    static final int LOADING = 5;
    static final int LOAD_SUCC = 10;
    static final int LOAD_FAILED = 14;
    static final int LOAD_CANCEL = 16;

    String url;
    long contentLength;
    long downloadLength;
    String filePath;
    boolean canceled = false;
    int downloadState;

    private Context context;
    private Handler downloadHandler;

    public static DownloadTask instance;

    public static DownloadTask getInstance() {
        return  instance;
    }

    public static DownloadTask newInstance(Context context, String url, Handler downloadHandler) {

        if (instance == null) {
            instance = new DownloadTask(context, url, downloadHandler);
            instance.downloadState = PREPARE;
        }

        return instance;
    }

    private DownloadTask(Context context, String url, Handler downloadHandler) {
        this.context = context;
        this.url = url;
        this.downloadHandler = downloadHandler;
    }

    public long getContentLength() {
        return contentLength;
    }

    public int getState() {
        return downloadState;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public String getFileName() {
        return filePath;
    }

    public void cancelTask() {
        canceled = true;
    }

    @Override
    public void run() {

        downloadState = PREPARE;
        String apkName = url.substring(url.lastIndexOf("/") + 1);
        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory(), "quickgame/update");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            dir = context.getCacheDir();
        }

        File apkFile = new File(dir, apkName);
        if (apkFile.exists()) {
            apkFile.delete();
            try {
                apkFile.createNewFile();
                this.filePath = apkFile.getAbsolutePath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (canceled) {
            downloadState = LOAD_CANCEL;
            Message msg = downloadHandler.obtainMessage(MSG_DOWNLOAD_CANCEL);
            Bundle bundle = new Bundle();
            bundle.putInt("status", downloadState);
            bundle.putString("fileName", getFileName());
            msg.obj = bundle;
//            msg.obj = getFileName();
            msg.sendToTarget();
            return;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                downloadState = LOADING;
                contentLength = conn.getContentLength();

//                Message msgStart = downloadHandler.obtainMessage(MSG_DOWNLOAD_START);
//                msgStart.obj = contentLength;
//                msgStart.sendToTarget();

                InputStream inputStream = conn.getInputStream();

                FileOutputStream fos = new FileOutputStream(apkFile);

                downloadLength = 0;
                int bufLength = 1024 * 1024;
                byte[] buf = new byte[1024 * 1024];
                int len = -1;
                while (contentLength > downloadLength && !canceled) {

                    long startTime = System.currentTimeMillis();
                    len = inputStream.read(buf, 0, bufLength);

                    if (len != -1) {
                        downloadLength += len;

                        fos.write(buf, 0, len);

                        long endTime = System.currentTimeMillis();
                        long beTime = endTime - startTime;
                        Message msg = downloadHandler.obtainMessage();
                        msg.what = MSG_DOWNLOAD_PROGRESS;

                        Bundle bundle = new Bundle();
                        bundle.putLong("metaTime", beTime);
                        bundle.putLong("downloadLength", downloadLength);
                        bundle.putLong("totalLength", contentLength);
                        bundle.putInt("metaLength", len);

                        msg.obj = bundle;
//                        msg.obj = beTime;
//                        msg.arg1 = downloadLength;
//                        msg.arg2 = len;
                        msg.sendToTarget();
                    }

                }

                fos.close();
                inputStream.close();
                if (canceled) {
                    downloadState = LOAD_CANCEL;
                    Message msg = downloadHandler.obtainMessage(MSG_DOWNLOAD_CANCEL);
                    msg.obj = getFileName();
                    msg.sendToTarget();
                    return;
                }
                if (contentLength == downloadLength) {
                    downloadState = LOAD_SUCC;
                    Message msg = downloadHandler.obtainMessage(MSG_DOWNLOAD_SUCCESS);
                    msg.obj = filePath;
                    downloadHandler.sendMessage(msg);
                    return;
                }

            } else {
                downloadState = LOAD_FAILED;
                Message msg = downloadHandler.obtainMessage(MSG_DOWNLOAD_FAILED);
                msg.obj = context.getString(R.string.str_err_net);
                msg.sendToTarget();
                return;
            }

        } catch (IOException e) {
            downloadState = LOAD_FAILED;
            Message msg = downloadHandler.obtainMessage(MSG_DOWNLOAD_FAILED);
            msg.obj = context.getString(R.string.str_err_net);
            msg.sendToTarget();
            e.printStackTrace();
            return;
        }

    }

}

