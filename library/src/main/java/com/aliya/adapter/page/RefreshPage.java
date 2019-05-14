package com.aliya.adapter.page;

import android.animation.ValueAnimator;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SimpleOnItemTouchListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * RefreshPage
 *
 * @author a_liYa
 * @date 2019/4/30 08:56.
 */
public abstract class RefreshPage extends PageItem {

    protected static final int DROP_DOWN = 1;   // 下拉可以刷新
    protected static final int LOOSEN_UP = 2;   // 松开立即刷新
    protected static final int REFRESHING = 3;  // 正在刷新
    protected static final int COMPLETE = 4;    // 刷新完成

    int refreshStatus;

    boolean refreshing;
    boolean startTouching;

    boolean enabled = true; // 是否可用
    protected int collapseDelay;
    protected int triggerHeight;

    private int mOverScrollMode;
    private RecyclerView mRecyclerView;
    private OnRefreshListener mListener;

    public RefreshPage(int layoutRes, OnRefreshListener listener) {
        super(layoutRes);
        mListener = listener;
    }

    @CallSuper
    @Override
    public void onViewCreated(final View itemView) {
        itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (v == itemView) {
                    ViewParent parent = v.getParent();
                    if (parent instanceof RecyclerView) {
                        mRecyclerView = (RecyclerView) parent;
                        mOverScrollMode = mRecyclerView.getOverScrollMode();
                        mRecyclerView.removeOnItemTouchListener(itemTouchListener);
                        mRecyclerView.addOnItemTouchListener(itemTouchListener);
                    }
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (v == itemView && mRecyclerView != null) {
                    startTouching = false;
                    mRecyclerView.setOverScrollMode(mOverScrollMode);
                    mRecyclerView.removeOnItemTouchListener(itemTouchListener);
                }
            }
        });
        itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (v == itemView) {
                    if (bottom - top > triggerHeight && !refreshing) {
                        if (refreshStatus != LOOSEN_UP) {
                            refreshStatus = LOOSEN_UP;
                            onRefreshStatusChange(refreshStatus);
                        }
                    } else if (bottom - top < triggerHeight && !refreshing) {
                        if (refreshStatus != DROP_DOWN) {
                            refreshStatus = DROP_DOWN;
                            onRefreshStatusChange(refreshStatus);
                        }
                    } else if (!startTouching && refreshStatus != COMPLETE) {
                        if (refreshStatus != REFRESHING) {
                            refreshStatus = REFRESHING;
                            onRefreshStatusChange(refreshStatus);
                        }
                    }
                    // 高度变化进度可以在此监听
                }
            }
        });
    }

    RecyclerView.OnItemTouchListener itemTouchListener = new SimpleOnItemTouchListener() {
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
                                mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                                int top = itemView.getTop() - rv.getPaddingTop();
                                if (top <= 0 && top + dy > 0) {
                                    eY = y - (top + dy);
                                }
                            }
                        }
                        if (eY != NO_VALUE) { // 触发刷新控件可见
                            heightTo(onDampedOperation(y - eY));
                            if (y - eY >= 0) {
                                rv.scrollToPosition(0);
                            }
                        }
                        if (dy < 0) { // 上滑
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
                    eY = NO_VALUE;
                    startTouching = false;
                    handleLoosen();
                    break;
            }
            lastY = y;
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (disallowIntercept && startTouching) {
                eY = NO_VALUE;
                startTouching = false;
                handleLoosen();
            }
        }
    };

    protected abstract void onRefreshStatusChange(int status);

    /**
     * 阻尼运算，原始值 val 阻尼运算之后返回
     */
    protected float onDampedOperation(float val) {
        int minHeight = triggerHeight;
        if (minHeight <= 0) {
            return val;
        }
        if (val <= minHeight) {
            return val;
        } else {
            return (float) (minHeight * Math.pow(val / minHeight, 1 / 2d));
        }
    }

    // 处理松开逻辑
    private void handleLoosen() {
        int height = itemView.getHeight();

        if (height >= triggerHeight) {
            smoothHeightTo(triggerHeight);
            refreshing = true;
            if (mListener != null) {
                mListener.onRefresh();
            }
        } else {
            smoothHeightTo(0);
        }
    }

    private void smoothHeightTo(int to) {
        int from = itemView.getHeight();
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
     * 设置下拉刷新是否可用
     *
     * @param enabled true : 可用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.startTouching = false;
    }

    /**
     * 刷新完成
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            this.refreshing = true;
            smoothHeightTo(triggerHeight);
        } else {
            refreshComplete();
        }
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    private void refreshComplete() {
        if (refreshStatus != COMPLETE) {
            refreshStatus = COMPLETE;
            onRefreshStatusChange(refreshStatus);
        }
        if (collapseDelay > 0) {
            itemView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothHeightTo(0);
                    RefreshPage.this.refreshing = false;
                }
            }, collapseDelay);
        } else {
            smoothHeightTo(0);
            this.refreshing = false;
        }
    }

    /**
     * 下拉刷新回调接口
     */
    public interface OnRefreshListener {

        void onRefresh();
    }
}
