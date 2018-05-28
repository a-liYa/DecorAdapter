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
 * @date 2018/5/27 20:33.
 */
public class ListArgs {

    public static final int NO_COLOR_ID = 0;

    int space;                  // 分割线高度
    int marginLeft;             // 左边距
    int marginRight;            // 右边距

    boolean isVertical;         // 是否为竖直方向
    boolean ignoreLastItem;     // 是否忽略最后一条

    @ColorInt
    int color = Color.TRANSPARENT;
    @ColorRes
    int colorRes = NO_COLOR_ID;

    final Context context;

    public ListArgs(Context context) {
        this.context = context;
        space = dp2px(0.5f);
    }

    public ListArgs setSpace(float space) {
        this.space = dp2px(space);
        return this;
    }

    public ListArgs setMarginLeft(float marginLeft) {
        this.marginLeft = dp2px(marginLeft);
        return this;
    }

    public ListArgs setMarginRight(float marginRight) {
        this.marginRight = dp2px(marginRight);
        return this;
    }

    public ListArgs setMargin(float margin) {
        this.marginLeft = dp2px(margin);
        this.marginRight = dp2px(margin);
        return this;
    }

    public ListArgs setVertical(boolean vertical) {
        isVertical = vertical;
        return this;
    }

    public ListArgs setIgnoreLastItem(boolean ignore) {
        this.ignoreLastItem = ignore;
        return this;
    }

    public ListArgs setColor(int color) {
        this.color = color;
        return this;
    }

    public ListArgs setColorRes(int colorRes) {
        this.colorRes = colorRes;
        return this;
    }

    protected int dp2px(float dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()));
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

}