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

public class IndexDetailActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.index_detailwview)
    LinearLayout indexDetailwview;
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
        setContentView(R.layout.activity_index_detail);
    }

    @Override
    protected void initData() {

        title.setText("小说");
        gIntent = getIntent();
        url = gIntent.getStringExtra("index_detail_url");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(indexDetailwview, new LinearLayout.LayoutParams(-1, -1))
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



    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
