package com.jayangche.android.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayangche.android.R;

/**
 * Created by chongge on 15/5/27.
 */
public class BaseActivity extends ActionBarActivity {

    Toolbar toolbar;
    TextView toolbarTitle;
    LinearLayout llloc;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        View t = findViewById(R.id.titlebar);
        if (t != null) {
            toolbar = (Toolbar) t;
            setSupportActionBar(toolbar);
            llloc = (LinearLayout) toolbar.findViewById(R.id.ll_cur_loc);
            toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);

        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }
}
