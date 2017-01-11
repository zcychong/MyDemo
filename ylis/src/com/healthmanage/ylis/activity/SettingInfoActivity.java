package com.healthmanage.ylis.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;






import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
//import okhttp3.OkHttpClient.Builder;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.application.MainApplication;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.download.DownloadProgressHandler;
import com.healthmanage.ylis.network.download.ProgressHelper;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.CheckVersionResponse;
import com.healthmanage.ylis.model.StaffInfoResponse;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.okhttp.ResponseBody;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingInfoActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_back) LinearLayout llBackTitle;

	@Bind(R.id.tv_staff_name) TextView tvStaffName;
	@Bind(R.id.tv_staff_numebr) TextView tvStaffNumebr;
	@Bind(R.id.rl_change_password) RelativeLayout rlChangePwd;
	@Bind(R.id.rl_update_vertion) RelativeLayout rlUpdateVertion;
	@Bind(R.id.rl_about_us) RelativeLayout rlAboutUs;

	@Bind(R.id.tv_logout) TextView tvLogout;
	@Bind(R.id.tv_version_info) TextView tvVersionInfo;
	
	private SharedPreferences userInfo;
	private String userId, userName, userNo;
	private String apkUrl, apkLog;
	private boolean newVersion = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_info);
		context = this;

		initView();
		initData();
		
		checkVersion();
	}

	private void initView() {
		ButterKnife.bind(context);
		
		llBackTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("设置");

		RxView.clicks(llBackTitle).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});

		RxView.clicks(rlUpdateVertion).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						if(newVersion){
							update(apkUrl, apkLog);
						}

					}
				});
		RxView.clicks(rlChangePwd).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, ChangePasswordActivity.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
			}
		});
		
		RxView.clicks(rlAboutUs).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, AboutUsActivity.class);
				startActivity(intent);
			}
		});
		
		RxView.clicks(tvLogout).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				cleanLocalData();
				
				MainApplication.getInstance().exit(context);
				startActivity(new Intent(context, LoginActivity.class));
			}
		});

	}
	
	private void initData(){
		userId = getIntent().getStringExtra("userId");
		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);
		userName = userInfo.getString("userName", "");
		userNo = userInfo.getString("userNo", "");
		
		tvStaffName.setText(userName);
		tvStaffNumebr.setText(userNo);
	}
	/**
	 * 检查是否需要更新
	 */
	private void checkVersion(){
		if (Network.checkNet(context)) {
			checkUpdate();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 检查软件更新
	 */
	public void checkUpdate() {
		Map<String, String> map = new HashMap<String, String>();

		Subscriber<CheckVersionResponse> subscriber = new Subscriber<CheckVersionResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
				
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(CheckVersionResponse checkVersion) {
				Log.e(TAG, "onNext");
				if (checkVersion.isSuccess()) {
					int versionVode = getVersionCode();
					if(versionVode != checkVersion.getVersionCode()){
						apkUrl = checkVersion.getApkUrl();
						apkLog = checkVersion.getVersionLog();
						tvVersionInfo.setText(getResources().getString(R.string.have_hight_vertion));
						newVersion = true;
					}else{
						tvVersionInfo.setText(getResources().getString(R.string.hight_vertion));
					}
				}
			}
		};
		HttpMethodImp.getInstance().checkVersionInfo(subscriber, map);
	}

	private void update(String apkUrl, String versionLog) {
		final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressNumberFormat("%1d KB/%2d KB");
        dialog.setTitle("更新");
        dialog.setMessage(versionLog);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
	        
		ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
			@Override
			protected void onProgress(long bytesRead, long contentLength,
					boolean done) {
				Log.e("是否在主线程中运行",
						String.valueOf(Looper.getMainLooper() == Looper
								.myLooper()));
				Log.e("onProgress",
						String.format("%d%% done\n", (100 * bytesRead)
								/ contentLength));
				Log.e("done", "--->" + String.valueOf(done));
				 dialog.setMax((int) (contentLength/1024));
	                dialog.setProgress((int) (bytesRead/1024));

	                if(done){
	                    dialog.dismiss();
	                }
			}
		});
		Call<ResponseBody> call = HttpMethodImp.getInstance().newApkDownload(ProgressHelper.addProgress(null),apkUrl);
	    call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onFailure(Throwable arg0) {
				Log.e(TAG, "下载完成");
			}

			@Override
			public void onResponse(Response<ResponseBody> response,Retrofit arg1) {
				 try {
	                    InputStream is = response.body().byteStream();
	                    File file = new File(Environment.getExternalStorageDirectory(), "ylis.apk");
	                    FileOutputStream fos = new FileOutputStream(file);
	                    BufferedInputStream bis = new BufferedInputStream(is);
	                    byte[] buffer = new byte[1024];
	                    int len;
	                    while ((len = bis.read(buffer)) != -1) {
	                        fos.write(buffer, 0, len);
	                        fos.flush();
	                    }
	                    fos.close();
	                    bis.close();
	                    is.close();
	                    
	                 // 通过Intent安装APK文件
	            		Intent i = new Intent(Intent.ACTION_VIEW);
	            		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            		i.setDataAndType(Uri.parse("file://" + file.toString()),
	            				"application/vnd.android.package-archive");
	            		startActivity(i);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
					
				}
	        });

	}
	
	private void cleanLocalData(){
		SharedPreferences loginInfo = getSharedPreferences(
				getString(R.string.apk_name), MODE_PRIVATE);
		Editor editor = loginInfo.edit();
		editor.clear();
		editor.commit();
		
	}
	
	/**
	 * 获取软件版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = getPackageManager().getPackageInfo(
					getString(R.string.package_name), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e("update", e.toString());
		}
		return versionCode;
	}

	private void getStaffInfo() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();

		Subscriber<StaffInfoResponse> subscriber = new Subscriber<StaffInfoResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(StaffInfoResponse staff) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (staff != null) {
					if (staff.isSuccess()) {
					}
				}
			}
		};

		HttpMethodImp.getInstance().getStaffInfo(subscriber, map);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
