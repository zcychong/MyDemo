package com.healthmanage.ylis.common;

import com.healthmanage.ylis.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {

	/**
	 * 查询数据
	 * 
	 * @param context
	 */
	public static Dialog loadingFind(Context context) {
		return createLoadingDialog(context, context.getString(R.string.loading));
	}

	/**
	 * 连接设备
	 * 
	 * @param context
	 */
	public static Dialog loadingConnect(Context context) {
		return createLoadingDialog(context,
				context.getString(R.string.connec_device));
	}

	/**
	 * 保存数据
	 * 
	 * @param context
	 */
	public static Dialog loadingSaveData(Context context) {
		return createLoadingDialog(context,
				context.getString(R.string.loading_save2));
	}

	/**
	 * 提交数据
	 * 
	 * @param context
	 */
	public static Dialog loadingSave(Context context) {
		return createLoadingDialog(context,
				context.getString(R.string.save_loading));
	}

	/**
	 * 登陆
	 * 
	 * @param context
	 */
	public static Dialog loadingLogin(Context context) {
		return createLoadingDialog(context, context.getString(R.string.logging));
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_loading_layout, null);// 得到加载view
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) view.findViewById(R.id.img);
		TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_anim);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}

}
