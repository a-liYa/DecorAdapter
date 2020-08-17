package com.aliya.adapter.sample.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.divider.ListItemDecoration;

/**
 * 自定义ListItemDecoration
 *
 * @author a_liYa
 * @date 2018/7/17 15:00.
 */
public class SubListItemDecoration extends ListItemDecoration {

    Paint paint;

    public SubListItemDecoration(@NonNull ListBuilder args) {
        super(args);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        if (parent.getChildLayoutPosition(view) % 2 == 0)
            super.getItemOffsets(outRect, view, parent, state);
        else {
            outRect.set(0, 0, 0, 5);
            setItemOffset(view, 5);
        }
    }

    @Override
    protected void onDrawItemDecor(Canvas c, View child, RecyclerView parent, int left, int top,
                                   int right, int bottom) {
        if (parent.getChildLayoutPosition(child) % 2 == 0) {
            c.drawRect(left, top, right, bottom, paint);
        } else {
            super.onDrawItemDecor(c, child, parent, left, top, right, bottom);
        }
    }

}
