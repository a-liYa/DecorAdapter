package com.aliya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView的拓展, 结合BaseRecyclerAdapter使用
 *
 * @param <T> 数据的泛型
 * @author a_liYa
 * @date 16/10/19 09:52.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public T mData;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setData(T data) {
        this.mData = data;
        bindView(mData);
    }

    public T getData() {
        return mData;
    }

    /**
     * 把数据绑定到View上
     */
    public abstract void bindView(T data);
}
