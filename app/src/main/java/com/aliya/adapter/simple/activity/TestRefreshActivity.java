package com.aliya.adapter.simple.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.callback.LoadingCallBack;
import com.aliya.adapter.simple.page.LoadMoreFooter;
import com.aliya.adapter.simple.page.RefreshHeader;

/**
 * 测试下拉刷新与AppBar冲突
 *
 * @author a_liYa
 * @date 2018/6/5 下午6:51.
 */
public class TestRefreshActivity extends AppCompatActivity implements RefreshHeader
        .OnRefreshListener, LoadMoreFooter.LoadMoreListener<Object> {

    AppBarLayout mAppBar;
    RecyclerView mRecycler;
    RefreshHeader mRefreshHeader;
    private RecyclerAdapter mAdapter;
    private LoadMoreFooter<Object> mMoreFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAppBar = findViewById(R.id.appbar);

        mAdapter = new RecyclerAdapter(null) {
            @Override
            public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };
        mAdapter.setHeaderRefresh(mRefreshHeader = new RefreshHeader(mRecycler, this));
        mMoreFooter = new LoadMoreFooter<>(this);
        mMoreFooter.onEmpty();
        mAdapter.setFooterLoadMore(mMoreFooter);
        mRecycler.setAdapter(mAdapter);

        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mRefreshHeader.setEnabled(true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mRefreshHeader.setEnabled(false);
                } else {
                    //中间状态
                    mRefreshHeader.setEnabled(false);
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
