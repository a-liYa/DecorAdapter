package com.aliya.adapter.divider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.TypedValue;

/**
 * 分割线参数相关
 *
 * @author a_liYa
 * @date 2018/5/27 21:42.
 */
public class GridBuilder {

    protected static final int NO_COLOR_ID = 0;

    float space = 0;      // item间隔 单位 : px
    boolean includeEdge;  // 默认false 不包含边缘

    @ColorInt
    int color = Color.TRANSPARENT;
    @ColorRes
    int colorRes = NO_COLOR_ID;

    final Context context;

    public GridBuilder(Context context) {
        this.context = context;
    }

    /**
     * item 间隔
     *
     * @param space 单位：dp
     * @return this
     */
    public GridBuilder setSpace(float space) {
        this.space = dp2px(space);
        return this;
    }

    public GridBuilder setIncludeEdge(boolean includeEdge) {
        this.includeEdge = includeEdge;
        return this;
    }

    public GridBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public GridBuilder setColorRes(int colorRes) {
        this.colorRes = colorRes;
        return this;
    }

    public GridItemDecoration build() {
        return new GridItemDecoration(this);
    }

    /**
     * 获取当前日夜间模式分割线的颜色
     *
     * @return Color的int值
     */
    int getColor() {
        if (colorRes != NO_COLOR_ID) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return context.getColor(colorRes);
                }
                return context.getResources().getColor(colorRes);
            } catch (Resources.NotFoundException e) {
                // no-op
            }
        }
        return color;
    }

    protected float dp2px(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

}