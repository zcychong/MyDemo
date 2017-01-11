package com.healthmanage.ylis.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.PeopleItemModelTwo;
import com.healthmanage.ylis.model.TaskDetailEntity;
import com.healthmanage.ylis.model.TaskDetailListEntity;
import com.healthmanage.ylis.model.TastPersonEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskListModelOneView extends LinearLayout {
	private Context mContext;
	private View view;
	private TextView tvfrequency;
	private GridViewForScrollView gvUserList;
	private TaskAdapter adapter;
	private TaskDetailListEntity detailList;
	private List<Map<String, TaskDetailEntity>> imageList = new ArrayList<Map<String, TaskDetailEntity>>();
	
	public TaskListModelOneView(Context context, TaskDetailListEntity listEntity) {
		super(context);
		mContext = context;
		detailList = listEntity;
		init();
	}
	
	public TaskListModelOneView(Context context, AttributeSet attrs, TaskDetailListEntity listEntity) {
		super(context, attrs);
		mContext = context;
		detailList = listEntity;
		init();
	}
	
	private void init(){
		for (TaskDetailEntity item : detailList.getTaskList()) {
			HashMap<String, TaskDetailEntity> map;
			map = new HashMap<String, TaskDetailEntity>();
			map.put("user", item);
			imageList.add(map);
		}
		
		initView();
	}
	
	private void initView(){
		LayoutInflater localinflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = localinflater.inflate(R.layout.task_detail_model_one_item, this);
		
		tvfrequency = (TextView) view.findViewById(R.id.tv_frequency);
		gvUserList = (GridViewForScrollView) view.findViewById(R.id.dv_task_item);
		if (StringUtils.isNotEmpty(detailList.getExecTime())) {
			if(detailList.getExecTime().equals("A")){
				tvfrequency.setText("晨间");
			}else if(detailList.getExecTime().equals("B")){
				tvfrequency.setText("上午");
			}else if(detailList.getExecTime().equals("C")){
				tvfrequency.setText("下午");
			}else if(detailList.getExecTime().equals("D")){
				tvfrequency.setText("晚间");
			}else{
				tvfrequency.setText("其他");
			}
		}
		adapter = new TaskAdapter(mContext, detailList.getTaskList());
		gvUserList.setAdapter(adapter);
		
	}
	
	class TaskAdapter extends BaseAdapter {
		private List<TaskDetailEntity> taskList;

		public TaskAdapter(Context context, List<TaskDetailEntity> list) {
			taskList = list;
		}

		@Override
		public int getCount() {
			return taskList.size();
		}

		@Override
		public Object getItem(int position) {
			return taskList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.task_item, null);
				viewHolder = new ViewHolder();
				viewHolder.ivFinishState = (ImageView) convertView
						.findViewById(R.id.iv_finish_state);
				viewHolder.ivSecondTask = (ImageView) convertView
						.findViewById(R.id.iv_second_task);
				viewHolder.tvTaskName = (TextView) convertView
						.findViewById(R.id.tv_task_name);
				viewHolder.llView = (LinearLayout) convertView
						.findViewById(R.id.ll_view);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			TaskDetailEntity tempItem =  taskList.get(position);
			String strWczt = taskList.get(position).getWczt();
			if (StringUtils.isNotEmpty(strWczt)) {
				if (strWczt.equals("0") || strWczt.equals("2")) {
					viewHolder.ivFinishState
							.setImageResource(R.drawable.icon_task_default);
				} else if (strWczt.equals("1") || strWczt.equals("3")) {
					viewHolder.ivFinishState.setImageResource(R.drawable.icon_task_finishd);
				} else {
					viewHolder.ivFinishState.setImageResource(R.drawable.icon_task_checked);
				}
			}

			if (StringUtils.isNotEmpty(tempItem.getIsCollect())) {
				if (tempItem.getIsCollect().equals("0")) {
					viewHolder.ivSecondTask.setVisibility(View.GONE);
				} else {
					viewHolder.ivSecondTask.setVisibility(View.VISIBLE);
				}
			}

			if (StringUtils.isNotEmpty(tempItem.getItemName())) {
				viewHolder.tvTaskName.setText(tempItem.getItemName());
			}
			
			return convertView;
		}

		class ViewHolder {
			private ImageView ivFinishState, ivSecondTask;
			private TextView tvTaskName;
			private LinearLayout llView;
		}

	}

	public GridViewForScrollView getGvUserList() {
		return gvUserList;
	}

	public void setGvUserList(GridViewForScrollView gvUserList) {
		this.gvUserList = gvUserList;
	}
	
	public void gvNotifyDataSetChanged(){
		adapter.notifyDataSetChanged();
	}

}
