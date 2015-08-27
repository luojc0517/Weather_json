package com.luojc.weather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * dao层类，主要实现与数据库的交互
 * @author luojc
 * 利用中央台api获取，教程：
 * http://blog.163.com/yuanzhf_2012/blog/static/2112011482012929454663/
 */
public class CityCodeDB {
    // 以下表在assets下data.db数据库下
    public static final String TABLE_PROVINCE = "province";// 省份表
    public static final String TABLE_CITY = "city";// 城市表
    public static final String TABLE_AREA = "area";// 地区表
    public static final String TABLE_CITY_CODE = "city_code";// 城市码表
    private Context context;

    public CityCodeDB(Context context) {
        super();
        this.context = context;
    }

    /**
     * 通过AssetsDatabaseManager工具获取aseets目录下的数据库
     * 
     * @param dbName
     * @return
     */
    SQLiteDatabase getDatabase(String dbName) {
        SQLiteDatabase db;
        AssetsDatabaseManager.initManager(context);
        AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
        db = manager.getDatabase(dbName);
        return db;
    }

    /**
     * 返回所有省份的结果集
     * 
     * @param db
     * @return
     */
    Cursor getAllProvince(SQLiteDatabase db) {
        Cursor cursor;
        if (db != null) {
            cursor = db.query(TABLE_PROVINCE, new String[] {
                    "id", "name"
            }
                    , null, null, null, null, null);
            return cursor;
        } else {
            return null;
        }
    }

    /**
     * 根据省份id获取该省份所有的城市结果集
     * 
     * @param db
     * @param provinceId
     * @return
     */
    Cursor getAllCity(SQLiteDatabase db, String provinceId) {
        Cursor cursor;
        if (db != null) {
            cursor = db.query(TABLE_CITY, new String[] {
                    "id", "p_id", "name"
            }, "p_id=?", new String[] {
                    provinceId
            }, null, null, null, null);
            return cursor;

        } else {
            return null;
        }
    }

    /**
     * 根据城市id获取该城市所有的地区结果集
     * 
     * @param db
     * @param cityId
     * @return
     */
    Cursor getAllArea(SQLiteDatabase db, String cityId) {
        Cursor cursor;
        if (db != null) {
            cursor = db.query(TABLE_AREA, new String[] {
                    "id", "c_id", "name"
            }, "c_id=?", new String[] {
                    cityId
            }, null, null, null);
            return cursor;
        } else {
            return null;
        }
    }

    /**
     * 根据地区id获取城市码
     * 
     * @param db
     * @param areaId
     * @return
     */
    String getCityCodeById(SQLiteDatabase db, String areaId) {
        Cursor cursor;
        String cityCode = null;
        if (db != null) {
            cursor = db.query(TABLE_CITY_CODE, new String[] {
                    "id", "code", "name"
            }, "id=?", new String[] {
                    areaId
            }, null, null, null);
            if (cursor.moveToFirst()) {
                cityCode = cursor.getString(cursor.getColumnIndex("code"));
            }
            return cityCode;
        } else {
            return null;
        }

    }

    /**
     * 根据地区name获取城市码
     * 
     * @param db
     * @param areaName
     * @return
     */
    String getCityCodeByName(SQLiteDatabase db, String areaName) {
        Cursor cursor;
        String cityCode = null;
        if (db != null) {
            cursor = db.query(TABLE_CITY_CODE, new String[] {
                    "id", "code", "name"
            }, "name=?", new String[] {
                    areaName
            }, null, null, null);
            if (cursor.moveToFirst()) {
                cityCode = cursor.getString(cursor.getColumnIndex("code"));

            }
            return cityCode;
        } else {
            return null;
        }
    }

}
