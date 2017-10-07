package com.aliya.adapter.divider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.adapter.OverlayViewHolder;
import com.aliya.adapter.RecyclerAdapter;

import static android.view.View.MeasureSpec.makeMeasureSpec;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 悬浮吸顶 - ItemDecoration
 *
 * @author a_liYa
 * @date 2017/9/23 16:08.
 */
public class OverlayItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getAdapter() instanceof RecyclerAdapter)) return;

        RecyclerAdapter adapter = (RecyclerAdapter) parent.getAdapter();

        int firstVisiblePosition = RecyclerView.NO_POSITION;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager())
                    .findFirstVisibleItemPosition();
        }
        if (firstVisiblePosition == RecyclerView.NO_POSITION) return;

        int overlayPosition = getOverlayPosition(parent, adapter, firstVisiblePosition);
        if (overlayPosition == RecyclerView.NO_POSITION) return;

        OverlayViewHolder overlayHolder = adapter.onCreateOverlayViewHolder(
                parent,
                adapter.getAbsItemViewType(adapter.cleanPosition(overlayPosition)));

        if (overlayHolder == null) {
            Log.e("TAG", adapter.getClass().getName() + " must be override " +
                    "onCreateOverlayViewHolder(ViewGroup, viewType)");
            return;
        }
        adapter.onBindViewHolder(overlayHolder, overlayPosition);

        Bitmap bitmap = getOverlayViewBitmap(parent, overlayHolder);
        int top = calcOverlayViewTop(parent, firstVisiblePosition, overlayHolder);
        if (bitmap != null) {
            int left = parent.getPaddingLeft() + overlayHolder.getOverlayView().getLeft();
            c.drawBitmap(bitmap, left, top, null);
        }

    }

    /**
     * calc overlay view top
     *
     * @param parent        recycler view
     * @param start         start position
     * @param overlayHolder overlay view holder
     * @return Calculation of the top
     */
    @Nullable
    protected int calcOverlayViewTop(RecyclerView parent, int start, OverlayViewHolder
            overlayHolder) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        View overlayItemView = overlayHolder.itemView;
        View overlayView = overlayHolder.getOverlayView();
        int offset = overlayHolder.getOverlayOffset();
        int overlayTop = overlayItemView.getTop() + offset;
        int overlayBottom = overlayItemView.getTop() + overlayView.getHeight() + offset;

        if (overlayView != overlayItemView) {
            overlayTop += overlayView.getTop();
            overlayBottom = overlayItemView.getTop() + overlayView.getBottom() + offset;
        }

        int count = adapter.getItemCount();
        while (start < count) {
            ViewHolder viewHolder = parent.findViewHolderForAdapterPosition(start);
            if (viewHolder == null) {
                break;
            }
            if (viewHolder.itemView.getTop() > overlayBottom) {
                break;
            }
            if (viewHolder instanceof OverlayViewHolder) {
                OverlayViewHolder nextHolder = (OverlayViewHolder) viewHolder;
                int nextOverlayTop = nextHolder.itemView.getTop();
                if (nextHolder.itemView != nextHolder.getOverlayView()) { // overlayView != itemView
                    nextOverlayTop += nextHolder.getOverlayView().getTop();
                }
                if (nextOverlayTop > overlayTop && nextOverlayTop < overlayBottom) {
                    return overlayTop - (overlayBottom - nextOverlayTop);
                }
            }
            start++;
        }
        return overlayTop;
    }

    protected Bitmap getOverlayViewBitmap(RecyclerView parent, OverlayViewHolder overlayHolder) {
        Bitmap bitmap = overlayHolder.getOverlayView().getDrawingCache();
        if (bitmap == null || bitmap.isRecycled()) { // is null， 重新强制 drawCache
            overlayHolder.getOverlayView().setDrawingCacheEnabled(true);
            initOverlayLayout(parent, overlayHolder.itemView);
            overlayHolder.getOverlayView().buildDrawingCache();
            bitmap = overlayHolder.getOverlayView().getDrawingCache();
        }
        return bitmap;
    }

    /**
     * 初始化Overlay布局
     *
     * @param parent   parent recycler view
     * @param itemView overlay itemView
     */
    protected void initOverlayLayout(RecyclerView parent, View itemView) {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        }

        int wMeasureSpec = ViewGroup.getChildMeasureSpec(
                makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                parent.getPaddingLeft() + parent.getPaddingRight(), lp.width);
        int hMeasureSpec = ViewGroup.getChildMeasureSpec(0,
                parent.getPaddingTop() + parent.getPaddingBottom(), lp.height);

        itemView.measure(wMeasureSpec, hMeasureSpec);
        itemView.layout(0,
                parent.getPaddingTop(),
                itemView.getMeasuredWidth(),
                itemView.getMeasuredHeight());
    }

    /**
     * 获取悬浮ViewHolder的position
     *
     * @param parent               parent is recycler view
     * @param adapter              {@link RecyclerAdapter}
     * @param firstVisiblePosition first visible position
     * @return position
     */
    protected int getOverlayPosition(RecyclerView parent, RecyclerAdapter adapter,
                                     final int firstVisiblePosition) {
        int headerCount = adapter.getHeaderCount();
        int position = firstVisiblePosition;
        while (position >= headerCount) {
            if (adapter.isOverlayViewType(adapter.cleanPosition(position))) {
                if (position == firstVisiblePosition) { // 特殊处理
                    ViewHolder viewHolder = parent.findViewHolderForAdapterPosition(position);
                    if (viewHolder instanceof OverlayViewHolder) {
                        OverlayViewHolder overlay = (OverlayViewHolder) viewHolder;
                        if (overlay.itemView.getTop() - overlay.getOverlayOffset() > 0) {
                            position--;
                            continue;
                        }
                    }
                }
                return position;
            }
            position--;
        }
        return RecyclerView.NO_POSITION;
    }

}