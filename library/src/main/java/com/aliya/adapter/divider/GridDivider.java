package com.aliya.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

import com.aliya.adapter.CompatAdapter;

/**
 * Grid 分割线
 *
 * @author a_liYa
 * @date 16/10/22 14:30.
 */
public class GridDivider extends AbsSpaceDivider {

    /**
     * item间隔 单位 : px
     */
    private float mSpace = 0;
    // 画笔
    private Paint mPaint;
    /**
     * 默认false 不包含边缘
     */
    private boolean includeEdge;

    /**
     * @param spaceDip 间隔距离 单位 : dip
     */
    public GridDivider(float spaceDip) {
        this(spaceDip, Color.TRANSPARENT, false);
    }

    /**
     * @param spaceDip      间隔距离 单位 : dip
     * @param colorOrAttrId 分割线color或attrId
     * @param isAttrId      true:表示是attrId；false:表示是Color
     */
    public GridDivider(float spaceDip, int colorOrAttrId, boolean isAttrId) {
        this(spaceDip, colorOrAttrId, false, isAttrId);
    }

    /**
     * @param spaceDip      间隔距离 单位 : dip
     * @param colorOrAttrId 分割线color或attrId
     * @param includeEdge   是否包括边缘 true 包含
     * @param isAttrId      true:表示是attrId；false:表示是Color
     */
    public GridDivider(float spaceDip, int colorOrAttrId, boolean includeEdge,
                       boolean isAttrId) {
        super(colorOrAttrId, isAttrId);
        if (isAttrId || colorOrAttrId != Color.TRANSPARENT) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true); // 设置画笔抗锯齿
        }

        if (spaceDip > 0)
            mSpace = dp2px(spaceDip);
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager.SpanSizeLookup spanSizeLookup;
        int spanCount;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) parent.getLayoutManager();
            spanCount = gridManager.getSpanCount();
            spanSizeLookup = gridManager.getSpanSizeLookup();
        } else {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) return;

        // 去除DecorAdapter之后的position
        int takePosition = position;
        if (parent.getAdapter() instanceof CompatAdapter) {
            CompatAdapter adapter = (CompatAdapter) parent.getAdapter();
            if (adapter.isInnerPosition(position)) {
                return;
            } else {
                takePosition = adapter.cleanPosition(position);
            }
        }

        int spanSize = spanSizeLookup.getSpanSize(position);
        int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
        int preCount = 0;
        int totalSpanSize = 0;
        if (spanIndex != 0) {
            do {
                totalSpanSize += spanSizeLookup.getSpanSize(position - ++preCount);
            } while (spanSizeLookup.getSpanIndex(position - preCount, spanCount) != 0);
        }
        if (includeEdge) { // 包括边界
            // mSpace - column * ((1f / spanCount) * mSpace)
            outRect.left = Math.round(mSpace - totalSpanSize * mSpace / spanCount);

            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = Math.round((totalSpanSize + spanSize) * mSpace / spanCount);

            if (takePosition == spanIndex) { // top edge
                outRect.top = Math.round(mSpace);
            }
            outRect.bottom = Math.round(mSpace); // item bottom
        } else {

            // column * ((1f / spanCount) * spacing)
            outRect.left = Math.round(totalSpanSize * mSpace / spanCount);

            // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            outRect.right = Math.round(mSpace - (totalSpanSize + spanSize) * mSpace / spanCount);
            if (takePosition != spanIndex) {
                outRect.top = Math.round(mSpace); // item top
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mPaint == null) return;
        mPaint.setColor(getUiModeColor(parent.getContext())); // 设置颜色
        GridLayoutManager.SpanSizeLookup spanSizeLookup;
        int spanCount;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) parent.getLayoutManager();
            spanCount = gridManager.getSpanCount();
            spanSizeLookup = gridManager.getSpanSizeLookup();
        } else {
            return;
        }

        CompatAdapter adapter = null;
        if (parent.getAdapter() instanceof CompatAdapter) {
            adapter = (CompatAdapter) parent.getAdapter();
        }

        int childCount = parent.getChildCount();
        float lnX, lwX, tnY, twY, rnX, rwX, bnY, bwY;

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }

            // 去除DecorAdapter之后的position
            int takePosition = position;
            if (adapter != null) {
                if (adapter.isInnerPosition(position)) {
                    continue;
                } else {
                    takePosition = adapter.cleanPosition(position);
                }
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            lnX = child.getLeft() - lp.leftMargin;

            twY = tnY = child.getTop() - lp.topMargin;

            rnX = child.getRight() + lp.rightMargin;

            bwY = bnY = child.getBottom() + lp.bottomMargin;

            int spanSize = spanSizeLookup.getSpanSize(position);
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            int preCount = 0;
            int totalSpanSize = 0;
            if (spanIndex != 0) {
                do {
                    totalSpanSize += spanSizeLookup.getSpanSize(position - ++preCount);
                } while (spanSizeLookup.getSpanIndex(position - preCount, spanCount) != 0);
            }
            if (includeEdge) { // 包括边界
                lwX = lnX - Math.round(mSpace - totalSpanSize * mSpace / spanCount);
                rwX = rnX + Math.round((totalSpanSize + spanSize) * mSpace / spanCount);

                if (takePosition == spanIndex) { // top edge
                    twY = tnY - Math.round(mSpace);
                }
                bwY = bnY + Math.round(mSpace);
            } else {
                lwX = lnX - Math.round(totalSpanSize * mSpace / spanCount);
                rwX = rnX + Math.round(mSpace - (totalSpanSize + spanSize) * mSpace / spanCount);

                if (takePosition != spanIndex) { // item top
                    twY = tnY - Math.round(mSpace);
                }
            }

            if (lnX != lwX) {
                c.drawRect(lwX, twY, lnX, bnY, mPaint);
            }
            if (tnY != twY) {
                c.drawRect(lnX, twY, rwX, tnY, mPaint);
            }
            if (rnX != rwX) {
                c.drawRect(rnX, tnY, rwX, bwY, mPaint);
            }
            if (bnY != bwY) {
                c.drawRect(lwX, bnY, rnX, bwY, mPaint);
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
