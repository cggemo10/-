package com.rrja.carja.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;

/**
 * Created by chongge on 15/5/27.
 */
public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle;
    LinearLayout llloc;
    TextView txtLoc;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        View t = findViewById(R.id.titlebar);
        if (t != null) {
            toolbar = (Toolbar) t;
            setSupportActionBar(toolbar);
            llloc = (LinearLayout) toolbar.findViewById(R.id.ll_cur_loc);
            txtLoc = (TextView) llloc.findViewById(R.id.txt_location);
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
