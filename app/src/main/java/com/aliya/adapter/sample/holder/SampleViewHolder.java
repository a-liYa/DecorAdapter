package com.aliya.adapter.sample.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.RecyclerViewHolder;
import com.aliya.adapter.sample.R;

import androidx.annotation.NonNull;

/**
 * 简单示例 - ViewHolder
 *
 * @author a_liYa
 * @date 2017/9/23 15:42.
 */
public class SampleViewHolder extends RecyclerViewHolder {

    TextView mTextView;

    public SampleViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_demo_sample);
        mTextView = itemView.findViewById(R.id.tv);
    }

    @Override
    public void bindData(Object data) {
        mTextView.setText("item内容 - " + data);
    }

}
