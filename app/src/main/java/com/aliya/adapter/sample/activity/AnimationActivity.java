package com.aliya.adapter.sample.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.adapter.RecyclerAdapter;
import com.aliya.adapter.sample.R;
import com.aliya.adapter.sample.databinding.ActivityAnimationBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 演示 ListAdapter 导致的动画异常问题分析
 *
 * @author a_liYa
 * @date 2021/6/26 20:44.
 */
public class AnimationActivity extends AppCompatActivity {

    private Adapter adapter;

    private int count = 0;
    ActivityAnimationBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAnimationBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(count++);
                adapter.submitList(list);
            }
        });

        viewBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recycler.setAdapter(adapter = new Adapter());
        CustomItemAnimator itemAnimator = new CustomItemAnimator();
        itemAnimator.setAddDuration(2000);
        itemAnimator.setChangeDuration(2000);
        itemAnimator.setMoveDuration(2000);
        itemAnimator.setRemoveDuration(2000);
        viewBinding.recycler.setItemAnimator(itemAnimator);

        viewBinding.recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("TAG", "onLayoutChange: " + top + " - " + bottom);
            }
        });

    }


    private class Adapter extends ListAdapter<Integer, RecyclerView.ViewHolder> {

        public Adapter() {
            super(new DiffUtil.ItemCallback<Integer>() {
                @Override
                public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Integer oldItem,
                                                  @NonNull Integer newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new RecyclerAdapter.StaticViewHolder(parent, R.layout.item_view_type_0);
            }
            return new RecyclerAdapter.StaticViewHolder(parent, R.layout.item_view_type_1);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        }


        @Override
        public int getItemViewType(int position) {
            return getCurrentList().get(position) % 2;
        }
    }

}
