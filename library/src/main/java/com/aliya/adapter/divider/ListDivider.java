package com.aliya.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

import com.aliya.adapter.DecorAdapter;

/**
 * 自定义RecyclerView的线性布局分割线
 *
 * @author a_liYa
 * @date 16/11/6 下午7:21.
 */
public class ListDivider extends AbsSpaceDivider {

    private int mDividerHeight = 0;
    private int mLeftMargin = 0;
    private int mRightMargin = 0;
    // 画笔
    private Paint mPaint;
    private boolean mIsHorizontal = true;
    private boolean mIncludeLastItem = true;

    /**
     * 默认0.5dp 的间距, 没有颜色
     */
    public ListDivider() {
        this(0.5, Color.TRANSPARENT, false);
    }

    /**
     * @param heightDip     分割线高度
     * @param colorOrAttrId 分割线color或attrId
     * @param isAttrId      true:表示是attrId；false:表示是Color
     */
    public ListDivider(double heightDip, int colorOrAttrId, boolean isAttrId) {
        this(heightDip, colorOrAttrId, true, isAttrId);
    }

    /**
     * @param heightDip     分割线高度
     * @param colorOrAttrId 分割线color或attrId
     * @param isHorizontal  是否为水平方向
     * @param isAttrId      true:表示是attrId；false:表示是Color
     */
    public ListDivider(double heightDip, int colorOrAttrId, boolean isHorizontal,
                       boolean isAttrId) {
        this(heightDip, colorOrAttrId, 0, isHorizontal, isAttrId);
    }

    /**
     * @param heightDip     分割线高度
     * @param margin        左右两边的边距
     * @param colorOrAttrId 分割线color或attrId
     * @param isHorizontal  是否为水平方向
     * @param isAttrId      true:表示是attrId；false:表示是Color
     */
    public ListDivider(double heightDip, int colorOrAttrId, float margin, boolean isHorizontal,
                       boolean isAttrId) {
        this(heightDip, colorOrAttrId, margin, margin, isHorizontal, true, isAttrId);
    }

    /**
     * @param heightDip       分割线高度
     * @param colorOrAttrId   分割线color或attrId
     * @param leftMargin      左边距
     * @param rightMargin     右边距
     * @param isHorizontal    是否为水平方向
     * @param includeLastItem 是否包括最后一条
     * @param isAttrId        true:表示是attrId；false:表示是Color
     */
    public ListDivider(double heightDip, int colorOrAttrId, float leftMargin,
                       float rightMargin, boolean isHorizontal,
                       boolean includeLastItem, boolean isAttrId) {
        super(colorOrAttrId, isAttrId);
        mDividerHeight = dp2px((float) heightDip);
        if (isAttrId || colorOrAttrId != Color.TRANSPARENT) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true); // 设置画笔抗锯齿
        }
        mLeftMargin = dp2px(leftMargin);
        mRightMargin = dp2px(rightMargin);
        mIsHorizontal = isHorizontal;
        mIncludeLastItem = includeLastItem;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int footerCount = 0;
        DecorAdapter adapter = null;
        if (parent.getAdapter() instanceof DecorAdapter) {
            adapter = (DecorAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
        }
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }
            if (!mIncludeLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            c.drawRect(left + mLeftMargin, top, right - mRightMargin, bottom, mPaint);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        int footerCount = 0;
        DecorAdapter adapter = null;
        if (parent.getAdapter() instanceof DecorAdapter) {
            adapter = (DecorAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
        }
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (!mIncludeLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }
            LayoutParams params = (LayoutParams) child
                    .getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerHeight;
            c.drawRect(left, top + mLeftMargin, right, bottom - mRightMargin, mPaint);
        }
    }

    // 绘制
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPaint == null) return;
        mPaint.setColor(getUiModeColor(parent.getContext())); // 设置颜色
        if (mIsHorizontal) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    // 在绘制ItemDivider之前,需要调用此方法。以便RecyclerView给该条目预留空间,供我们绘制Divider
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int footerCount = 0;
        if (parent.getAdapter() instanceof DecorAdapter) {
            DecorAdapter adapter = (DecorAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
            if (adapter.isInnerPosition(position)) {
                return;
            }
        }
        if (!mIncludeLastItem) {
//            ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if (position == parent.getAdapter().getItemCount() - 1 - footerCount) {
                return;
            }
        }
        if (mIsHorizontal) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }

}