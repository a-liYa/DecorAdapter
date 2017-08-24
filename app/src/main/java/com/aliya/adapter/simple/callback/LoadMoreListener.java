package com.aliya.adapter.simple.callback;

/**
 * TODO (一句话描述)
 *
 * @author a_liYa
 * @date 2017/8/24 20:02.
 */
public interface LoadMoreListener<M>{

    void onLoadMoreSuccess(M data);

    void onLoadMore(LoadingCallBack<M> callBack);
}