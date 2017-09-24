package com.aliya.adapter;

import android.support.annotation.CallSuper;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.click.OnItemLongClickListener;

/**
 * Adapter的装饰器
 *
 * @author a_liYa
 * @date 2017/8/23 19:37.
 */
public class DecorAdapter extends RecyclerView.Adapter implements CompatAdapter {

    private static final int VIEW_TYPE_HEADER = -20000; // [-20000, 0)
    private static final int VIEW_TYPE_FOOTER = -40000; // [-40001, -20000)
    private static final int VIEW_TYPE_PULL_REFRESH = VIEW_TYPE_HEADER;  // 下拉刷新 -> -20000
    private static final int VIEW_TYPE_EMPTY = VIEW_TYPE_FOOTER - 1;     // 空页面   -> -40001
    private static final int VIEW_TYPE_LOAD_MORE = VIEW_TYPE_HEADER - 1; // 加载更多 -> -20001
    private static final int KEY_TAG = R.id.tag_view_holder;
    public static final int DEFAULT_VIEW_TYPE = 0;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter adapter;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 点击 listener
     */
    private View.OnClickListener innerOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Object tag = v.getTag(KEY_TAG);
            if (tag instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) tag;
                if (mOnItemClickListener != null) {
                    int position = holder.getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return; // 说明已经是废弃的Item
                    }
                    mOnItemClickListener.onItemClick(holder.itemView, cleanPosition(position));
                }
            }
        }
    };

    /**
     * 长按 listener
     */
    private View.OnLongClickListener innerOnLongClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            Object tag = v.getTag(KEY_TAG);
            if (tag instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) tag;
                if (mOnItemLongClickListener != null) {
                    int position = holder.getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false; // 说明已经是废弃的Item
                    }
                    return mOnItemLongClickListener.onItemLongClick(
                            holder.itemView, cleanPosition(position));
                }
            }
            return false;
        }

    };

    /**
     * 留给子类实现, 实现DecorAdapter功能的另一种方案
     */
    protected DecorAdapter() {
    }

    public DecorAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (adapter == null) {
            throw new RuntimeException("adapter can't be null");
        }
    }

    public final RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    /**
     * 添加 header view
     * <p>
     * 注意：不能大量添加，否则会导致内存过大；且不能超过20000个， 否则会导致错乱
     *
     * @param view .
     */
    public final void addHeaderView(View view) {
        mHeaderViews.put(VIEW_TYPE_HEADER + 1 + mHeaderViews.size(), view);
    }

    /**
     * 添加 footer view
     * <p>
     * 注意：不能大量添加，否则会导致内存过大；且不能超过20000个，否则会导致错乱
     *
     * @param view .
     */
    public final void addFooterView(View view) {
        mFooterViews.put(VIEW_TYPE_FOOTER + mFooterViews.size(), view);
    }

    /**
     * 专门为下拉刷新提供的方法, 保证下拉刷新header永远在最第一个
     *
     * @param view .
     */
    public final void setHeaderRefresh(View view) {
        mHeaderViews.put(VIEW_TYPE_PULL_REFRESH, view);
    }

    /**
     * 专门为加载更多提供的方法，保证加载更多footer永远在最后一个
     *
     * @param view .
     */
    public final void setFooterLoadMore(View view) {
        mFooterViews.put(VIEW_TYPE_LOAD_MORE, view);
    }

    protected View emptyView;

    /**
     * Sets the view to show if the adapter item count is empty
     *
     * @param view .
     */
    public final void setEmptyView(View view) {
        emptyView = view;
    }

    @Override
    public final int getHeaderCount() {
        return mHeaderViews.size();
    }

    @Override
    public final int getFooterCount() {
        if (mFooterViews.get(VIEW_TYPE_EMPTY) != null
                && mFooterViews.get(VIEW_TYPE_LOAD_MORE) != null) {
            // 空页面与加载更多同时存在时，只取空页面
            return mFooterViews.size() - 1;
        }
        return mFooterViews.size();
    }

    private boolean isHeaderPosition(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterPosition(int position) {
        return position >= getItemCount() - getFooterCount() && position < getItemCount();
    }

    @Override
    public final boolean isInnerPosition(int position) {
        return isHeaderPosition(position) || isFooterPosition(position);
    }

    @Override
    public final int cleanPosition(int position) {
        return position - getHeaderCount();
    }

    // 默认为false
    @Override
    public boolean isOverlayViewType(int position) {
        return false;
    }

    // 需要悬浮效果时需要子类重写 Override
    @Override
    public OverlayViewHolder onCreateOverlayViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public final void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public final void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    @CallSuper
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new SimpleViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new SimpleViewHolder(mFooterViews.get(viewType));
        }
        if (adapter != null) {
            return adapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isInnerPosition(position)) return;
        onSetupItemClick(holder);
        if (adapter != null) {
            adapter.onBindViewHolder(holder, cleanPosition(position));
        }
    }

    /**
     * 处理设置Click事件
     *
     * @param holder ViewHolder
     */
    protected final void onSetupItemClick(ViewHolder holder) {
        if (holder != null && holder.itemView != null) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(innerOnClick);
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(innerOnLongClick);
            }
            holder.itemView.setTag(KEY_TAG, holder);
        }
    }

    @CallSuper
    @Override
    public int getItemCount() {
        if (isEmptyData()) { // 设置 empty view
            if (emptyView != null) {
                mFooterViews.put(VIEW_TYPE_EMPTY, emptyView);
            }
            return getHeaderCount() + getFooterCount();
        } else { // remove empty view
           mFooterViews.remove(VIEW_TYPE_EMPTY);
            if (adapter == null) {
                return getHeaderCount() + getFooterCount();
            }
            return getHeaderCount() + getFooterCount() + adapter.getItemCount();
        }
    }

    /**
     * 留给子类复写 提供是否为 empty
     *
     * @return true：empty
     */
    protected boolean isEmptyData() {
        return adapter == null || adapter.getItemCount() == 0;
    }

    @CallSuper
    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) { // 页眉
            return mHeaderViews.keyAt(position);
        } else if (isFooterPosition(position)) { // 页脚
            return mFooterViews.keyAt(getFooterCount() - (getItemCount() - position));
        }

        if (adapter != null) {
            return adapter.getItemViewType(cleanPosition(position));
        }
        return DEFAULT_VIEW_TYPE;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        if (adapter != null) {
            adapter.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public long getItemId(int position) {
        if (isHeaderPosition(position)) { // 页眉
            return mHeaderViews.keyAt(position);
        } else if (isFooterPosition(position)) { // 页脚
            return mFooterViews.keyAt(mFooterViews.size() - (getItemCount() - position));
        }
        if (adapter != null) {
            return adapter.getItemId(position);
        }
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (adapter != null) {
            adapter.onViewRecycled(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (adapter != null) {
            return adapter.onFailedToRecycleView(holder);
        }
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (adapter != null) {
            adapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (adapter != null) {
            adapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    // 解决Grid布局时添加Header、footer合并一行
    @CallSuper
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (adapter != null) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(
                    new WrapSpanSizeLookup(
                            gridManager.getSpanCount(), gridManager.getSpanSizeLookup()));
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (adapter != null) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private class WrapSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        int spanCount;

        GridLayoutManager.SpanSizeLookup spanSizeLookup;

        public WrapSpanSizeLookup(int spanCount, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            this.spanCount = spanCount;
            this.spanSizeLookup = spanSizeLookup;
        }

        @Override
        public int getSpanSize(int position) {
            if (isInnerPosition(position)) {
                return spanCount;
            }
            if (spanSizeLookup != null) {
                return spanSizeLookup.getSpanSize(cleanPosition(position));
            }
            return 1;
        }
    }

    private static final class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

}
