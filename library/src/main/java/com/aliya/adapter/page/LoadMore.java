package com.aliya.adapter.page;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 上拉加载更多 - 接口
 *
 * @author a_liYa
 * @date 2017/8/25 13:02.
 */
public interface LoadMore {

    /**
     * 闲置状态
     */
    int TYPE_IDLE = 0;

    /**
     * 加载中状态
     */
    int TYPE_LOADING = 1;

    /**
     * 没有更多状态
     */
    int TYPE_NO_MORE = 2;

    /**
     * 失败状态
     */
    int TYPE_ERROR = 3;

    /**
     * 设置加载状态
     *
     * @param state 状态
     */
    void setState(@StateMode int state);

    @IntDef(flag = true, value = {
            TYPE_IDLE,
            TYPE_LOADING,
            TYPE_NO_MORE,
            TYPE_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface StateMode{}

}
