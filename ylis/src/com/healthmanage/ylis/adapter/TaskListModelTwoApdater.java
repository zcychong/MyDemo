package com.healthmanage.ylis.adapter;

import java.util.List;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.TaskItemModelTwo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListModelTwoApdater extends BaseAdapter {
	private List<TaskItemModelTwo> list;
	private Context mContext;

	public TaskListModelTwoApdater(Context context,
			List<TaskItemModelTwo> taskList) {
		mContext = context;
		list = taskList;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewholder;
		if (convertView == null) {
			view = View.inflate(mContext, R.layout.task_item_model_two, null);
			viewholder = new ViewHolder();
			viewholder.tvNume = (TextView) view.findViewById(R.id.tv_item_name);
			viewholder.tvExecTimes = (TextView) view
					.findViewById(R.id.tv_frequency);
			viewholder.tvCount = (TextView) view
					.findViewById(R.id.tv_task_count);

			view.setTag(viewholder);
		} else {
			view = convertView;
			viewholder = (ViewHolder) view.getTag();
		}

		if (StringUtils.isNotEmpty(list.get(position).getItemName())) {
			viewholder.tvNume.setText(list.get(position).getItemName());
		}

		if (StringUtils.isNotEmpty(list.get(position).getExecTime())) {
			if (list.get(position).getExecTime().equals("A")) {
				viewholder.tvExecTimes.setText("晨间");
				viewholder.tvExecTimes
						.setBackgroundResource(R.color.exec_time_a);
			} else if (list.get(position).getExecTime().equals("B")) {
				viewholder.tvExecTimes.setText("上午");
				viewholder.tvExecTimes
						.setBackgroundResource(R.color.exec_time_b);
			} else if (list.get(position).getExecTime().equals("C")) {
				viewholder.tvExecTimes.setText("下午");
				viewholder.tvExecTimes
						.setBackgroundResource(R.color.exec_time_c);
			} else if (list.get(position).getExecTime().equals("D")){
				viewholder.tvExecTimes.setText("晚间");
				viewholder.tvExecTimes
						.setBackgroundResource(R.color.exec_time_d);
			} else{
				viewholder.tvExecTimes.setText("其他");
				viewholder.tvExecTimes
						.setBackgroundResource(R.color.main_color);
			}
		}else{
			viewholder.tvExecTimes.setText("其他");
			viewholder.tvExecTimes
					.setBackgroundResource(R.color.main_color);
		}

		if (StringUtils.isNotEmpty(list.get(position).getFinishCount())
				&& StringUtils.isNotEmpty(list.get(position).getSunCount())) {
			viewholder.tvCount.setText(list.get(position).getFinishCount()
					+ " / " + list.get(position).getSunCount());
		}

		return view;
	}

	class ViewHolder {
		TextView tvNume;
		TextView tvExecTimes;
		TextView tvCount;
	}

}
