package com.aliya.adapter.sample.holder;

import android.view.ViewGroup;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.sample.R;

import androidx.annotation.NonNull;

/**
 * 空页面 - 封装
 *
 * @author a_liYa
 * @date 2017/9/24 16:37.
 */
public class EmptyPageHolder extends PageItem {

    public EmptyPageHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_empty_sample);
    }

}
