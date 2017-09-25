package com.aliya.adapter.simple.holder;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.simple.R;

/**
 * 页眉的实现 - 示例
 *
 * @author a_liYa
 * @date 2017/9/24 17:00.
 */
public class HeaderHolderSimple extends PageItem {

    TextView tv;

    public HeaderHolderSimple(@NonNull ViewGroup parent) {
        this(parent, null);
    }

    public HeaderHolderSimple(@NonNull ViewGroup parent, String title) {
        super(parent, R.layout.item_header_simple_layout);
        tv = itemView.findViewById(R.id.tv);
        if (!TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

}
