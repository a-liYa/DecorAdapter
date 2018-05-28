package com.aliya.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Recycler.Adapter的封装, extends {@link DecorAdapter}
 * <p>
 * 1. 内部集成数据集合 {@link #datas}
 * 2. 封装 {@link #datas} 系列的操作
 * 3. 结合 {@link RecyclerViewHolder} 使用更简单
 *
 * @param <T> item 数据类型
 * @author a_liYa
 * @date 2017/8/24 15:42.
 */
public abstract class RecyclerAdapter<T> extends DecorAdapter {

    protected List<T> datas;

    public RecyclerAdapter(List<T> data) {
        this.datas = data;
    }

    @CallSuper
    @Override
    public int getItemCount() {
        if (isEmptyData()) {
            return super.getItemCount();
        }
        return super.getItemCount() + datas.size();
    }

    @Override
    protected boolean isEmptyData() {
        return datas == null || datas.isEmpty();
    }

    public final T getData(int index) {
        if (datas != null && index < datas.size() && index >= 0) {
            return datas.get(index);
        }
        return null;
    }

    public final List<T> getData() {
        return datas;
    }

    public int getDataSize() {
        return datas == null ? 0 : datas.size();
    }

    public void setData(List<T> data) {
        setData(data, false);
    }

    /**
     * 设置数据
     *
     * @param data       数据集合
     * @param autoNotify 自动刷新 true:自动
     */
    public void setData(List<T> data, boolean autoNotify) {
        datas = data;
        if (autoNotify) {
            notifyDataSetChanged();
        }
    }

    /**
     * 追加数据集合
     *
     * @param data       数据集
     * @param autoNotify 是否自动局部刷新 true ： 自动刷新
     * @return false:数据为空追加失败； true:追加成功
     */
    public boolean addData(List<T> data, boolean autoNotify) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        int positionStart = getItemCount() - getFooterCount();
        boolean notifyAll = false; // 是否全部刷新
        if (datas == null || datas.isEmpty()) {
            datas = data;
            notifyAll = emptyView != null; // datas 为空，且有空页面时，全部刷新
        } else {
            datas.addAll(data);
        }
        if (autoNotify) {
            if (notifyAll) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeInserted(positionStart, data.size());
            }
        }
        return true;
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (isInnerPosition(position)) return; // super.onBindViewHolder已经处理
        if (!onAbsBindViewHolder(holder, cleanPosition(position))) { // 没有拦截
            ((RecyclerViewHolder<T>) holder).setData(getData(cleanPosition(position)));
        }
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            holder = onAbsCreateViewHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public final int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        if (viewType == DEFAULT_VIEW_TYPE) {
            viewType = getAbsItemViewType(cleanPosition(position));
        }
        return viewType;
    }

    /**
     * 自定义重写getItemViewType
     *
     * @param position 屏蔽了内部其他内部item转换过的索引
     * @return int类型
     */
    public int getAbsItemViewType(int position) {
        return DEFAULT_VIEW_TYPE;
    }

    /**
     * see onBindViewHolder
     *
     * @param holder   ViewHolder
     * @param position 当前绑定的下标
     * @return true 表示拦截的自动bindView方法 需要自己处理, 默认false
     * @see RecyclerView.Adapter#onBindViewHolder(ViewHolder, int)
     */
    public boolean onAbsBindViewHolder(ViewHolder holder, int position) {
        return false;
    }

    /**
     * see onCreateViewHolder
     *
     * @param parent   .
     * @param viewType .
     * @return .
     * @see RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    public abstract RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 静态的 ViewHolder 对应布局没有交互，没有点击，一切效果来源于原始xml布局
     *
     * @author a_liYa
     * @date 2017/9/13 下午4:27.
     */
    public static class StaticViewHolder extends RecyclerViewHolder {

        public StaticViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
            super(parent, layoutRes);
        }

        public StaticViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Object data) {
            itemView.setClickable(false); // 设置条目不可点击
        }

    }

}
