package com.rrja.carja.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.Forum;

/**
 * Created by Administrator on 2015/6/6.
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {

    private Context context;

    public ForumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_forum, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return CoreManager.getManager().getDiscounts().size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView discountScope;
        TextView discountTime;
        TextView discountContent;
        ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
