package com.healthmanage.ylis.adapter;

import java.util.List;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.LeaveElderlyEntity;
import com.healthmanage.ylis.model.TaskItemModelTwo;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaveElderlyApdater extends BaseAdapter {
	private List<LeaveElderlyEntity> list;
	private Context mContext;
	private String strSearchContent;

	public LeaveElderlyApdater(Context context,
			List<LeaveElderlyEntity> taskList,String text) {
		mContext = context;
		list = taskList;
		strSearchContent = text;
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
			view = View.inflate(mContext, R.layout.leave_elderly_item, null);
			viewholder = new ViewHolder();
			viewholder.tvName = (TextView) view.findViewById(R.id.tv_name);
			viewholder.tvRoom = (TextView) view.findViewById(R.id.tv_room);
			viewholder.tvBed = (TextView) view.findViewById(R.id.tv_bed);

			view.setTag(viewholder);
		} else {
			view = convertView;
			viewholder = (ViewHolder) view.getTag();
		}

		LeaveElderlyEntity tempEntity = list.get(position);
		
		if(StringUtils.isNotEmpty(tempEntity.getName())){
			viewholder.tvName.setText(setColor(tempEntity.getName()));
		}else{
			viewholder.tvName.setText("");
		}

		if(StringUtils.isNotEmpty(tempEntity.getRoomNo())){
			viewholder.tvRoom.setText(tempEntity.getRoomNo() + "房间");
		}else{
			viewholder.tvRoom.setText("");
		}
		
		if(StringUtils.isNotEmpty(tempEntity.getBedNo())){
			viewholder.tvBed.setText(tempEntity.getBedNo() + "号床");
		}else{
			viewholder.tvBed.setText("");
		}

		return view;
	}
	
	private SpannableString setColor(String name){
		SpannableString spStr = new SpannableString(name);
		if(StringUtils.isNotEmpty(strSearchContent)){
			 int start = name.indexOf(strSearchContent);
	         int end = start + strSearchContent.length();
	         spStr.setSpan(new ClickableSpan(){
				@Override
				public void onClick(View widget) {
					
				}
			  @Override
	            public void updateDrawState(TextPaint ds) {
	                ds.setColor(mContext.getResources().getColor(R.color.main_color));
	                ds.setUnderlineText(false);//去掉下划线
	            }
	        	 
	         }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spStr;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvRoom;
		TextView tvBed;
	}

}
