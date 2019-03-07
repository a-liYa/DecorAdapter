package com.aliya.adapter.sample.holder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.sample.R;

/**
 * 简单示例 - ViewHolder
 *
 * @author a_liYa
 * @date 2017/9/23 15:42.
 */
public class SimpleViewHolder extends RecyclerViewHolder {

    TextView mTextView;

    public SimpleViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_demo_simple);
        mTextView = itemView.findViewById(R.id.tv);
    }

    @Override
    public void bindData(Object data) {
        mTextView.setText("item内容 - " + data);
    }

}
