package com.aliya.adapter.sample.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.callback.LoadingCallBack;
import com.aliya.adapter.sample.page.LoadMoreFooter;
import com.aliya.adapter.sample.page.Refresh2Header;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 测试下拉刷新与AppBar冲突
 *
 * @author a_liYa
 * @date 2018/6/5 下午6:51.
 */
public class TestRefreshActivity extends AppCompatActivity implements Refresh2Header
        .OnRefreshListener, LoadMoreFooter.LoadMoreListener<Object> {

    AppBarLayout mAppBar;
    RecyclerView mRecycler;
    Refresh2Header mRefreshHeader;
    private RecyclerAdapter mAdapter;
    private LoadMoreFooter<Object> mMoreFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAppBar = findViewById(R.id.appbar);

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        mAdapter = new RecyclerAdapter(list) {
            @Override
            public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerViewHolder(parent, R.layout.item_demo_sample) {
                };
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("TAG", "onItemClick: " + position);
            }
        });
        mAdapter.setHeaderRefresh(mRefreshHeader = new Refresh2Header(this));
        mRefreshHeader.setFitParentScroll(true);
        mMoreFooter = new LoadMoreFooter<>(this);
        mMoreFooter.onEmpty();
        mAdapter.setFooterLoadMore(mMoreFooter);
        mRecycler.setAdapter(mAdapter);

        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    // 展开状态
                    mRefreshHeader.setEnabled(true);
                } else if (state == State.COLLAPSED) {
                    // 折叠状态
                    mRefreshHeader.setEnabled(false);
                } else {
                    // 中间状态
                    mRefreshHeader.setEnabled(false);
                }
            }

        });
        mAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mRefreshHeader.isRefreshing()) {
                    mRefreshHeader.setRefreshing(true);
                    onRefresh();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
              mRefreshHeader.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onLoadMoreSuccess(Object data, LoadMore loadMore) {
    }

    @Override
    public void onLoadMore(LoadingCallBack<Object> callback) {
    }

    public static abstract class AppBarStateChangeListener implements AppBarLayout
            .OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }
}
