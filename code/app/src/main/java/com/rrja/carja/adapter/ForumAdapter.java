package com.rrja.carja.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Forum;

/**
 * Created by Administrator on 2015/6/6.
 */
public class ForumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;

    public ForumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_forum_header, viewGroup, false);
            return new HeaderHolder(header);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_forum, viewGroup, false);
            return new ForumViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            HeaderHolder headerHolder = (HeaderHolder) viewHolder;
            headerHolder.txtAll.setOnClickListener(ForumAdapter.this);
            headerHolder.txtSame.setOnClickListener(ForumAdapter.this);
            headerHolder.txtMaster.setOnClickListener(ForumAdapter.this);
//            headerHolder.txtAll.
        } else {
//            Forum discount = CoreManager.getManager().getForums().get(position - 1);
//        viewHolder.title.setText(discount.());
//        viewHolder.discountScope.setText(discount.getScope());
//        viewHolder.discountTime.setText(discount.getTime());
//        viewHolder.discountContent.setText(discount.getContent());
        }


    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p/>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return CoreManager.getManager().getDiscounts().size();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_all_tag:
                break;
            case R.id.txt_same_tag:
                break;
            case R.id.txt_master_tag:
                break;
        }
    }

//    @Override
//    public int getCount() {
//        return CoreManager.getManager().getDiscounts().size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return CoreManager.getManager().getDiscounts().indexOf(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Holder holder = null;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_list_forum,null);
//
//            holder = new Holder();
//            convertView.setTag(holder);
//        }
//
//        if (holder == null) {
//            holder = (Holder) convertView.getTag();
//        }
//
//        Forum discount = CoreManager.getManager().getForums().get(position);
////        holder.title.setText(discount.getName());
////        holder.discountScope.setText(discount.getScope());
////        holder.discountTime.setText(discount.getTime());
////        holder.discountContent.setText(discount.getContent());
////        /* TODO later
////        if (!TextUtils.isEmpty(discount.getImgUrl())) {
////            if (discount.getImgUrl().contains("http")) {
////                // TODO load from download service
////            } else {
////                holder.pic.setImageURI(Uri.fromFile(new File(discount.getImgUrl())));
////            }
////        }*/
//
////        try {
////            holder.pic.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open("juyouhui-img.jpg")));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//        return convertView;
//    }

    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView discountScope;
        TextView discountTime;
        TextView discountContent;
        ImageView pic;

        public ForumViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        TextView txtAll;
        TextView txtSame;
        TextView txtMaster;

        public HeaderHolder(View itemView) {
            super(itemView);

            txtAll = (TextView) itemView.findViewById(R.id.txt_all_tag);
            txtSame = (TextView) itemView.findViewById(R.id.txt_same_tag);
            txtMaster = (TextView) itemView.findViewById(R.id.txt_master_tag);

        }


    }
}
