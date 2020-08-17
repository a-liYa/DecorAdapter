package com.aliya.adapter.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.DecorAdapter;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.page.RefreshPage;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.adapter.OriginalAdapterSample;
import com.aliya.adapter.sample.callback.LoadingCallBack;
import com.aliya.adapter.sample.page.LoadMoreFooter;
import com.aliya.adapter.sample.page.RefreshHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * DecorAdapter 上拉加载更多使用示例
 *
 * @author a_liYa
 * @date 2017/8/25 上午9:05.
 */
public class DecorAdapterLoadMoreSampleActivity extends AppCompatActivity implements
        RefreshHeader.OnRefreshListener {

    RecyclerView recycle;
    private DecorAdapter mAdapter;

    private int count;
    private List<String> mList;
    private RefreshPage mRefreshHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sample);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mList.add(String.valueOf(i));
        }

        mAdapter = new DecorAdapter(new OriginalAdapterSample(mList));

        recycle.setAdapter(mAdapter);

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

        mAdapter.setFooterLoadMore(new LoadMoreFooter(new LoadMoreFooter.LoadMoreListener<String>() {

            @Override
            public void onLoadMoreSuccess(String data, LoadMore loadMore) {
                mList.add(data);
                mAdapter.notifyDataSetChanged();
                Log.e("TAG", "notifyDataSetChanged");
            }

            @Override
            public void onLoadMore(final LoadingCallBack<String> callback) {
                Log.e("TAG", "onLoadMore " + count);
                recycle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if (count == 8) {
                            callback.onEmpty();
                        } else if (count % 3 == 0) {
                            callback.onError("", 1);
                        } else {
                            callback.onSuccess("成功 " + count);
                        }
                    }
                }, 2000);
            }

        }));

        View footer1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer1.findViewById(R.id.tv)).setText("第1个footer");
        mAdapter.addFooterView(footer1);

        View footer2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer2.findViewById(R.id.tv)).setText("第2个footer");
        mAdapter.addFooterView(footer2);

        View refresh = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) refresh.findViewById(R.id.tv)).setText("我是下拉刷新");
        mRefreshHeader = new RefreshHeader(this);
        mAdapter.setHeaderRefresh(mRefreshHeader);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("TAG", "onItemClick " + position);
            }
        });
    }

    @Override
    public void onRefresh() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshHeader.setRefreshing(false);
            }
        }, 2000);
    }

}