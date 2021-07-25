package com.aliya.adapter.sample.v2;

import android.util.Log;
import android.view.ViewGroup;

import com.aliya.adapter.sample.R;
import com.decor.adapter.RecyclerViewHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * EmptyAdapter
 *
 * @author a_liYa
 * @date 2021/7/25 22:24.
 */
class EmptyAdapter extends RecyclerView.Adapter {

    private ConcatAdapter mConcatAdapter;

    public EmptyAdapter(ConcatAdapter concatAdapter, final RecyclerView.Adapter adapter) {
        mConcatAdapter = concatAdapter;
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            private int itemCount = adapter.getItemCount();
            { // 首次判断为0,添加
                if (itemCount == 0) {
                    addEmptyAdapter();
                }
            }

            @Override
            public void onChanged() {
                checkEmptyStatus();
            }


            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart == 0) {
                    checkEmptyStatus();
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if (positionStart == 0) {
                    checkEmptyStatus();
                }
            }

            private void checkEmptyStatus() {
                int newItemCount = adapter.getItemCount();
                if (this.itemCount == 0 && newItemCount > 0) {
                    removeEmptyAdapter();
                } else if (this.itemCount > 0 && newItemCount == 0) {
                    addEmptyAdapter();
                }
                itemCount = newItemCount;
            }

        });
    }

    private void addEmptyAdapter() {
        Log.e("TAG", "addEmptyAdapter: ");
        mConcatAdapter.addAdapter(this);

    }

    private void removeEmptyAdapter() {
        mConcatAdapter.removeAdapter(this);
        Log.e("TAG", "removeEmptyAdapter: ");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class EmptyViewHolder extends RecyclerViewHolder {


        public EmptyViewHolder(@NonNull ViewGroup parent) {
            super(parent, R.layout.item_empty_sample);
        }

        @Override
        protected void onBindData(Object data) {
        }

    }
}
