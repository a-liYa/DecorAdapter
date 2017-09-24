package com.aliya.adapter.simple.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aliya.adapter.divider.ListSpaceDivider;
import com.aliya.adapter.simple.R;

/**
 * 空页面 示例 - Activity
 *
 * @author a_liYa
 * @date 2017/9/24 下午3:17.
 */
public class EmptyPageSimpleActivity extends Activity {

    RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_page_simple);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(1, Color.parseColor("#cccccc"), false));

    }



}
