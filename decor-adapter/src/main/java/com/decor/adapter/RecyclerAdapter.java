package com.decor.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.decor.adapter.click.ItemClickCallback;
import com.decor.adapter.click.ItemLongClickCallback;
import com.decor.adapter.click.OnItemClickListener;
import com.decor.adapter.click.OnItemLongClickListener;

import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * {@link RecyclerView.Adapter}的装饰者
 * <p>
 * 实现功能：
 * 1. 设置OnItemClick     {@link #setOnItemClickListener(OnItemClickListener)}
 * 2. 设置OnItemLongClick {@link #setOnItemLongClickListener(OnItemLongClickListener)}
 *
 * @author a_liYa
 * @date 2017/8/23 19:37.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter {

    private static final int KEY_TAG = R.id.tag_holder;

    protected List<T> mList;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * Click listener
     */
    private View.OnClickListener innerOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Object tag = v.getTag(KEY_TAG);
            if (tag instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) tag;
                if (mOnItemClickListener != null || holder instanceof ItemClickCallback) {
                    int position = holder.getBindingAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return; // 无效的 position
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                    if (holder instanceof ItemClickCallback) {
                        ((ItemClickCallback) holder)
                                .onItemClick(holder.itemView, position);
                    }
                }
            }
        }
    };

    /**
     * LongClick listener
     */
    private View.OnLongClickListener innerOnLongClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            Object tag = v.getTag(KEY_TAG);
            if (tag instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) tag;
                if (mOnItemLongClickListener != null || holder instanceof ItemLongClickCallback) {
                    int position = holder.getBindingAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false; // 无效的 position
                    }
                    boolean longClick = false;
                    if (mOnItemLongClickListener != null) {
                        longClick = mOnItemLongClickListener
                                .onItemLongClick(holder.itemView, position);
                    }
                    if (holder instanceof ItemLongClickCallback) {
                        longClick = longClick | ((ItemLongClickCallback) holder)
                                .onItemLongClick(holder.itemView, position);
                    }
                    return longClick;
                }
            }
            return false;
        }

    };

    public RecyclerAdapter(List<T> list) {
        mList = list;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        onSetupItemClick(holder);
        if (holder instanceof ViewHolderBinding) {
            ((ViewHolderBinding) holder).bindData(getItem(position));
        }
    }


    /**
     * 处理设置Click事件
     *
     * @param holder ViewHolder
     */
    private void onSetupItemClick(ViewHolder holder) {
        if (mOnItemClickListener != null || holder instanceof ItemClickCallback) {
            holder.itemView.setOnClickListener(innerOnClick);
        }
        if (mOnItemLongClickListener != null || holder instanceof ItemLongClickCallback) {
            holder.itemView.setOnLongClickListener(innerOnLongClick);
        }
        holder.itemView.setTag(KEY_TAG, holder);
    }

    @CallSuper
    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public final T getItem(int position) {
        if (mList != null && position < mList.size() && position >= 0) {
            return mList.get(position);
        }
        return null;
    }

    public final List<T> getList() {
        return mList;
    }

    /**
     * 静态的 ViewHolder 布局不需要 bind data，也不需要 click 事件
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
        protected void onBindData(Object data) {
            itemView.setClickable(false); // 设置控件点击无效，若 itemView 设置了 longClick，则 itemView 仍然响应 click 和 longClick
        }

    }

}
