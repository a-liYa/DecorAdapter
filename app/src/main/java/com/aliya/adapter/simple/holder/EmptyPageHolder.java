package com.aliya.adapter.simple.holder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.simple.R;

/**
 * 空页面 - 封装
 *
 * @author a_liYa
 * @date 2017/9/24 16:37.
 */
public class EmptyPageHolder extends PageItem {

    public EmptyPageHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_empty_simple);
    }

}
