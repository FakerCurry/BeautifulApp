package com.sjw.beautifulapp.activity;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.base.BaseActivity;
import com.sjw.beautifulapp.bean.EpisodeDetailBean;
import com.sjw.beautifulapp.bean.EpisodeDetailText;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EpisodeDetailActivity extends BaseActivity {


    @BindView(R.id.contain_ll)
    LinearLayout containLl;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    private Context context = EpisodeDetailActivity.this;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subname)
    TextView subname;
    @BindView(R.id.subtime)
    TextView subtime;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    EpisodeDetailBean bean;

    @Override
    protected void initView() {
      title.setText("段子详情");
    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_episode_detail);
    }

    @Override
    protected void initData() {
        bean = (EpisodeDetailBean) getIntent().getSerializableExtra("EpisodeDetailBean");

        detailTitle.setText(bean.getHeadBean().getHeadTitle());
        subname.setText(bean.getHeadBean().getHeadSubname());
        subtime.setText(bean.getHeadBean().getHeadSubtime());
//        Log.i("aa",bean.getImgUrls().get(0));
        //添加图片控件
        List<String> imgurls = new ArrayList<>();
        imgurls = bean.getImgUrls();
        if (imgurls != null) {

            if (imgurls.size() != 0) {
                for (int i = 0; i < imgurls.size(); i++) {
                    ImageView iv = new ImageView(context);
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    iv.setLayoutParams(ll);
                    Glide.with(context)
                            .load(imgurls.get(i))
//                .apply(options)
                            .into(iv);

                    containLl.addView(iv);


                }

            }
        }
        //添加textview
        List<EpisodeDetailText> detailTexts = new ArrayList<>();
        detailTexts = bean.getTextContents();
        if (detailTexts != null) {
            if (detailTexts.size() != 0) {
                for (int i = 0; i < detailTexts.size(); i++) {
                    TextView tv = new TextView(context);

                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    tv.setText(detailTexts.get(i).getText());
                    if (detailTexts.get(i).isBold()) {
                        tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {

                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }

                    tv.setLayoutParams(ll);
                    containLl.addView(tv);


                }


            }
        }


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

