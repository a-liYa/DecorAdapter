package com.aliya.adapter.simple.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.BaseRecyclerAdapter;
import com.aliya.adapter.BaseRecyclerViewHolder;
import com.aliya.adapter.simple.R;

import java.util.List;

/**
 * 统一数据 Adapter
 *
 * @author a_liYa
 * @date 2017/8/23 20:17.
 */
public class UnifyDataSimpleAdapter extends BaseRecyclerAdapter<String> {

    public UnifyDataSimpleAdapter(List<String> data) {
        super(data);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    class Holder extends BaseRecyclerViewHolder<String> {

        TextView tv;

        public Holder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent, false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindView(String data) {
            tv.setText(data);
        }
    }
}
