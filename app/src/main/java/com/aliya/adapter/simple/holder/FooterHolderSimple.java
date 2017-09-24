package com.aliya.adapter.simple.holder;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.simple.R;

/**
 * 页脚的实现 - 示例
 *
 * @author a_liYa
 * @date 2017/9/24 17:00.
 */
public class FooterHolderSimple extends PageItem {

    TextView tv;

    public FooterHolderSimple(@NonNull ViewGroup parent) {
        this(parent, null);
    }

    public FooterHolderSimple(@NonNull ViewGroup parent, String title) {
        super(inflate(R.layout.item_footer_simple_layout, parent, false));
        tv = itemView.findViewById(R.id.tv);
        if (!TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

}
