package com.aliya.adapter.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.click.OnItemLongClickListener;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.adapter.DiffDataSampleAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * BaseRecyclerAdapter不同数据类型的使用示例
 *
 * @author a_liYa
 * @date 2017/8/24 下午5:55.
 */
public class AdapterDiffDataSampleActivity extends AppCompatActivity {

    RecyclerView recycle;
    private RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sample);

        recycle = (RecyclerView) findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            switch (i % 3) {
                case 0:
                    list.add(String.valueOf(i));
                    break;
                case 1:
                    list.add(Integer.valueOf(i));
                    break;
                case 2:
                    list.add(null);
                    break;
                default:
                    list.add(String.valueOf(i));
                    break;
            }
        }

        mAdapter = new DiffDataSampleAdapter(list);

        recycle.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("TAG", "onItemClick " + mAdapter.getData(position));
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                Log.e("TAG", "onItemLongClick " + mAdapter.getData(position));
                return true;
            }
        });

        recycle.addItemDecoration(
                new ListBuilder(this).setSpace(5).setColor(Color.BLUE).build());

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
