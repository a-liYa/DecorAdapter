package com.aliya.adapter;

/**
 * 提供适配器兼容性的接口
 *
 * @author a_liYa
 * @date 2017/8/24 15:20.
 */
public interface CompatAdapter {

    /**
     * header的个数
     *
     * @return .
     */
    int getHeaderCount();

    /**
     * footer的个数
     *
     * @return .
     */
    int getFooterCount();

    /**
     * 是否为内部position
     *
     * @param position .
     * @return true:内部
     */
    boolean isInnerPosition(int position);

    /**
     * 第一个 position
     *
     * @return .
     */
    int getFirstPosition();

    /**
     * 最后一个 position
     *
     * @return .
     */
    int getLastPosition();

}
