package com.healthmanage.ylis.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.R.layout;
import com.healthmanage.ylis.common.DensityUtil;
import com.healthmanage.ylis.view.GridViewForScrollView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	private int viewWeith, viewHeight, ivWeith;
	private final int COUNT = 4;
	private List<ImageView> ivList = new ArrayList<ImageView>();
	private PhotoAdapter adapter;
	@Bind(R.id.tv_title)
	TextView tvTitle;

	@Bind(R.id.et_message)
	EditText etMessage;
	@Bind(R.id.gv_user_photo)
	GridLayout gvUserPhotos;
	@Bind(R.id.btn_commit)
	Button btnCommit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		context = this;

		initView();
	}

	private void initView() {
		ButterKnife.bind(this);
		tvTitle.setText("举报");

		addDefaultImage(0);
		addDefaultImage(0);
		addDefaultImage(0);
		addDefaultImage(0);
		addDefaultImage(0);

		// adapter = new PhotoAdapter(ivList);
		// gvUserPhotos.setAdapter(adapter);
	}

	private void addDefaultImage(int position) {

		ImageView ivAdd = new ImageView(context);
		viewWeith = DensityUtil.getScreenWidthPix(context);
		ivWeith = (viewWeith - DensityUtil.dip2px(context, (60 + 30))) / COUNT;
		Log.e(TAG, "ivWeith" + ivWeith);
		GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
				new LinearLayout.LayoutParams(ivWeith, ivWeith));
		lp.setMargins(5, 5, 5, 5);
		ivAdd.setLayoutParams(lp);
		ivAdd.setImageResource(R.drawable.gg);
		ivList.add(ivAdd);

		gvUserPhotos.addView(ivAdd, position);
	}

	private class PhotoAdapter extends BaseAdapter {
		private List<ImageView> photoList;

		PhotoAdapter(List<ImageView> list) {
			photoList = list;
		}

		@Override
		public int getCount() {
			return photoList.size();
		}

		@Override
		public Object getItem(int position) {
			return photoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.user_photo_item,
						null);
			}
			LinearLayout llPhoto = (LinearLayout) convertView
					.findViewById(R.id.ll_photo);
			llPhoto.addView(photoList.get(position));

			return convertView;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
