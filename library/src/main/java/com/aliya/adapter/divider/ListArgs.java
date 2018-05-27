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

    int dividerHeight;                  // 分割线高度
    int marginLeft;                     // 左边距
    int marginRight;                    // 右边距

    boolean isVertical;                 // 是否为竖直方向
    boolean includeLastItem = true;     // 是否包括最后一条

    @ColorInt
    int color = Color.TRANSPARENT;
    @ColorRes
    int colorRes = NO_COLOR_ID;

    final Context context;

    public ListArgs(Context context) {
        this.context = context;
        dividerHeight = dp2px(0.5f);
    }

    public ListArgs setDividerHeight(float dividerHeight) {
        this.dividerHeight = dp2px(dividerHeight);
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

    public ListArgs setIncludeLastItem(boolean includeLastItem) {
        this.includeLastItem = includeLastItem;
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