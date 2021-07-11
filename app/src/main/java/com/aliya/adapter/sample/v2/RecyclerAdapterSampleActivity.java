package com.aliya.adapter.sample.v2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.sample.databinding.ActivityRecyclerSampleBinding;
import com.decor.adapter.RecyclerAdapter;
import com.decor.adapter.click.OnItemClickListener;
import com.decor.adapter.click.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * BaseRecyclerAdapter不同数据类型的使用示例
 *
 * @author a_liYa
 * @date 2017/8/24 下午5:55.
 */
public class RecyclerAdapterSampleActivity extends AppCompatActivity {

    private RecyclerAdapter mAdapter;
    private ActivityRecyclerSampleBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityRecyclerSampleBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recycler.addItemDecoration(
                new ListBuilder(this).setSpace(5).setColor(Color.CYAN).build());

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            switch (i % 3) {
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

        mAdapter = new MultiAdapter(list);

        mViewBinding.recycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("TAG", "onItemClick " + mAdapter.getItem(position));
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                Log.e("TAG", "onItemLongClick " + mAdapter.getItem(position));
                return true;
            }
        });

    }
}
