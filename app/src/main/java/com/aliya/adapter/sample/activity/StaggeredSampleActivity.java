package com.aliya.adapter.sample.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.DecorAdapter;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.click.OnItemLongClickListener;
import com.aliya.adapter.divider.StaggeredBuilder;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.adapter.OriginalAdapterSample;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredSampleActivity extends AppCompatActivity {

    RecyclerView recycle;

    private DecorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_sample);
        recycle = findViewById(R.id.recycler);
        recycle.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycle.addItemDecoration(new StaggeredBuilder(this).setSpace(5).setIncludeEdge(true).build());
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 21; i++) {
            list.add(String.valueOf(i));
        }

        mAdapter = new DecorAdapter(new OriginalAdapterSample(list));

        recycle.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("TAG", "onItemClick " + position);
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                Log.e("TAG", "onItemLongClick " + position);
                return true;
            }
        });

        View inflate = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate.findViewById(R.id.tv)).setText("第1个header");
        mAdapter.addHeaderView(inflate);

        View inflate1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate1.findViewById(R.id.tv)).setText("第2个header");
        mAdapter.addHeaderView(inflate1);

        View inflate2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate2.findViewById(R.id.tv)).setText("第3个header");
        mAdapter.addHeaderView(inflate2);

        View footer = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer.findViewById(R.id.tv)).setText("我是加载更多");
        mAdapter.setFooterLoadMore(footer);

        View footer0 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer0.findViewById(R.id.tv)).setText("我是覆盖加载更多");
        mAdapter.setFooterLoadMore(footer0);

        View footer1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer1.findViewById(R.id.tv)).setText("第1个footer");
        mAdapter.addFooterView(footer1);

        View footer2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer2.findViewById(R.id.tv)).setText("第2个footer");
        mAdapter.addFooterView(footer2);

        View refresh = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) refresh.findViewById(R.id.tv)).setText("我是下拉刷新");
        mAdapter.setHeaderRefresh(refresh);

        View refresh1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) refresh1.findViewById(R.id.tv)).setText("我要覆盖下拉刷新");
        mAdapter.setHeaderRefresh(refresh1);
    }
}
