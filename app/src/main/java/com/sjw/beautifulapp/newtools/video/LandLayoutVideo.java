package com.sjw.beautifulapp.newtools.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.sjw.beautifulapp.R;

/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时候使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    private boolean isLinkScroll = false;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.video_land;
        }
        return R.layout.video_normal;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if(mStartButton instanceof  ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

    @Override
    public int getEnlargeImageRes() {
            return R.drawable.custom_enlarge;
    }

    @Override
    public int getShrinkImageRes() {
        return R.drawable.custom_shrink;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLinkScroll && !isIfCurrentIsFullscreen()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        LandLayoutVideo landLayoutVideo = (LandLayoutVideo)gsyVideoPlayer;
        landLayoutVideo.dismissProgressDialog();
        landLayoutVideo.dismissVolumeDialog();
        landLayoutVideo.dismissBrightnessDialog();
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    public void setLinkScroll(boolean linkScroll) {
        isLinkScroll = linkScroll;
    }
}
