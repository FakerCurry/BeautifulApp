package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.FoodDetailActivity;
import com.sjw.beautifulapp.bean.FoodInfo;
import com.bumptech.glide.Glide;

import java.util.List;

public class FoodAdapter extends BaseRecyclerAdapter<FoodAdapter.FoodAdapterViewHolder> {
    private List<FoodInfo> list;
    private Context mContext;
    private int largeCardHeight, smallCardHeight;

    public FoodAdapter(List<FoodInfo> list, Context context) {
        this.list = list;
        this.mContext = context;
//        largeCardHeight = DensityUtil.dip2px(context, 150);
//        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(FoodAdapterViewHolder holder, int position, boolean isItem) {
        final FoodInfo info = list.get(position);
        holder.food_item_title_tv.setText(info.getFoodName());
//        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(info.getFoodImgUrl())
//                .apply(options)
                .into(holder.food_item_iv);

        holder.food_item_author_tv.setText(info.getFoodAuthor());
        holder.food_item_content_tv.setText(info.getFoodOrigin());

        holder.click_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,FoodDetailActivity.class);
                intent.putExtra("foodDetailUrl",info.getFoodDetailUrl());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public FoodAdapterViewHolder getViewHolder(View view) {
        return new FoodAdapterViewHolder(view, false);
    }

    public void setData(List<FoodInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public FoodAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_food, parent, false);
        FoodAdapterViewHolder vh = new FoodAdapterViewHolder(v, true);
        return vh;
    }

//    public void insert(EpisodeBean person, int position) {
//        insert(list, person, position);
//    }
//
//    public void remove(int position) {
//        remove(list, position);
//    }
//
//    public void clear() {
//        clear(list);
//    }

    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView food_item_iv;
        public TextView food_item_title_tv;
        public TextView food_item_author_tv;
        public TextView food_item_content_tv;
        private LinearLayout click_ll;

        public FoodAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                food_item_iv = (ImageView) itemView.findViewById(R.id.food_item_iv);
                food_item_title_tv = (TextView) itemView.findViewById(R.id.food_item_title_tv);
                food_item_author_tv = (TextView) itemView.findViewById(R.id.food_item_author_tv);
                food_item_content_tv = (TextView) itemView.findViewById(R.id.food_item_content_tv);
                click_ll=(LinearLayout)itemView.findViewById(R.id.click_ll);
            }

        }
    }

//    public Person getItem(int position) {
//        if (position < list.size())
//            return list.get(position);
//        else
//            return null;
//    }

}