package com.aliya.adapter.divider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

/**
 * 主要是实现日夜间模式切换
 *
 * @author a_liYa
 * @date 16/11/6 19:32.
 */
public abstract class AbsSpaceDivider extends RecyclerView.ItemDecoration {

    private int mAttrId = NO_ATTR_ID;
    protected int mCurrColor = Color.TRANSPARENT;

    private static final TypedValue sOutValue = new TypedValue();
    protected static final int NO_ATTR_ID = -1;

    protected AbsSpaceDivider(@ColorInt int color) {
        mCurrColor = color;
    }

    protected AbsSpaceDivider(@ColorInt @AttrRes int colorOrAttrId, boolean isAttrId) {
        if (isAttrId) {
            mAttrId = colorOrAttrId;
        } else {
            mCurrColor = colorOrAttrId;
        }
    }

    /**
     * 获取当前日夜间模式分割线的颜色
     *
     * @return Color的int值
     */
    public int getUiModeColor(Context context) {
        if (context != null && mAttrId != NO_ATTR_ID) {
            if (null != context.getTheme()) {
                context.getTheme().resolveAttribute(mAttrId, sOutValue, true);
                switch (sOutValue.type) {
                    case TypedValue.TYPE_INT_COLOR_ARGB4:
                    case TypedValue.TYPE_INT_COLOR_ARGB8:
                    case TypedValue.TYPE_INT_COLOR_RGB4:
                    case TypedValue.TYPE_INT_COLOR_RGB8:
                        mCurrColor = sOutValue.data;
                        break;
                }
            }
        }
        return mCurrColor;
    }

    protected int dp2px(float dp) {
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().getDisplayMetrics()
        ) + 0.5f);
    }
}
