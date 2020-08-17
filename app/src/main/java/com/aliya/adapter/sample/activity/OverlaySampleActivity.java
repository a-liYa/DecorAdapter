package com.aliya.adapter.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.OverlayViewHolder;
import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.divider.OverlayItemDecoration;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.holder.HeaderHolderSample;
import com.aliya.adapter.sample.holder.SampleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬浮吸顶效果 示例 Activity
 *
 * @author a_liYa
 * @date 2017/9/23 下午3:34.
 */
public class OverlaySampleActivity extends AppCompatActivity {

    RecyclerView recycle;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_sample);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(new ListBuilder(this)
                .setColor(Color.GRAY)
                .setSpace(0.5f).build()
        );
        recycle.addItemDecoration(new OverlayItemDecoration());
        mAdapter = new Adapter(createTestData());
        mAdapter.addHeaderView(new HeaderHolderSample(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSample(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSample(recycle, "我是页头").itemView);
        mAdapter.addHeaderView(new HeaderHolderSample(recycle, "我是页头").itemView);
        recycle.setAdapter(mAdapter);
    }

    private List createTestData() {
        List list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            if (i % 5 == 0) {
                list.add("悬浮 item " + (i / 5));
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
            return new SampleViewHolder(parent);
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

    /**
     * 悬浮 - ViewHolder
     *
     * @author a_liYa
     * @date 2017/10/6 下午5:22.
     */
    static class OverlayHolder extends OverlayViewHolder {

        TextView tv;

        public OverlayHolder(@NonNull ViewGroup parent) {
            super(parent, R.layout.item_overlay_sample);
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindData(Object data) {
            tv.setText("" + data);
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