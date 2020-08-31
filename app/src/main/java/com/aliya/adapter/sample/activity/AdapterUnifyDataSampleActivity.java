package com.aliya.adapter.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.DecorAdapter;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.click.OnItemLongClickListener;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.adapter.UnifyDataSampleAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * BaseRecyclerAdapter统一数据类型的使用示例
 *
 * @author a_liYa
 * @date 2017/8/24 17:20.
 */
public class AdapterUnifyDataSampleActivity extends AppCompatActivity {

    RecyclerView recycle;
    private DecorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sample);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }

        mAdapter = new UnifyDataSampleAdapter(list);

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

        recycle.addItemDecoration(
                new ListBuilder(this)
                        .setSpace(5)
                        .setColor(Color.parseColor("#cccccc")).build());

        View inflate = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate.findViewById(R.id.tv)).setText("第1个header");
        mAdapter.addHeaderView(inflate);

        View inflate1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate1.findViewById(R.id.tv)).setText("第2个header");
        mAdapter.addHeaderView(inflate1);

        View inflate2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate2.findViewById(R.id.tv)).setText("第3个header");
        mAdapter.addHeaderView(inflate2);

        View loadMareFooter = getLayoutInflater().inflate(R.layout.item_header_layout, recycle,
                false);
        ((TextView) loadMareFooter.findViewById(R.id.tv)).setText("我是加载更多");
        mAdapter.setFooterLoadMore(loadMareFooter);

        View loadMareCoverFooter = getLayoutInflater().inflate(R.layout.item_header_layout,
                recycle, false);
        ((TextView) loadMareCoverFooter.findViewById(R.id.tv)).setText("我是覆盖加载更多");
        mAdapter.setFooterLoadMore(loadMareCoverFooter);

        View footer1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer1.findViewById(R.id.tv)).setText("第1个footer");
        mAdapter.addFooterView(footer1);

        View footer2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer2.findViewById(R.id.tv)).setText("第2个footer");
        mAdapter.addFooterView(footer2);

        View refreshHeader = getLayoutInflater().inflate(R.layout.item_header_layout, recycle,
                false);
        ((TextView) refreshHeader.findViewById(R.id.tv)).setText("我是下拉刷新");
        mAdapter.setHeaderRefresh(refreshHeader);

        View refreshCoverHeader = getLayoutInflater().inflate(R.layout.item_header_layout,
                recycle, false);
        ((TextView) refreshCoverHeader.findViewById(R.id.tv)).setText("我要覆盖下拉刷新");
        mAdapter.setHeaderRefresh(refreshCoverHeader);

    }
}
