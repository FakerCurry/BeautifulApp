package com.sjw.beautifulapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.adapter.Index2HeadtabAdapter;
import com.sjw.beautifulapp.adapter.Index2MultiItemAdapter;
import com.sjw.beautifulapp.adapter.IndexMultiItemAdapter;
import com.sjw.beautifulapp.base.BaseFragment;
import com.sjw.beautifulapp.bean.Index2BannerBean;
import com.sjw.beautifulapp.bean.Index2Bean;
import com.sjw.beautifulapp.bean.Index2HeadBean;
import com.sjw.beautifulapp.bean.Index2HeadtabBean;
import com.sjw.beautifulapp.bean.Index2Item1Bean;
import com.sjw.beautifulapp.bean.Index2Item1ItemBean;
import com.sjw.beautifulapp.bean.Index2Item1Page;
import com.sjw.beautifulapp.bean.Index2Item2Bean;
import com.sjw.beautifulapp.bean.Index2Item2ItemBean;
import com.sjw.beautifulapp.bean.Index2Item2Page;
import com.sjw.beautifulapp.bean.Index2Item3Bean;
import com.sjw.beautifulapp.bean.Index2Item3ItemBean;
import com.sjw.beautifulapp.bean.Index2Item3Page;
import com.sjw.beautifulapp.bean.IndexBannerBean;
import com.sjw.beautifulapp.bean.IndexBean;
import com.sjw.beautifulapp.bean.TabsBean;
import com.umeng.commonsdk.debug.E;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

public class Index2Fragment extends BaseFragment implements BGABanner.Adapter<ImageView, String> {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    Unbinder unbinder;
    private View mRootView;
    private LinearLayoutManager layoutManager;

    private Index2HeadBean index2HeadBean;
    private Index2Item1Bean index2Item1Bean;
    public Handler handler;
    View headView;
//    private Index2Bean index2Bean;
    List<Index2Bean> index2BeanList;

    private Index2MultiItemAdapter adapter;
    BGABanner bannerMainDepth;

    private RecyclerView header_tab_rv;
    private Index2HeadtabAdapter index2HeadtabAdapter;
    private List<Index2HeadtabBean>  index2HeadtabBeanList;
    private GridLayoutManager gltab;
    private ImageView header_iv;

    private Index2Item2Bean index2Item2Bean;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected View setLayoutId(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_index2, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void initView(View view) {

        index2HeadBean=new Index2HeadBean();
//        index2Bean=new Index2Bean();
        index2Item1Bean=new Index2Item1Bean();
        index2BeanList=new ArrayList<>();
        index2HeadtabBeanList=new ArrayList<>();
        index2Item2Bean=new Index2Item2Bean();




//初始化
        init();
    }

    private void init() {
        recyclerView.setHasFixedSize(true);

        adapter = new Index2MultiItemAdapter(index2BeanList, getActivity(),getActivity().getSupportFragmentManager());

        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


//         静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setPullLoadEnable(false);
        xrefreshview.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        headView = adapter.setHeaderView(R.layout.header_index2, recyclerView);
        bannerMainDepth = (BGABanner) headView.findViewById(R.id.header_banner);
        header_tab_rv=(RecyclerView)headView.findViewById(R.id.header_tab_rv);
        header_iv=(ImageView)headView.findViewById(R.id.header_iv);

        gltab = new GridLayoutManager(getContext(), 4);

        header_tab_rv.setLayoutManager(gltab);

        //设置静默加载时提前加载的item个数
//        xRefreshView1.setPreLoadCount(4);
        xrefreshview.setLoadComplete(false);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.startRefresh();
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      optinData();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                    }
                }, 1000);
            }
        });
        //如果想在数据加载完成以后不隐藏footerview则需要调用下面这行代码并传入false
//        xRefreshView1.setHideFooterWhenComplete(false);

    }

    private void optinData() {

        new Thread(new LoadThread()).start();


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void initListener() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {


                    adapter.setData(index2BeanList);
                    xrefreshview.stopRefresh();
                    loadHeadView();


                }

            }
        };


        bannerMainDepth.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

                Intent intent=new Intent(getActivity(),IndexDetailActivity.class);
                intent.putExtra("index_detail_url",index2HeadBean.getBannerBeanList().get(position).getDetailUrl());
                startActivity(intent);
            }
        });

    }

    private void loadHeadView() {

        //加载头部轮播图
        loadBanner(bannerMainDepth, index2HeadBean);
        index2HeadtabBeanList=index2HeadBean.getHeadtabBeansList();
        index2HeadtabAdapter=new Index2HeadtabAdapter(index2HeadtabBeanList,getActivity());
        header_tab_rv.setAdapter(index2HeadtabAdapter);

        Glide.with(getActivity())
                .load(index2HeadBean.getImgUrl())
                .apply(new RequestOptions().placeholder(R.drawable.holder).error(R.drawable.holder).dontAnimate().centerCrop())
                .into( header_iv);

    }

    private void loadBanner(BGABanner banner, Index2HeadBean index2HeadBean) {
        /**
         * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
         * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
         */
        banner.setAutoPlayAble(index2HeadBean.getBannerBeanList().size() > 1);

        banner.setAdapter(this);
        List<String> imgs = new ArrayList<>();
        List<String> tips = new ArrayList<>();
        for (int i = 0; i < index2HeadBean.getBannerBeanList().size(); i++) {
            imgs.add(index2HeadBean.getBannerBeanList().get(i).getImgUrl());
//            tips.add(index2HeadBean.getBannerBeanList().get(i).getImgName());
        }

//        banner.setData(imgs, tips);

        banner.setData(imgs,tips);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.holder).error(R.drawable.holder).dontAnimate().centerCrop())
                .into(itemView);
    }


    private class LoadThread implements Runnable {
        @Override
        public void run() {
            try {
                Document document = Jsoup.connect("http://m.1kkk.com/").timeout(20000).get();
                //轮播图
                Elements imgs = document
                        .select("div.index-banner")
                        .select("div.swiper-container")
                        .select("div.swiper-wrapper")
                        .select("div.swiper-slide");
               //headtab
                Elements headTab=document.select("div.index-menu")
                        .select("div.index-menu-item");

//                item
                Elements item1item=document.select("div.manga-list");



                List<Index2BannerBean> bannerList=new ArrayList<>();
                //这是轮播图
                for (int i = 0; i < imgs.size(); i++) {
                    Index2BannerBean  bean = new Index2BannerBean();

                    String url=imgs.get(i).select("a")
                            .select("img")
                            .attr("src");

                    String detailUrl = "http://m.1kkk.com"+imgs.get(i).select("a")
                            .attr("href");


                    bean.setImgUrl(url);

                    bean.setDetailUrl(detailUrl);
                    bannerList.add(bean);

                }
                List<Index2HeadtabBean> headerTabList=new ArrayList<>();
                //这是headtab
                for (int i=0;i<headTab.size();i++){

                    Index2HeadtabBean bean=new Index2HeadtabBean();
                    String title=headTab.get(i).select("a").select("p").text();
                    String detailUrl="http://m.1kkk.com"+headTab.get(i).select("a").attr("href");
                    String imgUrl=headTab.get(i).select("a")
                            .select("img")
                            .attr("src");
                    bean.setDetailUrl(detailUrl);
                    bean.setImgUrl(imgUrl);
                    bean.setTitle(title);

                    headerTabList.add(bean);


                }
                //头部底部的图片
                String headBottomimgurl=document.select("div.ad-top")
                        .select("a")
                        .select("img")
                        .attr("src");


                Index2HeadBean headBean=new Index2HeadBean();

                headBean.setBannerBeanList(bannerList);
                headBean.setHeadtabBeansList(headerTabList);
                headBean.setImgUrl(headBottomimgurl);
                index2HeadBean=headBean;


                Index2Item1Bean index2Item1Bean=null;

                //第一种item（两行的）
                for (int i=0;i<item1item.size();i++){

//每次重新设置
                    Index2Bean index2BeanNew=new Index2Bean();

                    Elements item1=item1item.get(i)
                            .select("ul.manga-list-1");
                    if (item1.size()!=0){

                        index2Item1Bean=new Index2Item1Bean();
                       String leftT=item1item.get(i).select("div.manga-list-title")
                               .text().replace("\"","").replace("\"","").replace("更多","");
                        String rightT=item1item.get(i).select("div.manga-list-title").select("a")
                                .text();
                        String rightDetail="http://m.1kkk.com"+item1item.get(0).select("div.manga-list-title")
                                .select("a").attr("href");

                        Elements item1Pages=item1
                                .select("div.swiper-container1")
                                .select("div.swiper-wrapper")
                                .select("div.swiper-slide");

                        List<Index2Item1Page> index2Item1PageList=new ArrayList<>();
                       for (int j=0;j<item1Pages.size();j++){
                           Index2Item1Page  index2Item1Page=new Index2Item1Page();
                           index2Item1Page.setPage(j+"");

                           Elements item1List=item1Pages.get(j).select("li");

                           List<Index2Item1ItemBean> index2Item1ItemBeanList=new ArrayList<>();
                           for (int k=0;k<item1List.size();k++){
                               Index2Item1ItemBean bean1=new Index2Item1ItemBean();
                               String detail="http://m.1kkk.com"+item1List.get(k)
                                       .select("a").attr("href");
                               String imgUrl=item1List.get(k).select("a")
                                       .select("div.manga-list-1-cover").select("img")
                                       .attr("src");
                               String tag=item1List.get(k).select("a")
                                       .select("div.manga-list-1-cover").select("span")
                                       .text();

                               String title=item1List.get(k).select("a")
                                       .select("p.manga-list-2-title")
                                       .text();
                               String content=item1List.get(k).select("a")
                                       .select("p.manga-list-1-tip")
                                       .text();
                               bean1.setContent(content);
                               bean1.setDetailUrl(detail);
                               bean1.setImgUrl(imgUrl);
                               if (TextUtils.isEmpty(tag)){
                                   bean1.setTag("");
                               }else {
                                   bean1.setTag(tag);
                               }
                               bean1.setTitle(title);
                               index2Item1ItemBeanList.add(bean1);






                           }

                           index2Item1Page.setList(index2Item1ItemBeanList);


                           index2Item1PageList.add(index2Item1Page);

                       }


                        index2Item1Bean.setTitle1(leftT);
                        index2Item1Bean.setTitle2(rightT);
                        index2Item1Bean.setTitle2Detail(rightDetail);
                        index2Item1Bean.setIndex2Item1PageList(index2Item1PageList);


                        //第一种item(添加)
                        index2BeanNew.setItem1Bean(index2Item1Bean);

                        index2BeanNew.setType(0);
//               //设置头部bean
//                index2Bean.setHeadBean(headBean);

                        index2BeanList.add(index2BeanNew);



                    }








                }





                //第二种item（两行的）
                for (int i=0;i<item1item.size();i++){





                    Elements item1=item1item.get(i)
                            .select("ul.manga-list-2");
                    if (item1.size()!=0){

                        //每次重新设置
                        Index2Bean index2BeanNew=new Index2Bean();

                        Index2Item2Bean index2Item2Bean=new Index2Item2Bean();
                        String leftT=item1item.get(i).select("div.manga-list-title")
                                .text().replace("\"","").replace("更多","");
                        String rightT=item1item.get(i).select("div.manga-list-title").select("a")
                                .text();
                        String rightDetail="http://m.1kkk.com"+item1item.get(0).select("div.manga-list-title")
                                .select("a").attr("href");

                        Elements item1Pages=item1
                                .select("div.swiper-container2")
                                .select("div.swiper-wrapper")
                                .select("div.swiper-slide");

                        List<Index2Item2Page> index2Item2PageList=new ArrayList<>();
                        for (int j=0;j<item1Pages.size();j++){
                            Index2Item2Page  index2Item2Page=new Index2Item2Page();
                            index2Item2Page.setPage(j+"");

                            Elements item1List=item1Pages.get(j).select("li");

                            List<Index2Item2ItemBean> index2Item2ItemBeanList=new ArrayList<>();
                            for (int k=0;k<item1List.size();k++){
                                Index2Item2ItemBean bean1=new Index2Item2ItemBean();
                                String detail="http://m.1kkk.com"+
                                        item1List.get(k).select("div.manga-list-2-cover")
                                        .select("a").attr("href");
                                String imgUrl=item1List.get(k).select("div.manga-list-2-cover")
                                        .select("a")
                                        .select("img")
                                        .attr("src");
                                String tag=item1List.get(k).select("div.manga-list-2-cover").select("a")
                                        .select("span")
                                        .text();

                                String title=item1List.get(k).select("p.manga-list-2-title").select("a")
                                        .text();
                                String content=item1List.get(k).select("p.manga-list-2-tip").select("a")
                                        .text();
                                bean1.setContent(content);
                                bean1.setDetailUrl(detail);
                                bean1.setImgUrl(imgUrl);
                                if (TextUtils.isEmpty(tag)){
                                    bean1.setTag("");
                                }else {
                                    bean1.setTag(tag);
                                }
                                bean1.setTitle(title);
                                index2Item2ItemBeanList.add(bean1);






                            }

                            index2Item2Page.setList(index2Item2ItemBeanList);


                            index2Item2PageList.add(index2Item2Page);

                        }


                        index2Item2Bean.setTitle1(leftT);
                        index2Item2Bean.setTitle2(rightT);
                        index2Item2Bean.setTitle2Detail(rightDetail);
                        index2Item2Bean.setIndex2Item2PageList(index2Item2PageList);


                        //第二种item
                        index2BeanNew.setItem2Bean(index2Item2Bean);
                        index2BeanNew.setType(1);
                        index2BeanList.add(index2BeanNew);
                    }








                }






                //第三种item（排行）
                for (int i=0;i<item1item.size();i++){
                    Elements item1=item1item.get(i)
                            .select("ul.rank-list");

                    if(item1.size()!=0){

                        //每次重新设置
                        Index2Bean index2BeanNew=new Index2Bean();
                        Index2Item3Bean bean=new Index2Item3Bean();

                        String titile=item1item.get(i).select("div.manga-list-title")
                                .text().replace("\"","").trim().substring(0,3);

                        Elements tabs=item1item.get(i).select("div.manga-list-title")
                                .select("div.manga-list-title-right").
                                select("a");

                        List<TabsBean> tabList=new ArrayList<>();
                        for (int k=0;k<tabs.size();k++){
                            TabsBean tabsBean=new TabsBean();
                            String tabText=tabs.get(k).text();
                            tabsBean.setTabText(tabText);
                            if (k==0){
                                tabsBean.setSelect(true);

                            }else {

                                tabsBean.setSelect(false);
                            }

                            tabList.add(tabsBean);

                        }



                        Elements ranklist=item1item.get(i).select("ul.rank-list")
                                .select("div.swiper-container4")
                                .select("div.swiper-wrapper")
                                .select("div.swiper-slide");






                        List<Index2Item3Page>  item3PageList = new ArrayList<>();



                        for (int j=0;j<ranklist.size();j++){
                            Elements rankEs=ranklist.get(j).select("li");


                            Index2Item3Page item3PageBean=new Index2Item3Page();



                            List<Index2Item3ItemBean>  itemBean3List=new ArrayList<>();

                            for (int k=0;k<rankEs.size();k++){

                                Index2Item3ItemBean bean3=new Index2Item3ItemBean();

                                String detail="http://m.1kkk.com"+
                                        rankEs.get(k)
                                                .select("a").attr("href");
                                String imgUrl=rankEs.get(k).select("a")
                                        .select("div.rank-list-cover")
                                        .select("img")
                                        .attr("src");

                                String title=rankEs.get(k).select("a")
                                        .select("div.rank-list-info")
                                        .select("div.rank-list-info-right")
                                        .select("p.rank-list-info-right-title")
                                        .text();

                                String content=rankEs.get(k).select("a")
                                        .select("div.rank-list-info")
                                        .select("div.rank-list-info-right")
                                        .select("p.rank-list-info-right-subtitle")
                                        .text();

                                bean3.setContent(content);
                                bean3.setDetailUrl(detail);
                                bean3.setImgUrl(imgUrl);
                                bean3.setTitle(title);

                                itemBean3List.add(bean3);


                            }

                            item3PageBean.setPage(j+"");
                            item3PageBean.setList(itemBean3List);


                            item3PageList.add(item3PageBean);





                        }



                        bean.setTitle(titile);
                        bean.setTabArr(tabList);

                        bean.setIndex2Item3PageList(item3PageList);

                        index2BeanNew.setType(2);
                        index2BeanNew.setItem3Bean(bean);


                        index2BeanList.add(index2BeanNew);




                    }






                }


                Log.i("aaaaa",index2BeanList.toString());



                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
