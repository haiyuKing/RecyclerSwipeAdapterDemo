package com.why.project.recyclerswipeadapterdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.why.project.recyclerswipeadapterdemo.R;
import com.why.project.recyclerswipeadapterdemo.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by HaiyuKing
 * Used 列表适配器
 */

public class NewsAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
	/**上下文*/
	private Context myContext;
	/**集合*/
	private ArrayList<NewsBean> listitemList;

	/**
	 * 构造函数
	 */
	public NewsAdapter(Context context, ArrayList<NewsBean> itemlist) {
		myContext = context;
		listitemList = itemlist;
	}

	/**
	 * 获取总的条目数
	 */
	@Override
	public int getItemCount() {
		return listitemList.size();
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(myContext).inflate(R.layout.news_list_item, parent, false);
		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
		return itemViewHolder;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipeLayout;//实现只展现一条列表项的侧滑区域
	}

	/**
	 * 声明grid列表项ViewHolder*/
	static class ItemViewHolder extends RecyclerView.ViewHolder
	{
		public ItemViewHolder(View view)
		{
			super(view);

			listItemLayout = (LinearLayout) view.findViewById(R.id.surfaceView);
			mChannelName = (TextView) view.findViewById(R.id.tv_channelName);

			swipeLayout = (SwipeLayout) view.findViewById(R.id.swipeLayout);
			bottom_wrapper = (LinearLayout) view.findViewById(R.id.bottom_wrapper);
			mTop = (LinearLayout) view.findViewById(R.id.swipe_bottom_top_layout);
			mDelete = (LinearLayout) view.findViewById(R.id.swipe_bottom_del_layout);
		}

		LinearLayout listItemLayout;
		TextView mChannelName;

		SwipeLayout swipeLayout;
		LinearLayout bottom_wrapper;//侧滑区域
		LinearLayout mTop;//置顶
		LinearLayout mDelete;//删除
	}

	/**
	 * 将数据绑定至ViewHolder
	 */
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {

		//判断属于列表项
		if(viewHolder instanceof ItemViewHolder){
			NewsBean newsBean = listitemList.get(index);
			final ItemViewHolder itemViewHold = ((ItemViewHolder)viewHolder);

			itemViewHold.mChannelName.setText(newsBean.getNewsTitle());

			//设置侧滑显示模式
			itemViewHold.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
			//设置侧滑显示位置方向
			//itemViewHold.parentLayout.addDrag(SwipeLayout.DragEdge.Right,itemViewHold.bottom_wrapper);

			itemViewHold.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
				@Override
				public void onOpen(SwipeLayout layout) {
					//实现动画效果展现隐藏层
					YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.bottom_wrapper));
				}
			});

			//如果设置了回调，则设置点击事件
			if (mOnItemClickLitener != null)
			{
				itemViewHold.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
						int position = itemViewHold.getLayoutPosition();//在增加数据或者减少数据时候，position和index就不一样了
						mOnItemClickLitener.onItemClick(itemViewHold.swipeLayout, position);
					}
				});
				//长按事件
				itemViewHold.swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View view) {
						itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
						int position = itemViewHold.getLayoutPosition();//在增加数据或者减少数据时候，position和index就不一样了
						mOnItemClickLitener.onItemLongClick(itemViewHold.swipeLayout, position);
						return false;
					}
				});

				//置顶
				itemViewHold.mTop.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
						int position = itemViewHold.getLayoutPosition();//在增加数据或者减少数据时候，position和index就不一样了
						mOnItemClickLitener.onTopClick(position);
					}
				});

				//删除
				itemViewHold.mDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						itemViewHold.swipeLayout.close();//隐藏侧滑菜单区域
						int position = itemViewHold.getLayoutPosition();//在增加数据或者减少数据时候，position和index就不一样了
						mOnItemClickLitener.onDeleteClick(position);
					}
				});
			}
			mItemManger.bindView(viewHolder.itemView, index);//实现只展现一条列表项的侧滑区域
		}
	}

	/**
	 * 添加Item--用于动画的展现*/
	public void addItem(int position,NewsBean listitemBean) {
		listitemList.add(position,listitemBean);
		notifyItemInserted(position);
	}
	/**
	 * 删除Item--用于动画的展现*/
	public void removeItem(int position) {
		listitemList.remove(position);
		notifyItemRemoved(position);
	}

	/*=====================添加OnItemClickListener回调================================*/
	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
		/**置顶*/
		void onTopClick(int position);
		/**删除*/
		void onDeleteClick(int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
