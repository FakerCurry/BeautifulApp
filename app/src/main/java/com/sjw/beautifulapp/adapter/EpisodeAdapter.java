package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.EpisodeDetailActivity;
import com.sjw.beautifulapp.bean.EpisodeBean;
import com.sjw.beautifulapp.bean.EpisodeDetailBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class EpisodeAdapter extends BaseRecyclerAdapter<EpisodeAdapter.FoodAdapterViewHolder> {
    private List<EpisodeBean> list;
    private Context mContext;
    private int largeCardHeight, smallCardHeight;




    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public EpisodeAdapter(List<EpisodeBean> list, Context context) {
        this.list = list;
        this.mContext = context;
//        largeCardHeight = DensityUtil.dip2px(context, 150);
//        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(FoodAdapterViewHolder holder, int position, boolean isItem) {




        final EpisodeBean speek = list.get(position);
        holder.episode_item_title_tv.setText(speek.getTitle());
//        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(speek.getImgUrl())
//                .apply(options)
                .into(holder.episode_item_iv);

        holder.episode_item_time_tv.setText(speek.getTime());
        holder.episode_item_type_tv.setText(speek.getName());

        holder.itemView.setTag(position);
        holder.itemclickLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EpisodeDetailBean bean=speek.getDetailBean();
                Bundle bundle=new Bundle();
                bundle.putSerializable("EpisodeDetailBean", bean);
                Intent intent=new Intent(mContext,EpisodeDetailActivity.class);
                intent.putExtras(bundle);
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

    public void setData(List<EpisodeBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public FoodAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_episode, parent, false);
        FoodAdapterViewHolder vh = new FoodAdapterViewHolder(v, true);

        return vh;
    }


    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView episode_item_iv;
        public TextView episode_item_title_tv;
        public ImageView episode_item_type_iv;
        public TextView episode_item_type_tv;
        public ImageView episode_item_time_iv;
        public TextView episode_item_time_tv;
        public LinearLayout itemclickLl;

        public FoodAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                episode_item_iv = (ImageView) itemView.findViewById(R.id.episode_item_iv);
                episode_item_title_tv = (TextView) itemView.findViewById(R.id.episode_item_title_tv);
                episode_item_type_iv = (ImageView) itemView.findViewById(R.id.episode_item_type_iv);
                episode_item_type_tv = (TextView) itemView.findViewById(R.id.episode_item_type_tv);
                episode_item_time_iv = (ImageView) itemView.findViewById(R.id.episode_item_time_iv);
                episode_item_time_tv = (TextView) itemView.findViewById(R.id.episode_item_time_tv);
                itemclickLl=(LinearLayout)itemView.findViewById(R.id.itemclickLl);
            }

        }
    }


}