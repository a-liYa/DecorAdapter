package com.aliya.adapter.simple.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.aliya.adapter.simple.R;
import com.aliya.adapter.simple.callback.LoadingCallBack;
import com.aliya.adapter.simple.page.LoadMoreFooter;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements LoadMoreFooter
        .LoadMoreListener<String>,OnItemClickListener {

    private List<String> data;
    private RecyclerAdapter<String> adapter;

    private int count = 0;
    private RecyclerView mRecycler;

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
//        adapter.setFooterLoadMore(new LoadMoreFooter<>(this));
        adapter.setOnItemClickListener(this);
        mRecycler.getItemAnimator().setChangeDuration(5000);
    }

    @Override
    public void onLoadMoreSuccess(String data, LoadMore loadMore) {
        this.data.add(data);
        adapter.notifyItemRangeChanged(this.data.size() - 1, 2);
    }

    @Override
    public void onLoadMore(final LoadingCallBack<String> callback) {
        mRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess("加载 " + count++);
            }
        }, 2000);
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
//                return new RecyclerViewHolder<String>(loadMore) {
//                    @Override
//                    public void bindView(final String str) {
////                        ((TextView) itemView).setText(str);
//                        View childAt = ((ViewGroup) itemView).getChildAt(0);
//                        ((TextView) childAt).setText(str);
//
//                        Log.e("TAG", "bindView: " );
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                data.add(data.size() - 2, "new one");
//                                adapter.notifyItemRangeChanged(data.size() - 2, 1);
//                            }
//                        }, 3000);
//                    }
//                };
        }
    }

//    class FooterPageItem extends PageItem {
//
//        public FooterPageItem(int layoutRes) {
//            super(layoutRes);
//        }
//    }

}
