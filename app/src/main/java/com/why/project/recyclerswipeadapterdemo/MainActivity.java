package com.why.project.recyclerswipeadapterdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.why.project.recyclerswipeadapterdemo.adapter.NewsAdapter;
import com.why.project.recyclerswipeadapterdemo.bean.NewsBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

	private RecyclerView mRecyclerView;
	private ArrayList<NewsBean> mNewsBeanArrayList;
	private NewsAdapter mNewsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		initDatas();
		initEvents();

	}

	private void initViews() {
		mRecyclerView = findViewById(R.id.recycler_view);
	}

	private void initDatas() {
		//初始化集合
		mNewsBeanArrayList = new ArrayList<NewsBean>();
		for(int i=0; i<10;i++){
			NewsBean newsBean = new NewsBean();
			newsBean.setNewsId("123"+i);
			newsBean.setNewsTitle("标题"+i);

			mNewsBeanArrayList.add(newsBean);
		}

		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		//设置适配器
		if(mNewsAdapter == null){
			//设置适配器
			mNewsAdapter = new NewsAdapter(this, mNewsBeanArrayList);
			mNewsAdapter.setMode(Attributes.Mode.Single);//设置只有一个拖拽打开的时候，其他的关闭
			mRecyclerView.setAdapter(mNewsAdapter);
			//添加分割线
			//设置添加删除动画
			//调用ListView的setSelected(!ListView.isSelected())方法，这样就能及时刷新布局
			mRecyclerView.setSelected(true);
		}else{
			mNewsAdapter.notifyDataSetChanged();
		}
	}

	private void initEvents() {
		//列表适配器的点击监听事件
		mNewsAdapter.setOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onItemLongClick(View view, int position) {
				Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTopClick(int position) {
				Toast.makeText(MainActivity.this,"置顶",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDeleteClick(int position) {
				Toast.makeText(MainActivity.this,"删除",Toast.LENGTH_SHORT).show();
			}
		});
	}
}
