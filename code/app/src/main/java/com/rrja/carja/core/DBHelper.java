package com.rrja.carja.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rrja.carja.model.Coupons;
import com.rrja.carja.model.DiscountInfo;

/**
 * Created by Administrator on 2015/7/19.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "rrja_db";

    private Dao<Coupons, Integer> couponDao = null;
    private Dao<DiscountInfo, Integer> discountDao = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, DiscountInfo.class);
            TableUtils.createTable(connectionSource, Coupons.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (oldVersion < newVersion) {
                TableUtils.dropTable(connectionSource, DiscountInfo.class, true);
                TableUtils.dropTable(connectionSource, Coupons.class, true);

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
}
