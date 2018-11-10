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

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.bean.BookInfo;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by pc on 2018/5/4.
 */

public class IndexItemRightAdapter extends RecyclerView.Adapter<IndexItemRightAdapter.MyViewHolder> {

    private Context mContext;
    private List<BookInfo> mDatas;

    public IndexItemRightAdapter(Context context, List<BookInfo> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    //刷新
    public void update(List<BookInfo> mUpDatas) {

        this.mDatas = mUpDatas;
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // TODO Auto-generated method stub
        final BookInfo bean = mDatas.get(position);
        holder.item_itemindex_right_content.setText(bean.getBookIntroduce());
        //        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(bean.getBookImg())
//                .apply(options)
                .into(holder.item_itemindex_right_iv);
        holder.item_itemindex_right_title.setText(bean.getBookTitle());
        holder.item_itemindex_right_type.setText(bean.getBookType());
        holder.index_right_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,IndexDetailActivity.class);
                intent.putExtra("index_detail_url",bean.getDetailurl());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_itemindex_rvright, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView item_itemindex_right_iv;
        TextView item_itemindex_right_title;
        TextView item_itemindex_right_type;
        TextView item_itemindex_right_content;
        LinearLayout index_right_ll;

        public MyViewHolder(View view) {
            super(view);
            item_itemindex_right_iv = (ImageView) view.findViewById(R.id.item_itemindex_right_iv);
            item_itemindex_right_title = (TextView) view.findViewById(R.id.item_itemindex_right_title);
            item_itemindex_right_type = (TextView) view.findViewById(R.id.item_itemindex_right_type);
            item_itemindex_right_content = (TextView) view.findViewById(R.id.item_itemindex_right_content);
            index_right_ll=(LinearLayout)view.findViewById(R.id.index_right_ll);

        }

    }


}
