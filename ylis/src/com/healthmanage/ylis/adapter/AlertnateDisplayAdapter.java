package com.healthmanage.ylis.adapter;

import java.util.List;
import java.util.Map;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;


public class AlertnateDisplayAdapter extends SimpleAdapter {
	private int [] mTo;
	private String [] mFrom;
	public AlertnateDisplayAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mTo = to;
		mFrom = from;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if(StringUtils.isEmpty(mFrom[2])){
			view.findViewById(mTo[2]).setVisibility(View.GONE);
		}else{
			view.findViewById(mTo[2]).setVisibility(View.VISIBLE);
		}
		if(StringUtils.isEmpty(mFrom[3])){
			view.findViewById(mTo[3]).setVisibility(View.GONE);
		}else{
			view.findViewById(mTo[3]).setVisibility(View.VISIBLE);
		}
//		if(StringUtils.isEmpty(mFrom[4])){
//			view.findViewById(mTo[4]).setVisibility(View.GONE);
//		}else{
//			view.findViewById(mTo[4]).setVisibility(View.VISIBLE);
//		}
//		int colorPos = position % 2;
//		if (colorPos == 1) {
			view.setBackgroundResource(R.drawable.corner_writh_shap);
//		} else {
//			view.setBackgroundResource(R.color.list_item_bg);
//		}
		return view;
	}
	
}
