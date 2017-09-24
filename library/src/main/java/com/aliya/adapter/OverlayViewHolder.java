package com.aliya.adapter;

import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 悬浮、吸顶功能 - 标识 ViewHolder
 * <p>
 * 注意：悬浮原理
 * {@link android.support.v7.widget.RecyclerView.ItemDecoration#onDrawOver(Canvas, RecyclerView, RecyclerView.State)} draw bitmap
 * 没有交互事件，{@link #getOverlayView()} 返回的view, 通过{@link View#getDrawingCache()} 获取bitmap，预防bitmap过大
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
        if (mData != data) { // 悬浮类型 针对重复setData做优化
            super.setData(data);
            if (itemView.isDrawingCacheEnabled()) { // 真正悬浮的 view holder
                itemView.destroyDrawingCache();
                itemView.buildDrawingCache();
            }
        }
    }

    /**
     * 真正悬浮的view，子类可通过 Override 返回自己的view
     *
     * @return 默认返回 {@link #itemView}
     */
    public View getOverlayView() {
        return itemView;
    }

}
