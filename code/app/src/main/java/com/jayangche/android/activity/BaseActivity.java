package com.jayangche.android.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jayangche.android.R;

/**
 * Created by chongge on 15/5/27.
 */
public class BaseActivity extends ActionBarActivity {

    Toolbar toolbar;
    TextView toolbarTitle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        View t = findViewById(R.id.titlebar);
        if (t != null) {
            toolbar = (Toolbar) t;
            setSupportActionBar(toolbar);
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
