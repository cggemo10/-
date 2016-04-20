package com.rrja.carja.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.FileService;

import org.apache.http.util.TextUtils;

public class SplshActivity extends Activity implements Handler.Callback {

    Handler mHandler;

    private static final int MSG_TIMEEND = 11;
    private static final int MSG_UPDATE = 12;

    private static final String TAG = SplshActivity.class.getName();

    private boolean timeEnd = false;
    private Boolean needUpdate;

    private UpdateReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CoreManager.getManager().init(this);

        //DialogHelper.getHelper().init(this);

        Intent service = new Intent(this, DataCenterService.class);
        service.setAction(Constant.ACTION_INIT_SERVICE);
        startService(service);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splsh);

        mHandler = new Handler(this);
    }

    @Override
    protected void onStart() {

        super.onStart();

        registReceiver();

        checkUpdate();

        mHandler.sendEmptyMessageDelayed(MSG_TIMEEND, 3000);
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_UPDATE);

            mReceiver = new UpdateReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    @Override
    protected void onStop() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }


        super.onStop();
    }

    private void checkUpdate() {
        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_CHECK_UPDATE);
        startService(intent);
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_TIMEEND:
                timeEnd = true;
                break;
            case MSG_UPDATE:
                if (msg.arg1 == 1) {
                    needUpdate = false;
                } else {
                    needUpdate = true;
                    Bundle bundle = (Bundle) msg.obj;
                    int versionCode = bundle.getInt("VersionCode");
                    String versionUrl = bundle.getString("VersionUrl");
                    String versionDesc = bundle.getString("VersionDesc");

                    showUpdateDialog(versionCode, versionUrl, versionDesc);
                }


        }

        if (timeEnd && needUpdate != null && !needUpdate) {
            Intent intent = new Intent(SplshActivity.this, MainActivity.class);
            startActivity(intent);
            SplshActivity.this.finish();
            return true;
        }

        return false;
    }

    private void showUpdateDialog(int versionCode, final String url, String desc) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("发现新版");

        StringBuffer buffer = new StringBuffer();
        buffer.append("版本号：" + versionCode + "\n");
        buffer.append("更新内容：" + desc);

        builder.setMessage(buffer.toString());
        builder.setCancelable(false);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                needUpdate = false;

                Intent intent = new Intent(SplshActivity.this, FileService.class);
                intent.setAction(FileService.ACTION_APP_UPDATE);
                intent.putExtra("app_url", url);
                startService(intent);

                mHandler.sendEmptyMessage(1);
            }
        });
        builder.show();
    }

    private class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_UPDATE.equals(action)) {

                int version = intent.getIntExtra("updateVersion", -1);
                String desc = intent.getStringExtra("version_desc");
                String url = intent.getStringExtra("version_url");

                Log.e(TAG, "online version:" + version);
                Log.e(TAG, "online version:" + desc);
                Log.e(TAG, "online version:" + url);

                if (version > Constant.VERSION && !TextUtils.isEmpty(url)) {
                    Message msg = mHandler.obtainMessage(MSG_UPDATE);
                    msg.arg1 = 0;
                    Bundle bundle = new Bundle();
                    bundle.putInt("VersionCode", version);
                    bundle.putString("VersionUrl", url);
                    bundle.putString("VersionDesc", desc);
                    msg.obj = bundle;
                    msg.sendToTarget();
                } else {
                    Message msg = mHandler.obtainMessage(MSG_UPDATE);
                    msg.arg1 = 1;
                    msg.sendToTarget();
                }
            }
        }
    }
}
