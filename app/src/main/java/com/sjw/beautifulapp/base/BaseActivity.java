package com.sjw.beautifulapp.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;


import com.sjw.beautifulapp.utils.ActivityListUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by admin on 2018/4/14.
 */

public abstract class BaseActivity extends Activity {
    //定义并且解除绑定控件
    private Unbinder unbinder;
    //获取登录的标志
    //0是用户登录 1是个人登录
    private String flag;


    //防止rxjava内存泄漏
    public CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListUtil.getInstance().addActivity(this);
        //1.设置布局
        setLayoutId();
        //防止rxjava内存泄漏
        mCompositeDisposable = new CompositeDisposable();
//        绑定butterknife
        unbinder = ButterKnife.bind(this);

//        //注册EventBus
//        EventBus.getDefault().register(this);
        //初始化控件
        initView();
        //初始化数据
        initData();
        //判断是否有网络
        judgeNet();
        //设置监听
        setListener();

    }

    /**
     * 判断网络
     *
     * @return
     */
    protected abstract void initView();

    /**
     * 判断网络
     *
     * @return
     */
    protected abstract void judgeNet();

    /**
     * 设置布局
     */
    protected abstract void setLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 监听方法
     */
    protected abstract void setListener();


    /**
     * 界面销毁的时候
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDisposable();
        //销毁控件之前的操作
        beforeUnbinder();
        unbinder.unbind();
//        //取消EventBus
//        EventBus.getDefault().unregister(this);
//        ActivityListUtil.getInstance().removeActivity(this);
    }

    protected abstract void beforeUnbinder();

    /**
     * 启动一个Activity
     * * @param activity 需要启动的Activity的Class
     * * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Activity> activity, boolean needFinish) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        if (needFinish) {
            finish();
        } else {

        }
    }

    /**
     * 启动一个Activity
     *
     * @param activity   需要启动的Activity的Class
     * @param bundle     需要穿的值
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Activity> activity, Bundle bundle, boolean needFinish) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);
        if (needFinish) {
            finish();
        } else {

        }
    }

    /**
     * 启动一个Activity,并且有返回
     * * @param activity 需要启动的Activity的Class
     * * @param requestCode 请求code
     *
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Activity> activity, int requestCode, boolean needFinish) {
        Intent intent = new Intent(this, activity);
        startActivityForResult(intent, requestCode);
        if (needFinish) {
            finish();
        } else {

        }
    }

    /**
     * 启动一个Activity,并且有返回
     * * @param activity 需要启动的Activity的Class
     * * @param bundle 需要传的值
     * * @param requestCode 请求code
     *
     * @param needFinish true表示有finish
     */
    public void startActivity(Class<? extends Activity> activity, Bundle bundle, int requestCode, boolean needFinish) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        if (needFinish) {
            finish();
        } else {

        }
    }

    /**
     * 全屏
     */
    public void fullScreen() {

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


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


}
