package com.jayangche.android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayangche.android.R;
import com.jayangche.android.fragment.home.BigDiscountFragment;
import com.jayangche.android.fragment.home.ForumFragment;
import com.jayangche.android.fragment.home.HomeFragment;
import com.jayangche.android.fragment.home.UserCenterFragment;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    DrawerLayout mDrawerlayout;
    ActionBarDrawerToggle mDrawerToggle;
    ViewPager fragmentPager;

    HomeFragment homeFragment;
    BigDiscountFragment discountFragment;
    ForumFragment forumFragment;

    ImageView imgMenuHome;
    ImageView imgMenuForum;
    ImageView imgMenuDiscount;

    LinearLayout llHOme;
    LinearLayout llForum;
    LinearLayout llDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(R.string.title_main_page);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(R.string.title_user_center);
            }
        };
        mDrawerToggle.syncState();

        mDrawerlayout.setDrawerListener(mDrawerToggle);

        homeFragment = HomeFragment.getFragment();
        discountFragment = BigDiscountFragment.getFragment();
        forumFragment = ForumFragment.getFragment();

        fragmentPager = (ViewPager) findViewById(R.id.pager_main_content);
        fragmentPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return homeFragment;
                    case 1:
                        return discountFragment;
                    case 2:
                        return forumFragment;
                }

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        fragmentPager.setOnPageChangeListener(this);

        imgMenuHome = (ImageView) findViewById(R.id.img_main_menu_home);
        imgMenuForum = (ImageView) findViewById(R.id.img_main_menu_forum);
        imgMenuDiscount = (ImageView) findViewById(R.id.img_main_menu_onsale);

        llHOme = (LinearLayout) findViewById(R.id.ll_menu_home);
        llHOme.setOnClickListener(this);
        llForum = (LinearLayout) findViewById(R.id.ll_menu_forum);
        llForum.setOnClickListener(this);
        llDiscount = (LinearLayout) findViewById(R.id.ll_menu_onsale);
        llDiscount.setOnClickListener(this);

        imgMenuHome.setImageLevel(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                imgMenuHome.setImageLevel(1);
                imgMenuForum.setImageLevel(0);
                imgMenuDiscount.setImageLevel(0);
                break;
            case 1:
                imgMenuHome.setImageLevel(0);
                imgMenuDiscount.setImageLevel(1);
                imgMenuForum.setImageLevel(0);
                break;
            case 2:
                imgMenuHome.setImageLevel(0);
                imgMenuDiscount.setImageLevel(0);
                imgMenuForum.setImageLevel(1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_menu_home:
                fragmentPager.setCurrentItem(0, true);
                break;
            case R.id.ll_menu_onsale:
                fragmentPager.setCurrentItem(1, true);
                break;
            case R.id.ll_menu_forum:
                fragmentPager.setCurrentItem(2, true);
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------fragment interaction

    public BigDiscountFragment.OnBigDiscountInteractionListener getBigDiscountInteraction() {
        return new BigDiscountInteraction();
    }

    private class BigDiscountInteraction implements BigDiscountFragment.OnBigDiscountInteractionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    public ForumFragment.OnForumInteractionListener getForumInteraction() {
        return new ForumInteraction();
    }

    private class ForumInteraction implements ForumFragment.OnForumInteractionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    public HomeFragment.OnHomeInteractionListener getHomeInteraction() {
        return new HomeInteraction();
    }

    private class HomeInteraction implements HomeFragment.OnHomeInteractionListener {

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }

    public UserCenterFragment.OnUserCenterInteractionListener getUserCenterInteraction() {
        return new UserCenterInteraction();
    }

    private class UserCenterInteraction implements UserCenterFragment.OnUserCenterInteractionListener {

        @Override
        public void loginInteraction() {

        }

        @Override
        public void modifyNickname() {

        }
    }
}
