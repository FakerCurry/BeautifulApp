package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjw.beautifulapp.R;

import java.util.List;

/**
 * Created by pc on 2018/5/4.
 */

public class Index2ItemAdapter extends RecyclerView.Adapter<Index2ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mDatas;

    private OnListener listener;


    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    //去更新另外一个Rv
    public interface OnListener {
        void notifyOtherRv(String title);
    }

    public Index2ItemAdapter(Context context, List<String> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    //刷新
    public void update(List<String> mUpDatas) {

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
        String content = mDatas.get(position);
        holder.index2_itemtv.setText(content);

    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_index2rv, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView index2_itemtv;

        public MyViewHolder(View view) {
            super(view);
            index2_itemtv= (TextView) view.findViewById(R.id.index2_itemtv);

        }

    }


}
