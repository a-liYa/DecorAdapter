package com.aliya.adapter.divider;

import android.content.Context;
import android.util.TypedValue;

/**
 * DecorationBuilder
 *
 * @author a_liYa
 * @date 2020/9/7 23:24.
 */
public abstract class DecorationBuilder {

    final Context context;

    float space = 0;      // item间隔 单位 : px
    boolean includeEdge;  // 默认 false 不包含边缘

    public DecorationBuilder(Context context) {
        this.context = context;
    }

    protected float dp2px(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }
}
