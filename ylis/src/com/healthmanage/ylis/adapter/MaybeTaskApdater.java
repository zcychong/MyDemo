package com.healthmanage.ylis.adapter;

import java.util.List;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.TaskItemModelTwo;
import com.healthmanage.ylis.model.TaskMaybeModelTwo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MaybeTaskApdater extends BaseAdapter {
	private List<TaskMaybeModelTwo> list;
	private Context mContext;

	public MaybeTaskApdater(Context context,
			List<TaskMaybeModelTwo> taskList) {
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
			view = View.inflate(mContext, R.layout.maybe_task_item, null);
			viewholder = new ViewHolder();
			viewholder.tvName = (TextView) view.findViewById(R.id.tv_sannitation);

			view.setTag(viewholder);
		} else {
			view = convertView;
			viewholder = (ViewHolder) view.getTag();
		}

		if (StringUtils.isNotEmpty(list.get(position).getGoodName())) {
			viewholder.tvName.setText(list.get(position).getGoodName());
		}

		return view;
	}

	class ViewHolder {
		TextView tvName;
	}

}
