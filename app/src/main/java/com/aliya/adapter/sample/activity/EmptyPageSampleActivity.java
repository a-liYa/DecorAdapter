package com.aliya.adapter.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.adapter.RecyclerAdapterSample;
import com.aliya.adapter.sample.holder.EmptyPageHolder;
import com.aliya.adapter.sample.holder.FooterHolderSample;
import com.aliya.adapter.sample.holder.HeaderHolderSample;

import java.util.ArrayList;
import java.util.List;

/**
 * 空页面 示例 - Activity
 *
 * @author a_liYa
 * @date 2017/9/24 下午3:17.
 */
public class EmptyPageSampleActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecycler;

    RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_page_sample);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListBuilder(this).
                setSpace(1).setColor(Color.parseColor("#cccccc")).build());

        mAdapter = new RecyclerAdapterSample(null);

        mAdapter.setEmptyView(new EmptyPageHolder(mRecycler).itemView);
        mAdapter.addHeaderView(new HeaderHolderSample(mRecycler, "我是页眉一").itemView);
        mAdapter.setFooterLoadMore(new FooterHolderSample(mRecycler, "我是加载更多").itemView);
        mAdapter.addFooterView(new FooterHolderSample(mRecycler, "我是页脚1").itemView);
        mAdapter.addFooterView(new FooterHolderSample(mRecycler, "我是页脚2").itemView);

        mRecycler.setAdapter(mAdapter);

        initOther();

    }

    private void initOther() {
        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                ArrayList<String> list = new ArrayList<>();
                list.add("加一");
                mAdapter.addData(list, true);
                break;
            case R.id.tv_delete:
                List data = mAdapter.getData();
                if (data != null && !data.isEmpty()) {
                    data.remove(data.size() - 1);
                    mAdapter.notifyItemRemoved(mAdapter.getHeaderCount() + data.size());
                }
                break;
        }
    }

}
