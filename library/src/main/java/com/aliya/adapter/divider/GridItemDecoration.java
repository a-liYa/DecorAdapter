package com.aliya.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.aliya.adapter.CompatAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

import static com.aliya.adapter.divider.GridBuilder.NO_COLOR_ID;

/**
 * Grid 分割线, 只支持 {@link LinearLayoutManager#VERTICAL}
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
        // 缩写注释 liX -> leftInsideX; lox -> leftOutsideX.
        float liX, loX, tiY, toY, riX, roX, biY, boY;

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

            liX = child.getLeft() - lp.leftMargin;
            toY = tiY = child.getTop() - lp.topMargin;
            riX = child.getRight() + lp.rightMargin;
            boY = biY = child.getBottom() + lp.bottomMargin;

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
                loX = liX - Math.round(mArgs.space - totalSpanSize * mArgs.space / spanCount);
                roX = riX + Math.round((totalSpanSize + spanSize) * mArgs.space / spanCount);

                if (takePosition == spanIndex) { // top edge
                    toY = tiY - Math.round(mArgs.space);
                }
                boY = biY + Math.round(mArgs.space);
            } else {
                loX = liX - Math.round(totalSpanSize * mArgs.space / spanCount);
                roX = riX + Math.round(mArgs.space - (totalSpanSize + spanSize) * mArgs.space /
                        spanCount);

                if (takePosition != spanIndex) { // item top
                    toY = tiY - Math.round(mArgs.space);
                }
            }

            if (liX != loX) {
                c.drawRect(loX, toY, liX, biY, mPaint);
            }
            if (tiY != toY) {
                c.drawRect(liX, toY, roX, tiY, mPaint);
            }
            if (riX != roX) {
                c.drawRect(riX, tiY, roX, boY, mPaint);
            }
            if (biY != boY) {
                c.drawRect(loX, biY, riX, boY, mPaint);
            }

            // 补全当前行剩余空白部分的分割线
            if (mArgs.includeLineBlank) {
                int nextPosition = position + 1;
                // 判断当前 child 是否为该行的最后一个
                if (nextPosition >= parent.getAdapter().getItemCount() ||
                        spanSizeLookup.getSpanIndex(nextPosition, spanCount) == 0) {
                    totalSpanSize += spanSizeLookup.getSpanSize(position);
                    if (totalSpanSize < spanCount) { // 当前行有剩余
                        // 画当前行 上分割线
                        if (tiY != toY) {
                            c.drawRect(roX, toY, parent.getWidth() - parent.getPaddingRight(),
                                    tiY, mPaint);
                        }
                        // 画 item 间隔
                        float itemWidth = riX - liX;
                        int iteratorSpanIndex = totalSpanSize;
                        while (mArgs.includeEdge ?
                                iteratorSpanIndex <= spanCount : iteratorSpanIndex < spanCount) {
                            float left =
                                    riX + (mArgs.space + itemWidth) * (iteratorSpanIndex - totalSpanSize);
                            c.drawRect(left, tiY, left + mArgs.space, biY, mPaint);
                            iteratorSpanIndex++;
                        }
                        // 画当前行 下分割线
                        if (biY != boY) {
                            c.drawRect(roX, biY, parent.getWidth() - parent.getPaddingRight(),
                                    boY, mPaint);
                        }
                    }
                }
            }
        }
    }
}
