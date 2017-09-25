package com.aliya.adapter.simple.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.OverlayViewHolder;
import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.divider.ListSpaceDivider;
import com.aliya.adapter.divider.OverlayItemDecoration;
import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.holder.HeaderHolderSimple;
import com.aliya.adapter.simple.holder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬浮吸顶效果 示例 Activity
 *
 * @author a_liYa
 * @date 2017/9/23 下午3:34.
 */
public class OverlaySimpleActivity extends Activity {

    RecyclerView recycle;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_simple);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(new ListSpaceDivider(0.5f, Color.GRAY, false));
        recycle.addItemDecoration(new OverlayItemDecoration());
        mAdapter = new Adapter(createTestData());
        mAdapter.addHeaderView(new HeaderHolderSimple(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSimple(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSimple(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSimple(recycle, "我是页头").itemView);
        recycle.setAdapter(mAdapter);
    }

    private List createTestData() {
        List list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            if (i % 5 == 0) {
                list.add("我是悬浮item" + (i / 5));
            } else {
                list.add(i);
            }
        }
        return list;
    }

    static class Adapter extends RecyclerAdapter {

        static final int TYPE_OVERLAY = 1;

        private RecyclerView.ViewHolder overlayViewHolder;

        public Adapter(List data) {
            super(data);
        }

        @Override
        public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_OVERLAY == viewType) {
                return new OverlayHolder(parent);
            }
            return new SimpleViewHolder(parent);
        }

        @Override
        public int getAbsItemViewType(int position) {
            Object data = getData(position);
            if (data instanceof String) {
                return TYPE_OVERLAY;
            }
            return super.getAbsItemViewType(position);
        }

        @Override
        public boolean isOverlayViewType(int position) {
            return getAbsItemViewType(position) == TYPE_OVERLAY;
        }

        @Override
        public OverlayViewHolder onCreateOverlayViewHolder(ViewGroup parent, int viewType) {
            if (overlayViewHolder == null) {
                overlayViewHolder = onAbsCreateViewHolder(parent, viewType);
            }
            return (OverlayViewHolder) overlayViewHolder;
        }

    }

    static class OverlayHolder extends OverlayViewHolder {

        TextView tv;

        public OverlayHolder(@NonNull ViewGroup parent) {
            super(parent, R.layout.item_overlay_simple);
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindView(Object data) {
            tv.setText("悬浮 - - " + data);
        }

        @Override
        public View getOverlayView() {
            return tv;
        }

        @Override
        public int getOverlayOffset() {
            return -tv.getTop();
        }
    }

}