package com.sjw.beautifulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.bean.BookHotInfo;
import com.sjw.beautifulapp.bean.Index2Bean;
import com.sjw.beautifulapp.bean.Index2Item1ItemBean;
import com.sjw.beautifulapp.bean.Index2Item2Bean;
import com.sjw.beautifulapp.bean.Index2Item2ItemBean;
import com.sjw.beautifulapp.bean.Index2Item3ItemBean;
import com.sjw.beautifulapp.bean.Index2Item3Page;
import com.sjw.beautifulapp.bean.IndexBean;
import com.sjw.beautifulapp.bean.TabsBean;
import com.sjw.beautifulapp.fragment.Index2EveryFragment;
import com.sjw.beautifulapp.fragment.Index2Fragment;
import com.sjw.beautifulapp.view.HorizontalPageLayoutManager;
import com.sjw.beautifulapp.view.PagingItemDecoration;
import com.sjw.beautifulapp.view.PagingScrollHelper;
import com.sjw.beautifulapp.view.TopTabView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Index2MultiItemAdapter extends BaseRecyclerAdapter<Index2MultiItemAdapter.SimpleAdapterViewHolder> implements PagingScrollHelper.onPageChangeListener, TopTabView.TopTabListener, ViewPager.OnPageChangeListener {
    private List<Index2Bean> list;
    private Context mContext;

    private Index21ItemAdapter index21ItemAdapter;
    private Index22ItemAdapter index22ItemAdapter;
    private Index23ItemAdapter index23ItemAdapter;


    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;
    private PagingItemDecoration pagingItemDecoration = null;
    FragmentManager supportFragmentManager;
    PagingScrollHelper scrollHelper;

    private ViewPager viewPager;

    private TopTabView ttView;
    public Index2MultiItemAdapter(List<Index2Bean> list, Context context, FragmentManager supportFragmentManager) {
        this.list = list;
        this.mContext = context;
        this.supportFragmentManager=supportFragmentManager;
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

            holder.right_more_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,IndexDetailActivity.class);
                    intent.putExtra("index_detail_url",bean.getItem1Bean().getTitle2Detail());
                    mContext.startActivity(intent);
                }
            });


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
            holder.right_more_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,IndexDetailActivity.class);
                    intent.putExtra("index_detail_url",bean.getItem2Bean().getTitle2Detail());
                    mContext.startActivity(intent);
                }
            });



        } else {

            List<TabsBean> list=new ArrayList<>();
            list=bean.getItem3Bean().getTabArr();

            List<Index2Item3Page> index2Item3PageList=bean.getItem3Bean().getIndex2Item3PageList();

            index23ItemAdapter=new Index23ItemAdapter(list,mContext);

            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,list.size());

            holder.recyclerview3.setLayoutManager(gridLayoutManager);
            holder.recyclerview3.setAdapter(index23ItemAdapter);
            holder.item_index2_rv3_lefttv.setText(bean.getItem3Bean().getTitle());


            List<Fragment> listFragmentList=new ArrayList<>();

            for (int i=0;i<index2Item3PageList.size();i++){

                List<Index2Item3ItemBean> index2Item3ItemBeanList=index2Item3PageList.get(i).getList();

                Bundle bundle = new Bundle();
                bundle.putSerializable("itemList", (Serializable) index2Item3ItemBeanList);
                Fragment fg = Index2EveryFragment.newInstance(bundle);

                listFragmentList.add(fg);
            }





            holder.vp.setAdapter(new Index2FragmentStateAdapter(supportFragmentManager, listFragmentList));

            holder.vp.setOnPageChangeListener(this);
            List<String> tabList=new ArrayList<>();
            for (int i=0;i<list.size();i++){

                tabList.add(list.get(i).getTabText());

            }


            holder.toptabview.setTabList(tabList,mContext,0);

            holder.toptabview.setOnselectTabListener(this);

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

            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_index2_rv3, parent, false);
        }
        return new SimpleAdapterViewHolder(v, viewType, true);
    }

    @Override
    public void onPageChange(int index) {




    }

    @Override
    public void onSlect(int position) {
        viewPager.setCurrentItem(position);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ttView.setSelectPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView item_index2_rv1_lefttv;
         TextView item_index2_rv1_righttv;
         RecyclerView recyclerview;
        TextView item_index2_rv3_lefttv;
        RecyclerView recyclerview3;
        TopTabView toptabview;

        ViewPager vp;

        LinearLayout right_more_ll;

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

                        right_more_ll=(LinearLayout)itemView.findViewById(R.id.right_more_ll);

                        break;
                    case 1:
                        item_index2_rv1_lefttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_lefttv);
                        item_index2_rv1_righttv=(TextView)itemView.findViewById(R.id.item_index2_rv1_righttv);
                        recyclerview=(RecyclerView)itemView.findViewById(R.id.recyclerview);
                        right_more_ll=(LinearLayout)itemView.findViewById(R.id.right_more_ll);
                        break;
                    default:
                        item_index2_rv3_lefttv=(TextView)itemView.findViewById(R.id.item_index2_rv3_lefttv);
                        recyclerview3=(RecyclerView)itemView.findViewById(R.id.recyclerview3);
                        vp=(ViewPager)itemView.findViewById(R.id.vp);
                        viewPager=vp;
                        toptabview=(TopTabView)itemView.findViewById(R.id.toptabview);
                        ttView=toptabview;
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