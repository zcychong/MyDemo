package com.healthmanage.ylis.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainApplication extends Application {
	// 存放Activity
	private static List<Activity> activityList = new LinkedList<Activity>();
	private static MainApplication instance;

	// 单例模式中获取唯一的ExitApplication实例
	public static MainApplication getInstance() {
		if (instance == null) {
			instance = new MainApplication();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 结束所有Activity
	 */
	public void exit(Context context) {
		// Log.e("MainApplication exit 1", "Activitys=" + activityList.size());
		while (activityList.size() > 0) {
			activityList.get(0).finish();
			activityList.remove(0);
		}
		// Log.e("MainApplication exit 2", "Activitys=" + activityList.size());
	}

	public void removeActivity(Context context) {
		for (int i = 0; i < activityList.size(); i++) {
			if (activityList.get(i) == context) {
				activityList.remove(i);
			}
		}
		// Log.e("MainApplication", "Activitys=" + activityList.size());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityStarted(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
			}

			@Override
			public void onActivityResumed(Activity activity) {
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				// Log.e("onActivityDestroyed",
				// activity.getClass().getSimpleName());
				removeActivity(activity);
				// Log.e("MainApplication", "Activitys=" + activityList.size());
			}

			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
				// Log.e("onActivityCreated",
				// activity.getClass().getSimpleName());
				addActivity(activity);
				// Log.e("MainApplication", "Activitys=" + activityList.size());
			}
		});
	}
}
