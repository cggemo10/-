package com.rrja.carja.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.rrja.carja.R;
import com.rrja.carja.RRjaApplication;
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
import com.rrja.carja.utils.RrjaUtils;

import org.apache.http.Consts;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


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
//    LinearLayout llForum;
    LinearLayout llDiscount;

    UserBinder userService;

    LocationChangeListener locationChangeListener;

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

        locationChangeListener = new LocationChangeListener();
        ((RRjaApplication)getApplication()).registLocationChangeListener(MainActivity.class.getName(), locationChangeListener);
        ((RRjaApplication)getApplication()).requestLocation();
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

        if (locationChangeListener != null) {
            ((RRjaApplication)getApplication()).unRegistLocationChangeListener(MainActivity.class.getName());
            locationChangeListener = null;
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
//                    case 2:
//                        return forumFragment;
                }

                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        fragmentPager.setOnPageChangeListener(this);

        imgMenuHome = (ImageView) findViewById(R.id.img_main_menu_home);
        imgMenuForum = (ImageView) findViewById(R.id.img_main_menu_forum);
        imgMenuCoupons = (ImageView) findViewById(R.id.img_main_menu_onsale);

        llHOme = (LinearLayout) findViewById(R.id.ll_menu_home);
        llHOme.setOnClickListener(this);
//        llForum = (LinearLayout) findViewById(R.id.ll_menu_forum);
//        llForum.setOnClickListener(this);
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
//            case R.id.ll_menu_forum:
//                fragmentPager.setCurrentItem(2, true);
//                break;
            case R.id.ll_cur_loc:
                Intent intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, REQUEST_CITY);
                break;
        }
    }

    public static final int TAKE_PICTURE = 20;
    public static final int MAKE_PICTURE = 21;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!RrjaUtils.getNetworkStatus(this)) {
            Toast.makeText(this, R.string.str_err_net, Toast.LENGTH_LONG).show();
            return;
        }

        String avatarPath = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    // Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(),
                    // options); // 此时返回bm为空
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
                    options.inJustDecodeBounds = false;
                    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                    int be = (int) (options.outHeight / (float) 300);
                    if (be <= 0)
                        be = 1;
                    options.inSampleSize = be;
                    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);

                    // Bitmap img = BitmapFactory.decodeStream(fis);
                    // imageCache.put(position, bitmap);
                    // imgHead.setImageBitmap(bitmap);
                    File dir = new File(Constant.getUserAvatarCacheDir());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String fileName = CoreManager.getManager().getCurrUser().getId() + ".jpg";
                    File myCaptureFile = new File(dir, fileName);
                    FileOutputStream fos = new FileOutputStream(myCaptureFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                    avatarPath = myCaptureFile.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "解析文件失败，请重新选择图片", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else if (requestCode == MAKE_PICTURE) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                // imgHead.setImageBitmap(bm);// 想图像显示在ImageView视图上，private
                // ImageView
                // img;
                File myCaptureFile = new File(getFilesDir(), UUID.randomUUID().toString() + ".jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
					/*
					 * 采用压缩转档方法 bm.compress(Bitmap.CompressFormat.JPEG, 80,
					 * bos); /* 调用flush()方法，更新BufferStream bos.flush();
					 * 结束OutputStream
					 */
                    bos.close();
                    bm.recycle();
                    // imgHead.setImageBitmap(BitmapFactory
                    // .decodeStream(new FileInputStream(myCaptureFile)));
                    avatarPath = myCaptureFile.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "解析文件失败，请重新选择图片", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            if (!TextUtils.isEmpty(avatarPath)) {

                DialogHelper.getHelper().showWaitting();
                userService.updateAvatar(new File(avatarPath));

            }
        }

        super.onActivityResult(requestCode, resultCode, data);

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

    // ---------------------------------------------------------------------------------------------
    private class LocationChangeListener implements RRjaApplication.OnLocationChangeListener {

        @Override
        public void onLocationChanged(BDLocation location) {


                if (!CoreManager.getManager().isCustomeChange()) {
                    Region gpsRegion = CoreManager.getManager().getGpsRegion();
                    if (gpsRegion != null) {
                        txtLoc.setText(gpsRegion.getName());
                    }
                }


        }
    }

}
