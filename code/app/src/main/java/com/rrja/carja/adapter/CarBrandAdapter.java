package com.rrja.carja.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rrja.carja.R;
import com.rrja.carja.adapter.holder.CarBrandHeaderHolder;
import com.rrja.carja.adapter.holder.CarBrandHolder;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.CarBrand;
import com.rrja.cja.view.recycler.stickyside.GridSLM;
import com.rrja.cja.view.recycler.stickyside.LayoutManager;
import com.rrja.cja.view.recycler.stickyside.LinearSLM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class CarBrandAdapter extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    private final ArrayList<LineItem> mItems;

    private Context mContext;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    // brand 已经按firstLetter 排好序
    public CarBrandAdapter(Context context) {
        List<CarBrand> carBrandList = CoreManager.getManager().getCarBrand();
        this.mContext = context;

        mItems = new ArrayList<>();

        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;

        for (int i = 0; i < carBrandList.size(); i++) {
            CarBrand brand = carBrandList.get(i);
            if (!TextUtils.equals(lastHeader, brand.getFirstLetter())) {
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = brand.getFirstLetter();
                headerCount += 1;
                mItems.add(new LineItem(brand, true, sectionManager, sectionFirstPosition));
            }
            mItems.add(new LineItem(brand, false, sectionManager, sectionFirstPosition));
        }

    }

    public boolean isItemHeader(int position) {
        return mItems.get(position).isHeader;
    }

    public String itemToString(int position) {
        return mItems.get(position).brand.toString();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand_header, parent, false);
            return new CarBrandHeaderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand, parent, false);
            return new CarBrandHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (item.isHeader) {
            lp.headerDisplay = mHeaderDisplay;
            CarBrandHeaderHolder headerHolder = (CarBrandHeaderHolder) holder;
            headerHolder.bindHeader(item.brand);
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;

            lp.headerEndMarginIsAuto = true;
            lp.headerStartMarginIsAuto = true;
        }

        lp.setSlm(LinearSLM.ID);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            LineItem item = mItems.get(i);
            if (item.isHeader) {
                notifyItemChanged(i);
            }
        }
    }

    private static class LineItem {

        public int sectionManager;

        public int sectionFirstPosition;

        public boolean isHeader;

        public CarBrand brand;

        public LineItem(CarBrand brand, boolean isHeader, int sectionManager,
                        int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.brand = brand;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }
}
