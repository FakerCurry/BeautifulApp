package com.sjw.beautifulapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.activity.IndexDetailActivity;
import com.sjw.beautifulapp.adapter.IndexMultiItemAdapter;
import com.sjw.beautifulapp.base.BaseFragment;
import com.sjw.beautifulapp.bean.BookActive;
import com.sjw.beautifulapp.bean.BookClassify;
import com.sjw.beautifulapp.bean.BookHot;
import com.sjw.beautifulapp.bean.BookHotInfo;
import com.sjw.beautifulapp.bean.BookInfo;
import com.sjw.beautifulapp.bean.IndexBannerBean;
import com.sjw.beautifulapp.bean.IndexBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by donglinghao on 2016-01-28.
 */
public class IndexFragment extends BaseFragment implements BGABanner.Adapter<ImageView, String> {

    BGABanner bannerMainDepth;
    Unbinder unbinder;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private View mRootView;
    List<IndexBannerBean> indexBannerBeanList;
    List<IndexBean> indexBeanList;

    public Handler handler;
    IndexMultiItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    View headView;


    @Override

    protected void lazyLoad() {

    }

    @Override
    protected View setLayoutId(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_index, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    protected void initView(View view) {


    }

    @Override
    protected void initData() {
        indexBannerBeanList = new ArrayList<>();
        indexBeanList = new ArrayList<>();
        //初始化
        init();

//        //从网页上获取轮播图的信息
//        optinBannerData();


    }

    private void init() {
        recyclerView.setHasFixedSize(true);

        adapter = new IndexMultiItemAdapter(indexBeanList, getActivity());
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setPullLoadEnable(false);
        xrefreshview.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        headView = adapter.setHeaderView(R.layout.headview_index, recyclerView);
        bannerMainDepth = (BGABanner) headView.findViewById(R.id.banner_main_depth);
        
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
                        optinBannerData();
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

    private void optinBannerData() {

        new Thread(new LoadThread()).start();
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
                    indexBeanList = (List<IndexBean>) msg.obj;

                    loadIndex();


                }

            }
        };


        bannerMainDepth.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

                Intent intent=new Intent(getActivity(),IndexDetailActivity.class);
                intent.putExtra("index_detail_url",indexBannerBeanList.get(position).getDetailUrl());
                startActivity(intent);
            }
        });
    }

    private void loadIndex() {
        //加载头部轮播图
        loadData(bannerMainDepth, indexBannerBeanList);
//        //加载所有项数据
//        adapter.setData(indexBeanList);
        if (xrefreshview!=null){
            xrefreshview.stopRefresh();
        }

    }

    private void loadData(BGABanner banner, List<IndexBannerBean> bannerList) {
        /**
         * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
         * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
         */
        banner.setAutoPlayAble(bannerList.size() > 1);

        banner.setAdapter(this);
        List<String> imgs = new ArrayList<>();
        List<String> tips = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            imgs.add(bannerList.get(i).getImgUrl());
            tips.add(bannerList.get(i).getImgName());
        }

        banner.setData(imgs, tips);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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
                Document document = Jsoup.connect("http://www.zhulang.com/").timeout(20000).get();
                Elements imgs = document
                        .select("div.bx-viewport")
                        .select("div.main")
                        .select("div.bx-clone");
//                Elements classify = document.select("div.focus-list")
//                        .select("ul.focus-tab")
//                        .select("li");
//                Elements classifyDetail = document.select("div.focus-con")
//                        .select("ul");
//
//                Elements actives = document.select("div.news-flash")
//                        .select("dl")
//                        .select("dd")
//                        .select("p")
//                        .select("a");
//                Elements bookHots = document.select("div.img-list")
//                        .select("ul")
//                        .select("li");


                //整个的首页所有的值得bean
                IndexBean indexBean = new IndexBean();

                //这是轮播图
                for (int i = 0; i < imgs.size(); i++) {
                    IndexBannerBean bean = new IndexBannerBean();

                    String url=imgs.get(i).select("a")
                            .select("img")
                            .attr("data-src");

                    String detailUrl = imgs.get(i).select("a")
                            .attr("href");
                    if (TextUtils.isEmpty(url)) {
                        url = imgs.get(i).select("a")
                                .select("img")
                                .attr("src");

                    }
                    String des = imgs.get(i).select("a")
                            .attr("title");
                    bean.setImgUrl(url);
                    bean.setImgName(des);
                    bean.setDetailUrl(detailUrl);
                    indexBannerBeanList.add(bean);
                }
//                indexBean.setIndexBannerBeans(indexBannerBeanList);
//                indexBeanList.add(indexBean);
//                indexBean = new IndexBean();


//                List<BookClassify> bookClassifies = new ArrayList<>();
//                //这是书籍分类
//                for (int i = 0; i < classify.size(); i++) {
//                    BookClassify bookClassify = new BookClassify();
//                    String classifyName = classify.get(i).select("a").text();
//                    bookClassify.setBookInfoType(classifyName);
//                    bookClassifies.add(bookClassify);
//
//                }
//
//                for (int i = 0; i < classifyDetail.size(); i++) {
//                    Elements classifyInfos = classifyDetail.get(i).select("li");
//                    List<BookInfo> bookInfos = new ArrayList<>();
//                    for (int j = 0; j < classifyInfos.size(); j++) {
//                        BookInfo bookInfo = new BookInfo();
//                        String imgurl;
//                                if (i==0){
//                                    imgurl= classifyInfos.get(j).select("dl")
//                                            .select("dt")
//                                            .select("a")
//                                            .select("img")
//                                            .attr("src");
//                                }else{
//                                    imgurl= classifyInfos.get(j).select("dl")
//                                            .select("dt")
//                                            .select("a")
//                                            .select("img")
//                                            .attr("data-src");
//                                     }
//
//                                     String detailurl=classifyInfos.get(j).select("dl")
//                                             .select("dt")
//                                             .select("a")
//                                             .attr("href");
//
//                        String name = classifyInfos.get(j).select("dl")
//                                .select("dd")
//                                .select("h3")
//                                .select("a")
//                                .text();
//                        String type = classifyInfos.get(j).select("dl")
//                                .select("dd")
//                                .select("p")
//                                .get(0)
//                                .text();
//                        String content = classifyInfos.get(j).select("dl")
//                                .select("dd")
//                                .select("p")
//                                .get(1)
//                                .text();
//                        bookInfo.setBookImg(imgurl);
//                        bookInfo.setBookIntroduce(content);
//                        bookInfo.setBookTitle(name);
//                        bookInfo.setBookType(type);
//                        bookInfo.setDetailurl(detailurl);
//                        bookInfos.add(bookInfo);
//
//
//                    }
//                    bookClassifies.get(i).setBookInfos(bookInfos);
//                }
//                //先赋值选择(初始化)
//                for (int i = 0; i < bookClassifies.size(); i++) {
//                    if (i == 0) {
//                        bookClassifies.get(i).setSelect(true);
//                    } else {
//                        bookClassifies.get(i).setSelect(false);
//                    }
//                }
//                indexBean.setBookClassifies(bookClassifies);
//                indexBean.setType(0);
//                indexBeanList.add(indexBean);
//                indexBean = new IndexBean();
//
//
//                //这是活动快讯
//                //①先添加title
//                BookActive bookActive = new BookActive();
//                String activeTitle = document.select("div.news-flash")
//                        .select("dl")
//                        .select("dt")
//                        .text();
//                bookActive.setTitle(activeTitle);
//                //②再添加所有
//                List<String> activesList = new ArrayList<>();
//                for (int i = 0; i < actives.size(); i++) {
//                    String activeName = actives.get(i).text();
//                    activesList.add(activeName);
//                }
//                bookActive.setContent(activesList);
//                indexBean.setBookActive(bookActive);
//                indexBean.setType(1);
//                indexBeanList.add(indexBean);
//                indexBean = new IndexBean();
//
//
//                //这是热门精选
//                //①先加上标题
//                BookHot bookHot = new BookHot();
//                String hotTitle = document.select("div.img-list")
//                        .select("div.bdrbox-tit2")
//                        .select("h2")
//                        .text();
//                bookHot.setTitle(hotTitle);
//
//                //②加上内容
//                List<BookHotInfo> bookHotInfos = new ArrayList<>();
//                for (int i = 0; i < bookHots.size(); i++) {
//                    BookHotInfo bookHotInfo = new BookHotInfo();
//                    String hotInfoTitle = bookHots.get(i)
//                            .select("a")
//                            .attr("title");
//                    String hotInfoImg = bookHots.get(i)
//                            .select("a")
//                            .select("img")
//                            .attr("data-src");
//                    String detailurl=bookHots.get(i)
//                            .select("a")
//                            .attr("href");
//                    String author = bookHots.get(i)
//                            .select("span")
//                            .select("a")
//                            .text();
//                    bookHotInfo.setHotAuthor(author);
//                    bookHotInfo.setHotImg(hotInfoImg);
//                    bookHotInfo.setHotTitle(hotInfoTitle);
//                    bookHotInfo.setDetailurl(detailurl);
//                    bookHotInfos.add(bookHotInfo);
//
//                }
//                bookHot.setBookHotInfos(bookHotInfos);
//                indexBean.setBookHot(bookHot);
//                indexBean.setType(2);
//                indexBeanList.add(indexBean);
//                indexBean = new IndexBean();


                Message msg = new Message();
                msg.what = 1;
                msg.obj = indexBeanList;
                handler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
