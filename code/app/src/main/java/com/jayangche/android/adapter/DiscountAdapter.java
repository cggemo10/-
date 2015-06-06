package com.jayangche.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.core.CoreManager;

/**
 * Created by Administrator on 2015/6/6.
 */
public class DiscountAdapter extends BaseAdapter {

    private Context context;

    public DiscountAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return CoreManager.getManager().getDiscounts().size();
    }

    @Override
    public Object getItem(int position) {
        return CoreManager.getManager().getDiscounts().indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_discount,null);

            Holder holder = new Holder();
        }

        return null;
    }

    private class Holder {
        TextView title;
        TextView discountTime;
        TextView discountDetial;
        ImageView pic;
    }
}
