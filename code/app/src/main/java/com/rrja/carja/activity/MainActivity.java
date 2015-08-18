package com.rrja.carja.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.fragment.home.CouponsFragment;
import com.rrja.carja.fragment.home.ForumFragment;
import com.rrja.carja.fragment.home.HomeFragment;
import com.rrja.carja.fragment.home.UserCenterFragment;
import com.rrja.carja.model.Region;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.UserBinder;
import com.rrja.carja.utils.DialogHelper;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static int REQUEST_CITY = 10;

    DrawerLayout mDrawerlayout;
    ActionBarDrawerToggle mDrawerToggle;
    ViewPager fragmentPager;

    HomeFragment homeFragment;
    CouponsFragment couponsFragment;
    ForumFragment forumFragment;

    ImageView imgMenuHome;
    ImageView imgMenuForum;
    ImageView imgMenuCoupons;

    LinearLayout llHOme;
    LinearLayout llForum;
    LinearLayout llDiscount;

    UserBinder userService;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            userService = (UserBinder) service;

            UserInfo user = CoreManager.getManager().getCurrUser();
            if (user == null) {
                SharedPreferences sp = getSharedPreferences("authsp", MODE_PRIVATE);
                String auth = sp.getString("auth", "");
                String tel = sp.getString("tel", "");
                if (!TextUtils.isEmpty(auth) && !TextUtils.isEmpty(tel)) {
                    userService.checkAuth(auth, tel);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            userService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout lllocation = (LinearLayout) toolbar.findViewById(R.id.ll_cur_loc);
        lllocation.setOnClickListener(this);

        //DialogHelper.getHelper().init(this);
        CoreManager.getManager().initCompanyInfo(this);

        if (userService == null) {
            Intent intent = new Intent(this, DataCenterService.class);
            intent.setAction(Constant.ACTION_USER_SERVICE);
            bindService(intent, conn, BIND_AUTO_CREATE);

        }

        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Region costumerRegion = CoreManager.getManager().getCostumerRegion();
        if (costumerRegion != null && !TextUtils.isEmpty(costumerRegion.getName())) {
            txtLoc.setText(costumerRegion.getName());
        }
    }

    @Override
    protected void onDestroy() {
        if (userService != null) {
            unbindService(conn);
        }
        super.onDestroy();
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
//                if (drawerView.getId() == R.id.side_left) {
                setTitle(R.string.title_user_center);
//                    mDrawerlayout.closeDrawer(Gravity.END);
//                }
//                if (drawerView.getId() == R.id.side_right) {
//                    setTitle(R.string.title_my_city);
//                    mDrawerlayout.closeDrawer(Gravity.START);
//                }
                super.onDrawerOpened(drawerView);
                
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };

        mDrawerToggle.syncState();

        mDrawerlayout.setDrawerListener(mDrawerToggle);

        homeFragment = HomeFragment.getFragment();
        couponsFragment = CouponsFragment.getFragment();
        forumFragment = ForumFragment.getFragment();

        fragmentPager = (ViewPager) findViewById(R.id.pager_main_content);
        fragmentPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return homeFragment;
                    case 1:
                        return couponsFragment;
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
        imgMenuCoupons = (ImageView) findViewById(R.id.img_main_menu_onsale);

        llHOme = (LinearLayout) findViewById(R.id.ll_menu_home);
        llHOme.setOnClickListener(this);
        llForum = (LinearLayout) findViewById(R.id.ll_menu_forum);
        llForum.setOnClickListener(this);
        llDiscount = (LinearLayout) findViewById(R.id.ll_menu_onsale);
        llDiscount.setOnClickListener(this);

        imgMenuHome.setImageLevel(1);
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
                imgMenuCoupons.setImageLevel(0);
                break;
            case 1:
                imgMenuHome.setImageLevel(0);
                imgMenuCoupons.setImageLevel(1);
                imgMenuForum.setImageLevel(0);
                break;
            case 2:
                imgMenuHome.setImageLevel(0);
                imgMenuCoupons.setImageLevel(0);
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
            case R.id.ll_cur_loc:
                Intent intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, REQUEST_CITY);
                break;
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO
    }

    //------------------------------------------------------------------------------------------------------------fragment interaction

    public CouponsFragment.OnBigDiscountInteractionListener getBigDiscountInteraction() {
        return new BigDiscountInteraction();
    }

    private class BigDiscountInteraction implements CouponsFragment.OnBigDiscountInteractionListener {

        @Override
        public void requestCouponsData(int page) {

            if (userService == null) {
                Intent intent = new Intent(MainActivity.this, DataCenterService.class);
                intent.setAction(Constant.ACTION_DATA_GET_COUPONS_GOODS);
                intent.putExtra("page",page);
                startService(intent);
            } else {
                userService.getCouponsGoods(page);
            }
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
        public void requestRecommendData(int page) {

            if (userService == null) {
                Intent intent = new Intent(MainActivity.this, DataCenterService.class);
                intent.setAction(Constant.ACTION_DATA_GET_RECOMMEND);
                intent.putExtra("page",page);
                startService(intent);
            } else {
                userService.getRecommendGoods(page);
            }

        }
    }

    public UserCenterFragment.OnUserCenterInteractionListener getUserCenterInteraction() {
        return new UserCenterInteraction();
    }

    private class UserCenterInteraction implements UserCenterFragment.OnUserCenterInteractionListener {

        @Override
        public void loginInteraction() {
            mDrawerlayout.closeDrawers();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @Override
        public void modifyNickname() {

        }
    }
}
