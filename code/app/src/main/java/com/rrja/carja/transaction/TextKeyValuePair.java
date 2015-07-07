package com.rrja.carja.transaction;

/**
 * Created by chongge on 15/7/7.
 */
public class TextKeyValuePair {

    private String name;
    private String value;

    public void TextKeyValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
