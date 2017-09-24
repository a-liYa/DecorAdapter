package com.aliya.adapter.page;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * page item，为了规范header、footer使用，提供的通用父类
 *
 * @author a_liYa
 * @date 2017/8/25 13:12.
 */
public abstract class PageItem {

    public View itemView;

    public PageItem(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        this(inflate(layoutRes, parent, false));
    }

    public PageItem(@NonNull View itemView) {
        this.itemView = itemView;
    }

    public View getView() {
        return itemView;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    /**
     * Inflate a new view hierarchy from the specified xml resource
     *
     * @param resource     ID for an XML layout
     * @param parent       the parent of
     * @param attachToRoot .
     * @return The root View of the inflated hierarchy.
     * @see LayoutInflater#inflate(int, ViewGroup, boolean)
     */
    protected static View inflate(@LayoutRes int resource, ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, attachToRoot);
    }

}
