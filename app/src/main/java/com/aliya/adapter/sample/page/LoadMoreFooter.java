package com.aliya.adapter.sample.page;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.aliya.adapter.DecorAdapter;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.callback.LoadingCallBack;

/**
 * 加载更多，自定义示例
 *
 * @param <M> 网络数据范型
 * @author a_liYa
 * @date 2017/8/24 18:11.
 */
public class LoadMoreFooter<M> extends PageItem implements LoadMore, View.OnClickListener,
        View.OnAttachStateChangeListener, LoadingCallBack<M> {

    private int state = 0;
    private boolean isLoading = false;
    LoadMoreListener<M> loadMoreListener;

    private RelativeLayout mLoadMoreView;
    private RelativeLayout mErrorMoreView;
    private View mNoMoreView;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private int mFooterCount;

    public LoadMoreFooter(LoadMoreListener<M> loadMoreListener) {
        super(R.layout.item_footer_load_more);
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onViewCreated(final View itemView) {
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (itemView.getParent() instanceof RecyclerView) {
                    mRecycler = (RecyclerView) itemView.getParent();
                }
                if (mRecycler != null) {
                    mLayoutManager = (LinearLayoutManager) mRecycler.getLayoutManager();
                    if (mRecycler.getAdapter() instanceof DecorAdapter) {
                        mFooterCount = ((DecorAdapter) mRecycler.getAdapter()).getFooterCount();
                    }
                    mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            int lastCompletelyVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                            boolean idle = lastCompletelyVisibleItemPosition < mLayoutManager.getItemCount() - 1 - mFooterCount;
                            if (idle) {
                                mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                if (!isLoading && state != TYPE_ERROR && state != TYPE_NO_MORE) {
                                    loadMore();
                                }
                            }
                        }

                    });
                }
            }
        });


        mLoadMoreView = findViewById(R.id.rl_more_loading);
        mErrorMoreView = findViewById(R.id.rl_more_error);
        mNoMoreView = findViewById(R.id.layout_no_more);

        mErrorMoreView.setOnClickListener(this);
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        updateState();
    }

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
        if (itemView != null) {
            mLoadMoreView.setVisibility(state == TYPE_LOADING ? View.VISIBLE : View.GONE);
            mErrorMoreView.setVisibility(state == TYPE_ERROR ? View.VISIBLE : View.GONE);
            mNoMoreView.setVisibility(state == TYPE_NO_MORE ? View.VISIBLE : View.GONE);
        }
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
    public interface LoadMoreListener<M> {

        void onLoadMoreSuccess(M data, LoadMore loadMore);

        void onLoadMore(LoadingCallBack<M> callback);
    }

}
