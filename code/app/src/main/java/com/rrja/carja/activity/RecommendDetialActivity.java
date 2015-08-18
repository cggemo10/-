package com.rrja.carja.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.service.FileService;

import java.io.File;
import java.text.SimpleDateFormat;

public class RecommendDetialActivity extends BaseActivity {

    private static final String TAG = "rrja.RecommendDetial";

    RecommendGoods currDiscount;

    ImageView imgDiscount;
    TextView discountScop;
    TextView discountTime;
    TextView discountContent;
    TextView discountTel;
    TextView discountDetal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("recommend_info")) {
            currDiscount = extras.getParcelable("recommend_info");

        }

        if(currDiscount == null) {
            finish();
        }

        initView();
    }

    private void initView() {
        setTitle(currDiscount.getName());
        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        try {
//            imgDiscount.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("juyouhui-img.jpg")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // TODO if show title

        discountScop = (TextView) findViewById(R.id.txt_item_coupons_scope_content);
        discountScop.setText(currDiscount.getScope());

        discountTime = (TextView) findViewById(R.id.txt_item_discount_time_content);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String time = format.format(currDiscount.getStartTime()) + " 至\n " + format.format(currDiscount.getEndTime());
        discountTime.setText(time);

        discountContent = (TextView) findViewById(R.id.txt_item_discount_content_content);
        discountContent.setText(currDiscount.getContent());

        discountTel = (TextView) findViewById(R.id.txt_item_discount_mobile_content);
        discountTel.setText(currDiscount.getMobileNo());

        discountDetal = (TextView) findViewById(R.id.txt_item_discount_detial_content);
        discountDetal.setText(currDiscount.getDetial());

        imgDiscount = (ImageView) findViewById(R.id.img_discount);
        String picUrl = currDiscount.getImgUrl();
        if (!TextUtils.isEmpty(picUrl)) {

            String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);

            File img = new File(Constant.getRecommendCacheDir(), fileName);
            if (img.exists()) {
                try {
                    imgDiscount.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }

            } else {
                Intent intent = new Intent(this, FileService.class);
                intent.setAction(FileService.ACTION_IMG_DISCOUNT);
                intent.putExtra("recommend_info", currDiscount);
                startService(intent);
            }

        }




    }

}
