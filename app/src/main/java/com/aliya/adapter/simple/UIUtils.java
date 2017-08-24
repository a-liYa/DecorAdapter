package com.aliya.adapter.simple;

import android.content.Context;

/**
 * TODO (一句话描述)
 *
 * @author a_liYa
 * @date 2017/8/24 13:27.
 */
public class UIUtils {

    public static Context sContext;

    /**
     * dip转换px
     */
    public static int dip2px(float dip) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
