package com.aliya.adapter.sample.v2;

import android.view.ViewGroup;

import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.databinding.ItemDemoSampleBinding;
import com.decor.adapter.RecyclerAdapter;
import com.decor.adapter.RecyclerViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MultiAdapter 支持不同类型的数据
 *
 * @author a_liYa
 * @date 2021/7/11 22:58.
 */
public class MultiAdapter extends RecyclerAdapter {

    private static final int VIEW_TYPE_NULL = 0;
    private static final int VIEW_TYPE_STRING = 1;
    private static final int VIEW_TYPE_INTEGER = 2;

    public MultiAdapter(List list) {
        super(list);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = VIEW_TYPE_NULL;
        Object item = getItem(position);
        if (item == null) {
            viewType = VIEW_TYPE_NULL;
        } else if (item instanceof String) {
            viewType = VIEW_TYPE_STRING;
        } else if (item instanceof Integer){
            viewType = VIEW_TYPE_INTEGER;
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NULL:
                return new NullHolder(parent);
            case VIEW_TYPE_STRING:
                return new StringHolder(parent);
            case VIEW_TYPE_INTEGER:
                return new IntegerHolder(parent);
        }
        return new NullHolder(parent);
    }

    class StringHolder extends RecyclerViewHolder<String> {

        private final ItemDemoSampleBinding mViewBinding;

        public StringHolder(ViewGroup parent) {
            super(parent, R.layout.item_demo_sample);
            mViewBinding = ItemDemoSampleBinding.bind(itemView);
        }

        @Override
        protected void onBindData(String data) {
            mViewBinding.tv.setText("我是String类型 - " + data);
        }
    }

    class IntegerHolder extends RecyclerViewHolder<Integer> {

        private final ItemDemoSampleBinding mViewBinding;

        public IntegerHolder(ViewGroup parent) {
            super(parent, R.layout.item_demo_sample);
            mViewBinding = ItemDemoSampleBinding.bind(itemView);
        }

        @Override
        protected void onBindData(Integer data) {
            mViewBinding.tv.setText("我是Integer类型 - " + data);
        }
    }

    class NullHolder extends RecyclerAdapter.StaticViewHolder {

        private final ItemDemoSampleBinding mViewBinding;

        public NullHolder(ViewGroup parent) {
            super(parent, R.layout.item_demo_sample);
            mViewBinding = ItemDemoSampleBinding.bind(itemView);
        }

        @Override
        protected void onBindData(Object data) {
            super.onBindData(data);
            mViewBinding.tv.setText("我是Null类型 - " + data);
        }
    }
}
