package com.aliya.adapter;

import android.support.annotation.CallSuper;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.adapter.click.ItemClickCallback;
import com.aliya.adapter.click.ItemLongClickCallback;
import com.aliya.adapter.click.OnItemClickListener;
import com.aliya.adapter.click.OnItemLongClickListener;
import com.aliya.adapter.page.PageItem;

/**
 * {@link RecyclerView.Adapter}的装饰者
 * <p>
 * 实现功能：
 * 1. 设置Header {@link #addHeader(PageItem)}
 * 2. 设置Footer {@link #addFooter(PageItem)}
 * 3. 设置OnItemClick     {@link #setOnItemClickListener(OnItemClickListener)}
 * 4. 设置OnItemLongClick {@link #setOnItemLongClickListener(OnItemLongClickListener)}
 * 5. 设置HeaderRefresh   {@link #setHeaderRefresh(PageItem)}
 * 6. 设置FooterLoadMore  {@link #setFooterLoadMore(PageItem)}
 * 7. 设置EmptyView       {@link #setEmpty(PageItem)}
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
    private static final int KEY_TAG = R.id.tag_holder;
    public static final int DEFAULT_VIEW_TYPE = 0;

    private SparseArrayCompat<PageItem> mHeaders = new SparseArrayCompat<>();
    private SparseArrayCompat<PageItem> mFooters = new SparseArrayCompat<>();

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
                if (mOnItemClickListener != null || holder instanceof ItemClickCallback) {
                    int position = holder.getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return; // 说明已经是废弃的Item
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(holder.itemView, cleanPosition(position));
                    }
                    if (holder instanceof ItemClickCallback) {
                        ((ItemClickCallback) holder)
                                .onItemClick(holder.itemView, cleanPosition(position));
                    }
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
                if (mOnItemLongClickListener != null || holder instanceof ItemLongClickCallback) {
                    int position = holder.getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false; // 说明已经是废弃的Item
                    }
                    boolean longClick = false;
                    if (mOnItemLongClickListener != null) {
                        longClick = longClick | mOnItemLongClickListener
                                .onItemLongClick(holder.itemView, cleanPosition(position));
                    }
                    if (holder instanceof ItemLongClickCallback) {
                        longClick = longClick | ((ItemLongClickCallback) holder)
                                .onItemLongClick(holder.itemView, cleanPosition(position));
                    }
                    return longClick;
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
     *
     * @param view item view
     * @see #addHeader(PageItem)
     */
    public final void addHeaderView(View view) {
        addHeader(new PageItem(view));
    }

    /**
     * 添加 header PageItem
     * <p>
     * 注意：不能大量添加，否则会导致内存过大；且不能超过20000个， 否则会导致错乱
     *
     * @param page page item
     */
    public final void addHeader(PageItem page) {
        mHeaders.put(VIEW_TYPE_HEADER + 1 + mHeaders.size(), page);
    }

    /**
     * 添加 footer view
     *
     * @param view item view
     * @see #addFooter(PageItem)
     */
    public final void addFooterView(View view) {
        addFooter(new PageItem(view));
    }

    /**
     * 添加 footer PageItem
     * <p>
     * 注意：不能大量添加，否则会导致内存过大；且不能超过20000个，否则会导致错乱
     *
     * @param page page item
     */
    public final void addFooter(PageItem page) {
        mFooters.put(VIEW_TYPE_FOOTER + mFooters.size(), page);
    }

    /**
     * 专门为下拉刷新提供的方法, 保证下拉刷新header永远在第一个
     *
     * @param view item view
     * @see #setHeaderRefresh(PageItem)
     */
    public final void setHeaderRefresh(View view) {
        setHeaderRefresh(new PageItem(view));
    }

    /**
     * 专门为下拉刷新提供的方法, 保证下拉刷新header永远在第一个
     *
     * @param page page item
     */
    public final void setHeaderRefresh(PageItem page) {
        mHeaders.put(VIEW_TYPE_PULL_REFRESH, page);
    }

    /**
     * 专门为加载更多提供的方法，保证加载更多footer永远在最后一个
     *
     * @param view item view
     * @see #setFooterLoadMore(PageItem)
     */
    public final void setFooterLoadMore(View view) {
        setFooterLoadMore(new PageItem(view));
    }

    /**
     * 专门为加载更多提供的方法，保证加载更多footer永远在最后一个
     *
     * @param page page item
     */
    public final void setFooterLoadMore(PageItem page) {
        mFooters.put(VIEW_TYPE_LOAD_MORE, page);
    }

    protected PageItem emptyView;

    /**
     * Sets the view to show if the adapter item count is empty
     *
     * @param view item view
     * @see #setEmpty(PageItem)
     */
    public final void setEmptyView(View view) {
        setEmpty(new PageItem(view));
    }

    /**
     * Sets the view to show if the adapter item count is empty
     *
     * @param page page item
     */
    public final void setEmpty(PageItem page) {
        emptyView = page;
    }

    @Override
    public final int getHeaderCount() {
        return mHeaders.size();
    }

    @Override
    public final int getFooterCount() {
        if (mFooters.get(VIEW_TYPE_EMPTY) != null
                && mFooters.get(VIEW_TYPE_LOAD_MORE) != null) {
            // 空页面与加载更多同时存在时，只取空页面
            return mFooters.size() - 1;
        }
        return mFooters.size();
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
        if (mHeaders.get(viewType) != null) {
            return new SimpleViewHolder(mHeaders.get(viewType).onCreateView(parent));
        } else if (mFooters.get(viewType) != null) {
            return new SimpleViewHolder(mFooters.get(viewType).onCreateView(parent));
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
            if (mOnItemClickListener != null || holder instanceof ItemClickCallback) {
                holder.itemView.setOnClickListener(innerOnClick);
            }
            if (mOnItemLongClickListener != null || holder instanceof ItemLongClickCallback) {
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
                mFooters.put(VIEW_TYPE_EMPTY, emptyView);
            }
            return getHeaderCount() + getFooterCount();
        } else { // remove empty view
            mFooters.remove(VIEW_TYPE_EMPTY);
            if (adapter == null) {
                return getHeaderCount() + getFooterCount();
            }
            return getHeaderCount() + getFooterCount() + adapter.getItemCount();
        }
    }

    /**
     * 留给子类复写 提供数据是否为empty
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
            return mHeaders.keyAt(position);
        } else if (isFooterPosition(position)) { // 页脚
            return mFooters.keyAt(getFooterCount() - (getItemCount() - position));
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
            return mHeaders.keyAt(position);
        } else if (isFooterPosition(position)) { // 页脚
            return mFooters.keyAt(mFooters.size() - (getItemCount() - position));
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

    /**
     * 供内部 Header、Footer使用的ViewHolder
     *
     * @author a_liYa
     * @date 2017/9/24 下午8:21.
     */
    private static final class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

}
