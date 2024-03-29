package com.rrja.carja.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.rrja.carja.model.TagableElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TagableService implements TagableElement, Parcelable {

    private MaintenanceService service;
    private ArrayList<TagableSubService> subServiceList = new ArrayList<>();
    private double serviceAmount;

    @Override
    public int getTag() {
        return MaintenanceOrder.TAG_ORDER_SERVICE;
    }

    public MaintenanceService getService() {
        return service;
    }

    public void setService(MaintenanceService service) {

        this.service = service;
    }

    public void addTagableGood(TagableSubService subService) {
        TagableSubService duplicateService = null;

        for (TagableSubService orignalService : subServiceList) {
            if (subService.getSubService().getId().equals(orignalService.getSubService().getId())) {
                duplicateService = orignalService;
            }
        }
        if (duplicateService != null) {
            subServiceList.remove(duplicateService);
        }
        subServiceList.add(subService);
    }

    public void removeTagableGood(TagableSubService subService) {
        if (subServiceList.contains(subService)) {
            subServiceList.remove(subService);
        }

        if (subServiceList.size() == 1) {
            TagableSubService mayFeeubService = subServiceList.get(0);
            if ("服务费".equals(mayFeeubService.getServiceName())) {
                subServiceList.remove(mayFeeubService);
            }
        }

    }

    public ArrayList<TagableSubService> getSubServiceList() {
        return subServiceList;
    }

    public double calculateServiceTotalFee() {

        serviceAmount = 0;

        if (subServiceList.size() != 0) {
            for (TagableSubService service : subServiceList) {
                if ("服务费".equals(service.getServiceName())) {
                    serviceAmount += service.getServiceAmount();
                } else {
                    if (service.getCoupons() != null) {
                        serviceAmount += service.getCoupons().getCouponsPrice();
                    } else if (service.getGoods() != null) {
                        serviceAmount += service.getGoods().getPrice();
                    }
                }
            }
        }

        return serviceAmount;
    }

    public double caculateServiceFee() {

        if (subServiceList.size() != 0) {
            for (TagableSubService service : subServiceList) {
                if ("服务费".equals(service.getServiceName())) {
                    return service.getServiceAmount();
                }
            }
        }

        return 0;
    }

    public void removeSubService(TagableSubService subService) {
        if (subServiceList.contains(subService)) {
            subServiceList.remove(subService);
        }

        if (subServiceList.size() == 1) {
            TagableSubService mayFeeSubService = subServiceList.get(0);
            if ("服务费".equals(mayFeeSubService.getServiceName())) {
                subServiceList.remove(mayFeeSubService);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(service, flags);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("sub_service", subServiceList);
//        dest.writeBundle(bundle);
        dest.writeList(subServiceList);
        dest.writeDouble(serviceAmount);
    }

    public static Creator<TagableService> CREATOR = new Creator<TagableService>() {
        @Override
        public TagableService createFromParcel(Parcel source) {

            TagableService tagableService = new TagableService();
            tagableService.setService((MaintenanceService) source.readParcelable(MaintenanceService.class.getClassLoader()));
//            Bundle bundle = source.readBundle();
//            ArrayList<TagableSubService> sub_service = bundle.getParcelableArrayList("sub_service");
//            tagableService.subServiceList = new ArrayList<>();
//            tagableService.subServiceList.addAll(sub_service);
            tagableService.subServiceList = source.readArrayList(TagableSubService.class.getClassLoader());
            tagableService.serviceAmount = source.readDouble();
            return tagableService;
        }

        @Override
        public TagableService[] newArray(int size) {
            return new TagableService[0];
        }
    };

    public JSONObject getCommitContent() throws JSONException {

        JSONObject json = new JSONObject();
        json.put("serviceId", service.getId());
        json.put("tag", service.getParentId());
        json.put("serviceAmount", caculateServiceFee());

        JSONArray array = new JSONArray();
        for (TagableSubService subService : subServiceList) {
            JSONObject subJson = subService.getCommitContent();
            if (subJson == null) {
                if (TextUtils.isEmpty(subService.getServiceName()) ||
                        !"服务费".equals(subService.getServiceName())) {
                    throw new JSONException("subservice is null");
                } else {
                    continue;
                }
            }

            array.put(subJson);
        }

        json.put("subService", array);

        return json;
    }

}