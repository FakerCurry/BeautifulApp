package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.BookClassify;

import java.util.List;

/**
 * Created by pc on 2018/5/4.
 */

public class IndexItemLeftAdapter extends RecyclerView.Adapter<IndexItemLeftAdapter.MyViewHolder> {

    private Context mContext;
    private List<BookClassify> mDatas;

    private OnListener listener;


    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    //去更新另外一个Rv
    public interface OnListener {
        void notifyOtherRv(String title);
    }

    public IndexItemLeftAdapter(Context context, List<BookClassify> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    //刷新
    public void update(List<BookClassify> mUpDatas) {

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
        BookClassify bean = mDatas.get(position);
        holder.item_itemindex_title.setText(bean.getBookInfoType());
        if (bean.isSelect()) {
            holder.item_itemindex_ll.setBackgroundColor(Color.WHITE);
            holder.item_itemindex_selectline.setVisibility(View.VISIBLE);
            holder.item_itemindex_title.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            holder.item_itemindex_ll.setBackgroundColor(Color.GRAY);
            holder.item_itemindex_selectline.setVisibility(View.GONE);
            holder.item_itemindex_title.setTextColor(Color.BLACK);
        }

        holder.item_itemindex_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (position == i) {
                        mDatas.get(i).setSelect(true);
                        update(mDatas);
                        listener.notifyOtherRv(mDatas.get(i).getBookInfoType());

                    } else {
                        mDatas.get(i).setSelect(false);
                        update(mDatas);
                    }


                }


            }
        });
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_itemindex_rvleft, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item_itemindex_ll;
        View item_itemindex_selectline;
        TextView item_itemindex_title;

        public MyViewHolder(View view) {
            super(view);
            item_itemindex_ll = (LinearLayout) view.findViewById(R.id.item_itemindex_ll);
            item_itemindex_selectline = (View) view.findViewById(R.id.item_itemindex_selectline);
            item_itemindex_title = (TextView) view.findViewById(R.id.item_itemindex_title);

        }

    }


}
