package com.aliya.adapter.click;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * item点击事件的回调 - 接口
 * 使用方式：让你的ViewHolder实现此接口
 *
 * @author a_liYa
 * @date 2018/1/4 16:30.
 */
public interface ItemClickCallback {

    /**
     * 在 {@link OnItemClickListener#onItemClick(View, int)} 之后被调用
     *
     * @param itemView {@link RecyclerView.ViewHolder#itemView}
     * @param position .
     */
    void onItemClick(View itemView, int position);

}
