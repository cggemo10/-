package com.jayangche.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.core.CoreManager;
import com.jayangche.android.model.DiscountInfoToShow;

import java.io.File;

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
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_discount,null);

            holder = new Holder();
            holder.pic = (ImageView) convertView.findViewById(R.id.img_item_discount);
            holder.title = (TextView) convertView.findViewById(R.id.txt_item_discount_title);
            holder.discountScope = (TextView) convertView.findViewById(R.id.txt_item_discount_scope_content);
            holder.discountTime = (TextView) convertView.findViewById(R.id.txt_item_discount_time_content);
            holder.discountContent = (TextView) convertView.findViewById(R.id.txt_item_discount_detial_content);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (Holder) convertView.getTag();
        }

        DiscountInfoToShow discount = CoreManager.getManager().getDiscounts().get(position);
        holder.title.setText(discount.getName());
        holder.discountScope.setText(discount.getScope());
        holder.discountTime.setText(discount.getTime());
        holder.discountContent.setText(discount.getContent());
        if (!TextUtils.isEmpty(discount.getImgUrl())) {
            if (discount.getImgUrl().contains("http")) {
                // TODO load from download service
            } else {
                holder.pic.setImageURI(Uri.fromFile(new File(discount.getImgUrl())));
            }
        }

        return convertView;
    }

    private class Holder {
        TextView title;
        TextView discountScope;
        TextView discountTime;
        TextView discountContent;
        ImageView pic;
    }
}
