package com.rrja.carja.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Region;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter {

    private static final int TAG_CITY_HEADER = 10;
    private static final int TAG_CITY_CONTENT = 11;

    private int gpsIndex = -1;

    private List<CityItem> items = new ArrayList<>();

    OnCityItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TAG_CITY_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_header, parent, false);
            CityHeaderHolder headerHolder = new CityHeaderHolder(v);
            return headerHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_content, parent, false);
            CityHolder holder = new CityHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        CityItem cityItem = items.get(position);
        final Region region = cityItem.region;
        if (viewType == TAG_CITY_HEADER) {
            CityHeaderHolder headerHolder = (CityHeaderHolder) holder;
            headerHolder.bindData(region);
        } else {
            CityHolder cityHolder = (CityHolder) holder;
            cityHolder.bindData(region);
            cityHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onCityClicked(region);
                    }
                }
            });

            if (gpsIndex == position) {
                Log.e("gpsfind", position + "");
                cityHolder.txtContent.setTextColor(cityHolder.itemView.getContext().getResources().getColor(R.color.c_style_red));
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        CityItem region = items.get(position);
        if (region.isHeader) {
            return TAG_CITY_HEADER;
        } else {
            return TAG_CITY_CONTENT;
        }
    }

    public void setOnitemClickListener(OnCityItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void loadData(List<Region> regions) {
        if (regions != null && regions.size() != 0) {
            items.clear();
            for (int i = 0; i < regions.size(); i++) {
                Region region = regions.get(i);
                CityItem item = new CityItem(region);
                if (region.getLevel() == 2) {
                    CityItem itemHeader = new CityItem(region);
                    itemHeader.isHeader = true;
                    items.add(itemHeader);
                }
                items.add(item);
            }
        }
    }

    private class CityItem {

        boolean isHeader = false;
        Region region;

        public CityItem(Region region) {
            this.region = region;
        }

    }

    private class CityHolder extends RecyclerView.ViewHolder {

        TextView txtContent;

        public CityHolder(View itemView) {
            super(itemView);

            txtContent = (TextView) itemView.findViewById(R.id.txt_item_city);
        }

        public void bindData(Region region) {
            txtContent.setText(region.getName());
        }
    }

    private class CityHeaderHolder extends RecyclerView.ViewHolder {

        TextView txtheader;

        public CityHeaderHolder(View itemView) {
            super(itemView);

            txtheader = (TextView) itemView.findViewById(R.id.txt_item_city_header);
        }

        public void bindData(Region region) {
            txtheader.setText(region.getName());
        }
    }

    public interface OnCityItemClickListener {
        public void onCityClicked(Region region);
    }

    public int getCityIndex(Region region) {
        for (int i = 0; i < items.size(); i++) {
            CityItem item = items.get(i);
            if (!item.isHeader && item.region.equals(region)) {
                return i;
            }
        }

        return -1;
    }

    public void setGpsIndex(int index) {
        gpsIndex = index;
    }
}
