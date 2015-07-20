package com.rrja.carja.model;

import android.text.TextUtils;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/7/16.
 * This class is used too get image file from local dir ro from net, connecting with FileBinder.
 */
public class FileRequest {

    public static final String TYPE_STORE = "type_store";
    public static final String TYPE_AVATAR = "type_avatar";
    public static final String TYPE_COUPONS = "typ_coupons";
    public static final String TYPE_DISCOUNT = "type_discount";

    private String type;
    private String id;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUseable() {
        boolean typeEnable =  !TextUtils.isEmpty(type);
        return typeEnable;
    }
}
