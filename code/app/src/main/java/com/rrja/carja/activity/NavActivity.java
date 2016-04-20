package com.rrja.carja.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.rrja.carja.R;

import java.util.ArrayList;
import java.util.List;

public class NavActivity extends BaseActivity {

//    private BNRoutePlanNode mBNRoutePlanNode = null;

    PopupWindow endPop;
    View endView;
    TextView txtEndNav;
    AppCompatButton endBtn;
    AppCompatButton continueBtn;

    private View navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        navView = BNRouteGuideManager.getInstance().onCreate(this, new BNRouteGuideManager.OnNavigationListener() {

            @Override
            public void onNaviGuideEnd() {

                showEndNavPop(true);

            }

            @Override
            public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
                Log.e(NavActivity.this.getClass().getName(),
                        "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
            }
        });

        if (navView != null) {
            setContentView(navView);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
//                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(BNDemoMainActivity.ROUTE_PLAN_NODE);
            }
        }

        initEndView();
    }

    private void showEndNavPop(boolean isNavEnd) {

        if (isNavEnd) {
            continueBtn.setVisibility(View.GONE);
            txtEndNav.setText(R.string.str_end_nav);
        } else {
            continueBtn.setVisibility(View.VISIBLE);
            txtEndNav.setText(R.string.str_finish_nav);
        }

        if (endPop != null && endPop.isShowing()) {
            endPop.dismiss();
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        endPop = new PopupWindow(endView, metrics.widthPixels, (int) (200 * metrics.density));
        endPop.setContentView(endView);
        endPop.setOutsideTouchable(false);

        endPop.showAtLocation(navView, Gravity.BOTTOM, 0, 0);

    }

    private void initEndView() {
        endView = getLayoutInflater().inflate(R.layout.view_end_nav, null);
        txtEndNav = (TextView) endView.findViewById(R.id.txt_title_end_nav);
        endBtn = (AppCompatButton) endView.findViewById(R.id.btn_end_nav);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPop.dismiss();
                finish();
            }
        });
        continueBtn = (AppCompatButton) endView.findViewById(R.id.btn_continue_nav);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPop.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        BNRouteGuideManager.getInstance().onResume();
        super.onResume();
        if (hd != null) {
            hd.sendEmptyMessageDelayed(MSG_SHOW, 5000);
        }
    }

    protected void onPause() {
        super.onPause();
        BNRouteGuideManager.getInstance().onPause();
    }

    

    @Override
    protected void onDestroy() {
        BNRouteGuideManager.getInstance().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        BNRouteGuideManager.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        BNRouteGuideManager.getInstance().onBackPressed(false);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    

    private void addCustomizedLayerItems() {
//        List<BNRouteGuideManager.CustomizedLayerItem> items = new ArrayList<BNRouteGuideManager.CustomizedLayerItem>();
//        BNRouteGuideManager.CustomizedLayerItem item1 = null;
//        if (mBNRoutePlanNode != null) {
//            item1 = new BNRouteGuideManager.CustomizedLayerItem(mBNRoutePlanNode.getLongitude(), mBNRoutePlanNode.getLatitude(),
//                    mBNRoutePlanNode.getCoordinateType(), getResources().getDrawable(R.mipmap.ic_launcher), BNRouteGuideManager.CustomizedLayerItem.ALIGN_CENTER);
//            items.add(item1);
//
//            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
//        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
//						hd.sendEmptyMessageDelayed(MSG_HIDE, 5000);
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
                    }

                }

                
            };
        }
    }

    private void dissmissAvatarPop() {
        if (endPop != null && endPop.isShowing()) {
            endPop.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (endPop != null && endPop.isShowing()) {
                dissmissAvatarPop();
                finish();
                return true;
            } else {
                showEndNavPop(false);
            }

        }
        return super.dispatchKeyEvent(event);
    }
}
