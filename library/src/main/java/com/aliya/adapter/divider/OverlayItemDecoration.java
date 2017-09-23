package com.aliya.adapter.divider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aliya.adapter.CompatAdapter;
import com.aliya.adapter.RecyclerAdapter;

/**
 * 悬浮吸顶 - ItemDecoration
 *
 * @author a_liYa
 * @date 2017/9/23 16:08.
 */
public class OverlayItemDecoration extends RecyclerView.ItemDecoration {

    protected static final int NO_OVERLAY_POSITION = -1;

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getAdapter() instanceof CompatAdapter)) return;

        RecyclerAdapter adapter = (RecyclerAdapter) parent.getAdapter();

        if (parent.getChildCount() > 0) {
            View childAt = parent.getChildAt(0);
            int position = parent.getChildAdapterPosition(childAt);
            if (position == RecyclerView.NO_POSITION) return;
            int overlayPosition = getOverlayPosition(adapter, position);
            if (overlayPosition == NO_OVERLAY_POSITION) return;

            RecyclerView.ViewHolder overlayViewHolder = adapter.onCreateOverlayViewHolder(parent,
                    adapter.getAbsItemViewType(overlayPosition));

            if (overlayViewHolder == null) {
                Log.e("TAG", adapter.getClass().getName() + " must be override " +
                        "onCreateOverlayViewHolder(ViewGroup, viewType)");
                return;
            }

            adapter.onBindViewHolder(overlayViewHolder, overlayPosition);

            long startMs = SystemClock.uptimeMillis();

            View overlayView = overlayViewHolder.itemView;

            Bitmap bitmap = overlayView.getDrawingCache();
            if (bitmap == null || bitmap.isRecycled()) {
                overlayView.setDrawingCacheEnabled(true);
                overlayView.measure(
                        View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec
                                .EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(overlayView.getMinimumHeight(), View
                                .MeasureSpec.EXACTLY));
                //指定高度、宽度的groupView
                overlayView.layout(0, 0, overlayView.getMeasuredWidth(), overlayView
                        .getMeasuredHeight());
                overlayView.buildDrawingCache();
                bitmap = overlayView.getDrawingCache();
            }
            if (bitmap != null) {
                c.drawBitmap(bitmap, parent.getPaddingLeft(), 0, null);
            }
            long millis = SystemClock.uptimeMillis();
            Log.e("TAG", " DrawingCache 耗时 " + (millis - startMs));

        }

    }

    protected int getOverlayPosition(CompatAdapter adapter, int firstVisiblePosition) {
        while (firstVisiblePosition >= 0) {
            if (adapter.isOverlayViewType(firstVisiblePosition)) {
                return firstVisiblePosition;
            }
            firstVisiblePosition--;
        }
        return NO_OVERLAY_POSITION;
    }

}
