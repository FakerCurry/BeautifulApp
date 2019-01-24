package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.bean.Index2Item2ItemBean;
import com.sjw.beautifulapp.bean.TabsBean;

import java.util.List;

public class Index23ItemAdapter extends RecyclerView.Adapter<Index23ItemAdapter.MyViewHolder> {
    private List<TabsBean>  list;

    private Context context;

    public Index23ItemAdapter(List<TabsBean>  list, Context context){

        this.list=list;
        this.context=context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_index2_rv3_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TabsBean bean=list.get(position);

        holder.tabText.setText(bean.getTabText());

        if (position==list.size()-1){

             holder.tabView.setVisibility(View.GONE);
        }else {
            holder.tabView.setVisibility(View.VISIBLE);

        }

        if (bean.isSelect()){

            holder.item_ll.setBackgroundColor(context.getResources().getColor(R.color.tab_bg));

        }else {
            holder.item_ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tabText;

        View tabView;
        LinearLayout item_ll;


        public MyViewHolder(View itemView) {
            super(itemView);
            tabText=(TextView)itemView.findViewById(R.id.tabText);
            tabView=(View)itemView.findViewById(R.id.tabView);
            item_ll=(LinearLayout)itemView.findViewById(R.id.item_ll);

        }
    }
}
