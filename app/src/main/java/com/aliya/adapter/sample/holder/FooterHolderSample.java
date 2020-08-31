package com.aliya.adapter.sample.holder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.adapter.page.PageItem;
import com.aliya.adapter.sample.R;

import androidx.annotation.NonNull;

/**
 * 页脚的实现 - 示例
 *
 * @author a_liYa
 * @date 2017/9/24 17:00.
 */
public class FooterHolderSample extends PageItem {

    TextView tv;

    public FooterHolderSample(@NonNull ViewGroup parent) {
        this(parent, null);
    }

    public FooterHolderSample(@NonNull ViewGroup parent, String title) {
        super(parent, R.layout.item_footer_sample_layout);
        tv = itemView.findViewById(R.id.tv);
        if (!TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

}
