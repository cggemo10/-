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
import com.jayangche.android.model.Coupons;
import com.jayangche.android.model.DiscountInfoToShow;

import java.io.IOException;

/**
 * Created by chongge on 15/6/29.
 */
public class CouponsAdapter extends BaseAdapter {

    Context mContext;

    public CouponsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return CoreManager.getManager().getCoupons().size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return CoreManager.getManager().getCoupons().get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_coupons,null);

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

        Coupons coupon = CoreManager.getManager().getCoupons().get(position);
        holder.title.setText(coupon.getName());
        holder.discountScope.setText(coupon.getAddress());
        holder.discountTime.setText(coupon.getTime());
        holder.discountContent.setText(coupon.getContent());
        /* TODO later
        if (!TextUtils.isEmpty(discount.getImgUrl())) {
            if (discount.getImgUrl().contains("http")) {
                // TODO load from download service
            } else {
                holder.pic.setImageURI(Uri.fromFile(new File(discount.getImgUrl())));
            }
        }*/

        try {
            holder.pic.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("juyouhui-img.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
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
