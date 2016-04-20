package com.rrja.carja.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.usercoupons.TimeoutFragment;
import com.rrja.carja.fragment.usercoupons.UnuseFragment;
import com.rrja.carja.fragment.usercoupons.UsedFragment;
import com.rrja.carja.model.coupons.UserCoupons;

public class UserCouponsActivity extends BaseActivity implements View.OnClickListener {

    private int currTag = 0;

    TextView txtUnuseTag;
    TextView txtUsedTag;
    TextView txtTimeoutTag;

    Fragment unusedFragment;
    Fragment usedFragment;
    Fragment timeoutFragment;

    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coupons);

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

        initFragment();

        switchTag(Constant.TITLE_TAG_UNUSE);
    }

    private void initFragment() {
        usedFragment = UsedFragment.newInstance();
        unusedFragment = UnuseFragment.newInstance();
        timeoutFragment = TimeoutFragment.newInstance();

        fm = getSupportFragmentManager();
    }

    private void initView() {
        txtUnuseTag = (TextView) findViewById(R.id.txt_coupons_unuse_tag);
        txtUnuseTag.setOnClickListener(this);
        txtUsedTag = (TextView) findViewById(R.id.txt_coupons_used_tag);
        txtUsedTag.setOnClickListener(this);
        txtTimeoutTag = (TextView) findViewById(R.id.txt_coupons_timeout_tag);
        txtTimeoutTag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.txt_coupons_unuse_tag:
                switchTag(Constant.TITLE_TAG_UNUSE);
                break;
            case R.id.txt_coupons_used_tag:
                switchTag(Constant.TITLE_TAG_USED);
                break;
            case R.id.txt_coupons_timeout_tag:
                switchTag(Constant.TITLE_TAG_TIMEOUT);
                break;
        }
    }

    private void switchTag(int tag) {
        if (currTag == tag) {
            return;
        }

        currTag = tag;
        switchTitle(currTag);
        switchFragment(currTag);
    }

    private void switchFragment(int tag) {

        FragmentTransaction transaction = fm.beginTransaction();

        if (tag == Constant.TITLE_TAG_UNUSE) {
            transaction.replace(R.id.fl_usercoupons, unusedFragment);
        }

        if (tag == Constant.TITLE_TAG_USED) {
            transaction.replace(R.id.fl_usercoupons, usedFragment);
        }

        if (tag == Constant.TITLE_TAG_TIMEOUT) {
            transaction.replace(R.id.fl_usercoupons, timeoutFragment);
        }

        transaction.commitAllowingStateLoss();
    }

    private void switchTitle(int tag) {
        if (tag == Constant.TITLE_TAG_UNUSE) {
            txtUnuseTag.setBackgroundColor(Color.TRANSPARENT);
            txtUsedTag.setBackgroundColor(Color.WHITE);
            txtTimeoutTag.setBackgroundColor(Color.WHITE);
        }

        if (tag == Constant.TITLE_TAG_USED) {
            txtUnuseTag.setBackgroundColor(Color.WHITE);
            txtUsedTag.setBackgroundColor(Color.TRANSPARENT);
            txtTimeoutTag.setBackgroundColor(Color.WHITE);
        }

        if (tag == Constant.TITLE_TAG_TIMEOUT) {
            txtUnuseTag.setBackgroundColor(Color.WHITE);
            txtUsedTag.setBackgroundColor(Color.WHITE);
            txtTimeoutTag.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    // ---------------------------------------------------------------------------------------------
    public UnuseActionListener getUnuseListener() {
        return new UnuseActionListener();
    }

    private class UnuseActionListener implements UnuseFragment.OnUnuseActionListener {

        @Override
        public void onCouponsClicked(UserCoupons coupons) {

        }
    }

    public UsedFragment.OnUsedActionListener getUsedListener() {
        return new UsedActionListener();
    }

    private class UsedActionListener implements UsedFragment.OnUsedActionListener {

        @Override
        public void onCouponsClicked(UserCoupons coupons) {

        }
    }

    public TimeoutFragment.OnTimeoutActionListener getTimeoutListener() {
        return new TimeoutActionListener();
    }

    private class TimeoutActionListener implements TimeoutFragment.OnTimeoutActionListener {

        @Override
        public void onCouponsClicked(UserCoupons coupons) {

        }
    }
}
