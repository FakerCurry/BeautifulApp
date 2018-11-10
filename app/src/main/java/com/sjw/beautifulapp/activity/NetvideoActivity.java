package com.sjw.beautifulapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.base.BaseAppCompatActivity;
import com.sjw.beautifulapp.newtools.video.LandLayoutVideo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetvideoActivity extends BaseAppCompatActivity {
    @BindView(R.id.detail_player)
    LandLayoutVideo detailPlayer;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.btn_player)
    Button btnPlayer;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private OrientationUtils orientationUtils;
    private boolean cache;
    private String url;
    private boolean isPlay;
    private boolean isPause;

    @Override
    protected void initView() {

    }

    @Override
    protected void judgeNet() {

    }

    @Override
    protected void setLayoutId() {
        setContentView(R.layout.activity_netvideo);
    }

    @Override
    protected void initData() {
        title.setText("视频");
        url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.videoimg);
        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                .setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(cache)
                .setVideoTitle("我的视频")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                });
        gsyVideoOptionBuilder.build(detailPlayer);

//        playVideo();


    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(NetvideoActivity.this, true, true);
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalertDialog();
            }
        });


//        detailPlayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImageView testImage = findViewById(R.id.test_image_view);
//                Glide.with(InputUrlDetailActivity.this.getApplicationContext())
//                        .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525708180755&di=078af5aedf4b44268425be46bf25e407&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F203fb80e7bec54e78494e3a3bb389b504fc26a17.jpg")
//                        .into(testImage);
//            }
//        }, 5000);
    }

    private void showalertDialog() {

        XXDialog xxDialog = new XXDialog(this, R.layout.dialog_customze) {
            @Override
            public void convert(DialogViewHolder holder) {
                holder.setOnClick(R.id.dialog_sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url = etUrl.getText().toString();
                        playVideo();
                        dismiss();

                    }
                });
                holder.setOnClick(R.id.dialog_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                holder.getView(R.id.isCache_ll).setVisibility(View.VISIBLE);
                CheckBox isCache_cb = holder.getView(R.id.isCache_cb);
                isCache_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        cache = b;
                    }
                });
                holder.setText(R.id.dialog_msg, "是否播放视频");
                holder.setText(R.id.dialog_cancel_tv, "取消");

                holder.setText(R.id.dialog_sure_tv, "播放");

            }
        }.fromBottomToMiddle()
                .showDialog().setCanceledOnTouchOutside(false)
                .setCancelAble(false);

    }

    private void playVideo() {
        detailPlayer.release();
        gsyVideoOptionBuilder.setUrl(url)
                .setCacheWithPlay(cache)
                .setVideoTitle("我的视频")
                .build(detailPlayer);
        gsyVideoOptionBuilder.build(detailPlayer);
        detailPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                detailPlayer.startPlayLogic();
            }
        }, 1000);
    }


    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    @Override
    protected void beforeUnbinder() {
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }








    @OnClick({R.id.back, R.id.btn_player})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_player:
                
                break;
        }
    }


}

