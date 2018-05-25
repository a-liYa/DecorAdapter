package com.aliya.adapter.simple.page;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.callback.LoadingCallBack;

/**
 * 加载更多，自定义示例
 *
 * @author a_liYa
 * @date 2017/8/24 18:11.
 */
public class LoadMoreFooter<M> extends PageItem implements LoadMore, View.OnClickListener,
        View.OnAttachStateChangeListener, LoadingCallBack<M> {

    private int state = 0;
    private boolean isLoading = false;
    LoadMoreListener loadMoreListener;

    private RelativeLayout mLoadMoreView;
    private RelativeLayout mErrorMoreView;
    private View mNoMoreView;

    public LoadMoreFooter(ViewGroup parent, LoadMoreListener<M> loadMoreListener) {
        super(parent, R.layout.item_footer_load_more);

        mLoadMoreView = findViewById(R.id.rl_more_loading);
        mErrorMoreView = findViewById(R.id.rl_more_error);
        mNoMoreView = findViewById(R.id.layout_no_more);

        mErrorMoreView.setOnClickListener(this);
        itemView.addOnAttachStateChangeListener(this);

        this.loadMoreListener = loadMoreListener;
    }

    // 防止加载少量数据，加载更多布局没出屏幕，不会回调 onViewAttachedToWindow
    private Runnable mKeepRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isLoading && state != TYPE_ERROR && state != TYPE_NO_MORE) {
                loadMore();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_more_error) {
            loadMore();
        }
    }

    public void setState(int state) {
        this.state = state;
        isLoading = state == TYPE_LOADING;
        updateState();
    }

    protected void updateState() {
        mLoadMoreView.setVisibility(state == TYPE_LOADING ? View.VISIBLE : View.GONE);
        mErrorMoreView.setVisibility(state == TYPE_ERROR ? View.VISIBLE : View.GONE);
        mNoMoreView.setVisibility(state == TYPE_NO_MORE ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewAttachedToWindow(View view) {
        if (itemView == view) {
            if (!isLoading && state != TYPE_ERROR && state != TYPE_NO_MORE) {
                loadMore();
            }
        }
    }

    private void loadMore() {
        setState(TYPE_LOADING);
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore(this);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View view) {
        itemView.removeCallbacks(mKeepRunnable);
    }

    @Override
    public void onCancel() {
        setState(TYPE_ERROR);
    }

    @Override
    public void onError(String errMsg, int errCode) {
        setState(TYPE_ERROR);
    }

    @Override
    public void onEmpty() {
        setState(TYPE_NO_MORE);
    }

    @Override
    public void onSuccess(M data) {
        itemView.removeCallbacks(mKeepRunnable);
        itemView.postDelayed(mKeepRunnable, 500);
        isLoading = false;
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMoreSuccess(data, this);
        }
    }

    /**
     * LoadMoreListener
     *
     * @author a_liYa
     * @date 2017/8/24 20:02.
     */
    public interface LoadMoreListener<M>{

        void onLoadMoreSuccess(M data, LoadMore loadMore);

        void onLoadMore(LoadingCallBack<M> callback);
    }

}
