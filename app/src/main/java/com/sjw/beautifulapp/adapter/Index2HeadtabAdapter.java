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

import com.bumptech.glide.Glide;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.bean.Index2HeadtabBean;

import java.util.List;

public class Index2HeadtabAdapter extends RecyclerView.Adapter<Index2HeadtabAdapter.MyViewHolder> {
    private List<Index2HeadtabBean>  list;

    private Context context;


    public Index2HeadtabAdapter(List<Index2HeadtabBean>  list, Context context){

        this.list=list;
        this.context=context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.index2_headtab_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Index2HeadtabBean bean=list.get(position);

        //        RequestOptions options = new RequestOptions();
        Glide.with(context)
                .load(bean.getImgUrl())
//                .apply(options)
                .into(holder.tabimg);


        holder.tabtv.setText(bean.getTitle());

        holder.lltab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,IndexDetailActivity.class);
                intent.putExtra("index_detail_url",bean.getDetailUrl());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView tabimg;
        TextView tabtv;

        private LinearLayout lltab;

        public MyViewHolder(View itemView) {
            super(itemView);
            tabimg=(ImageView)itemView.findViewById(R.id.tabimg);
            tabtv=(TextView) itemView.findViewById(R.id.tabtv);
            lltab=(LinearLayout) itemView.findViewById(R.id.lltab);
        }
    }
}
