package com.aliya.adapter.divider;

import android.content.Context;

/**
 * StaggeredBuilder
 *
 * @author a_liYa
 * @date 2020/9/7 23:21.
 */
public class StaggeredBuilder extends DecorationBuilder {

    public StaggeredBuilder(Context context) {
        super(context);
    }

    /**
     * item 间隔
     *
     * @param space 单位：dp
     * @return this
     */
    public StaggeredBuilder setSpace(float space) {
        this.space = dp2px(space);
        return this;
    }

    public StaggeredBuilder setIncludeEdge(boolean includeEdge) {
        this.includeEdge = includeEdge;
        return this;
    }

    public StaggeredDecoration build() {
        return new StaggeredDecoration(this);
    }

}
