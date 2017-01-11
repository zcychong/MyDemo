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
import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.application.MainApplication;
import com.healthmanage.ylis.common.DensityUtil;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.MD5Code;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.download.DownloadProgressHandler;
import com.healthmanage.ylis.network.download.ProgressHelper;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.CheckVersionResponse;
import com.healthmanage.ylis.model.LoginResponse;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
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
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @类描述：登陆
 * @author peng
 *
 */
public class LoginActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.username) EditText username;
	@Bind(R.id.password) EditText password;
	@Bind(R.id.sign_in_button) Button signInButton;

	private String userName = "";
	private String pwd = "";
	private boolean userNameFlag = false;
	private boolean pwdFlag = false;
	private boolean close = false;
	private boolean unConnectLogin = false;
	private SharedPreferences userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置无标题
		setContentView(R.layout.activity_login);

		context = this;

		initUI();

		measure();
		checkVersion();

	}
	
	private void measure(){
		Log.e(TAG, String.valueOf(DensityUtil.dip2px(context, 100)));
		Log.e(TAG, String.valueOf(DensityUtil.px2dip(context, 100)));
	}

	private void checkLogin() {
		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);
		userName = userInfo.getString("login_userName", "");
		pwd = userInfo.getString("login_password", "");
 
		if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(pwd)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userName);
			map.put("pwd", MD5Code.MD5(pwd));
			
			try {
				userLoginMethod(map);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage());
			}
		}
	}

	/**
	 * @方法描述：绑定UI组件
	 */
	private void initUI() {
		ButterKnife.bind(this);
		RxTextView.textChanges(username).subscribe(new Action1<CharSequence>() {
			@Override
			public void call(CharSequence text) {
				userName = text.toString();
				if (!"".equals(userName)) {
					userNameFlag = true;
				} else {
					userNameFlag = false;
				}
				if (userNameFlag && pwdFlag) {
					signInButton.setEnabled(true);
				} else {
					signInButton.setEnabled(false);
				}
			}
		});

		RxTextView.textChanges(password).subscribe(new Action1<CharSequence>() {
			@Override
			public void call(CharSequence text) {
				pwd = text.toString();
				if (!"".equals(pwd)) {
					pwdFlag = true;
				} else {
					pwdFlag = false;
				}
				if (userNameFlag && pwdFlag) {
					signInButton.setEnabled(true);
				} else {
					signInButton.setEnabled(false);
				}
			}
		});

		RxView.clicks(signInButton).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("userName", userName);
						map.put("pwd", MD5Code.MD5(pwd));
						try {
							if(Network.checkNet(context)){
								unConnectLogin = true;
								userLoginMethod(map);
							}else{
								Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
							}

						} catch (Exception e) {
							Log.e(TAG, "---");
						}
					}
				});

	}

	/**
	 * @方法描述：普通登陆请求
	 * @param map
	 */
	public void userLoginMethod(Map<String, String> map) {
		loading = LoadingDialog.loadingLogin(context);
		loading.show();
		Subscriber<LoginResponse> subscriber = new Subscriber<LoginResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError1");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
			}

			@Override
			public void onNext(LoginResponse user) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (user != null) {
					if (user.isSuccess()) {
						Log.e(TAG, "userExecTime=" + user.getExecTime());

						SharedPreferences loginInfo = getSharedPreferences(
								getString(R.string.apk_name), MODE_PRIVATE);
						Editor editor = loginInfo.edit();

						editor.putString("login_userName", userName);// 用户号
						editor.putString("login_password", pwd);// 密码

						editor.putString("userId", user.getUserId());
						editor.putString("userNo", user.getUserNo());
						editor.putString("userName", user.getName());
						editor.putString("execTime", user.getExecTime());
						editor.putString("shiftId", user.getShiftId());
						Log.e(TAG, "userName= " + user.getName());
						Log.e(TAG, "userNo=" + user.getUserNo());
						editor.putString("usrOrg", user.getUsrOrg());
						editor.commit();

						if(StringUtils.isNotEmpty(user.getType())){
							if(user.getType().equals("J")){
								Intent intent = new Intent(context,
										DoseManagerActivity.class);
								intent.putExtra("shiftId", user.getShiftId());
								intent.putExtra("userId", user.getUserId());
								intent.putExtra("usrOrg", user.getUsrOrg());
								intent.putExtra("execTime", user.getExecTime());
								intent.putExtra("completStatus", user.isCompletStatus());
								startActivity(intent);
								finish();
								return;
							}
						}

						if (StringUtils.isNotEmpty(user.getShiftId())) {
							Log.e(TAG, "user.getShiftId()=" + user.getShiftId());
							if (!user.getShiftId().equals("-1")) {
								Log.e(TAG, "unConnectLogin=" + unConnectLogin);
									Intent intent = new Intent(context,
											MainActivity.class);
									intent.putExtra("shiftId", user.getShiftId());
									intent.putExtra("userId", user.getUserId());
									intent.putExtra("usrOrg", user.getUsrOrg());
									intent.putExtra("execTime", user.getExecTime());
									intent.putExtra("completStatus", user.isCompletStatus());
									startActivity(intent);
									finish();
							
							} else {
								if(unConnectLogin){
									Intent intent = new Intent(context,
											TastConnectFirstActivity.class);
									intent.putExtra("userId", user.getUserId());
									intent.putExtra("shiftId", user.getShiftId());
									startActivity(intent);
									finish();
								}
							}
						} else {
							Log.e(TAG, "user.getShiftId()=" + "null");

						}
						

					} else {
						if (StringUtils.isNotEmpty(user.getMessage())) {
							if (user.getMessage().equals("NOUSER")) {
								Toast.makeText(context, R.string.nouser,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("NOACTIVE")) {
								Toast.makeText(context, R.string.noactive,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("LOCK")) {
								Toast.makeText(context, R.string.lock,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("DELETE")) {
								Toast.makeText(context, R.string.delete_user,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("EXCEPTION")) {
								Toast.makeText(context, R.string.login_fail,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("NOPWD")) {
								Toast.makeText(context,
										R.string.error_incorrect_password,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage().equals("NOLIST")) {
								Toast.makeText(context, R.string.no_list,
										Toast.LENGTH_SHORT).show();
							} else if (user.getMessage()
									.equals("USEREXCEPTION")) {
								Toast.makeText(context,
										R.string.user_exception,
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(context, R.string.login_fail,
										Toast.LENGTH_SHORT).show();
							}

						}
					}
				}
			}
		};
		HttpMethodImp.getInstance().userLogin(subscriber, map);
	}

	/**
	 * 检查是否需要更新
	 */
	private void checkVersion() {
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
//				checkLogin();
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError2");
			}

			@Override
			public void onNext(CheckVersionResponse checkVersion) {
				Log.e(TAG, "onNext");
				if (checkVersion.isSuccess()) {
					int versionVode = getVersionCode();
					if(versionVode != checkVersion.getVersionCode()){
						Log.e(TAG, "apkUrl= " + checkVersion.getApkUrl());
						Log.e(TAG, "apkLog= " + checkVersion.getVersionLog());
						update(checkVersion.getApkUrl() ,checkVersion.getVersionLog());
					}else{
						checkLogin();
					}
				}
			}
		};
		HttpMethodImp.getInstance().checkVersionInfo(subscriber, map);
	}
	
	private void update(String url, String versionLog){
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
	                	if(dialog != null){
	                		 dialog.dismiss();
	                	}
	                   
	                }
			}
		});
		
		Call<ResponseBody> call = HttpMethodImp.getInstance().newApkDownload(ProgressHelper.addProgress(null),url);
        call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onFailure(Throwable arg0) {
				Log.e(TAG, "下载完成");
				
			}

			@Override
			public void onResponse(Response<ResponseBody> response, Retrofit arg1) {
				Log.e(TAG, "onResponse");
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
            		finish();
    				System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
        });
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

	/**
	 * 获取软件版本名称
	 * 
	 * @return
	 */
	private String getVersionName() {
		String versionName = "";
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = getPackageManager().getPackageInfo(
					getString(R.string.package_name), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("update", e.toString());
		}
		return versionName;
	}

	// 监听返回键
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (close) {
				MainApplication.getInstance().exit(context);
				System.exit(0);
			} else if (!close) {
				close = !close;
				Toast.makeText(context, getString(R.string.hint_logout),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

}
