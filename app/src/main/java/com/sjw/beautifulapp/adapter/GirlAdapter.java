package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.GirlBean;
import com.sjw.beautifulapp.newtools.bigimg.ZoomableActivity;
import com.bumptech.glide.Glide;


import java.util.List;

public class GirlAdapter extends BaseRecyclerAdapter<GirlAdapter.FoodAdapterViewHolder> {
    private List<GirlBean> list;
    private Context mContext;
    private int largeCardHeight, smallCardHeight;


    public GirlAdapter(List<GirlBean> list, Context context) {
        this.list = list;
        this.mContext = context;

//        largeCardHeight = DensityUtil.dip2px(context, 150);
//        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(FoodAdapterViewHolder holder, int position, boolean isItem) {
        final GirlBean bean = list.get(position);
        //        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(bean.getGirl_img())
//                .apply(options)
                .into(holder.girlitem_iv);
        holder.girlitem_eye.setText(bean.getGirl_nomenu());
        holder.girlitem_flag.setText(bean.getGirl_flag());
        holder.girlitem_time.setText(bean.getGirl_time());
        holder.girlitem_title.setText(bean.getGirl_name());


        holder.girlitem_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZoomableActivity.goToPage(mContext, bean.getDetail_imgs(), 0);
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

    public void setData(List<GirlBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public FoodAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_girl, parent, false);
        FoodAdapterViewHolder vh = new FoodAdapterViewHolder(v, true);
        return vh;
    }



    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView girlitem_iv;
        public TextView girlitem_title;
        public TextView girlitem_flag;
        public TextView girlitem_time;
        public TextView girlitem_eye;


        public FoodAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                girlitem_iv = (ImageView) itemView.findViewById(R.id.girlitem_iv);
                girlitem_title = (TextView) itemView.findViewById(R.id.girlitem_title);
                girlitem_flag = (TextView) itemView.findViewById(R.id.girlitem_flag);
                girlitem_time = (TextView) itemView.findViewById(R.id.girlitem_time);
                girlitem_eye = (TextView) itemView.findViewById(R.id.girlitem_eye);

            }

        }
    }



}