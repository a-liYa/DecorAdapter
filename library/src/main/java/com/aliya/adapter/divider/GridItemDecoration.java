package com.aliya.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

import com.aliya.adapter.CompatAdapter;

import static com.aliya.adapter.divider.GridBuilder.NO_COLOR_ID;

/**
 * Grid 分割线
 *
 * @author a_liYa
 * @date 16/10/22 14:30.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint; // 画笔
    private GridBuilder mArgs;

    protected GridItemDecoration(@NonNull GridBuilder args) {
        mArgs = args;
        if (mArgs.colorRes != NO_COLOR_ID || mArgs.color != Color.TRANSPARENT) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getAdapter() == null) return;

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

        // 去除header、footer因素的position
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
        if (mArgs.includeEdge) { // 包括边界
            // space - column * ((1f / spanCount) * space)
            outRect.left = Math.round(mArgs.space - totalSpanSize * mArgs.space / spanCount);

            // (column + 1) * ((1f / spanCount) * space)
            outRect.right = Math.round((totalSpanSize + spanSize) * mArgs.space / spanCount);

            if (takePosition == spanIndex) { // top edge
                outRect.top = Math.round(mArgs.space);
            }
            outRect.bottom = Math.round(mArgs.space); // item bottom
        } else {

            // column * ((1f / spanCount) * space)
            outRect.left = Math.round(totalSpanSize * mArgs.space / spanCount);

            // space - (column + 1) * ((1f /    spanCount) * space)
            outRect.right = Math.round(mArgs.space - (totalSpanSize + spanSize) * mArgs.space /
                    spanCount);
            if (takePosition != spanIndex) {
                outRect.top = Math.round(mArgs.space); // item top
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPaint == null || parent.getAdapter() == null) return;

        mPaint.setColor(mArgs.getColor());
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
            if (mArgs.includeEdge) { // 包括边界
                lwX = lnX - Math.round(mArgs.space - totalSpanSize * mArgs.space / spanCount);
                rwX = rnX + Math.round((totalSpanSize + spanSize) * mArgs.space / spanCount);

                if (takePosition == spanIndex) { // top edge
                    twY = tnY - Math.round(mArgs.space);
                }
                bwY = bnY + Math.round(mArgs.space);
            } else {
                lwX = lnX - Math.round(totalSpanSize * mArgs.space / spanCount);
                rwX = rnX + Math.round(mArgs.space - (totalSpanSize + spanSize) * mArgs.space /
                        spanCount);

                if (takePosition != spanIndex) { // item top
                    twY = tnY - Math.round(mArgs.space);
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

}
