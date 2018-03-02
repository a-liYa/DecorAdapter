package com.aliya.adapter.click;

import android.view.View;

/**
 * item长按事件的回调 - 接口
 * 使用方式：让你的ViewHolder实现此接口
 *
 * @author a_liYa
 * @date 2018/1/4 16:30.
 */
public interface ItemLongClickCallback {

    /**
     * 在 {@link OnItemLongClickListener#onItemLongClick(View, int)} 之后被调用
     *
     * @param itemView {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}
     * @param position .
     * @return true:表示处理 {@link View.OnLongClickListener#onLongClick(View)}
     */
    boolean onItemLongClick(View itemView, int position);

}
