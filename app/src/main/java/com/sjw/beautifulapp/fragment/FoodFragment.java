package com.sjw.beautifulapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.adapter.MyFragmentPagerAdapter;
import com.sjw.beautifulapp.base.BaseFragment;
import com.sjw.beautifulapp.bean.FoodBean;
import com.sjw.beautifulapp.bean.FoodTitle;
import com.sjw.beautifulapp.utils.LogUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description: 美食
 * Data：2018/9/4-10:39
 * QQ:1175476869@qq.com
 * Author: 沈佳伟
 */
public class FoodFragment extends BaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;
    private View mRootView;
    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mDataList = new ArrayList<String>(Arrays.asList(CHANNELS));
    private List<Fragment> fragmentList;
    private MyFragmentPagerAdapter pageAdapter;
    private CommonNavigator mCommonNavigator;

    private int currentPage = 1;
    //总的数据
    private List<FoodBean> foodBeanList;
    //title的url集合
    private List<FoodTitle> titleUrlList;
    private String TAG = "FoodFragment";
    public Handler handler;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected View setLayoutId(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_food, container, false);
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
        foodBeanList = new ArrayList<>();
        titleUrlList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        //获取数据
        optinDataFromHtml();
        initHandler();


    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    fragmentList.clear();
                    viewPager.setOffscreenPageLimit(titleUrlList.size());
                    for (int i = 0; i < titleUrlList.size(); i++) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", titleUrlList.get(i).getUrl());
                        Fragment fg = EveryFoodFragment.newInstance(bundle);
                        fragmentList.add(fg);
                        pageAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
                    }
                    viewPager.setAdapter(pageAdapter);
                    magicIndicator.setBackgroundColor(Color.parseColor("#56A5EB"));
                    mCommonNavigator = new CommonNavigator(getActivity());
                    mCommonNavigator.setSkimOver(true);
                    mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

                        @Override
                        public int getCount() {
                            return titleUrlList.size();
                        }

                        @Override
                        public IPagerTitleView getTitleView(Context context, final int index) {
                            ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                            clipPagerTitleView.setText(titleUrlList.get(index).getTitle());
                            clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                            clipPagerTitleView.setClipColor(Color.WHITE);
                            clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewPager.setCurrentItem(index);
                                }
                            });
                            return clipPagerTitleView;
                        }

                        @Override
                        public IPagerIndicator getIndicator(Context context) {
                            return null;
                        }
                    });
                    magicIndicator.setNavigator(mCommonNavigator);
                    ViewPagerHelper.bind(magicIndicator, viewPager);

                }
            }
        };

    }

    private void optinDataFromHtml() {
        new Thread(new LoadThread(currentPage)).start();

    }


    @Override
    protected void judgeNet() {

    }

    @Override
    protected void initListener() {

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
        private int page;

        public LoadThread(int currentPage) {
            this.page = currentPage;
        }

        @Override
        public void run() {
            try {
                Document document = Jsoup.connect("https://home.meishichina.com/show-top-type-recipe.html").get();
                Elements titles = document.select("div.nav_wrap2").select("ul").select("li");

//                Elements content = document.select("div.ui_newlist_1 get_num")
//                        .select("ul")
//                        .select("li");


                for (int i = 0; i < titles.size()-1; i++) {
                    //需要解析的url
                    String titleUrl = titles.get(i).select("a").attr("href");

                    String titleName = titles.get(i).select("a").text();
                    //需要拼接
                    FoodTitle foodTitle = new FoodTitle();
                    foodTitle.setTitle(titleName);
                    foodTitle.setUrl(titleUrl);
                    titleUrlList.add(foodTitle);
//                    //总的食物
//                    FoodBean foodBean = new FoodBean();
//                    foodBean.setTitle(titleName);
//                    foodBeanList.add(foodBean);


                }

//                //从结构可以看出页面的集合只有一个，点击的时候换个集合，所以直接就是获取集合
//                for (int i = 0; i < content.size(); i++) {
//                    List<FoodInfo> foodInfoList = new ArrayList<>();
//                    FoodBean foodBean = new FoodBean();
//                    foodBean.setTitle(foodBeanList.get(0).getTitle());
//                    FoodInfo foodInfo = new FoodInfo();
//                    String foodInfoName = content.select("div.pic").select("a").attr("title");
//                    String foodInfoImg = content.select("div.pic").select("a")
//                            .select("img").attr("src");
//                    String foodInfoAuthor = content.select("div.detail").
//                            select("p")
//                            .select("a").text();
//                    String foodInfoDetail = content.select("div.detail").
//                            select("p").text();
//
//                    foodInfo.setFoodName(foodInfoName);
//                    foodInfo.setFoodImgUrl(foodInfoImg);
//                    foodInfo.setFoodAuthor(foodInfoAuthor);
//                    foodInfo.setFoodOrigin(foodInfoDetail);
//                    foodBean.setFoodInfoList(foodInfoList);
//                    foodBeanList.set(0, foodBean);
//
//                }
                handler.sendEmptyMessage(1);


            } catch (Exception e) {
                LogUtils.logE(TAG, e.getMessage().toString());

            }


        }
    }
}
