package com.jayangche.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ScrollView;

/**
 * Created by chongge on 15/6/26.
 */
public class CareInfo implements Parcelable{

    private String id;
    private String frameNo6;
    private String buyTime;
    private String engineNo;
    private String phoneNo;

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

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id + "");
        dest.writeString(frameNo6);
        dest.writeString(buyTime);
        dest.writeString(engineNo);
        dest.writeString(phoneNo);

    }

    public static Creator<CareInfo> CREATOR = new Creator<CareInfo>() {
        @Override
        public CareInfo createFromParcel(Parcel source) {
            CareInfo info = new CareInfo();
            String id = source.readString();
            if (!"null".equals(id)) {
                info.setId(id);
            }
            info.setFrameNo6(source.readString());
            info.setBuyTime(source.readString());
            info.setEngineNo(source.readString());
            info.setPhoneNo(source.readString());
            return info;
        }

        @Override
        public CareInfo[] newArray(int size) {
            return new CareInfo[size];
        }
    };
}
