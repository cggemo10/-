package com.rrja.carja.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rrja.carja.model.CarBrand;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;
import com.rrja.carja.model.Region;

/**
 * Created by Administrator on 2015/7/19.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "rrja_db";

    private Dao<Coupons, Integer> couponDao = null;
    private Dao<DiscountInfo, Integer> discountDao = null;
    private Dao<Region, Integer> regionDao = null;
    private Dao<CarBrand, Integer> carBrandDao = null;

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
            TableUtils.createTable(connectionSource, DiscountInfo.class);
            TableUtils.createTable(connectionSource, Coupons.class);
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
                TableUtils.dropTable(connectionSource, DiscountInfo.class, true);
                TableUtils.dropTable(connectionSource, Coupons.class, true);
                TableUtils.dropTable(connectionSource, CarBrand.class, true);

                TableUtils.createTable(connectionSource, CarBrand.class);
                TableUtils.createTable(connectionSource, Region.class);
                TableUtils.createTable(connectionSource, DiscountInfo.class);
                TableUtils.createTable(connectionSource, Coupons.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Dao<Coupons, Integer> getCouponsDao() {
        if (couponDao == null) {
            couponDao = getRuntimeExceptionDao(Coupons.class);
        }

        return couponDao;
    }

    public Dao<DiscountInfo, Integer> getDiscountDao(){
        if (discountDao == null) {
            discountDao = getRuntimeExceptionDao(DiscountInfo.class);
        }
        return discountDao;
    }

    public Dao<Region, Integer> getRegionDao() {
        if (regionDao == null) {
            regionDao = getRuntimeExceptionDao(Region.class);
        }
        return regionDao;
    }

    public Dao<CarBrand, Integer> getCarBrandDao() {
        if (carBrandDao == null) {
            carBrandDao = getRuntimeExceptionDao(CarBrand.class);
        }
        return carBrandDao;
    }
}
