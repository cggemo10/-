package com.rrja.carja.model;

/**
 * Created by chongge on 15/7/15.
 */
public class Region {

    public static final int TAG_PROVINCE = 2;
    public static final int TAG_CITY = 3;

    private int code;
    private int id;
    private int level;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
