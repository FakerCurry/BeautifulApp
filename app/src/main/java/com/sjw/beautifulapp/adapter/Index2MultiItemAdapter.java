package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.BookHotInfo;
import com.sjw.beautifulapp.bean.Index2Bean;
import com.sjw.beautifulapp.bean.Index2Item1ItemBean;
import com.sjw.beautifulapp.bean.Index2Item2Bean;
import com.sjw.beautifulapp.bean.Index2Item2ItemBean;
import com.sjw.beautifulapp.bean.IndexBean;
import com.sjw.beautifulapp.view.HorizontalPageLayoutManager;
import com.sjw.beautifulapp.view.PagingItemDecoration;
import com.sjw.beautifulapp.view.PagingScrollHelper;

import java.util.ArrayList;
import java.util.List;


public class Index2MultiItemAdapter extends BaseRecyclerAdapter<Index2MultiItemAdapter.SimpleAdapterViewHolder> implements PagingScrollHelper.onPageChangeListener {
    private List<Index2Bean> list;
    private Context mContext;

    private Index21ItemAdapter index21ItemAdapter;
    private Index22ItemAdapter index22ItemAdapter;
    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;
    private PagingItemDecoration pagingItemDecoration = null;
    PagingScrollHelper scrollHelper;
    public Index2MultiItemAdapter(List<Index2Bean> list, Context context) {
        this.list = list;
        this.mContext = context;
        horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 2);
        pagingItemDecoration = new PagingItemDecoration(mContext, horizontalPageLayoutManager);
        scrollHelper= new PagingScrollHelper();
    }

    @Override
    public void onBindViewHolder(SimpleAdapterViewHolder holder, int position, boolean isItem) {
        final Index2Bean bean = list.get(position);
        int type = getAdapterItemViewType(position);
        if (type == 0) {



            index21ItemAdapter=new Index21ItemAdapter(bean.getItem1Bean().getIndex2Item1PageList().get(0).getList(),mContext);

            holder.recyclerview.setAdapter(index21ItemAdapter);
            holder.item_index2_rv1_lefttv.setText(bean.getItem1Bean().getTitle1());
            holder.item_index2_rv1_righttv.setText(bean.getItem1Bean().getTitle2());
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,2);
            holder.recyclerview.setLayoutManager(gridLayoutManager);


        } else if (type == 1) {

            List<Index2Item2ItemBean> list=new ArrayList<>();
            for (int i=0;i<bean.getItem2Bean().getIndex2Item2PageList().size();i++){

                list.addAll(bean.getItem2Bean().getIndex2Item2PageList().get(i).getList());
            }

            index22ItemAdapter=new Index22ItemAdapter(list,mContext);

            holder.recyclerview.setAdapter(index22ItemAdapter);
            holder.item_index2_rv1_lefttv.setText(bean.getItem2Bean().getTitle1());
            holder.item_index2_rv1_righttv.setText(bean.getItem2Bean().getTitle2());
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerview.setLayoutManager(linearLayoutManager);



        } else {

        }


    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position).getType();
        }
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Index2Bean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index2_rv1, parent, false);
        } else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index2_rv2, parent, false);
        } else {


        }
        return new SimpleAdapterViewHolder(v, viewType, true);
    }

    @Override
    public void onPageChange(int index) {

    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView item_index2_rv1_lefttv;
         TextView item_index2_rv1_righttv;
         RecyclerView recyclerview;


        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            init(itemView, -1, isItem);
        }

        public SimpleAdapterViewHolder(View itemView, int viewType, boolean isItem) {
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View itemView, int viewType, boolean isItem) {
            if (isItem) {
                switch (viewType) {
                    case 0:
                        item_index2_rv1_lefttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_lefttv);
                        item_index2_rv1_righttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_righttv);
                        recyclerview=(RecyclerView)itemView.findViewById(R.id.recyclerview);

                        break;
                    case 1:
                        item_index2_rv1_lefttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_lefttv);
                        item_index2_rv1_righttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_righttv);
                        recyclerview=(RecyclerView)itemView.findViewById(R.id.recyclerview);
                        break;
                    default:


                        break;
                }
            }
        }
    }

//    public void insert(Person person, int position) {
//        insert(list, person, position);
//    }
//
//    public void remove(int position) {
//        remove(list, position);
//    }
//
//    public void clear() {
//        clear(list);
//    }
//
//    public Person getItem(int position) {
//        if (position < list.size())
//            return list.get(position);
//        else
//            return null;
//    }

}