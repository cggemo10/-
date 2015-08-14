package com.rrja.carja.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.CouponGoods;
import com.rrja.carja.model.RecommendGoods;
import com.rrja.carja.model.Region;


public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "rrja_db";

    private RuntimeExceptionDao<CouponGoods, Integer> couponDao = null;
    private RuntimeExceptionDao<RecommendGoods, Integer> discountDao = null;
    private RuntimeExceptionDao<Region, Integer> regionDao = null;
    private RuntimeExceptionDao<CarBrand, Integer> carBrandDao = null;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static DBHelper instance;
    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, RecommendGoods.class);
            TableUtils.createTable(connectionSource, CouponGoods.class);
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, CarBrand.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (oldVersion < newVersion) {
                TableUtils.dropTable(connectionSource, Region.class, true);
                TableUtils.dropTable(connectionSource, RecommendGoods.class, true);
                TableUtils.dropTable(connectionSource, CouponGoods.class, true);
                TableUtils.dropTable(connectionSource, CarBrand.class, true);

                TableUtils.createTable(connectionSource, CarBrand.class);
                TableUtils.createTable(connectionSource, Region.class);
                TableUtils.createTable(connectionSource, RecommendGoods.class);
                TableUtils.createTable(connectionSource, CouponGoods.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public RuntimeExceptionDao<CouponGoods, Integer> getCouponsDao() {
        if (couponDao == null) {
            couponDao = getRuntimeExceptionDao(CouponGoods.class);
        }

        return couponDao;
    }

    public RuntimeExceptionDao<RecommendGoods, Integer> getDiscountDao(){
        if (discountDao == null) {
            discountDao = getRuntimeExceptionDao(RecommendGoods.class);
        }
        return discountDao;
    }

    public RuntimeExceptionDao<Region, Integer> getRegionDao() {
        if (regionDao == null) {
            regionDao = getRuntimeExceptionDao(Region.class);
        }
        return regionDao;
    }

    public RuntimeExceptionDao<CarBrand, Integer> getCarBrandDao() {
        if (carBrandDao == null) {
            carBrandDao = getRuntimeExceptionDao(CarBrand.class);
        }
        return carBrandDao;
    }
}
