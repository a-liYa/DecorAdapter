package com.aliya.adapter.sample.page;

import android.view.View;
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

    private TextView mTvState;
    private ImageView mIvIcon;

    public Refresh2Header(OnRefreshListener listener) {
        super(R.layout.layout_header_refresh2, listener);
        collapseDelay = 500;
    }

    @Override
    public void onViewCreated(View itemView) {
        super.onViewCreated(itemView);
        mTvState = findViewById(R.id.tv_state);
        mIvIcon = findViewById(R.id.iv_icon);
        triggerHeight = itemView.getMinimumHeight();
    }

    @Override
    protected void onRefreshStatusChange(int status) {
        switch (status) {
            case DROP_DOWN:
                mTvState.setText("下拉可以刷新");
                mIvIcon.setImageResource(R.mipmap.icon_refresh_down);
                break;
            case LOOSEN_UP:
                mTvState.setText("松开立即刷新");
                mIvIcon.setImageResource(R.mipmap.icon_refresh_up);
                break;
            case REFRESHING:
                mTvState.setText("正在刷新中...");
                mIvIcon.setImageResource(R.mipmap.icon_refreshing);
                refreshAnimation();
                break;
            case COMPLETE:
                mIvIcon.clearAnimation();
                mTvState.setText("刷新完成");
                mIvIcon.setImageResource(R.mipmap.icon_refresh_done);
                break;
        }
    }

    private void refreshAnimation() {
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
