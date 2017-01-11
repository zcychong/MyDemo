package com.healthmanage.ylis.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	public static boolean checkNet(Context context) {
		// 获取手机�?以连接管理对象（包括wi-fi，net等连接的管理�?
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn != null) {
			// 网络管理连接对象
			NetworkInfo info = conn.getActiveNetworkInfo();

			if (info != null && info.isConnected()) {
				// 判断当前网络是否连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * 判断当前网络是否是wifi
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

}
