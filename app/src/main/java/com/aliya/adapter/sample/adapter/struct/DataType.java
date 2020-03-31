package com.aliya.adapter.sample.adapter.struct;

/**
 * 辅助 {@link com.aliya.adapter.RecyclerAdapter} 构建 {@link com.aliya.adapter.RecyclerViewHolder}
 *
 * @author a_liYa
 * @date 2019/4/17 10:23.
 */
public class DataType<Data> {

    private Data data;
    private int viewType;

    public DataType(Data data, int viewType) {
        this.data = data;
        this.viewType = viewType;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
