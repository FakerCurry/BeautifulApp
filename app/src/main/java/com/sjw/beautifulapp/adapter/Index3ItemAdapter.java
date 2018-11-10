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
import com.sjw.beautifulapp.bean.BookHotInfo;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by pc on 2018/5/4.
 */

public class Index3ItemAdapter extends RecyclerView.Adapter<Index3ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<BookHotInfo> mDatas;

    private OnListener listener;


    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    //去更新另外一个Rv
    public interface OnListener {
        void notifyOtherRv(String title);
    }

    public Index3ItemAdapter(Context context, List<BookHotInfo> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    //刷新
    public void update(List<BookHotInfo> mUpDatas) {

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
        final BookHotInfo bean=mDatas.get(position);

        holder.index3item_author.setText("作者:"+bean.getHotAuthor());
        holder.index3item_title.setText(bean.getHotTitle());
        Glide.with(mContext)
                .load(bean.getHotImg())
//                .apply(options)
                .into(holder.index3item_img);

        holder.index3_ll.setOnClickListener(new View.OnClickListener() {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_index3rv, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {

      public ImageView index3item_img;
      public TextView index3item_title;
        public TextView index3item_author;
        public LinearLayout index3_ll;

        public MyViewHolder(View view) {
            super(view);
            index3item_img=(ImageView)view.findViewById(R.id.index3item_img);
            index3item_title=(TextView)view.findViewById(R.id.index3item_title);
            index3item_author=(TextView)view.findViewById(R.id.index3item_author);

            index3_ll=(LinearLayout)view.findViewById(R.id.index3_ll);

        }

    }


}
