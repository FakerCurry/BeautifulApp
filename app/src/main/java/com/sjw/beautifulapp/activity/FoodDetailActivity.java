package com.sjw.beautifulapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.food_detailwview)
    LinearLayout foodDetailwview;
    @BindView(R.id.title)
    TextView title;
    private Intent gIntent;
    private String url;

    AgentWeb mAgentWeb;

    @Override
    protected void initView() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_food_detail);
    }

    @Override
    protected void initData() {

        title.setText("美食");
        gIntent = getIntent();
        url = gIntent.getStringExtra("foodDetailUrl");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(foodDetailwview, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void beforeUnbinder() {

    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }
//    @Override
//    public void onDestroyView() {
//        mAgentWeb.getWebLifeCycle().onDestroy();
//        super.onDestroy();
//    }


    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


}
