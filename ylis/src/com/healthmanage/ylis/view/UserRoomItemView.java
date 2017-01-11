package com.healthmanage.ylis.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.TastPersonEntity;
import com.healthmanage.ylis.model.TastRoomEntity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class UserRoomItemView extends LinearLayout {
	private Context mContext;
	private View view;
	private TextView tvGroupName;
	private GridViewForScrollView gvUserList;
	private List<Map<String, TastPersonEntity>> imageList = new ArrayList<Map<String, TastPersonEntity>>();
	private TastRoomEntity roomItem;
	private SimpleAdapter adapter;

	public UserRoomItemView(Context context, TastRoomEntity room) {
		super(context);
		mContext = context;
		roomItem = room;
		for (TastPersonEntity item : roomItem.getBeds()) {
			HashMap<String, TastPersonEntity> map;
			map = new HashMap<String, TastPersonEntity>();
			map.put("user", item);
			imageList.add(map);
		}
		initView();
	}

	private void initView() {
		LayoutInflater localinflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = localinflater.inflate(R.layout.group_item, this);

		tvGroupName = (TextView) view.findViewById(R.id.tv_group_name);
		tvGroupName.setText(roomItem.getRoomNo());
		gvUserList = (GridViewForScrollView) view
				.findViewById(R.id.gv_user_list);
		adapter = new SimpleAdapter(mContext, imageList, R.layout.person_item,
				new String[] {}, new int[] {}) {
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.person_item, null);

				LinearLayout llBg = (LinearLayout) convertView
						.findViewById(R.id.ll_bg);
				TextView tvPeopleName = (TextView) convertView
						.findViewById(R.id.tv_people_name);
				TextView tvRoomName = (TextView) convertView
						.findViewById(R.id.tv_room_num);
				ImageView iv48Hours = (ImageView) convertView
						.findViewById(R.id.iv_48hours);
				TextView tvLeave = (TextView) convertView
						.findViewById(R.id.tv_Leave);

				if (roomItem.getBeds().get(position).getState() != null) {
					if (roomItem.getBeds().get(position).getState().equals("1")) {
						llBg.setBackgroundResource(R.drawable.icon_task_finish);
					} else {
						llBg.setBackgroundResource(R.drawable.icon_task_unfinish);
					}
				}

				if (roomItem.getBeds().get(position).getElderlyName() != null) {
					tvPeopleName.setText(roomItem.getBeds().get(position)
							.getElderlyName());
				}

				// if(roomItem.getRoomNo() != null){
				// tvRoomName.setText(roomItem.getRoomNo());
				// }
				if (StringUtils.isNotEmpty(roomItem.getBeds().get(position)
						.getBedNo())) {
					tvRoomName.setText("床号:"
							+ roomItem.getBeds().get(position).getBedNo());
				}

				if (StringUtils.isNotEmpty(roomItem.getBeds().get(position)
						.getIshous())) {
					if (roomItem.getBeds().get(position).getIshous()
							.equals("1")) {
						iv48Hours.setVisibility(View.VISIBLE);
					} else {
						iv48Hours.setVisibility(View.GONE);
					}
				} else {
					iv48Hours.setVisibility(View.GONE);
				}

				if (StringUtils.isNotEmpty(roomItem.getBeds().get(position)
						.getElderlySta())) {
					if (roomItem.getBeds().get(position).getElderlySta()
							.equals("3")) {// 已请假
						tvLeave.setVisibility(View.VISIBLE);
					} else {
						tvLeave.setVisibility(View.INVISIBLE);
					}
				}

				return convertView;
			}
		};
		gvUserList.setAdapter(adapter);
	}

	public void setFinish(int position) {
		adapter.notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	public GridViewForScrollView getGvRoomList() {
		return gvUserList;
	}

	public void setGvRoomList(GridViewForScrollView gvUserList) {
		this.gvUserList = gvUserList;
	}

}
