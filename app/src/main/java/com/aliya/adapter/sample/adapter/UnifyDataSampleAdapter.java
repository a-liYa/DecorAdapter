package com.aliya.adapter.sample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.sample.R;

import java.util.List;

/**
 * 统一数据 Adapter
 *
 * @author a_liYa
 * @date 2017/8/23 20:17.
 */
public class UnifyDataSampleAdapter extends RecyclerAdapter<String> {

    public UnifyDataSampleAdapter(List<String> data) {
        super(data);
    }

    @Override
    public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    class Holder extends RecyclerViewHolder<String> {

        TextView tv;

        public Holder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo_sample, parent, false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindData(String data) {
            tv.setText(data);
        }
    }
}
