package com.aliya.adapter.simple.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.callback.LoadMoreListener;
import com.aliya.adapter.simple.callback.LoadingCallBack;

/**
 * 加载更多
 *
 * @author a_liYa
 * @date 2017/8/24 18:11.
 */
public class FooterLoadMore<M> implements View.OnClickListener, View.OnAttachStateChangeListener,
        LoadingCallBack<M> {

    /**
     * 加载中
     */
    public static final int TYPE_LOADING = 1;
    /**
     * 没有更多的数据
     */
    public static final int TYPE_NO_MORE = 2;
    /**
     * 失败
     */
    public static final int TYPE_ERROR = 3;

    private int state = 0;
    private boolean isLoading = false;
    LoadMoreListener loadMoreListener;

    private RelativeLayout mLoadMoreView;
    private RelativeLayout mErrorMoreView;
    private View mNoMoreView;
    public View rootView;

    public FooterLoadMore(ViewGroup parent, LoadMoreListener<M> loadMoreListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        rootView = inflater.inflate(R.layout.item_footer_load_more, parent, false);
        mLoadMoreView = (RelativeLayout) rootView.findViewById(R.id.rl_more_loading);
        mErrorMoreView = (RelativeLayout) rootView.findViewById(R.id.rl_more_error);
        mNoMoreView = rootView.findViewById(R.id.layout_no_more);

        mErrorMoreView.setOnClickListener(this);
        rootView.addOnAttachStateChangeListener(this);
        this.loadMoreListener = loadMoreListener;
    }

    public View getView() {
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_more_error) {

        }
    }

    public void setLoadState(int state) {
        mLoadMoreView.setVisibility(state == TYPE_LOADING ? View.VISIBLE : View.GONE);
        mErrorMoreView.setVisibility(state == TYPE_ERROR ? View.VISIBLE : View.GONE);
        mNoMoreView.setVisibility(state == TYPE_NO_MORE ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewAttachedToWindow(View view) {
        if (rootView == view) {
            if (!isLoading) {
                loadMore();
            }
        }
    }

    private void loadMore() {
        setLoadState(TYPE_LOADING);
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore(this);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View view) {}

    @Override
    public void onCancel() {
        setLoadState(TYPE_ERROR);
        isLoading = false;
    }

    @Override
    public void onError(String errMsg, int errCode) {
        setLoadState(TYPE_ERROR);
        isLoading = false;
    }

    @Override
    public void onEmpty() {
        setLoadState(TYPE_NO_MORE);
        isLoading = false;
    }

    @Override
    public void onSuccess(M data) {
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMoreSuccess(data);
        }
        isLoading = false;
    }

}
