package com.aliya.adapter.sample.page;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.adapter.page.RefreshPage;
import com.aliya.adapter.sample.R;

/**
 * 下拉刷新 - header （浙江24小时）
 *
 * @author a_liYa
 * @date 2017/8/4 13:48.
 */
public class Refresh2Header extends RefreshPage {

    public static final String HINT_NORMAL = "下拉可以刷新";
    public static final String HINT_RELEASE = "松开立即刷新";
    public static final String HINT_LOADING = "正在刷新数据中…";

    private TextView mTvState;
    private ImageView mIvIcon;

    public Refresh2Header(RecyclerView recycler, OnRefreshListener listener) {
        super(recycler, R.layout.layout_header_refresh2, listener);
        mTvState = findViewById(R.id.tv_state);
        mIvIcon = findViewById(R.id.iv_icon);
    }

    @Override
    protected void onRefreshStatusChange(int status) {
        Log.e("TAG", "onRefreshStatusChange: " + status);
        switch (status) {
            case DROP_DOWN:
                mTvState.setText(HINT_NORMAL);
                mIvIcon.setImageResource(R.mipmap.icon_refresh_down);
                break;
            case LOOSEN_UP:
                mTvState.setText(HINT_RELEASE);
                mIvIcon.setImageResource(R.mipmap.icon_refresh_up);
                break;
            case REFRESHING:
                mTvState.setText(HINT_LOADING);
                startRefreshAnim();
                break;
            case COMPLETE:
                mIvIcon.clearAnimation();
                break;
        }
    }

    private void startRefreshAnim() {
        mIvIcon.setImageResource(R.mipmap.icon_refreshing);
        RotateAnimation animation = new RotateAnimation(
                0f,
                359f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        mIvIcon.startAnimation(animation);
    }
}
