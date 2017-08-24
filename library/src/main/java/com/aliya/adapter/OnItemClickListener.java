package com.aliya.adapter;

import android.view.View;

/**
 * OnItemClickListener
 *
 * @author a_liYa
 * @date 16/9/4 09:36.
 */
public interface OnItemClickListener {

    /**
     * item点击回调
     *
     * @param itemView .
     * @param position .
     */
    void onItemClick(View itemView, int position);
}
