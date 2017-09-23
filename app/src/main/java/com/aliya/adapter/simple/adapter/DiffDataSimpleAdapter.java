package com.aliya.adapter.simple.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.simple.R;

import java.util.List;

/**
 * 不用数据 Adapter
 *
 * @author a_liYa
 * @date 2017/8/23 20:17.
 */
public class DiffDataSimpleAdapter extends RecyclerAdapter {


    public DiffDataSimpleAdapter(List data) {
        super(data);
    }

    @Override
    public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new VoidHolder(parent);
            case 1:
                return new StringHolder(parent);
            case 3:
                return new IntegerHolder(parent);
        }
        return new StringHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        int viewType;
        if (getData(position) == null) {
            viewType = 0;
        } else if (getData(position) instanceof String) {
            viewType = 1;
        } else {
            viewType = 3;
        }
        return viewType;
    }

    class StringHolder extends RecyclerViewHolder<String> {

        TextView tv;

        public StringHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent,
                    false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindView(String data) {
            tv.setText("我是String类型 - " + data);
        }
    }

    class IntegerHolder extends RecyclerViewHolder<Integer> {

        TextView tv;

        public IntegerHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent,
                    false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindView(Integer data) {
            tv.setText("我是Integer类型 - " + data);
        }
    }

    class VoidHolder extends RecyclerViewHolder<Void> {

        TextView tv;

        public VoidHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent,
                    false));
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void bindView(Void data) {
            tv.setText("我是Void类型 - " + data);
        }
    }
}
