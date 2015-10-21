package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.FeedbackBinder;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private EditText edTitle;
    private EditText edContent;
    private AppCompatButton btnCommit;

    private FeedbackBinder feedbackService;
    private FeedbackReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initView();
        bindService();
        registReceiver();
    }

    @Override
    protected void onDestroy() {
        unBindService();
        unregistReceiver();
        super.onDestroy();
    }

    private void initView() {
        edTitle = (EditText) findViewById(R.id.ed_feedback_title);
        edTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edContent.setFocusable(true);
                }
                return false;
            }
        });
        edContent = (EditText) findViewById(R.id.ed_feedback_content);
        edContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    boolean canCommit = checkContent();
                    if (canCommit) {
                        commitContent();
                    }
                }
                return false;
            }
        });
        btnCommit = (AppCompatButton) findViewById(R.id.btn_feedback_commit);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_feedback_commit) {
            boolean canCommit = checkContent();
            if (canCommit) {
                commitContent();
            }
        }
    }

    private boolean checkContent() {
        boolean canBeCommit = false;
        if (TextUtils.isEmpty(edTitle.getText().toString())) {
            Toast.makeText(this, R.string.str_err_feedtitle_empty, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(edTitle.getText().toString())) {
            Toast.makeText(this, R.string.str_err_feedcontent_empty, Toast.LENGTH_LONG).show();
        } else {
            canBeCommit = true;
            btnCommit.setEnabled(false);
        }
        return canBeCommit;
    }

    private void commitContent() {
        String title = edTitle.getText().toString();
        String content = edContent.getText().toString();
        feedbackService.commitFeedback(title, content);
    }

    private void bindService() {

        if (feedbackService == null) {
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_FEEDBACK_SERVICE);
            bindService(intent, conn, BIND_AUTO_CREATE);
        }
    }

    private void unBindService() {
        if (feedbackService != null) {
            unbindService(conn);
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            feedbackService = (FeedbackBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            feedbackService = null;
        }
    };

    private void registReceiver() {

        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_FEEDBACK);
            filter.addAction(Constant.ACTION_BROADCAST_FEEDBACK_ERR);

            mReceiver = new FeedbackReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unregistReceiver() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private class FeedbackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_FEEDBACK.equals(action)) {
                btnCommit.setEnabled(true);


                FeedbackActivity.this.finish();
            }

            if (Constant.ACTION_BROADCAST_FEEDBACK_ERR.equals(action)) {
                btnCommit.setEnabled(true);
                String message = getString(R.string.str_err_net);
                if (intent.hasExtra("description")) {
                    message = intent.getStringExtra("description");
                }

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
