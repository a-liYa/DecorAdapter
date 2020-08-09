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
public class PageItem {

    public View itemView;
    private int layoutRes;

    public PageItem(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        this(inflate(layoutRes, parent, false));
    }

    public PageItem(@NonNull View itemView) {
        this.itemView = itemView;
        onViewCreated(itemView);
    }

    /**
     * 此构造方法实现懒加载模式，加载时机{@link com.aliya.adapter.DecorAdapter#onCreateViewHolder(ViewGroup, int)}，
     * 通过 {@link #onCreateView(ViewGroup)} 加载布局
     *
     * @param layoutRes a layout resource reference
     */
    public PageItem(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public final View onCreateView(ViewGroup parent) {
        if (itemView == null) {
            itemView = inflate(layoutRes, parent, false);
            onViewCreated(itemView);
        } else if (itemView.getParent() instanceof ViewGroup) {
            /**
             * @see android.support.v7.widget.RecyclerView.Adapter#createViewHolder(ViewGroup, int)
             * <code>
             *    if (holder.itemView.getParent() != null) {
             *        throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
             *    }
             * </code>
             */
            ((ViewGroup) itemView.getParent()).removeView(itemView);
        }
        return itemView;
    }

    public void onViewCreated(View itemView) {}

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
