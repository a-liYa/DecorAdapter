package com.aliya.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * 悬浮、吸顶功能 - 标识 ViewHolder
 *
 * @author a_liYa
 * @date 2017/9/23 15:55.
 */
public abstract class OverlayViewHolder<T> extends RecyclerViewHolder<T> {

    public OverlayViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    public OverlayViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void setData(T data) {
        if (mData != data) { // 悬浮类型做 重复setData做优化
            super.setData(data);
            if (itemView.isDrawingCacheEnabled()) { // 真正悬浮的 view holder
                itemView.destroyDrawingCache();
                itemView.buildDrawingCache();
            }
        }
    }
}
