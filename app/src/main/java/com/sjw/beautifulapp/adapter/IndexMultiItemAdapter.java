package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.bean.BookHotInfo;
import com.sjw.beautifulapp.bean.IndexBean;

import java.util.List;


public class IndexMultiItemAdapter extends BaseRecyclerAdapter<IndexMultiItemAdapter.SimpleAdapterViewHolder> {
    private List<IndexBean> list;
    private Context mContext;

    public IndexMultiItemAdapter(List<IndexBean> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(SimpleAdapterViewHolder holder, int position, boolean isItem) {
        final IndexBean bean = list.get(position);
        int type = getAdapterItemViewType(position);
        if (type == 0) {
            holder.item_index_rv1_left.setLayoutManager(new LinearLayoutManager(mContext));
            IndexItemLeftAdapter leftAdapter = new IndexItemLeftAdapter(mContext, bean.getBookClassifies());
            holder.item_index_rv1_left.setAdapter(leftAdapter);
            holder.item_index_rv1_right.setLayoutManager(new LinearLayoutManager(mContext));
            final IndexItemRightAdapter rightAdapter = new IndexItemRightAdapter(mContext, bean.getBookClassifies().get(0).getBookInfos());
            holder.item_index_rv1_right.setAdapter(rightAdapter);
            leftAdapter.setOnListener(new IndexItemLeftAdapter.OnListener() {
                @Override
                public void notifyOtherRv(String title) {
                    for (int i = 0; i < list.get(0).getBookClassifies().size(); i++) {
                        if (bean.getBookClassifies().get(i).getBookInfoType().equals(title)) {
                            rightAdapter.update(bean.getBookClassifies().get(i).getBookInfos());
                        }
                    }

                }
            });

        } else if (type == 1) {
            holder.index2_title.setText(bean.getBookActive().getTitle());
            List<String> index2_list=bean.getBookActive().getContent();
           LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
           linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Index2ItemAdapter adapter=new Index2ItemAdapter(mContext,index2_list);
            holder.index2_rv.setLayoutManager(linearLayoutManager);
            holder.index2_rv.setAdapter(adapter);


        } else {
            holder.index3_item_title.setText(bean.getBookHot().getTitle());
            List<BookHotInfo> info_list=bean.getBookHot().getBookHotInfos();
          GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,4);
           Index3ItemAdapter adapter=new Index3ItemAdapter(mContext,info_list);
            holder.index3_item_rv.setLayoutManager(gridLayoutManager);
            holder.index3_item_rv.setAdapter(adapter);
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

    public void setData(List<IndexBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index_rv1, parent, false);
        } else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index_rv2, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index_rv3, parent, false);

        }
        return new SimpleAdapterViewHolder(v, viewType, true);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView item_index_rv1_left, item_index_rv1_right;
        public RecyclerView index2_rv;
        public TextView index2_title;
        public RecyclerView index3_item_rv;
        public TextView  index3_item_title;

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
                        item_index_rv1_left = (RecyclerView) itemView.findViewById(R.id.item_index_rv1_left);
                        item_index_rv1_right = (RecyclerView) itemView.findViewById(R.id.item_index_rv1_right);

                        break;
                    case 1:
                        index2_rv=(RecyclerView)itemView.findViewById(R.id.index2_rv);
                        index2_title=(TextView)itemView.findViewById(R.id.index2_title);
                        break;
                    default:

                        index3_item_rv=(RecyclerView)itemView.findViewById(R.id.index3_item_rv);
                        index3_item_title=(TextView)itemView.findViewById(R.id.index3_item_title);
//                        tvRight = (TextView) itemView.findViewById(R.id.tv_multi_right);
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