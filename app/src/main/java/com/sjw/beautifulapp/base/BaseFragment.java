package com.sjw.beautifulapp.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by admin on 2018/4/14.
 */

public abstract class BaseFragment extends Fragment {


    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    //防止rxjava内存泄漏
    public CompositeDisposable mCompositeDisposable;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setLayoutId(inflater, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//防止rxjava内存泄漏
        mCompositeDisposable = new CompositeDisposable();
        initView(view);
        initData();
        judgeNet();
        initListener();
    }


    /**
     * 设置view
     *
     * @param inflater
     * @param container
     */
    protected abstract View setLayoutId(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化控件
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 判断有没有网络
     */
    protected abstract void judgeNet();

    /**
     * 设置监听事件
     */
    protected abstract void initListener();

    /**
     * 启动一个Activity
     * * @param activity 需要启动的Activity的Class
     * * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Fragment> activity, boolean needFinish) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
//        if (needFinish) {
//            finish();
//        } else {
//
//        }
    }

    /**
     * 启动一个Activity
     *
     * @param activity   需要启动的Activity的Class
     * @param bundle     需要穿的值
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Fragment> activity, Bundle bundle, boolean needFinish) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtras(bundle);
        startActivity(intent);
//        if (needFinish) {
//            finish();
//        } else {
//
//        }
    }

    /**
     * 启动一个Activity,并且有返回
     * * @param activity 需要启动的Activity的Class
     * * @param requestCode 请求code
     *
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Fragment> activity, int requestCode, boolean needFinish) {
        Intent intent = new Intent(getActivity(), activity);
        startActivityForResult(intent, requestCode);
//        if (needFinish) {
//            finish();
//        } else {
//
//        }
    }

    /**
     * 启动一个Activity,并且有返回
     * * @param activity 需要启动的Activity的Class
     * * @param bundle 需要传的值
     * * @param requestCode 请求code
     *
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Fragment> activity, Bundle bundle, int requestCode, boolean needFinish) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
//        if (needFinish) {
//            finish();
//        } else {
//
//        }
    }

//    /**
//     * 全屏
//     */
//    public void fullScreen() {
//
//        //去除标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去除状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }


    /**
     * 添加订阅
     */
    public void addDisposable(Disposable mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
    }

    /**
     * 取消所有订阅
     */
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDisposable();
    }
}
