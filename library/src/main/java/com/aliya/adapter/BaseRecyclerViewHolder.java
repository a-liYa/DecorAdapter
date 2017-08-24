package com.aliya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link RecyclerView.ViewHolder}的拓展, 结合{@link BaseRecyclerAdapter}使用
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
     * bind data to view
     *
     * @param data .
     */
    public abstract void bindView(T data);

}
