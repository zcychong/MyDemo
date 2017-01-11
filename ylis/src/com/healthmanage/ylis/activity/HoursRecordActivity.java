package com.healthmanage.ylis.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.HourQuestionItem;
import com.healthmanage.ylis.model.HourRecordResponse;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HoursRecordActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title)
	TextView tvTitle;
	
	@Bind(R.id.ll_back)
	LinearLayout llBack;
	
	@Bind(R.id.tv_commit)
	TextView tvCommit;
	@Bind(R.id.tv_user_name)
	TextView tvUserName;

	@Bind(R.id.et_record_ques_health_1)
	EditText etRecordQuesHealth1;
	@Bind(R.id.et_record_ques_health_2)
	EditText etRecordQuesHealth2;
	@Bind(R.id.et_record_ques_health_3)
	EditText etRecordQuesHealth3;
	@Bind(R.id.et_record_ques_health_4_1)
	EditText etRecordQuesHealth4_1;
	@Bind(R.id.et_record_ques_health_4_2)
	EditText etRecordQuesHealth4_2;
	@Bind(R.id.et_record_ques_health_5_1)
	EditText etRecordQuesHealth5_1;
	@Bind(R.id.et_record_ques_health_5_2)
	EditText etRecordQuesHealth5_2;
	@Bind(R.id.et_record_ques_health_6_1)
	EditText etRecordQuesHealth6_1;
	@Bind(R.id.et_record_ques_health_6_2)
	EditText etRecordQuesHealth6_2;
	@Bind(R.id.et_record_ques_health_7_1)
	EditText etRecordQuesHealth7_1;
	@Bind(R.id.et_record_ques_health_7_2)
	EditText etRecordQuesHealth7_2;
	@Bind(R.id.et_record_ques_health_8_1)
	EditText etRecordQuesHealth8_1;
	@Bind(R.id.et_record_ques_health_8_2)
	EditText etRecordQuesHealth8_2;

	@Bind(R.id.et_record_ques_health_9)
	Spinner spRecordQuesHealth9;
	@Bind(R.id.et_record_ques_health_10)
	Spinner spRecordQuesHealth10;
	@Bind(R.id.et_record_ques_health_11)
	Spinner spRecordQuesHealth11;
	@Bind(R.id.et_record_ques_health_12)
	Spinner spRecordQuesHealth12;
	@Bind(R.id.et_record_ques_health_13)
	Spinner spRecordQuesHealth13;
	@Bind(R.id.et_record_ques_health_14)
	Spinner spRecordQuesHealth14;
	@Bind(R.id.et_record_ques_health_15)
	Spinner spRecordQuesHealth15;
	@Bind(R.id.et_record_ques_health_16)
	Spinner spRecordQuesHealth16;

	@Bind(R.id.et_record_ques_mind_1)
	Spinner spRecordQuesMind1;
	@Bind(R.id.et_record_ques_mind_2)
	Spinner spRecordQuesMind2;
	@Bind(R.id.et_record_ques_mind_3)
	Spinner spRecordQuesMind3;
	@Bind(R.id.et_record_ques_mind_4)
	Spinner spRecordQuesMind4;
	@Bind(R.id.et_record_ques_mind_5)
	Spinner spRecordQuesMind5;
	@Bind(R.id.et_record_ques_mind_6)
	Spinner spRecordQuesMind6;
	@Bind(R.id.et_record_ques_mind_7)
	Spinner spRecordQuesMind7;
	@Bind(R.id.et_record_ques_mind_8)
	Spinner spRecordQuesMind8;
	@Bind(R.id.et_record_ques_mind_9)
	Spinner spRecordQuesMind9;
	@Bind(R.id.et_record_ques_mind_10)
	Spinner spRecordQuesMind10;
	@Bind(R.id.et_record_ques_mind_11)
	Spinner spRecordQuesMind11;
	@Bind(R.id.et_record_ques_mind_12)
	Spinner spRecordQuesMind12;
	@Bind(R.id.et_record_ques_mind_13)
	Spinner spRecordQuesMind13;
	@Bind(R.id.et_record_ques_mind_14)
	Spinner spRecordQuesMind14;

	@Bind(R.id.et_record_ques_life_1)
	Spinner spRecordQuesLife1;
	@Bind(R.id.et_record_ques_life_2)
	Spinner spRecordQuesLife2;
	@Bind(R.id.et_record_ques_life_3_1)
	Spinner spRecordQuesLife3_1;
	@Bind(R.id.et_record_ques_life_3_2)
	Spinner spRecordQuesLife3_2;
	@Bind(R.id.et_record_ques_life_4)
	Spinner spRecordQuesLife4;
	@Bind(R.id.et_record_ques_life_5)
	Spinner spRecordQuesLife5;
	@Bind(R.id.et_record_ques_life_6)
	Spinner spRecordQuesLife6;
	@Bind(R.id.et_record_ques_life_7)
	Spinner spRecordQuesLife7;
	@Bind(R.id.et_record_ques_life_8)
	Spinner spRecordQuesLife8;
	@Bind(R.id.et_record_ques_life_9)
	Spinner spRecordQuesLife9;
	@Bind(R.id.et_record_ques_life_10)
	Spinner spRecordQuesLife10;
	@Bind(R.id.et_record_ques_life_11)
	Spinner spRecordQuesLife11;
	@Bind(R.id.et_record_ques_life_12)
	Spinner spRecordQuesLife12;

	@Bind(R.id.cb_record_ques_life_13_1)
	CheckBox cbRecordQuesLife1;
	@Bind(R.id.cb_record_ques_life_13_2)
	CheckBox cbRecordQuesLife2;
	@Bind(R.id.cb_record_ques_life_13_3)
	CheckBox cbRecordQuesLife3;
	@Bind(R.id.cb_record_ques_life_13_4)
	CheckBox cbRecordQuesLife4;
	@Bind(R.id.cb_record_ques_life_13_5)
	CheckBox cbRecordQuesLife5;
	@Bind(R.id.cb_record_ques_life_13_6)
	CheckBox cbRecordQuesLife6;
	@Bind(R.id.cb_record_ques_life_13_7)
	CheckBox cbRecordQuesLife7;
	@Bind(R.id.cb_record_ques_life_13_8)
	CheckBox cbRecordQuesLife8;
	@Bind(R.id.cb_record_ques_life_13_9)
	CheckBox cbRecordQuesLife9;
	@Bind(R.id.cb_record_ques_life_13_10)
	CheckBox cbRecordQuesLife10;
	@Bind(R.id.cb_record_ques_life_13_11)
	CheckBox cbRecordQuesLife11;
	@Bind(R.id.cb_record_ques_life_13_12)
	CheckBox cbRecordQuesLife12;
	@Bind(R.id.cb_record_ques_life_13_13)
	CheckBox cbRecordQuesLife13;
	@Bind(R.id.cb_record_ques_life_13_14)
	CheckBox cbRecordQuesLife14;
	@Bind(R.id.cb_record_ques_life_13_15)
	CheckBox cbRecordQuesLife15;
	@Bind(R.id.cb_record_ques_life_13_16)
	CheckBox cbRecordQuesLife16;

	@Bind(R.id.et_record_ques_mind_5_other)
	EditText etRecordQuesMind5Other;
	@Bind(R.id.ll_record_ques_mind_5_other)
	LinearLayout llRecordQuesMind5Other;
	@Bind(R.id.et_record_ques_All)
	EditText etRecordQuesAll;

	private final static String CHAR_NUM_BREAK = "!$!";
	private final static String CHAR_ANS_BREAK = "#&";
	private final static String CHAR_QUES_BREAK = "!:!";
	private final static String CHAR_BREAK = ",";

	private String userId, usrOrg, elderlyId, mainId;
	private SharedPreferences userInfo;
	private String strReqLife13Number, strReqLife14Number;
	private String strReqHeal1, strReqHeal2, strReqHeal3, strReqHeal4_1,
			strReqHeal4_2;
	private String strReqHeal5_1, strReqHeal5_2, strReqHeal6_1, strReqHeal6_2,
			strReqHeal7_1;
	private String strReqHeal7_8, strReqHeal8_1, strReqHeal8_2;

	private List<String> strReqHeal = new ArrayList<String>();
	private List<EditText> listRecordQuesHealthEt = new ArrayList<EditText>();
	private List<EditText> listRecordQuesHealthEtNor = new ArrayList<EditText>();
	private List<Spinner> listRecordQuesHealth = new ArrayList<Spinner>();
	private List<Spinner> listRecordQuesMind = new ArrayList<Spinner>();
	private List<Spinner> listRecordQuesLife = new ArrayList<Spinner>();
	private List<CheckBox> listRecordQuesCB = new ArrayList<CheckBox>();

	private boolean rqMindFiveChecked = false;
	private String[] spRecordQuesMindFive;
	private String[] etAnswerNumbers, etAnswerNumbersNor;
	private String[] spRecordQuesHealth, spRecordQuesMind, spRecordQuesLife;
	private StringBuffer sbAnswers = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hours_record);

		context = this;

		initView();
		initData();

		getElderLyHourRecord();
	}

	private void initView() {
		ButterKnife.bind(context);

		listRecordQuesHealthEtNor.add(etRecordQuesHealth1);
		listRecordQuesHealthEtNor.add(etRecordQuesHealth2);
		listRecordQuesHealthEtNor.add(etRecordQuesHealth3);

		listRecordQuesHealthEt.add(etRecordQuesHealth4_1);
		listRecordQuesHealthEt.add(etRecordQuesHealth4_2);
		listRecordQuesHealthEt.add(etRecordQuesHealth5_1);
		listRecordQuesHealthEt.add(etRecordQuesHealth5_2);
		listRecordQuesHealthEt.add(etRecordQuesHealth6_1);
		listRecordQuesHealthEt.add(etRecordQuesHealth6_2);
		listRecordQuesHealthEt.add(etRecordQuesHealth7_1);
		listRecordQuesHealthEt.add(etRecordQuesHealth7_2);
		listRecordQuesHealthEt.add(etRecordQuesHealth8_1);
		listRecordQuesHealthEt.add(etRecordQuesHealth8_2);

		listRecordQuesHealth.add(spRecordQuesHealth9);
		listRecordQuesHealth.add(spRecordQuesHealth10);
		listRecordQuesHealth.add(spRecordQuesHealth11);
		listRecordQuesHealth.add(spRecordQuesHealth12);
		listRecordQuesHealth.add(spRecordQuesHealth13);
		listRecordQuesHealth.add(spRecordQuesHealth14);
		listRecordQuesHealth.add(spRecordQuesHealth15);
		listRecordQuesHealth.add(spRecordQuesHealth16);

		listRecordQuesMind.add(spRecordQuesMind1);
		listRecordQuesMind.add(spRecordQuesMind2);
		listRecordQuesMind.add(spRecordQuesMind3);
		listRecordQuesMind.add(spRecordQuesMind4);
		listRecordQuesMind.add(spRecordQuesMind5);
		listRecordQuesMind.add(spRecordQuesMind6);
		listRecordQuesMind.add(spRecordQuesMind7);
		listRecordQuesMind.add(spRecordQuesMind8);
		listRecordQuesMind.add(spRecordQuesMind9);
		listRecordQuesMind.add(spRecordQuesMind10);
		listRecordQuesMind.add(spRecordQuesMind11);
		listRecordQuesMind.add(spRecordQuesMind12);
		listRecordQuesMind.add(spRecordQuesMind13);
		listRecordQuesMind.add(spRecordQuesMind14);

		spRecordQuesMind5
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (position == spRecordQuesMindFive.length - 1) {
							llRecordQuesMind5Other.setVisibility(View.VISIBLE);
							rqMindFiveChecked = true;
						} else {
							llRecordQuesMind5Other.setVisibility(View.GONE);
							rqMindFiveChecked = false;
							etRecordQuesMind5Other.setText("");
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		listRecordQuesLife.add(spRecordQuesLife1);
		listRecordQuesLife.add(spRecordQuesLife2);
		listRecordQuesLife.add(spRecordQuesLife3_1);
		listRecordQuesLife.add(spRecordQuesLife3_2);
		listRecordQuesLife.add(spRecordQuesLife4);
		listRecordQuesLife.add(spRecordQuesLife5);
		listRecordQuesLife.add(spRecordQuesLife6);
		listRecordQuesLife.add(spRecordQuesLife7);
		listRecordQuesLife.add(spRecordQuesLife8);
		listRecordQuesLife.add(spRecordQuesLife9);
		listRecordQuesLife.add(spRecordQuesLife10);
		listRecordQuesLife.add(spRecordQuesLife11);
		listRecordQuesLife.add(spRecordQuesLife12);

		listRecordQuesCB.add(cbRecordQuesLife1);
		listRecordQuesCB.add(cbRecordQuesLife2);
		listRecordQuesCB.add(cbRecordQuesLife3);
		listRecordQuesCB.add(cbRecordQuesLife4);
		listRecordQuesCB.add(cbRecordQuesLife5);
		listRecordQuesCB.add(cbRecordQuesLife6);
		listRecordQuesCB.add(cbRecordQuesLife7);
		listRecordQuesCB.add(cbRecordQuesLife8);
		listRecordQuesCB.add(cbRecordQuesLife9);
		listRecordQuesCB.add(cbRecordQuesLife10);
		listRecordQuesCB.add(cbRecordQuesLife11);
		listRecordQuesCB.add(cbRecordQuesLife12);
		listRecordQuesCB.add(cbRecordQuesLife13);
		listRecordQuesCB.add(cbRecordQuesLife14);
		listRecordQuesCB.add(cbRecordQuesLife15);
		listRecordQuesCB.add(cbRecordQuesLife16);

		tvTitle.setText("48小时观察记录");
		tvTitle.setVisibility(View.VISIBLE);
		llBack.setVisibility(View.VISIBLE);
		
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				finish();
			}
		});
		
		RxView.clicks(tvCommit).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {

						Map<String, String> answerMap = new HashMap<String, String>();
						answerMap.put("userId", userId);
						answerMap.put("usrOrg", usrOrg);
						answerMap.put("mainId", mainId);

//						Log.e("userId", userId);
//						Log.e("usrOrg", usrOrg);
//						Log.e("mainId", mainId);
						if (sbAnswers.length() > 0) {
							sbAnswers.delete(0, sbAnswers.length());
						}

						// 一、身体健康状况 1-3
						for (int i = 0; i < listRecordQuesHealthEtNor.size(); i++) {
							String str = listRecordQuesHealthEtNor.get(i)
									.getText().toString();
							if (StringUtils.isNotEmpty(str)) {
								// sbAnswers.append(etAnswerNumbersNor[i]);
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append("3");
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append(str);
								// sbAnswers.append(CHAR_QUES_BREAK);
								String value = etAnswerNumbersNor[i]
										+ CHAR_NUM_BREAK + "3" + CHAR_NUM_BREAK
										+ str;
								answerMap.put(etAnswerNumbersNor[i], value);
								Log.e(etAnswerNumbersNor[i], value);
							}
						}
						// 一、身体健康状况 4-8
						for (int i = 0; i < etAnswerNumbers.length; i++) {
							String str1 = listRecordQuesHealthEt.get(i * 2)
									.getText().toString();
							String str2 = listRecordQuesHealthEt.get(i * 2 + 1)
									.getText().toString();
							// sbAnswers.append(etAnswerNumbers[i]);
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append("3");
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append(str1);
							// sbAnswers.append(CHAR_ANS_BREAK);
							// sbAnswers.append(str2);
							// sbAnswers.append(CHAR_QUES_BREAK);
							String value = etAnswerNumbers[i] + CHAR_NUM_BREAK
									+ "3" + CHAR_NUM_BREAK + str1
									+ CHAR_ANS_BREAK + str2;
							answerMap.put(etAnswerNumbers[i], value);
							Log.e(etAnswerNumbers[i], value);
						}
						// 一、身体健康状况 9-16
						for (int i = 0; i < listRecordQuesHealth.size(); i++) {
							long index = listRecordQuesHealth.get(i)
									.getSelectedItemId();
							// sbAnswers.append(spRecordQuesHealth[i]);
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append("1");
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append(index);
							// sbAnswers.append(CHAR_QUES_BREAK);
							String value = spRecordQuesHealth[i]
									+ CHAR_NUM_BREAK + "1" + CHAR_NUM_BREAK
									+ index;
							answerMap.put(spRecordQuesHealth[i], value);
						}
						// 二、心理/精神状况：1-14
						for (int i = 0; i < listRecordQuesMind.size(); i++) {
							long index = listRecordQuesMind.get(i)
									.getSelectedItemId();
							if (i != 4) {
								// sbAnswers.append(spRecordQuesMind[i]);
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append("1");
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append(index);
								// sbAnswers.append(CHAR_QUES_BREAK);
								String value = spRecordQuesMind[i]
										+ CHAR_NUM_BREAK + "1" + CHAR_NUM_BREAK
										+ index;
								answerMap.put(spRecordQuesMind[i], value);
								Log.e(spRecordQuesMind[i], value);
							} else {
								// sbAnswers.append(spRecordQuesMind[i]);
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append("4");
								// sbAnswers.append(CHAR_NUM_BREAK);
								// sbAnswers.append(index);
								// if(rqMindFiveChecked){
								//
								// String another =
								// etRecordQuesMind5Other.getText().toString();
								// sbAnswers.append(CHAR_ANS_BREAK);
								// sbAnswers.append(another);
								// }
								// sbAnswers.append(CHAR_QUES_BREAK);
								if (rqMindFiveChecked) {
									String value = spRecordQuesMind[i]
											+ CHAR_NUM_BREAK
											+ "4"
											+ CHAR_NUM_BREAK
											+ index
											+ CHAR_ANS_BREAK
											+ etRecordQuesMind5Other.getText()
													.toString();
									answerMap.put(spRecordQuesMind[i], value);
									Log.e(spRecordQuesMind[i], value);
								} else {
									String value = spRecordQuesMind[i]
											+ CHAR_NUM_BREAK + "4"
											+ CHAR_NUM_BREAK + index;
									answerMap.put(spRecordQuesMind[i], value);
									Log.e(spRecordQuesMind[i], value);
								}
							}
						}

						// 三、生活能力： 1-12
						for (int i = 0; i < listRecordQuesLife.size(); i++) {
							long index = listRecordQuesLife.get(i)
									.getSelectedItemId();
							// sbAnswers.append(spRecordQuesLife[i]);
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append("1");
							// sbAnswers.append(CHAR_NUM_BREAK);
							// sbAnswers.append(index);
							// sbAnswers.append(CHAR_QUES_BREAK);
							String value = spRecordQuesLife[i] + CHAR_NUM_BREAK
									+ "1" + CHAR_NUM_BREAK + index;
							answerMap.put(spRecordQuesLife[i], value);
							Log.e(spRecordQuesLife[i], value);
						}

						// 三、生活能力： 13
						// sbAnswers.append(strReqLife13Number);
						// sbAnswers.append(CHAR_NUM_BREAK);
						// sbAnswers.append("2");
						// sbAnswers.append(CHAR_NUM_BREAK);
						// for(int i=0; i<listRecordQuesCB.size(); i++){
						// if(i != 0){
						// sbAnswers.append(CHAR_BREAK);
						// }
						// if(listRecordQuesCB.get(i).isChecked()){
						// sbAnswers.append("1");
						// }else{
						// sbAnswers.append("0");
						// }
						// }
						sbAnswers.append(strReqLife13Number);
						sbAnswers.append(CHAR_NUM_BREAK);
						sbAnswers.append("2");
						sbAnswers.append(CHAR_NUM_BREAK);
						StringBuffer cbAnswers = new StringBuffer();
						for (int i = 0; i < listRecordQuesCB.size(); i++) {
							if (i != 0) {
								cbAnswers.append(CHAR_BREAK);
							}
							if (listRecordQuesCB.get(i).isChecked()) {
								cbAnswers.append("1");
							} else {
								cbAnswers.append("0");
							}
						}
						// 四、综合评价：
						String value = strReqLife13Number + CHAR_NUM_BREAK
								+ "2" + CHAR_NUM_BREAK + cbAnswers.toString();
						answerMap.put(strReqLife13Number, value);
						Log.e(strReqLife13Number, value);

						value = strReqLife14Number + CHAR_NUM_BREAK + "6"
								+ CHAR_NUM_BREAK
								+ etRecordQuesAll.getText().toString();
						answerMap.put(strReqLife14Number, value);
						Log.e(strReqLife14Number, value);

						updateHourInfo(answerMap);

					}
				});
	}

	/**
	 * 获取48小时观察记录
	 */
	private void getElderLyHourRecord() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("elderlyId", elderlyId);
		Subscriber<HourRecordResponse> subscriber = new Subscriber<HourRecordResponse>() {

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(HourRecordResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList.isSuccess()) {
					if (StringUtils.isNotEmpty(taseList.getMainId())) {
						mainId = taseList.getMainId();

					}

					showData(taseList);

				}
			}
		};
		HttpMethodImp.getInstance().getHourRecord(subscriber, map);
	}

	private void updateHourInfo(Map<String, String> answerMap) {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "updateHourInfo - onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "updateHourInfo - onError");
			}

			@Override
			public void onNext(BaseResponseEntity taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "updateHourInfo - onNext");
				if (taseList.isSuccess()) {
					Toast.makeText(context, "48小时观察记录提交完成!", Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Log.e(TAG, "updateHourInfo - onNext - fail");
				}
			}
		};
		HttpMethodImp.getInstance().updateHourRecord(subscriber, answerMap);
	}

	private void initData() {
		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);
		String name = userInfo.getString("userName", "");
		usrOrg = userInfo.getString("usrOrg", "");
		tvUserName.setText(name);

		userId = getIntent().getStringExtra("userId");
		elderlyId = getIntent().getStringExtra("elderlyId");

		etAnswerNumbers = getResources().getStringArray(
				R.array.ques_heal_number_et);
		etAnswerNumbersNor = getResources().getStringArray(
				R.array.ques_heal_number_et_nor);
		spRecordQuesHealth = getResources().getStringArray(
				R.array.ques_heal_number_sp);
		spRecordQuesMind = getResources().getStringArray(
				R.array.ques_mind_number);
		spRecordQuesLife = getResources().getStringArray(
				R.array.ques_life_number);

		spRecordQuesMindFive = getResources().getStringArray(
				R.array.record_ques_mind_5);

		strReqLife13Number = getResources().getString(
				R.string.record_ques_life_13_number);
		strReqLife14Number = getResources().getString(
				R.string.record_ques_life_14_number);
	}

	private void showData(HourRecordResponse taseList) {
		for (int i = 0; i < taseList.getITEMS().size(); i++) {
			HourQuestionItem item = taseList.getITEMS().get(i);
			if (i <= 2) {
				for (int j = 0; j < etAnswerNumbersNor.length; j++) {
					if (item.getItemId().equals(etAnswerNumbersNor[j])) {
						if (item.getTypeId().equals("3")) {
							listRecordQuesHealthEtNor.get(j).setText(
									item.getAnswer());
						}
					}
				}
			} else if (i > 2 && i <= 7) {
				for (int j = 0; j < etAnswerNumbers.length; j++) {
					if (item.getItemId().equals(etAnswerNumbers[j])) {
						if (item.getTypeId().equals("3")) {
							if (StringUtils.isNotEmpty(item.getAnswer())) {
								String answers[] = item.getAnswer().split(
										CHAR_ANS_BREAK);
								if (answers.length == 1) {
									listRecordQuesHealthEt.get(j * 2).setText(
											answers[0]);
								} else {
									listRecordQuesHealthEt.get(j * 2).setText(
											answers[0]);
									listRecordQuesHealthEt.get(j * 2 + 1)
											.setText(answers[1]);
								}
							}
						}
					}
				}
			} else if (i > 7 && i <= 15) {
				for (int j = 0; j < spRecordQuesHealth.length; j++) {
					if (item.getItemId().equals(spRecordQuesHealth[j])) {
						if (item.getTypeId().equals("1")) {
							if (StringUtils.isNotEmpty(item.getAnsId())) {
								int index = Integer.valueOf(item.getAnsId());
								listRecordQuesHealth.get(j).setSelection(index);
							}

						}
					}
				}
			} else if (i > 15 && i <= 29) {
				for (int j = 0; j < spRecordQuesMind.length; j++) {
					if (item.getItemId().equals(spRecordQuesMind[j])) {
						if (item.getTypeId().equals("1")) {
							if (StringUtils.isNotEmpty(item.getAnsId())) {
								int index = Integer.valueOf(item.getAnsId());
								listRecordQuesMind.get(j).setSelection(index);
							}
						} else if (item.getTypeId().equals("4")) {
							if (StringUtils.isNotEmpty(item.getAnsId())) {
								int index = Integer.valueOf(item.getAnsId());
								listRecordQuesMind.get(j).setSelection(index);
							}
							if (StringUtils.isNotEmpty(item.getAnswer())) {
								etRecordQuesMind5Other
										.setText(item.getAnswer());
							}
						}
					}

				}
			} else if (i > 29 && i <= 42) {
				for (int j = 0; j < spRecordQuesLife.length; j++) {
					if (item.getItemId().equals(spRecordQuesLife[j])) {
						if (item.getTypeId().equals("1")) {
							if (StringUtils.isNotEmpty(item.getAnsId())) {
								int index = Integer.valueOf(item.getAnsId());
								listRecordQuesLife.get(j).setSelection(index);
							}
						}
					}
				}
			} else {
				if (item.getItemId().equals(strReqLife13Number)) {
					if (item.getTypeId().equals("2")) {
						if (StringUtils.isNotEmpty(item.getMultiAns())) {
							String[] items = item.getMultiAns().split(
									CHAR_BREAK);
							if (items.length == 16) {
								for (int n = 0; n < items.length; n++) {
									if (items[n].equals("1")) {
										// listRecordQuesCB.get(n).setSelected(true);
										if (!listRecordQuesCB.get(n)
												.isChecked()) {
											listRecordQuesCB.get(n)
													.setSelected(true);
											listRecordQuesCB.get(n).setChecked(
													true);
										}
									} else {
										listRecordQuesCB.get(n).setSelected(
												false);
										listRecordQuesCB.get(n).setChecked(
												false);
									}
								}
							}
						}
					}
					if (cbRecordQuesLife1.isSelected()) {
						Log.e(TAG, "cbRecordQuesLife1 = " + true);
					} else {
						Log.e(TAG, "cbRecordQuesLife1 = " + false);
					}
				} else if (item.getItemId().equals(strReqLife14Number)) {
					if (item.getTypeId().equals("6")) {
						if (StringUtils.isNotEmpty(item.getAnswer())) {
							etRecordQuesAll.setText(item.getAnswer());
						}
					}
				}
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
