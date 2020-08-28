package com.aliya.adapter.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.divider.ListBuilder;
import com.aliya.adapter.sample.activity.AdapterDiffDataSampleActivity;
import com.aliya.adapter.sample.activity.AdapterUnifyDataSampleActivity;
import com.aliya.adapter.sample.activity.DecorAdapterGridSampleActivity;
import com.aliya.adapter.sample.activity.DecorAdapterListSampleActivity;
import com.aliya.adapter.sample.activity.DecorAdapterLoadMoreSampleActivity;
import com.aliya.adapter.sample.activity.EmptyPageSampleActivity;
import com.aliya.adapter.sample.activity.OverlaySampleActivity;
import com.aliya.adapter.sample.activity.StaggeredSampleActivity;
import com.aliya.adapter.sample.activity.Test2Activity;
import com.aliya.adapter.sample.activity.TestActivity;
import com.aliya.adapter.sample.activity.TestRefreshActivity;
import com.aliya.adapter.sample.decoration.SubListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycle;

    private static List<Entity> items = new ArrayList<>();

    static {
        items.add(new Entity("普通用法 - 示例", AdapterUnifyDataSampleActivity.class));
        items.add(new Entity("列表样式 - 示例", DecorAdapterListSampleActivity.class));
        items.add(new Entity("Grid样式 - 示例", DecorAdapterGridSampleActivity.class));
        items.add(new Entity("Staggered样式 - 示例", StaggeredSampleActivity.class));
        items.add(new Entity("多item类型 - 示例", AdapterDiffDataSampleActivity.class));
        items.add(new Entity("下拉刷新 - 上拉加载", DecorAdapterLoadMoreSampleActivity.class));
        items.add(new Entity("悬浮吸顶 - 示例", OverlaySampleActivity.class));
        items.add(new Entity("Adapter空页面 - 示例", EmptyPageSampleActivity.class));
        items.add(new Entity("测试 - 下拉刷新", TestRefreshActivity.class));
        items.add(new Entity("测试 - footer加载 异常", TestActivity.class));
        items.add(new Entity("测试 - footer加载 异常，三方库", Test2Activity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycle = findViewById(R.id.recycler);

        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(
                new SubListItemDecoration(
                new ListBuilder(getApplicationContext())
                        .setSpace(1)                        // 分割线间距
                        .setColorRes(R.color.colorDivider)  // 分割线颜色
                        .setIgnoreLastItem(true)            // 是否忽略最后一条
                        .setMargin(5)                       // 两边间距
//                        .build()
                )
        );

        recycle.setAdapter(new Adapter(items));

    }

    static class Entity {

        String content;

        Class activity;

        public Entity(String content, Class activity) {
            this.content = content;
            this.activity = activity;
        }

        public String getContent() {
            return content;
        }

        public Entity setContent(String content) {
            this.content = content;
            return this;
        }

        public Class getActivity() {
            return activity;
        }

        public Entity setActivity(Class activity) {
            this.activity = activity;
            return this;
        }
    }

    static class Adapter extends RecyclerAdapter<Entity> {

        public Adapter(List<Entity> data) {
            super(data);
        }

        @Override
        public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }
    }

    static class ViewHolder extends RecyclerViewHolder<Entity> implements View.OnClickListener {

        TextView mTextView;

        public ViewHolder(@NonNull ViewGroup parent) {
            super(parent, R.layout.item_main_sample);
            mTextView = itemView.findViewById(R.id.tv_item);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void bindData(Entity data) {
            mTextView.setText(data.getContent());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            context.startActivity(new Intent(context, mData.getActivity()));
        }
    }

}
