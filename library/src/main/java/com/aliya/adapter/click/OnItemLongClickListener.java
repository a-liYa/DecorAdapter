package com.aliya.adapter.click;

import android.view.View;

/**
 * OnItemLongClickListener
 *
 * @author a_liYa
 * @date 16/10/16 12:53.
 */
public interface OnItemLongClickListener {

    /**
     * item 长按回调
     *
     * @param itemView .
     * @param position .
     * @return true:表示处理 {@link View.OnLongClickListener#onLongClick(View)}
     */
    boolean onItemLongClick(View itemView, int position);

}
