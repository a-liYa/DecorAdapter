package com.aliya.adapter.simple.callback;

/**
 * LoadMoreListener
 *
 * @author a_liYa
 * @date 2017/8/24 20:02.
 */
public interface LoadMoreListener<M>{

    void onLoadMoreSuccess(M data);

    void onLoadMore(LoadingCallBack<M> callback);
}