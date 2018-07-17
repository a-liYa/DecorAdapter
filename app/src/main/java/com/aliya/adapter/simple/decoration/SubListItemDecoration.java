package com.aliya.adapter.simple.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public SubListItemDecoration(@NonNull ListBuilder args) {
        super(args);
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
}
