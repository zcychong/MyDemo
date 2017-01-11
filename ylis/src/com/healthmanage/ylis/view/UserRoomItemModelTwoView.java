package com.healthmanage.ylis.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.PeopleItemModelTwo;
import com.healthmanage.ylis.model.RoomItemModelTwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class UserRoomItemModelTwoView extends LinearLayout {
	private Context mContext;
	private View view;
	private TextView tvGroupName;
	private GridViewForScrollView gvUserList;
	private List<Map<String, PeopleItemModelTwo>> imageList = new ArrayList<Map<String, PeopleItemModelTwo>>();
	private RoomItemModelTwo roomItem;
	private PeopleItemModelTwo peopleItem;
	private SimpleAdapter adapter;
	private String roomId;

	public UserRoomItemModelTwoView(Context context, RoomItemModelTwo room) {
		super(context);
		mContext = context;
		roomItem = room;
		roomId = room.getRoomNo();
		for (PeopleItemModelTwo item : roomItem.getBeds()) {
			HashMap<String, PeopleItemModelTwo> map;
			map = new HashMap<String, PeopleItemModelTwo>();
			map.put("user", item);
			imageList.add(map);
		}
		initView();
	}

	private void initView() {
		LayoutInflater localinflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = localinflater.inflate(R.layout.room_people_item_model_two, this);

		tvGroupName = (TextView) view.findViewById(R.id.tv_room_number);
		if (StringUtils.isNotEmpty(roomItem.getRoomId())) {
			tvGroupName.setText(roomItem.getRoomNo());
		}
		gvUserList = (GridViewForScrollView) view.findViewById(R.id.gv_people);
		adapter = new SimpleAdapter(mContext, imageList,
				R.layout.people_item_model_two, new String[] {}, new int[] {}) {
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.people_item_model_two, null);
				LinearLayout llBg = (LinearLayout) convertView
						.findViewById(R.id.ll_bg);
				TextView tvPeopleName = (TextView) convertView
						.findViewById(R.id.tv_people_name);
				TextView tvBedNumber = (TextView) convertView
						.findViewById(R.id.tv_bed_number);
				ImageView ivCheckState = (ImageView) convertView
						.findViewById(R.id.iv_check_state);
				peopleItem = roomItem.getBeds().get(position);
				// 设置完成状态
//				if (peopleItem.getWczt() != null) {
//					if (peopleItem.getWczt().equals("1")) {
//						ivCheckState.setVisibility(View.GONE);
//						llBg.setBackgroundResource(R.color.people_default_color);
//						tvPeopleName.setBackgroundResource(R.drawable.border_people_normal);
//					} else if (peopleItem.getWczt().equals("0")) {
//						ivCheckState.setVisibility(View.VISIBLE);
//						ivCheckState.setBackgroundResource(R.drawable.icon_people_unchoose);
//						llBg.setBackgroundResource(R.color.people_default_color);
//						tvPeopleName.setBackgroundResource(R.drawable.border_people_normal);
//					} else {
//						ivCheckState.setVisibility(View.VISIBLE);
//						ivCheckState.setBackgroundResource(R.drawable.icon_people_finished);
//						llBg.setBackgroundResource(R.color.people_choose_color);
//						tvPeopleName.setBackgroundResource(R.drawable.border_people_check);
//					}
//				}

				if (StringUtils.isNotEmpty(peopleItem.getBedNo())) {
					tvBedNumber.setText(peopleItem.getBedNo() + "号床");
				}

				if (StringUtils.isNotEmpty(peopleItem.getElderlySta())) {
					if (peopleItem.getElderlySta().equals("3")) {
						ivCheckState.setVisibility(View.GONE);
						tvPeopleName.setTextColor(getResources().getColor(R.color.main_color));
						if (StringUtils.isNotEmpty(peopleItem.getElderlyName())) {
							tvPeopleName.setText(peopleItem.getElderlyName() + "-请假");
						}
						
						llBg.setBackgroundResource(R.color.border_yellow);
						tvPeopleName.setBackgroundResource(R.drawable.border_people_normal);
						
					}else{
						ivCheckState.setVisibility(View.VISIBLE);
						tvPeopleName.setTextColor(getResources().getColor(R.color.main_black));
						if (StringUtils.isNotEmpty(peopleItem.getElderlyName())) {
							tvPeopleName.setText(peopleItem.getElderlyName());
						}
						if (peopleItem.getWczt() != null) {
							if (peopleItem.getWczt().equals("1")) {
								ivCheckState.setVisibility(View.VISIBLE);
								ivCheckState.setBackgroundResource(R.drawable.icon_people_finished);
								llBg.setBackgroundResource(R.color.people_default_color);
								tvPeopleName.setBackgroundResource(R.drawable.border_people_finish);
							} else if (peopleItem.getWczt().equals("0")) {
								ivCheckState.setVisibility(View.VISIBLE);
								ivCheckState.setBackgroundResource(R.drawable.icon_people_unchoose);
								llBg.setBackgroundResource(R.color.border_yellow);
								tvPeopleName.setBackgroundResource(R.drawable.border_people_normal);
							} else if (peopleItem.getWczt().equals("8")){
								ivCheckState.setVisibility(View.VISIBLE);
								ivCheckState.setBackgroundResource(R.drawable.icon_people_finished);
								llBg.setBackgroundResource(R.color.people_choose_color);
								tvPeopleName.setBackgroundResource(R.drawable.border_people_check);
							}
						}
					}
				}

				return convertView;
			}
		};
		gvUserList.setAdapter(adapter);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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
