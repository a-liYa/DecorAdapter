package com.aliya.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

import com.aliya.adapter.CompatAdapter;
import com.aliya.adapter.R;

import static com.aliya.adapter.divider.ListBuilder.NO_COLOR_ID;

/**
 * 自定义RecyclerView的线性布局分割线
 *
 * @author a_liYa
 * @date 16/11/6 下午7:21.
 */
public class ListItemDecoration extends RecyclerView.ItemDecoration {

    protected Paint mPaint; // 画笔
    protected ListBuilder mArgs;

    protected ListItemDecoration(@NonNull ListBuilder args) {
        mArgs = args;
        if (mArgs.colorRes != NO_COLOR_ID || mArgs.color != Color.TRANSPARENT) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    private void verticalDraw(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int left = parent.getPaddingLeft() +  mArgs.marginLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - mArgs.marginRight;
        int footerCount = 0;
        CompatAdapter adapter = null;
        if (parent.getAdapter() instanceof CompatAdapter) {
            adapter = (CompatAdapter) parent.getAdapter();
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
            if (mArgs.ignoreLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + getItemOffset(child);
            if (bottom > top) {
                onDrawItemDecor(c, child, parent, left, top, right, bottom);
            }
        }
    }

    private void horizontalDraw(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int top = parent.getPaddingTop() + mArgs.marginLeft;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - mArgs.marginRight;
        int footerCount = 0;
        CompatAdapter adapter = null;
        if (parent.getAdapter() instanceof CompatAdapter) {
            adapter = (CompatAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
        }
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (mArgs.ignoreLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + getItemOffset(child);
            if (right > left) {
                onDrawItemDecor(c, child, parent, left, top, right, bottom);
            }
        }
    }

    /**
     * 绘制 item 分割线
     *
     * @param c      Canvas
     * @param child  The child view to decorate
     * @param left   The left side of the rectangle to be drawn
     * @param right  The right side of the rectangle to be drawn
     * @param top    The top side of the rectangle to be drawn
     * @param bottom The bottom side of the rectangle to be drawn
     */
    protected void onDrawItemDecor(Canvas c, View child, RecyclerView parent, int left, int top, int right, int bottom) {
        c.drawRect(left, top, right, bottom, mPaint);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPaint == null || parent.getAdapter() == null || parent.getLayoutManager() == null) {
            return;
        }
        boolean isVertical = true;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            isVertical = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL;
        }

        mPaint.setColor(mArgs.getColor()); // 设置颜色
        if (isVertical) {
            verticalDraw(c, parent);
        } else {
            horizontalDraw(c, parent);
        }
    }

    @Override
    public final void onDraw(Canvas c, RecyclerView parent) {
        super.onDraw(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 在绘制ItemDivider之前,需要调用此方法。以便RecyclerView给该条目预留空间,供我们绘制Divider
        if (parent.getAdapter() == null || parent.getLayoutManager() == null) return;

        boolean isVertical = true;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            isVertical = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL;
        }

        int position = parent.getChildAdapterPosition(view);
        int footerCount = 0;
        if (parent.getAdapter() instanceof CompatAdapter) {
            CompatAdapter adapter = (CompatAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
            if (adapter.isInnerPosition(position)) {
                outRect.set(0, 0, 0, 0);
                return;
            }
        }
        if (mArgs.ignoreLastItem) {
//            ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if (position == parent.getAdapter().getItemCount() - 1 - footerCount) {
                return;
            }
        }
        setItemOffset(view, mArgs.space);
        if (isVertical) {
            outRect.set(0, 0, 0, mArgs.space);
        } else {
            outRect.set(0, 0, mArgs.space, 0);
        }
    }

    @Override
    public final void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
    }

    protected final void setItemOffset(View child, int offset) {
        child.setTag(R.id.tag_item_offset, offset);
    }

    protected final int getItemOffset(View child) {
        Object tag = child.getTag(R.id.tag_item_offset);
        if (tag instanceof Integer) {
            return (Integer) tag;
        }
        return 0;
    }

}