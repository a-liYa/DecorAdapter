# DecorAdapter

```
RecyclerView.Adapter的装饰器
```

核心功能：  

1. addHeaderView  
2. addFooterView
3. setHeaderRefresh
4. setFooterLoadMore
5. setEmptyView
6. setOnItemClickListener
7. setOnItemLongClickListener


其他功能：  

1. RecyclerViewHolder - ViewHolder 
2. OverlayViewHolder - 悬浮吸顶 ViewHolder
3. ListItemDecoration - List类型分割线  
4. GridItemDecoration - Grid类型分割线


Api声明：

```java
    
    /**
     * 添加 header PageItem
     *
     * @param page page item
     */
    public final void addHeaderView(PageItem page);
    
    /**
     * 添加 footer PageItem
     *
     * @param page page item
     */
    public final void addFooterView(PageItem page);
    
    /**
     * 专门为下拉刷新提供的方法, 保证下拉刷新header永远在第一个
     *
     * @param page page item
     */
    public final void setHeaderRefresh(PageItem page);
    
    /**
     * 专门为加载更多提供的方法，保证加载更多footer永远在最后一个
     *
     * @param page page item
     */
    public final void setFooterLoadMore(PageItem page);
    
    /**
     * Sets the view to show if the adapter item count is empty
     *
     * @param page page item
     */
    public final void setEmptyView(PageItem page);
    
    public final void setOnItemClickListener(OnItemClickListener listener);
    
    public final void setOnItemLongClickListener(OnItemLongClickListener listener)
    
```

### RecyclerViewHolder的用法

```java
class ViewHolder extends RecyclerViewHolder<Entity> implements ItemClickCallback{
	TextView mTextView;

	public ViewHolder(@NonNull ViewGroup parent) {
   		super(parent, R.layout.item_main_simple);
		mTextView = itemView.findViewById(R.id.tv_item);
	}

	@Override
	public void bindView(Entity data) {
		mTextView.setText(data.getContent());
	}
	
	@Override
	public void onItemClick(View itemView, int position) {
   		// 条目点击        
	}
}
```

### RecyclerAdapter的用法

```java

class Adapter extends RecyclerAdapter<Entity> {

	public Adapter(List<Entity> data) {
		super(data);
	}

	@Override
	public RecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(parent);
	}
}
```

### 分割线的用法

```java
RecycleView.addItemDecoration(
	new ListBuilder(getApplicationContext())
		.setSpace(1)                        // 分割线间距
		.setColorRes(R.color.colorDivider)  // 分割线颜色
		.setIgnoreLastItem(true)            // 是否忽略最后一条
		.setMargin(5)                       // 两边间距
		.build()
);
```