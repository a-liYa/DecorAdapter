package com.aliya.adapter.simple.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.simple.R;

import java.util.List;

/**
 * RecyclerAdapter 的简单示例
 *
 * @author a_liYa
 * @date 2017/9/24 16:54.
 */
public class RecyclerAdapterSimple extends RecyclerAdapter<String> {

    public RecyclerAdapterSimple(List<String> data) {
        super(data);
    }

    @Override
    public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    static class ViewHolder extends RecyclerViewHolder<String> {

        TextView tv;

        public ViewHolder(@NonNull ViewGroup parent) {
            super(inflate(R.layout.item_demo_simple, parent, false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindData(String data) {
            tv.setText("数据 -> " + data);
        }
    }

}
