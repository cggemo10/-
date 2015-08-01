package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;


@DatabaseTable(tableName = "car_brand")
public class CarBrand implements Parcelable{

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String firstLetter;
    @DatabaseField
    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(firstLetter);
        dest.writeString(logo);

    }

    public static Parcelable.Creator<CarBrand> CREATOR = new Parcelable.Creator<CarBrand>() {

        @Override
        public CarBrand createFromParcel(Parcel source) {

            CarBrand brand = new CarBrand();
            brand.setId(source.readString());
            brand.setName(source.readString());
            brand.setFirstLetter(source.readString());
            brand.setLogo(source.readString());

            return brand;
        }

        @Override
        public CarBrand[] newArray(int size) {
            return new CarBrand[size];
        }
    };

    public static CarBrand parse(JSONObject brandJson) {

        if (brandJson == null) {
            return null;
        }

        try {
            CarBrand brand = new CarBrand();
            brand.setId(brandJson.getInt("id") + "");
            brand.setName(brandJson.getString("name"));
            brand.setFirstLetter(brandJson.getString("firstLetter"));
            brand.setLogo(brandJson.getString("logo"));

            return brand;
        } catch (Exception e) {
            return null;
        }

    }
}
