//package com.aliya.adapter;
//
//import android.support.v4.util.SparseArrayCompat;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * RecyclerView适配器的简单基类封装
// *
// * @param <T>  Item数据类型
// * @author a_liYa
// * @date 2016-3-9 下午7:49:18
// */
//public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
//
//    private static final int ITEM_VIEW_TYPE_EMPTY_PAGE = -1; // 空页面Type
//
//    private static final int ITEM_VIEW_TYPE_HEADER = -10000;
//    private static final int ITEM_VIEW_TYPE_FOOTER = -20000;
//
//    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
//    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
//
//    public List<T> mDatas;
//
//    /**
//     * 保存所有Holder.itemView
//     */
//    private Set<View> mItemViews = new HashSet<>();
//
//    /**
//     * 空页面
//     */
////    private EmptyPageHolder mEmptyPageHolder;
////    protected EmptyPageInfo mEmptyInfo;
//
//    private OnItemClickListener<T> mOnItemClickListener;
//    private OnItemLongClickListener<T> mOnItemLongClickListener;
//
//    public BaseRecyclerAdapter(List<T> datas) {
//        this.mDatas = datas;
////        registerAdapterDataObserver(mAdapterDataObserver);
//    }
//
//    public Set<View> getItemViews() {
//        return mItemViews;
//    }
//
//    public SparseArrayCompat<View> getHeaderViews() {
//        return mHeaderViews;
//    }
//
//    public SparseArrayCompat<View> getFooterViews() {
//        return mFooterViews;
//    }
//
//    /**
//     * 获取数据集合
//     *
//     * @return 数据列表 默认会自动刷新
//     */
//    public List<T> getDatas() {
//        return mDatas;
//    }
//
////    public void setDatas(List<T> datas) {
////        setDatas(datas, false);
////    }
//
//    /**
//     * 返回数据数量
////     */
//    public int getDataSize() {
//        return mDatas == null ? 0 : mDatas.size();
//    }
//
//    @Override
//    public int getItemCount() { // 设置了空页面且元素个数为0时  返回1
//        return ((getDataSize() == 0 && mEmptyInfo != null)
//                ? 1
//                : getDataSize()) + getHeaderCount() + getFooterCount();
//    }
//
//    public void addHeaderView(View view) {
//        mHeaderViews.put(ITEM_VIEW_TYPE_HEADER - mHeaderViews.size(), view);
//    }
//
//    public void addFooterView(View view) {
//        mFooterViews.put(ITEM_VIEW_TYPE_FOOTER - mFooterViews.size(), view);
//    }
//
//    public int getHeaderCount() {
//        return mHeaderViews.size();
//    }
//
//    public int getFooterCount() {
//        return mFooterViews.size();
//    }
//
//    public boolean isHeaderViewPos(int position) {
//        return position < getHeaderCount();
//    }
//
//    public boolean isFooterViewPos(int position) {
//        return position >= getItemCount() - getFooterCount() && position < getItemCount();
//    }
//
//    @Override
//    public final void onBindViewHolder(ViewHolder holder, int position) {
//        if (isHeaderViewPos(position) || isFooterViewPos(position)) return;
//
//        if (onInnerBindViewHolder(holder, position)) return;
//
//
//        setupItemEvent(holder);  // 初始化条目的点击事件
//        if (!onAbsBindViewHolder(holder, position)) {
//            ((BaseRecyclerViewHolder) holder).setData(mDatas.get(position - getHeaderCount()));
//        }
//
//    }
//
//    /**
//     * 内部onBindViewHolder, 供子类拦截使用
//     */
//    protected boolean onInnerBindViewHolder(ViewHolder holder, int position) {
//        return false;
//    }
//
//    @Override
//    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mHeaderViews.get(viewType) != null) {
//            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
//            };
//
//        } else if (mFooterViews.get(viewType) != null) {
//            return new RecyclerView.ViewHolder(mFooterViews.get(viewType)) {
//            };
//        }
//        RecyclerView.ViewHolder holder = onInnerCreateViewHolder(parent, viewType);
//        if (holder != null)
//            return holder;
//
//        return onAbsCreateViewHolder(parent, viewType);
//    }
//
//    /**
//     * 内部onCreateViewHolder, 供子类拦截使用
//     */
//    protected ViewHolder onInnerCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public final int getItemViewType(int position) {
//        if (isHeaderViewPos(position)) { // 页眉
//            return mHeaderViews.keyAt(position);
//        } else if (isFooterViewPos(position)) { // 页脚
//            return mFooterViews.keyAt(mFooterViews.size() - (getItemCount() - position));
//        } else if (getDataSize() == 0
//                && mEmptyInfo != null
//                && position >= getHeaderCount()
//                && position < getItemCount() - getFooterCount()) { // 空页面
//            return ITEM_VIEW_TYPE_EMPTY_PAGE;
//        }
//        int innerItemViewType = getInnerItemViewType(position);
//        if (0 != innerItemViewType) {
//            return innerItemViewType;
//        }
//        return getAbsItemViewType(position - getHeaderCount());
//    }
//
//    /**
//     * 内部getItemViewType, 供子类拦截使用
//     *
//     * @param position 全部位置的索引
//     * @return int类型
//     */
//    protected int getInnerItemViewType(int position) {
//        return 0;
//    }
//
//    /**
//     * 自定义重写getItemViewType
//     *
//     * @param position 屏蔽了内部其他内部item转换过的索引
//     * @return int类型
//     */
//    public int getAbsItemViewType(int position) {
//        return 0;
//    }
//
//    /**
//     * 自定义重写onBindViewHolder(ViewHolder holder, int position)
//     *
//     * @param holder   ViewHolder
//     * @param position 当前绑定的下标
//     * @return true 表示拦截的自动bindView方法 需要自己处理
//     */
//    public <VH extends BaseRecyclerViewHolder<? extends T>> boolean onAbsBindViewHolder(VH holder, int position) {
//        return false;
//    }
//
//    /**
//     * 自定义重写onCreateViewHolder(ViewGroup parent, int viewType)
//     *
//     * @param parent   ViewGroup
//     * @param viewType {@see getItemViewType(int position)}
//     * @return
//     */
//    public abstract <VH extends BaseRecyclerViewHolder<? extends T>> VH onAbsCreateViewHolder(ViewGroup parent, int viewType);
//
//
//    /**
//     * 设置条目的点击事件(点按/长按)
//     *
//     * @param holder ViewHolder
//     */
//    protected void setupItemEvent(RecyclerView.ViewHolder holder) {
//        holder.itemView.setClickable(true);
//        if (mOnItemClickListener != null) {
//            // 1. onItemClick
////            holder.itemView.setOnClickListener(mInnerOnClickListener);
//        }
//        if (mOnItemLongClickListener != null) {
//            // 2. onItemLongClick
////            holder.itemView.setOnLongClickListener(mInnerOnLongClickListener);
//        }
//        mItemViews.add(holder.itemView);
////        holder.itemView.setTag(R.id.tag_holder, holder);
//    }
//
//    /**
//     * 增加一条数据
//     *
//     * @param position 增加数据的位置（下标）
//     * @param t        要增加的数据
//     */
//    public void addData(int position, T t) {
//        mDatas.add(position, t);
//
//        notifyItemInserted(position); // 注意: 不是调用notifyDataSetChanged();
//    }
//
//    /**
//     * 获取指定数据
//     *
//     * @param index 下标
//     * @return
//     */
//    public T getData(int index) {
//        if (mDatas != null && index < mDatas.size() && index >= 0) {
//            return mDatas.get(index);
//        }
//        return null;
//    }
//
//    /**
//     * 增加多个数据
//     *
//     * @param t 要增加的数据
//     */
//    public void addData(List<T> t) {
//        addData(t, false);
//    }
//
//    /**
//     * 增加多个数据
//     *
//     * @param t                要增加的数据
//     * @param isAutoPartNotify 是否自动局部刷新 true ： 自动刷新
//     */
//    public void addData(List<T> t, boolean isAutoPartNotify) {
//        if (t == null || t.isEmpty()) {
//            return;
//        }
//        int positionStart = getItemCount() - getFooterCount();
//        if (mDatas == null) {
//            mDatas = t;
//        } else {
//            mDatas.addAll(t);
//        }
//        if (isAutoPartNotify)
//            notifyItemRangeInserted(positionStart, t.size());
//    }
//
////    public OnItemClickListener<T> getOnItemClickListener() {
////        return mOnItemClickListener;
////    }
//
////    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
////        mOnItemClickListener = onItemClickListener;
////        if (mOnItemClickListener != null)
////            for (View itemView : mItemViews) {
////                itemView.setOnClickListener(mInnerOnClickListener);
////            }
////    }
////
////    public OnItemLongClickListener<T> getOnItemLongClickListener() {
////        return mOnItemLongClickListener;
////    }
////
////    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
////        mOnItemLongClickListener = onItemLongClickListener;
////        if (mOnItemLongClickListener != null)
////            for (View itemView : mItemViews) {
////                itemView.setOnLongClickListener(mInnerOnLongClickListener);
////            }
////    }
//
////    /**
////     * 点击监听
////     */
////    private View.OnClickListener mInnerOnClickListener = new View.OnClickListener() {
////
////        @Override
////        public void onClick(View v) {
////            if (v.getTag(R.id.tag_holder) instanceof ViewHolder) {
////                ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_holder);
////                if (mOnItemClickListener != null) {
////                    int layoutPosition = holder.getLayoutPosition();
////                    if (layoutPosition == RecyclerView.NO_POSITION) return; // 说明已经是废弃的Item
////
////                    int position = layoutPosition - getHeaderCount();
////                    mOnItemClickListener.onItemClick(holder.itemView, position,
////                            mDatas.get(position % mDatas.size()));
////                }
////            }
////        }
////    };
////
////    /**
////     * 长按点击监听
////     */
////    private View.OnLongClickListener mInnerOnLongClickListener = new View.OnLongClickListener() {
////
////        @Override
////        public boolean onLongClick(View v) {
////            if (v.getTag(R.id.tag_holder) instanceof ViewHolder) {
////                ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_holder);
////                if (mOnItemLongClickListener != null) {
////                    int position = holder.getLayoutPosition() - getHeaderCount();
////                    return mOnItemLongClickListener.onItemLongClick(holder.itemView, position,
////                            mDatas.get(position % mDatas.size()));
////                }
////            }
////            return false;
////        }
////    };
//
//    /**
//     * 获取空页面RootView
//     *
//     * @return View
//     */
////    public View getEmptyPageView() {
////        if (mEmptyPageHolder == null) return null;
////
////        return mEmptyPageHolder.itemView;
////    }
//
//}
