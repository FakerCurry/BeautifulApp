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
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.bean.Index2Item1ItemBean;
import com.sjw.beautifulapp.view.SquareImageView;

import java.util.List;

public class Index21ItemAdapter extends RecyclerView.Adapter<Index21ItemAdapter.MyViewHolder> {
    private List<Index2Item1ItemBean>  list;

    private Context context;

    public Index21ItemAdapter(List<Index2Item1ItemBean>  list,Context context){

        this.list=list;
        this.context=context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_index2_rv1_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Index2Item1ItemBean bean=list.get(position);

        //        RequestOptions options = new RequestOptions();
        Glide.with(context)
                .load(bean.getImgUrl())
//                .apply(options)
                .into(holder.img);


        if (TextUtils.isEmpty(bean.getTag())){

            holder.tag.setVisibility(View.GONE);
        }else {
            holder.tag.setVisibility(View.VISIBLE);
            holder.tag.setText(bean.getTag());

        }

        holder.name.setText(bean.getTitle());


        holder.content.setText(bean.getContent());


        holder.item_ll.setOnClickListener(new View.OnClickListener() {
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
        private ImageView img;
        private TextView tag;
        private TextView name;
        private TextView content;
        private LinearLayout item_ll;



        public MyViewHolder(View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.img);
            tag=(TextView)itemView.findViewById(R.id.tag);
            name =(TextView)itemView.findViewById(R.id.name);
            content =(TextView)itemView.findViewById(R.id.content);
            item_ll=(LinearLayout)itemView.findViewById(R.id.item_ll);
        }
    }
}
