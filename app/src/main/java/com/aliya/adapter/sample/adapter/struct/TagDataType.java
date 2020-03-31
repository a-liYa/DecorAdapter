package com.aliya.adapter.sample.adapter.struct;

/**
 * 辅助 {@link com.aliya.adapter.RecyclerAdapter} 构建 {@link com.aliya.adapter.RecyclerViewHolder}
 *
 * @author a_liYa
 * @date 2019/4/19 09:33.
 */
public class TagDataType<Data, Tag> extends DataType<Data> {

    private Tag tag;

    public TagDataType(Data data, int viewType, Tag tag) {
        super(data, viewType);
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
