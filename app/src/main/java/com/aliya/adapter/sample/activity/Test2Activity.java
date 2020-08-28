package com.aliya.adapter.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.aliya.adapter.page.LoadMore;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.callback.LoadingCallBack;
import com.aliya.adapter.sample.page.LoadMoreFooter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 演示加载更多刷新布局时同时调用notifyDataSetChanged()刷新会出现异常， 加载更多时快速上下滑动。
 *
 * 这是使用三方库 BaseRecyclerViewAdapterHelper
 *
 * @author a_liYa
 * @date 2020/8/28 17:34.
 */
public class Test2Activity extends AppCompatActivity implements LoadMoreFooter
        .LoadMoreListener<String> {

    private List<String> data;
    private TestAdapter adapter;

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
        adapter.setFooterView(new LoadMoreFooter<>(this).onCreateView(mRecycler));
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
        adapter.addData(Arrays.asList(data));
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

    private class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public TestAdapter(List<String> data) {
            super(R.layout.atest, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.tv_text, s);
        }
    }

}
