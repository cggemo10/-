package com.rrja.carja;

import android.app.Application;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.text.TextUtils;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/28.
 */
public class RRjaApplication extends Application {

    public LocationClient mLocationClient;
    public LocationListener mMyLocationListener;
    public Vibrator mVibrator;
    private BDLocation currLoc;
    private SharedPreferences sp;

    private HashMap<String, OnLocationChangeListener> locationListeners = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("rrja_loc", MODE_PRIVATE);

        mLocationClient = new LocationClient(this.getApplicationContext());
        requestLowLocationMode();
        mMyLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        mLocationClient.start();
    }


    /**
     * 实现实时位置回调监听
     */
    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            int locType = location.getLocType();
            if (locType == BDLocation.TypeGpsLocation || locType == BDLocation.TypeNetWorkLocation) {
                currLoc = location;
                // save
                saveLocation(location);

                // listener
                if (locationListeners.size() != 0) {
                    for (String key : locationListeners.keySet()) {
                        OnLocationChangeListener listener = locationListeners.get(key);
                        if (listener != null) {
                            listener.onLocationChanged(currLoc);
                        }
                    }
                }
            }
        }
    }

    private BDLocation readLocation() {
        String lastLocation = sp.getString("last_location", null);
        if (TextUtils.isEmpty(lastLocation)) {
            return null;
        } else {
            try {
                BDLocation location = new BDLocation();
                JSONObject json = new JSONObject(lastLocation);
                location.setTime(json.getString("time"));
                location.setLocType(json.getInt("errCode"));
                location.setLatitude(json.getDouble("lat"));
                location.setLongitude(json.getDouble("lng"));
                location.setRadius(Float.valueOf(json.getString("radius")));
                location.setAddrStr(json.getString("addr"));

                JSONObject addressJs = json.getJSONObject("address");
                Address.Builder builder = new Address.Builder();

                if (addressJs.has("country")) {
                    builder.country(addressJs.getString("country"));
                }
                if (addressJs.has("countryCode")) {
                    builder.countryCode(addressJs.getString("countryCode"));
                }
                if (addressJs.has("province")) {
                    builder.province(addressJs.getString("province"));
                }
                if (addressJs.has("city")) {
                    builder.city(addressJs.getString("city"));
                }
                if (addressJs.has("cityCode")) {
                    builder.cityCode(addressJs.getString("cityCode"));
                }
                if (addressJs.has("district")) {
                    builder.district("district");
                }
                if (addressJs.has("street")) {
                    builder.street(addressJs.getString("province"));
                }
                if (addressJs.has("streetNumber")) {
                    builder.streetNumber(addressJs.getString("streetNumber"));
                }
                location.setAddr(builder.build());
                return location;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void saveLocation(BDLocation location) {
        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("time", location.getTime());
            json.put("errCode", location.getLocType());
            json.put("lat", location.getLatitude());
            json.put("lng", location.getLongitude());
            json.put("radius", location.getRadius() + "");
            json.put("addr", location.getAddrStr());
            Address address = location.getAddress();
            if (address != null) {
                JSONObject addressJson = new JSONObject();
                addressJson.put("country", address.country);
                addressJson.put("countryCode", address.countryCode);
                addressJson.put("province", address.province);
                addressJson.put("city", address.city);
                addressJson.put("cityCode", address.countryCode);
                addressJson.put("district", address.district);
                addressJson.put("street", address.street);
                addressJson.put("streetNumber", address.streetNumber);

                json.put("address", addressJson);
            } else {
                json.put("address", new JSONObject());
            }
        } catch (Exception e) {
            json = null;
        }

        if (json != null) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("last_location", json.toString());
            edit.commit();
        }
    }

    public BDLocation getCurrentLocation() {
        if (currLoc != null) {
            return currLoc;
        } else {
            return readLocation();
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChanged(BDLocation location);
    }

    public void registLocationChangeListener(String key, OnLocationChangeListener listener) {
        if (TextUtils.isEmpty(key) || listener == null) {
            return;
        }
        if (locationListeners.containsKey(key)) {
            locationListeners.remove(key);
        }

        locationListeners.put(key, listener);
    }

    public void unRegistLocationChangeListener(String key) {
        if (locationListeners.containsKey(key)) {
            locationListeners.remove(key);
        }
    }

    public void requestHighLocationMode() {
        if (mMyLocationListener != null) {
            mLocationClient.stop();
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span=1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(option);
        if (mMyLocationListener != null) {
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
    }

    public void requestLowLocationMode() {
        if (mMyLocationListener != null) {
            mLocationClient.stop();
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setCoorType("bd09ll");
        int span=1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(option);
        if (mMyLocationListener != null) {
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
    }

    public void requestLocation() {
        mLocationClient.requestLocation();
    }
}
