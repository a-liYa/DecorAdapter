package com.aliya.adapter.sample.page;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.adapter.page.RefreshPage;
import com.aliya.adapter.sample.R;

import static android.view.animation.Animation.INFINITE;

/**
 * 下拉刷新 - header （样式一）
 *
 * @author a_liYa
 * @date 2017/8/4 13:48.
 */
public class RefreshHeader extends RefreshPage {

    private TextView mTvState;
    private ImageView mIvSearch;

    private ValueAnimator mIcSearchAnimator;

    public RefreshHeader(OnRefreshListener listener) {
        super(R.layout.layout_header_refresh, listener);
    }

    @Override
    public void onViewCreated(View itemView) {
        super.onViewCreated(itemView);
        mTvState = findViewById(R.id.tv_state);
        mIvSearch = findViewById(R.id.iv_search);
        triggerHeight = itemView.getMinimumHeight();
    }

    @Override
    protected void onRefreshStatusChange(int status) {
        switch (status) {

            case DROP_DOWN:
                mTvState.setText("下拉可以刷新");
                break;
            case LOOSEN_UP:
                mTvState.setText("松开立即刷新");
                break;
            case REFRESHING:
                mTvState.setText("正在刷新中 ");
                startSearchAnim();
                break;
            case COMPLETE:
                if (mIcSearchAnimator != null) {
                    mIcSearchAnimator.cancel();
                    mIvSearch.setTranslationX(0);
                    mIvSearch.setTranslationY(0);
                }
                break;
        }
    }

    private void startSearchAnim() {
        final float R = mIvSearch.getMinimumHeight() / 5f;
        if (mIcSearchAnimator == null) {
            mIcSearchAnimator = ValueAnimator.ofFloat(0, 2 * (float) Math.PI);
            mIcSearchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    mIvSearch.setTranslationX((float) Math.cos(val) * R);
                    mIvSearch.setTranslationY((float) Math.sin(val) * R);
                }
            });
            mIcSearchAnimator.setDuration(1200);
            mIcSearchAnimator.setRepeatCount(INFINITE);
        }
        mIcSearchAnimator.start();
    }

//    public static String friendlyTime(long time) {
//        //获取time距离当前的秒数
//        int ct = (int) ((System.currentTimeMillis() - time) / 1000);
//
//        if (ct == 0) {
//            return "刚刚";
//        }
//
//        if (ct > 0 && ct < 60) {
//            return ct + "秒前";
//        }
//
//        if (ct >= 60 && ct < 3600) {
//            return Math.max(ct / 60, 1) + "分钟前";
//        }
//        if (ct >= 3600 && ct < 86400)
//            return ct / 3600 + "小时前";
//        if (ct >= 86400 && ct < 2592000) { //86400 * 30
//            int day = ct / 86400;
//            return day + "天前";
//        }
//        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
//            return ct / 2592000 + "月前";
//        }
//        return ct / 31104000 + "年前";
//    }
}
