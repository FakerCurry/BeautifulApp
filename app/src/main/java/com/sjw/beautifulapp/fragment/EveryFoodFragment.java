package com.sjw.beautifulapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.FoodAdapter;
import com.sjw.beautifulapp.bean.FoodInfo;
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
 * Description:
 * Data：2018/9/4-10:47
 * QQ:1175476869@qq.com
 * Author: 沈佳伟
 */

public class EveryFoodFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    Unbinder unbinder;
    private Bundle arg;
    private String url;
    private List<FoodInfo> foodInfoList;
    private int currentPage = 1;
    public Handler handler;
    private FoodAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_every_food, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodInfoList = new ArrayList<>();
        //当前的url
        url = arg.getString("url");

        //获取到url上面的数据
        optData();
        initHandler();


    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    foodInfoList = (List<FoodInfo>) msg.obj;
                    adapter.setData(foodInfoList);
                    if (xrefreshview!=null) {

                        if (xrefreshview!=null){
                            xrefreshview.stopRefresh();
                        }
                    }
                }

                if (msg.what == 2) {

                    CustomToast.showToast("没有更多数据了");

                }
                if (msg.what == 3) {
                    foodInfoList = (List<FoodInfo>) msg.obj;
                    adapter.setData(foodInfoList);

                }
            }
        };
    }

    private void optData() {

        recyclerView.setHasFixedSize(true);

        adapter = new FoodAdapter(foodInfoList, getActivity());
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
                    boolean isBottom = adapter.getItemCount() - 1 == lastVisibleItem;
                    if (isBottom) {
                        onLoadData();
                    }
                }
            }
        });

        new Thread(new LoadThread(currentPage)).start();

    }

    public static EveryFoodFragment newInstance(Bundle args) {
        EveryFoodFragment fragment = new EveryFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class LoadThread implements Runnable {
        private int page;

        public LoadThread(int currentPage) {
            this.page = currentPage;
        }

        @Override
        public void run() {
            try {
                String newUrl = "";
                newUrl = url.substring(0, url.length() - 5) + "-page-" + page + ".html";
                Document document = Jsoup.connect(url).timeout(20000).get();
                Elements content = document.select("div.ui_newlist_1")
                        .select("ul")
                        .select("li");
                //从结构可以看出页面的集合只有一个，点击的时候换个集合，所以直接就是获取集合
                List<FoodInfo> foodInfos = new ArrayList<>();
                for (int i = 0; i < content.size(); i++) {

                    FoodInfo foodInfo = new FoodInfo();
                    String foodInfoName = content.get(i).select("div.pic").select("a").attr("title");
                    String foodInfoImg = content.get(i).select("div.pic").select("a")
                            .select("img").attr("data-src");
                    String foodInfoAuthor = content.get(i).select("div.detail").
                            select("p")
                            .select("a").text();
                    String foodInfoDetail = content.get(i).select("div.detail").
                            select("p").text();
                    String foodDetailUrl = content.get(i).select("div.pic").select("a").attr("href");
                    foodInfo.setFoodName(foodInfoName);
                    foodInfo.setFoodImgUrl(foodInfoImg);
                    foodInfo.setFoodAuthor(foodInfoAuthor);
                    foodInfo.setFoodOrigin(foodInfoDetail);
                    foodInfo.setFoodDetailUrl(foodDetailUrl);
                    foodInfos.add(foodInfo);

                }

                if (foodInfos.size() == 0) {

                    if (page == 1) {
                        Message msg = new Message();
                        msg.obj = foodInfos;
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
                        msg.obj = foodInfos;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {
                        foodInfoList.addAll(foodInfos);
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = foodInfoList;
                        handler.sendMessage(msg);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onLoadData() {
        currentPage++;
        new Thread(new LoadThread(currentPage)).start();
    }

    private void refreshFood() {
        foodInfoList.clear();
        currentPage = 1;
        new Thread(new LoadThread(currentPage)).start();
    }
}
