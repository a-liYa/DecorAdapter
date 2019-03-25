package com.aliya.adapter.sample.page;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.sample.R;

import static android.view.animation.Animation.INFINITE;

/**
 * 下拉刷新 - header
 *
 * @author a_liYa
 * @date 2017/8/4 13:48.
 */
public class RefreshHeader extends PageItem {

    public static final String HINT_NORMAL = "下拉可以刷新";
    public static final String HINT_RELEASE = "松开立即刷新";
    public static final String HINT_LOADING = "正在刷新中";

    private TextView mTvState;
    private ImageView mIvSearch;
    private RelativeLayout mContainer;
    /**
     * 正在刷新
     */
    boolean refreshing;
    boolean startTouching;

    boolean enabled = true; // 是否可用

    RecyclerView mRecycler;

    private ValueAnimator mIcSearchAnimator;
    private OnRefreshListener mListener;

    RecyclerView.OnItemTouchListener itemTouchListener = new RecyclerView.OnItemTouchListener() {

        private float lastY, eY = NO_VALUE;

        private static final float NO_VALUE = -1f;

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if (!enabled || refreshing) return false;

            final float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startTouching = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (startTouching) {
                        float dy = y - lastY;
                        if (dy > 0) { // 下拉
                            if (eY == NO_VALUE) {
                                int top = itemView.getTop() - rv.getPaddingTop();
                                if (top <= 0 && top + dy > 0) {
                                    eY = y - (top + dy);
                                }
                            }

                            if (eY != NO_VALUE) {
                                heightTo(dampedOperation(y - eY));
                                rv.scrollToPosition(0);
                            }

                        } else if (dy < 0) { // 上滑
                            if (eY != NO_VALUE) {
                                heightTo(dampedOperation(y - eY));
                            }

                            if (eY != NO_VALUE && itemView.getLayoutParams().height == 0) {
                                eY = NO_VALUE;
                            }
                        }
                    } else {
                        eY = NO_VALUE;
                        startTouching = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startTouching = false;
                    autoRecovery();

                    boolean cancelClick = false;
                    if (eY != NO_VALUE && Math.abs(y - eY) > ViewConfiguration.getTouchSlop()) {
                        // 触发下拉且移动范围大于 touch_slop
                        cancelClick = true;
                    }
                    eY = NO_VALUE;
                    return cancelClick;
            }
            if (eY != NO_VALUE) {
                e.setLocation(e.getX(), eY);
            }

            lastY = y;
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (disallowIntercept && startTouching) {
                eY = NO_VALUE;
                startTouching = false;
                autoRecovery();
            }
        }
    };

    private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View
            .OnAttachStateChangeListener() {

        @Override
        public void onViewAttachedToWindow(View v) {
            if (v == itemView && mRecycler != null) {
                mRecycler.removeOnItemTouchListener(itemTouchListener);
                mRecycler.addOnItemTouchListener(itemTouchListener);
            }
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (v == itemView && mRecycler != null) {
                startTouching = false;
                mRecycler.removeOnItemTouchListener(itemTouchListener);
            }
        }

    };

    private View.OnLayoutChangeListener mOnLayoutChangeListener = new View
            .OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                   int oldTop, int oldRight, int oldBottom) {
            if (v == itemView) { // 更改文字状态
                CharSequence text = mTvState.getText();
                if (bottom - top > itemView.getMinimumHeight() && !refreshing) {
                    if (!HINT_RELEASE.equals(text)) {
                        mTvState.setText(HINT_RELEASE);
                    }
                } else if (bottom - top < itemView.getMinimumHeight() && !refreshing) {
                    if (!HINT_NORMAL.equals(text)) {
                        mTvState.setText(HINT_NORMAL);
                    }
                } else if (!startTouching) {
                    if (!HINT_LOADING.equals(text)) {
                        mTvState.setText(HINT_LOADING);
                    }
                }

                // 高度变化进度可以在此监听
            }
        }
    };

    public RefreshHeader(RecyclerView recycler, OnRefreshListener listener) {
        super(recycler, R.layout.layout_header_refresh);
        this.mRecycler = recycler;
        this.mListener = listener;

        itemView.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        itemView.addOnLayoutChangeListener(mOnLayoutChangeListener);

        mTvState = findViewById(R.id.tv_state);
        mContainer = findViewById(R.id.container);
        mIvSearch = findViewById(R.id.iv_search);
    }


    // 自动复原
    private void autoRecovery() {
        int height = itemView.getHeight();
        int minHeight = itemView.getMinimumHeight();

        if (height >= minHeight) {
            if (height > minHeight) {
                animChangeHeight(height, minHeight);
            }
            refreshing = true;
            startSearchAnim();
            if (mListener != null) {
                mListener.onRefresh();
            }
        } else {
            animChangeHeight(height, 0);
        }
    }

    private void animChangeHeight(int from, int to) {
        if (from != to) {
            ValueAnimator animator = ValueAnimator.ofInt(from, to);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    heightTo((int) animation.getAnimatedValue());
                }

            });
            animator.start();
        }
    }

    /**
     * 阻尼运算
     *
     * @param val 原始值
     * @return 阻尼元算结果值
     */
    private float dampedOperation(float val) {
        int minHeight = itemView.getMinimumHeight();
        if (minHeight <= 0) {
            return val;
        }
        if (val <= minHeight) {
            return val;
        } else {
            return (float) (minHeight * Math.pow(val / minHeight, 1 / 1.5d));
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

    private void heightTo(float height) {
        if (height < 0) {
            height = 0f;
        }
        if (height != itemView.getHeight()) {
            itemView.getLayoutParams().height = Math.round(height);
            itemView.requestLayout();
        }
    }

    /**
     * 刷新完成
     */
    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        if (refreshing) {
            startSearchAnim();
            animChangeHeight(itemView.getHeight(), itemView.getMinimumHeight());
        } else {
            if (mIcSearchAnimator != null) {
                mIcSearchAnimator.cancel();
                mIvSearch.setTranslationX(0);
                mIvSearch.setTranslationY(0);
            }
            animChangeHeight(itemView.getHeight(), 0);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 设置下拉刷新是否可用
     *
     * @param enabled true : 可用
     * @return this
     */
    public RefreshHeader setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.startTouching = false;
        return this;
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

    public interface OnRefreshListener {

        void onRefresh();

    }

}
