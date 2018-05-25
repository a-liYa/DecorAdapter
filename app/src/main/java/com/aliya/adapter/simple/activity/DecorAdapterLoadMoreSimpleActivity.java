package com.aliya.adapter.simple.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.DecorAdapter;
import com.aliya.adapter.divider.ListSpaceDivider;
import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.adapter.OriginalAdapterSimple;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.simple.callback.LoadingCallBack;
import com.aliya.adapter.simple.page.LoadMoreFooter;
import com.aliya.adapter.simple.page.RefreshHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * DecorAdapter 上拉加载更多使用示例
 *
 * @author a_liYa
 * @date 2017/8/25 上午9:05.
 */
public class DecorAdapterLoadMoreSimpleActivity extends AppCompatActivity implements
        RefreshHeader.OnRefreshListener {

    RecyclerView recycle;
    private DecorAdapter mAdapter;

    private int count;
    private List<String> mList;
    private RefreshHeader mRefreshHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_simple);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mList.add(String.valueOf(i));
        }

        mAdapter = new DecorAdapter(new OriginalAdapterSimple(mList));

        recycle.setAdapter(mAdapter);

        recycle.addItemDecoration(new ListSpaceDivider(5, Color.BLUE, 0, 0, true, false, false));

        View inflate = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate.findViewById(R.id.tv)).setText("第1个header");
        mAdapter.addHeaderView(inflate);

        View inflate1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate1.findViewById(R.id.tv)).setText("第2个header");
        mAdapter.addHeaderView(inflate1);

        View inflate2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate2.findViewById(R.id.tv)).setText("第3个header");
        mAdapter.addHeaderView(inflate2);

        mAdapter.setFooterLoadMore(new LoadMoreFooter(recycle,
                new LoadMoreFooter.LoadMoreListener<String>() {

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

        }).itemView);

        View footer1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer1.findViewById(R.id.tv)).setText("第1个footer");
        mAdapter.addFooterView(footer1);

        View footer2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer2.findViewById(R.id.tv)).setText("第2个footer");
        mAdapter.addFooterView(footer2);

        View refresh = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) refresh.findViewById(R.id.tv)).setText("我是下拉刷新");
        mRefreshHeader = new RefreshHeader(recycle, this);
        mAdapter.setHeaderRefresh(mRefreshHeader.itemView);

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