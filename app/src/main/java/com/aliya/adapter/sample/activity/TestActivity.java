package com.aliya.adapter.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.callback.LoadingCallBack;
import com.aliya.adapter.sample.page.LoadMoreFooter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 演示加载更多刷新布局时同时调用notifyDataSetChanged()刷新会出现异常， 加载更多时快速上下滑动。
 *
 * @author a_liYa
 * @date 2020/8/28 17:42.
 */
public class TestActivity extends AppCompatActivity implements LoadMoreFooter
        .LoadMoreListener<String>, OnItemClickListener {

    private List<String> data;
    private RecyclerAdapter<String> adapter;

    private int count = 0;
    private RecyclerView mRecycler;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        data = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            data.add("data" + i);
        }

        mRecycler = findViewById(R.id.rv);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(adapter = new TestAdapter(data));
        adapter.setFooterLoadMore(new LoadMoreFooter<>(this));
        adapter.setOnItemClickListener(this);
        mRecycler.getItemAnimator().setChangeDuration(5000);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
            }
        };

    }

    @Override
    public void onLoadMoreSuccess(String data, LoadMore loadMore) {
//        adapter.notifyItemRangeChanged(this.data.size() - 1, 2);
        adapter.addData(Arrays.asList(data), true);
        mHandler.sendMessageDelayed(Message.obtain(), 100);
    }

    @Override
    public void onLoadMore(final LoadingCallBack<String> callback) {
        mRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess("加载 " + count++);
            }
        }, 200);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Log.e("TAG", "onItemClick: " + adapter.getDataSize() + " - " +
                position + " - " + adapter.getItemCount());
    }

    private class TestAdapter extends RecyclerAdapter<String> {

        public TestAdapter(List<String> data) {
            super(data);
        }

        @Override
        public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder<String>(LayoutInflater.from(TestActivity.this).inflate
                    (R.layout.atest, parent, false)) {
                @Override
                public void bindView(String data) {
                    View childAt = ((FrameLayout) itemView).getChildAt(0);
                    ((TextView) childAt).setText(data);
                }
            };

        }
    }

}
