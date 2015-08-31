package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable(tableName = "car_info")
public class CarInfo implements Parcelable {


    private String id = "0";
    private String carImg;

    private String frameNo6;
    private String engineNo;
    private String platNum;

    private String seriesId;
    private String seriesName;

    private String brandId;
    private String brandName;

    private String modelId;
    private String modelName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrameNo6() {
        return frameNo6;
    }

    public void setFrameNo6(String frameNo6) {
        this.frameNo6 = frameNo6;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public void setSeriesName(String name) {
        this.seriesName = name;
    }

    public void setSeriesId(String id) {
        this.seriesId = id;
    }

    public void setCarBrandName(String name) {
        this.brandName = name;
    }

    public void setCarBrandId(String id) {
        this.brandId = id;
    }

    public void setModelName(String name) {
        this.modelName = name;
    }

    public void setModelId(String id) {
        this.modelId = id;
    }

    public void setPlatNum(String platNum) {
        this.platNum = platNum;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public String getPlatNum() {
        return this.platNum;
    }

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id + "");
        dest.writeString(carImg);
        dest.writeString(frameNo6);

        dest.writeString(engineNo);
        dest.writeString(seriesName);
        dest.writeString(seriesId);
        dest.writeString(brandName);
        dest.writeString(brandId);
        dest.writeString(modelName);
        dest.writeString(modelId);
        dest.writeString(platNum);

    }

    public static Creator<CarInfo> CREATOR = new Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel source) {
            CarInfo info = new CarInfo();

            info.setId(source.readString());
            info.setCarImg(source.readString());
            info.setFrameNo6(source.readString());

            info.setEngineNo(source.readString());
            info.setSeriesName(source.readString());
            info.setSeriesId(source.readString());
            info.setCarBrandName(source.readString());
            info.setCarBrandId(source.readString());
            info.setModelName(source.readString());
            info.setModelId(source.readString());
            info.setPlatNum(source.readString());
            return info;
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };

    public boolean isDataEmpty() {
        return TextUtils.isEmpty(brandId) || TextUtils.isEmpty(seriesId) || TextUtils.isEmpty(modelId);
    }

    public static CarInfo parse(JSONObject js) throws JSONException {
        if (js == null || js.length() == 0) {
            return null;
        }

        CarInfo info = new CarInfo();
        info.setId(js.getInt("id") + "");
        info.setCarImg(js.getString("carImage"));
        info.setPlatNum(js.getString("plateNumber"));
        info.setEngineNo(js.getString("engineNumber"));
        info.setFrameNo6(js.getString("frameNumber"));

        info.setCarBrandId(js.getInt("carBrandId") + "");
        info.setCarBrandName(js.getString("carBrandName"));

        info.setModelId(js.getInt("carModelId") + "");
        info.setModelName(js.getString("carModelName"));

        info.setSeriesId(js.getInt("carSeriesId") + "");
        info.setSeriesName(js.getString("carSeriesName"));

        return info;
    }
}
