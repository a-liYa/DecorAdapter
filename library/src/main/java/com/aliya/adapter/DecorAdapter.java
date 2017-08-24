package com.aliya.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import static android.support.v7.widget.RecyclerView.NO_ID;

/**
 * Adapter的装饰器
 *
 * @author a_liYa
 * @date 2017/8/23 19:37.
 */
public class DecorAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HEADER = -20000;
    private static final int VIEW_TYPE_FOOTER = -40000;
    private static final int KEY_TAG = R.id.tag_view_holder;
    private static final int DEFAULT_VIEW_TYPE = 0;

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
                    mOnItemClickListener.onItemClick(holder.itemView, position - getHeaderCount());
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
                    int position = holder.getLayoutPosition() - getHeaderCount();
                    if (position == RecyclerView.NO_POSITION) {
                        return false; // 说明已经是废弃的Item
                    }
                    return mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                }
            }
            return false;
        }

    };

    /**
     * 留给子类实现的另外一个方案
     */
    protected DecorAdapter() {
    }

    public DecorAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (adapter == null) {
            throw new RuntimeException("adapter can't be null");
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    /**
     * 添加 header view
     * <p>
     * 注意：不能大量添加，会导致内存过大；且不能超过20000个， 会导致错乱
     *
     * @param view .
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(VIEW_TYPE_HEADER + 1 + mHeaderViews.size(), view);
    }

    /**
     * 添加 footer view
     * <p>
     * 注意：不能大量添加，会导致内存过大；且不能超过20000个，会导致错乱
     *
     * @param view .
     */
    public void addFooterView(View view) {
        mFooterViews.put(VIEW_TYPE_FOOTER + mFooterViews.size(), view);
    }

    public void setHeaderRefresh(View view) {
        mHeaderViews.put(VIEW_TYPE_HEADER, view);
    }

    public void setFooterLoadMore(View view) {
        mFooterViews.put(VIEW_TYPE_FOOTER + (VIEW_TYPE_HEADER - VIEW_TYPE_FOOTER - 1), view);
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    private boolean isHeaderPosition(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterPosition(int position) {
        return position >= getItemCount() - getFooterCount() && position < getItemCount();
    }

    public boolean isInnerPosition(int position) {
        return isHeaderPosition(position) || isFooterPosition(position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isInnerPosition(position)) return;
        onSetupItemClick(holder);
        if (adapter != null) {
            adapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    /**
     * 处理设置Click事件
     *
     * @param holder ViewHolder
     */
    protected void onSetupItemClick(ViewHolder holder) {
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

    @Override
    public int getItemCount() {
        if (adapter != null) {
            return getHeaderCount() + getFooterCount() + adapter.getItemCount();
        }
        return getHeaderCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) { // 页眉
            return mHeaderViews.keyAt(position);
        } else if (isFooterPosition(position)) { // 页脚
            return mFooterViews.keyAt(mFooterViews.size() - (getItemCount() - position));
        }

        if (adapter != null) {
            return adapter.getItemViewType(position - getHeaderCount());
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
        return NO_ID;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (adapter != null) {
            adapter.onViewRecycled(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (adapter != null) {
            return adapter.onFailedToRecycleView(holder);
        }
        return false;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (adapter != null) {
            adapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (adapter != null) {
            adapter.onViewDetachedFromWindow(holder);
        }

    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    // 解决Grid布局时添加Header、footer合并一行
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
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
        if (adapter != null) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private class WrapSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

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
                return spanSizeLookup.getSpanSize(position - getFooterCount());
            }
            return 1;
        }
    }

    private static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

}
