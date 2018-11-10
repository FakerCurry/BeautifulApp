package com.sjw.beautifulapp.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.GirlAdapter;
import com.sjw.beautifulapp.base.BaseFragment;
import com.sjw.beautifulapp.bean.GirlBean;
import com.sjw.beautifulapp.utils.CustomToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by donglinghao on 2016-01-28.
 */
public class GirlFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;
    private View mRootView;
    private int currentPage=1;
    private List<GirlBean> girlBeanList;
    public Handler handler;
    private GirlAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    private boolean isBottom;
    private int distance;

    private boolean visible = true;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
//    private ArrayList<String> mPaths;
    @Override
    protected void lazyLoad() {

    }

    @Override
    protected View setLayoutId(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_girl, container, false);
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

    private void onLoadData() {
        currentPage++;
        new Thread(new LoadThread(currentPage)).start();
    }

    private void refreshGirl() {
        girlBeanList.clear();
        currentPage = 1;
        new Thread(new LoadThread(currentPage)).start();
    }

    @Override
    protected void initData() {

//        mPaths = new ArrayList<>();
//        mPaths.add("http://img01.sogoucdn.com/app/a/100520024/b0badde998aa4fa9fd13212c13332c61");
//        mPaths.add("http://img02.sogoucdn.com/app/a/100520024/332b27eeb743580f8028eab1f1dda149");
//        mPaths.add("http://img01.sogoucdn.com/app/a/100520024/f2e39c989e6123722f9ce98eda22fd32");


        girlBeanList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        xrefreshview.startRefresh();
        adapter = new GirlAdapter(girlBeanList, getActivity());
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        gridLayoutManager=new GridLayoutManager(getActivity(),2);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
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
        //开始先加载
//        refreshGirl();
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
                        refreshGirl();

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
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
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



//        new Thread(new LoadThread(currentPage)).start();
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
                    girlBeanList = (List<GirlBean>) msg.obj;

                    adapter.setData(girlBeanList);

                    if (xrefreshview!=null){
                        xrefreshview.stopRefresh();
                    }

                }

                if (msg.what == 2) {

                    CustomToast.showToast("没有更多数据了");

                }
                if (msg.what == 3) {
                    girlBeanList = (List<GirlBean>) msg.obj;
                    adapter.setData(girlBeanList);

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


    private class LoadThread implements Runnable {
        int page = 1;

        public LoadThread(int currentPage) {
            this.page = currentPage;
        }

        @Override
        public void run() {


            try {
                List<GirlBean> beanList=new ArrayList<>();

                Document document = Jsoup.connect("http://www.27270.com/ent/meinvtupian/list_11_"+page+".html").timeout(10000).
                        get();
                Elements titleAndHttp = document.select("div.MeinvTuPianBox")
                        .select("ul")
                        .select("li");
                for (int i = 0; i < 10; i++) {
                    String girl_img = titleAndHttp.get(i)
                            .select("a")
                            .select("i")
                            .select("img")
                            .attr("src");
                    String girl_title = titleAndHttp.get(i)
                            .select("a")
                            .attr("title");

                    String girl_flag = titleAndHttp.get(i)
                            .select("span")
                            .select("u")
                            .select("a")
                            .attr("title");
                    String girl_time = titleAndHttp.get(i)
                            .select("span")
                            .select("em")
                            .text();
                    String girl_num=titleAndHttp.get(i)
                            .select("span")
                            .select("i")
                            .text();


                    GirlBean bean=new GirlBean();
                    bean.setGirl_flag(girl_flag);
                    bean.setGirl_name(girl_title);
                    bean.setGirl_nomenu(girl_num);
                    bean.setGirl_time(girl_time);
                    bean.setGirl_img(girl_img);

                    ArrayList<String> urls=new ArrayList<>();
                    //每个地址对应一张详情图
                    for (int j=0;j<8;j++){
                        //每张图片的大图的地址
                        String detail_img_url = titleAndHttp.get(i)
                                .select("a")
                                .attr("href");

                        if (j==0){

                            detail_img_url=detail_img_url;
                        }else {
                            detail_img_url=detail_img_url.replace(".html",("_"+(j+1)));
                            detail_img_url=detail_img_url+".html";
                        }
                        Document document_detail = Jsoup.connect(detail_img_url).timeout(10000).
                                get();
                        Elements titleAndHttp_detail = document_detail.select("div.articleV4Body")
                                .select("p")
                                .select("a");
                        String detail_img=titleAndHttp_detail
                                .select("img")
                                .attr("src");

                        urls.add(detail_img);
                    }
                    bean.setDetail_imgs(urls);

                    beanList.add(bean);


                }
                if (beanList.size() == 0) {

                    if (page == 1) {
                        Message msg = new Message();
                        msg.obj = beanList;
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
                        msg.obj = beanList;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {
                        girlBeanList.addAll(beanList);
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = girlBeanList;
                        handler.sendMessage(msg);

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
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
