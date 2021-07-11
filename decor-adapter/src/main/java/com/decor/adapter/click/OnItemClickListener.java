package com.decor.adapter.click;

import android.view.View;

/**
 * OnItemClickListener
 *
 * @author a_liYa
 * @date 16/9/4 09:36.
 */
public interface OnItemClickListener {

    /**
     * item 点击回调
     *
     * @param itemView .
     * @param position .
     */
    void onItemClick(View itemView, int position);
}
