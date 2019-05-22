# DecorAdapter

```
RecyclerView.Adapter的功能封装
```

### 依赖

[![License](https://img.shields.io/badge/License-Apache%202.0-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/a-liya/maven/decoradapter/images/download.svg)](https://bintray.com/a-liya/maven/decoradapter/_latestVersion)
[![MinSdk](https://img.shields.io/badge/%20MinSdk%20-%2014%20-f0ad4e.svg)](https://android-arsenal.com/api?level=12)

```

implementation 'com.aliya:decoradapter:0.7.6'

```

核心功能：  

1. addHeader
2. addFooter
3. setHeaderRefresh
4. setFooterLoadMore
5. setEmpty
6. setOnItemClickListener
7. setOnItemLongClickListener
8. removePageItem


其他功能：  

1. RecyclerViewHolder - ViewHolder 
2. OverlayViewHolder - 悬浮吸顶 ViewHolder
3. ListItemDecoration - List类型分割线  
4. GridItemDecoration - Grid类型分割线
5. RefreshPage - 下拉刷新


Api声明：

```java
    
    /**
     * 添加 header PageItem
     *
     * @param page page item
     */
    public final PageItem addHeader(PageItem page);
    
    /**
     * 添加 footer PageItem
     *
     * @param page page item
     */
    public final PageItem addFooter(PageItem page);
    
    /**
     * 专门为下拉刷新提供的方法, 保证下拉刷新header永远在第一个
     *
     * @param page page item
     */
    public final PageItem setHeaderRefresh(PageItem page);
    
    /**
     * 专门为加载更多提供的方法，保证加载更多footer永远在最后一个
     *
     * @param page page item
     */
    public final PageItem setFooterLoadMore(PageItem page);
    
    /**
     * Sets the view to show if the adapter item count is empty
     *
     * @param page page item
     */
    public final void setEmpty(PageItem page);
    
    /**
     * Removes a previously-added page item.
     *
     * @param page The page item to remove
     * @return removed page item position at adapter, -1 means it doesn't exist
     */    
    public final int removePageItem(PageItem page);
    
    public final void setOnItemClickListener(OnItemClickListener listener);
    
    public final void setOnItemLongClickListener(OnItemLongClickListener listener)
    
```

### RecyclerViewHolder的用法

```java
class ViewHolder extends RecyclerViewHolder<Entity> implements ItemClickCallback{
	TextView mTextView;

	public ViewHolder(@NonNull ViewGroup parent) {
   		super(parent, R.layout.item_main_sample);
		mTextView = itemView.findViewById(R.id.tv_item);
	}

	@Override
	public void bindData(Entity data) {
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

### 下拉刷新的实现

参见：  

* 实现一 [RefreshHeader](./app/src/main/java/com/aliya/adapter/sample/page/RefreshHeader.java)
* 实现二 [Refresh2Header](./app/src/main/java/com/aliya/adapter/sample/page/Refresh2Header.java)

### 分割线的用法

```java
recycleView.addItemDecoration(
	new ListBuilder(getApplicationContext())
		.setSpace(1)                        // 分割线间距
		.setColorRes(R.color.colorDivider)  // 分割线颜色
		.setIgnoreLastItem(true)            // 是否忽略最后一条
		.setMargin(5)                       // 两边间距
		.build()
);
```