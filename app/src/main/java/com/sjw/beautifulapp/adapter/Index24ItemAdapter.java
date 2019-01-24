package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.Index2Item3ItemBean;

import java.util.List;

public class Index24ItemAdapter extends RecyclerView.Adapter<Index24ItemAdapter.MyViewHolder> {
    private List<Index2Item3ItemBean>  list;

    private Context context;

    public Index24ItemAdapter(List<Index2Item3ItemBean>  list, Context context){

        this.list=list;
        this.context=context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_index2_rv3_item2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Index2Item3ItemBean bean=list.get(position);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.pic_no_square)
                .error(R.drawable.pic_no_square);
        Glide.with(context)
                .load(bean.getImgUrl())
                .apply(options)
                .into(holder.vp_fg_iv);


        holder.vp_fg_content.setText(bean.getContent());
        holder.vp_fg_title.setText(bean.getTitle());





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView vp_fg_iv;
        TextView vp_fg_title;
        TextView vp_fg_content;



        public MyViewHolder(View itemView) {
            super(itemView);
            vp_fg_iv=(ImageView)itemView.findViewById(R.id.vp_fg_iv);
            vp_fg_title=(TextView)itemView.findViewById(R.id.vp_fg_title);
            vp_fg_content=(TextView)itemView.findViewById(R.id.vp_fg_content);





        }
    }
}
