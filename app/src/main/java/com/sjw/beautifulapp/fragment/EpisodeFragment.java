package com.sjw.beautifulapp.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.EpisodeAdapter;
import com.sjw.beautifulapp.base.BaseFragment;
import com.sjw.beautifulapp.bean.EpisodeBean;
import com.sjw.beautifulapp.bean.EpisodeDetaiHeadBean;
import com.sjw.beautifulapp.bean.EpisodeDetailBean;
import com.sjw.beautifulapp.bean.EpisodeDetailText;
import com.sjw.beautifulapp.utils.CustomToast;
import com.sjw.beautifulapp.utils.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description: 美食页面
 * Data：2018/9/3-13:04
 * QQ:1175476869@qq.com
 * Author: 沈佳伟
 */
public class EpisodeFragment extends BaseFragment {

    private static final String TAG = "EpisodeFragment";
    @BindView(R.id.ll_contain)
    FrameLayout llContain;
    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private View mRootView;
    private int currentPage = 2;
    //集合
    private List<EpisodeBean> speakBeanList;
    EpisodeAdapter adapter;

    public static Handler handler;
    LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean isBottom;

    private int distance;

    private boolean visible = true;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;



    @Override
    protected void lazyLoad() {

    }

    @Override
    protected View setLayoutId(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_episode, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    protected void initView(View view) {
        //隐藏
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f);
        ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY, pvhZ).setDuration(10).start();
        distance = 0;
        visible = false;
    }

    @Override
    protected void initData() {

        speakBeanList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        adapter = new EpisodeAdapter(speakBeanList, getActivity());
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
//        //开始先加载
//        refreshFood();
        xrefreshview.startRefresh();
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听
        xrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshFood();

                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
////                        for (int i = 0; i < 6; i++) {
////                            recyclerviewAdapter.insert(new Person("More ", mLoadCount + "21"),
////                                    recyclerviewAdapter.getAdapterItemCount());
////                        }
//                        mLoadCount++;
//                        if (mLoadCount >= 3) {//模拟没有更多数据的情况
//                            xRefreshView.setLoadComplete(true);
//                        } else {
//                            // 刷新完成必须调用此方法停止加载
//                            xRefreshView.stopLoadMore(false);
//                            //当数据加载失败 不需要隐藏footerview时，可以调用以下方法，传入false，不传默认为true
//                            // 同时在Footerview的onStateFinish(boolean hideFooter)，可以在hideFooter为false时，显示数据加载失败的ui
////                            xRefreshView1.stopLoadMore(false);
//                        }
//                    }
//                }, 1000);
            }
        });
        // 实现Recyclerview的滚动监听，在这里可以自己处理到达底部加载更多的操作，可以不实现onLoadMore方法，更加自由
        xrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isBottom = adapter.getItemCount() - 1 == lastVisibleItem;
                    if (isBottom) {
                        onLoadData();
                    }
                }
            }
        });




    }

    private void onLoadData() {
        currentPage++;
        new Thread(new LoadThread(currentPage)).start();
    }

    private void refreshFood() {
        speakBeanList.clear();
        currentPage = 1;
        new Thread(new LoadThread(currentPage)).start();
    }


    @Override
    protected void judgeNet() {

    }

    @Override
    protected void initListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (distance < -ViewConfiguration.getTouchSlop() && visible) {
//隐藏
                    //iv_go_uploading.setVisibility(View.GONE);
                    hideFABAnimation(fab);
                    distance = 0;
                    visible = false;
                } else if (distance > ViewConfiguration.getTouchSlop() && !visible) {

                    //显示fab
                    //iv_go_uploading.setVisibility(View.VISIBLE);
                    showFABAnimation(fab);
                    distance = 0;
                    visible = true;


                }
                if ((dy > 0 && !visible) || (dy < 0 && visible))//向下滑并且可见  或者  向上滑并且不可见
                    distance += dy;
            }
        });
        //设置监听
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToPosition(recyclerView, 0);

            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    speakBeanList = (List<EpisodeBean>) msg.obj;
                    adapter.setData(speakBeanList);

                    if (xrefreshview!=null){
                        xrefreshview.stopRefresh();
                    }


                }

                if (msg.what == 2) {

                    CustomToast.showToast("没有更多数据了");

                }
                if (msg.what == 3) {
                    speakBeanList = (List<EpisodeBean>) msg.obj;
                    adapter.setData(speakBeanList);

                }
            }
        };
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

    public class LoadThread implements Runnable {
        int page = 1;

        public LoadThread(int currentPage) {
            this.page = currentPage;
        }

        @Override
        public void run() {

            try {
                //每次刷新的集合
                List<EpisodeBean> speeks = new ArrayList<>();

                Document document = Jsoup.connect("http://heibaimanhua.com/duanzishou/page/" + page).timeout(20000).get();
                Elements titleAndHttp = document.select("div.ajax-load-con");
                for (int i = 0; i < 10; i++) {
                    EpisodeBean speek = new EpisodeBean();
                    String title = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-content")
                            .select("h2")
                            .select("a")
                            .attr("title");
                    String imgurl = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-img")
                            .select("a")
                            .select("img")
                            .attr("src");
                    String nameImg = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-content")
                            .select("div.posts-default-info")
                            .select("ul")
                            .select("li.ico-cat")
                            .select("a")
                            .attr("href");
                    String name = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-content")
                            .select("div.posts-default-info")
                            .select("ul")
                            .select("li.ico-cat")
                            .select("a")
                            .text();
                    String time = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-content")
                            .select("div.posts-default-info")
                            .select("ul")
                            .select("li.ico-time")
                            .text().replace("\"", "");
                    //详情页的地址
                    String detail_url = titleAndHttp.get(i)
                            .select("div.content-box")
                            .select("div.posts-gallery-content")
                            .select("h2")
                            .select("a")
                            .attr("href");
                     //去获取详情页的内容
                    Document document_detail = Jsoup.connect(detail_url).timeout(20000).get();
                    Elements titleAndHttp_detail = document_detail.select("div.post");


                    String headTitle=titleAndHttp_detail.select("div.post-title")
                            .select("h1")
                            .text();
                    String headSubname=titleAndHttp_detail.select("div.post-title").select("div.post_icon")
                            .select("span.postcat").select("a").text();
                    String headSubTime=titleAndHttp_detail.select("div.post-title").select("div.post_icon")
                            .select("span.postclock").text();
                    headSubTime=headSubTime.replace("\"","");
                    EpisodeDetaiHeadBean  headBean=new EpisodeDetaiHeadBean();

                    headBean.setHeadTitle(headTitle);
                    headBean.setHeadSubname(headSubname);
                    headBean.setHeadSubtime(headSubTime);
                    EpisodeDetailBean  episodeDetailBean=new EpisodeDetailBean();
                    episodeDetailBean.setHeadBean(headBean);

                    //详情页的文字
                    Elements titleAndHttp_detail_text = document_detail.select("div.post")
                            .select("div.post-content").select("div.post-images-item").select("p");
                    List<EpisodeDetailText> detailTexts=new ArrayList<>();
                    for (int j=0;j<titleAndHttp_detail_text.size();j++){
                             String strongText=titleAndHttp_detail_text.get(j).select("strong").text();
                             String text=titleAndHttp_detail_text.get(j).text();
                             boolean isStrong=false;
                             EpisodeDetailText detailTextBean=new EpisodeDetailText();
                     if (TextUtils.isEmpty(strongText)){

                          isStrong=false;
                          detailTextBean.setBold(isStrong);
                          detailTextBean.setText(text);

                     }else {

                         isStrong=true;
                         detailTextBean.setBold(isStrong);
                         detailTextBean.setText(strongText);
                     }

                      detailTexts.add(detailTextBean);


                    }
                    episodeDetailBean.setTextContents(detailTexts);

                    //详情页的图片
                    Elements titleAndHttp_detail_img = document_detail.select("div.post")
                            .select("div.post-content").select("div.post-images-item").select("div.post-image");
                    List<String> imgurls=new ArrayList<>();
                   for (int k=0;k<titleAndHttp_detail_img.size();k++){
                       String url=titleAndHttp_detail_img.get(k).select("img").attr("src");
                       imgurls.add(url);


                   }
                   episodeDetailBean.setImgUrls(imgurls);




                    speek.setDetailBean(episodeDetailBean);
                    speek.setTitle(title);
                    speek.setImgUrl(imgurl);
//                    speek.setNameImg(nameImg);
                    speek.setName(name);
                    speek.setTime(time);
                    LogUtils.logE(TAG, imgurl);
                    speeks.add(speek);
                }

                if (speeks.size() == 0) {

                    if (page == 1) {
                        Message msg = new Message();
                        msg.obj = speeks;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);

                    }
                } else {
                    if (page == 1) {
                        Message msg = new Message();
                        msg.obj = speeks;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {
                        speakBeanList.addAll(speeks);
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = speakBeanList;
                        handler.sendMessage(msg);

                    }

                }


            } catch (Exception e) {
                LogUtils.logE(TAG, e.getMessage().toString());
            }


        }

    }


    /**
     * by moos on 2017.8.21
     * func:显示fab动画
     */
    public void showFABAnimation(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(400).start();
    }

    /**
     * by moos on 2017.8.21
     * func:隐藏fab的动画
     */

    public void hideFABAnimation(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(400).start();
    }


    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }
}
