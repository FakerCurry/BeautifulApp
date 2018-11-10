package com.sjw.beautifulapp.newtools.bigimg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.newtools.bigimg.adapter.ZoomableViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by idisfkj on 17/3/5.
 * Email : idisfkj@gmail.com.
 */

public class ZoomableActivity extends Activity implements ViewPager.OnPageChangeListener {
    private static final String EXTRA_ZOOMABLE_PATHS = "extra_zoomable_paths";
    private static final String EXTRA_ZOOMABLE_INDEX = "extra_zoomable_index";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    private ViewPager mViewPager;
    private TextView mZoomableIndex;
    private ArrayList<String> mPaths;
    private int mIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoommable_activity);
        ButterKnife.bind(this);
        getExtraData();
        initView();
        setupViewPager();
    }

    private void getExtraData() {
        mPaths = getIntent().getStringArrayListExtra(EXTRA_ZOOMABLE_PATHS);
        mIndex = getIntent().getIntExtra(EXTRA_ZOOMABLE_INDEX, 0);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mZoomableIndex = (TextView) findViewById(R.id.zoomable_index);
        title.setText("美女图片");
    }

    private void setupViewPager() {
        mViewPager.setAdapter(new ZoomableViewPagerAdapter(this, mPaths));
        mViewPager.setCurrentItem(mIndex);
        mZoomableIndex.setText(mIndex + 1 + "/" + mPaths.size());
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mZoomableIndex.setText(position + 1 + "/" + mPaths.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static void goToPage(Context context, ArrayList<String> paths, int index) {
        Intent intent = new Intent(context, ZoomableActivity.class);
        intent.putStringArrayListExtra(EXTRA_ZOOMABLE_PATHS, paths);
        intent.putExtra(EXTRA_ZOOMABLE_INDEX, index);
        context.startActivity(intent);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
