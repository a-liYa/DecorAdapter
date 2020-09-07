package com.aliya.adapter.divider;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import com.aliya.adapter.CompatAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * StaggeredDecoration
 *
 * @author a_liYa
 * @date 2020/9/7 22:28.
 */
public class StaggeredDecoration extends RecyclerView.ItemDecoration {

    StaggeredBuilder mArgs;

    StaggeredDecoration(@NonNull StaggeredBuilder args) {
        mArgs = args;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanCount;
        StaggeredGridLayoutManager layoutManager;
        StaggeredGridLayoutManager.LayoutParams layoutParams;

        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            spanCount = layoutManager.getSpanCount();
            layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        } else return;

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

        int spanIndex = layoutParams.getSpanIndex();

        if (mArgs.includeEdge) {
            // 每行第一个
            if (spanIndex == 0) {
                outRect.left = Math.round(mArgs.space);
            } else {
                outRect.left = Math.round(mArgs.space / 2);
            }
            // 每行最后一个
            if (layoutParams.isFullSpan() || spanIndex + 1 == spanCount) {
                outRect.right = Math.round(mArgs.space);
            } else {
                outRect.right = Math.round(mArgs.space / 2);
            }

            // 第一行
            if (takePosition == spanIndex) {
                outRect.top = Math.round(mArgs.space);
            }
            outRect.bottom = Math.round(mArgs.space);
        } else {
            if (spanIndex != 0) {
                outRect.left = Math.round(mArgs.space / 2);
            }
            if (!layoutParams.isFullSpan() && spanIndex + 1 < spanCount) {
                outRect.right = Math.round(mArgs.space / 2);
            }
            if (takePosition != spanIndex) {
                outRect.top = Math.round(mArgs.space);
            }
        }
    }

    protected float dp2px(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                mArgs.context.getResources().getDisplayMetrics());
    }
}
