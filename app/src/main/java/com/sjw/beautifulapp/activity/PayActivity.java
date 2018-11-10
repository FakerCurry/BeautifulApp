package com.sjw.beautifulapp.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void initView() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void initData() {
    title.setText("打赏");
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
