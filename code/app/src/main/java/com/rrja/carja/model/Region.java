package com.rrja.carja.model;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

/**
 * Created by chongge on 15/7/15.
 */
@DatabaseTable(tableName = "region")
public class Region {

    public static final int TAG_PROVINCE = 2;
    public static final int TAG_CITY = 3;

    @DatabaseField(unique = true)
    private String code;
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int level;
    @DatabaseField
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    code: "110000",
    id: 2,
    level: 2,
    name: "±±¾©ÊÐ"
     */

    public static Region parse(JSONObject proviceJson) {

        if (proviceJson == null) {
            return null;
        }

        try {
            Region region = new Region();
            region.setId(proviceJson.getInt("id"));
            region.setCode(proviceJson.getString("code"));
            region.setLevel(proviceJson.getInt("level"));
            region.setName(proviceJson.getString("name"));

            return region;

        } catch (Exception e) {

            Log.e("rrja.Region", "city parse error" , e);
            return null;

        }


    }
}
