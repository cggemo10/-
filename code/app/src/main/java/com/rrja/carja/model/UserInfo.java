package com.rrja.carja.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chongge on 15/5/31.
 */
@DatabaseTable(tableName = "user")
public class UserInfo implements Parcelable {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String authToken;
    @DatabaseField
    private String nikeName;
    @DatabaseField
    private String tel;
    @DatabaseField
    private String userType;
    @DatabaseField
    private String level;
    @DatabaseField
    private int userStoreId;
    @DatabaseField
    private String avatarPath;

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

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getUserStoreId() {
        return userStoreId;
    }

    public void setUserStoreId(int userStoreId) {
        this.userStoreId = userStoreId;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(authToken);
        dest.writeString(nikeName);
        dest.writeString(tel);
        dest.writeString(userType);
        dest.writeString(level);
        dest.writeInt(userStoreId);
        dest.writeString(avatarPath);
    }

    public static Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            UserInfo info = new UserInfo();
            info.setId(source.readString());
            info.setName(source.readString());
            info.setAuthToken(source.readString());
            info.setNikeName(source.readString());
            info.setTel(source.readString());
            info.setUserType(source.readString());
            info.setLevel(source.readString());
            info.setUserStoreId(source.readInt());
            info.setAvatarPath(source.readString());
            return info;
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public static UserInfo parse(JSONObject userJson) throws JSONException {
        if (userJson == null || TextUtils.isEmpty(userJson.toString())) {
            return null;
        }

        UserInfo info = new UserInfo();

        info.setAuthToken(userJson.getString("authToken"));
        info.setAvatarPath(userJson.getString("avatar"));
        info.setId(userJson.getInt("id") + "");
        info.setTel(userJson.getString("nattel"));
//        info.setLevel(userJson.getString("level"));
//        info.setUserType(userJson.getString("userType"));
//        info.setUserStoreId(userJson.getInt("userStoreId"));

        return info;

    }
}
