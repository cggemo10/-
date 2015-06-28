package com.jayangche.android.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.core.CoreManager;
import com.jayangche.android.model.DiscountInfoToShow;
import com.jayangche.android.model.Forum;

import java.io.IOException;

/**
 * Created by Administrator on 2015/6/6.
 */
public class ForumAdapter extends BaseAdapter {

    private Context context;

    public ForumAdapter(Context context) {
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
            convertView = View.inflate(context, R.layout.item_list_forum,null);

            holder = new Holder();
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (Holder) convertView.getTag();
        }

        Forum discount = CoreManager.getManager().getForums().get(position);
//        holder.title.setText(discount.getName());
//        holder.discountScope.setText(discount.getScope());
//        holder.discountTime.setText(discount.getTime());
//        holder.discountContent.setText(discount.getContent());
//        /* TODO later
//        if (!TextUtils.isEmpty(discount.getImgUrl())) {
//            if (discount.getImgUrl().contains("http")) {
//                // TODO load from download service
//            } else {
//                holder.pic.setImageURI(Uri.fromFile(new File(discount.getImgUrl())));
//            }
//        }*/

//        try {
//            holder.pic.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open("juyouhui-img.jpg")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
