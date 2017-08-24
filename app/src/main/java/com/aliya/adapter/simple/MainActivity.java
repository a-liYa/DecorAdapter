package com.aliya.adapter.simple;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliya.adapter.BaseRecyclerAdapter;
import com.aliya.adapter.divider.ListDivider;
import com.aliya.adapter.simple.adapter.DiffDataSimpleAdapter;
import com.aliya.adapter.simple.callback.LoadMoreListener;
import com.aliya.adapter.simple.callback.LoadingCallBack;
import com.aliya.adapter.simple.holder.FooterLoadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycle;
    private BaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mAdapter = new DiffDataSimpleAdapter(list);

        recycle.setAdapter(mAdapter);

//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View itemView, int position) {
//                Log.e("TAG", "onItemClick " + mAdapter.getData(position));
//            }
//        });
//        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(View itemView, int position) {
//                Log.e("TAG", "onItemLongClick " + mAdapter.getData(position));
//                return true;
//            }
//        });

        recycle.addItemDecoration(new ListDivider(5, Color.BLUE, 0, 0, true, false, false));

        View inflate = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate.findViewById(R.id.tv)).setText("第1个header");
        mAdapter.addHeaderView(inflate);

        View inflate1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate1.findViewById(R.id.tv)).setText("第2个header");
        mAdapter.addHeaderView(inflate1);

        View inflate2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) inflate2.findViewById(R.id.tv)).setText("第3个header");
        mAdapter.addHeaderView(inflate2);

        mAdapter.setFooterLoadMore(new FooterLoadMore(recycle, new LoadMoreListener<String>() {

            @Override
            public void onLoadMoreSuccess(String data) {
                List datas = mAdapter.getDatas();
                datas.add(data);
                mAdapter.notifyDataSetChanged();
                Log.e("TAG", "notifyDataSetChanged");
            }

            @Override
            public void onLoadMore(final LoadingCallBack<String> callBack) {
                Log.e("TAG", "onLoadMore");
                recycle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess("成功");
                    }
                }, 5000);
            }

        }).getView());

        View footer1 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer1.findViewById(R.id.tv)).setText("第1个footer");
        mAdapter.addFooterView(footer1);

        View footer2 = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) footer2.findViewById(R.id.tv)).setText("第2个footer");
        mAdapter.addFooterView(footer2);

        View refresh = getLayoutInflater().inflate(R.layout.item_header_layout, recycle, false);
        ((TextView) refresh.findViewById(R.id.tv)).setText("我是下拉刷新");
        mAdapter.setHeaderRefresh(refresh);

    }
}
