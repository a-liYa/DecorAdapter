package com.decor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.ViewHolder}的拓展, 结合{@link RecyclerAdapter}使用
 *
 * @param <T> 数据的泛型
 * @author a_liYa
 * @date 16/10/19 09:52.
 */
public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder implements ViewHolderBinding<T>{

    public T mData;

    public RecyclerViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        this(inflate(layoutRes, parent, false));
    }

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public T getData() {
        return mData;
    }

    /**
     * bind data to view
     *
     * @param data 数据
     */
    @Override
    public final void bindData(T data) {
        this.mData = data;
        onBindData(data);
    }

    protected abstract void onBindData(T data);


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
