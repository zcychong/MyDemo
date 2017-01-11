package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.fragment.NormalMonitorFragment;
import com.healthmanage.ylis.fragment.SendDoseFragment;
import com.healthmanage.ylis.fragment.UseDrugFragment;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class DoseManagerActivity extends BaseFragmentActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;

    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.vp_fragments) ViewPager vpFragments;
    @Bind(R.id.fl_fragment) FrameLayout flFragment;

    @Bind(R.id.ll_send_drug) LinearLayout llSendDrug;
    @Bind(R.id.iv_send_drug) ImageView ivSendDrug;
    @Bind(R.id.tv_send_drug) TextView tvSendDrug;

    @Bind(R.id.ll_use_drug) LinearLayout llUseDrug;
    @Bind(R.id.iv_use_drug) ImageView ivUseDrug;
    @Bind(R.id.tv_use_drug) TextView tvUseDrug;

    @Bind(R.id.ll_normal_monitor) LinearLayout llNormalMonitor;
    @Bind(R.id.iv_nromal_monitor) ImageView ivNromalMonitor;
    @Bind(R.id.tv_normal_monitor) TextView tvNormalMonitor;

    private String userId, shiftId, usrOrg;
    private SendDoseFragment sendDoseFragment;
    private UseDrugFragment useDrugFragment;
    private NormalMonitorFragment normalMonitorFragment;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentManager fragmentManager;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dose_manager);

        context = this;

        initView();
        initData();
    }

    private void initView() {
        ButterKnife.bind(context);

        llBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.drawable.icon_setting);
        tvBackText.setText("设置");

        RxView.clicks(llBack).
                subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(context, SettingInfoActivity.class).
                                putExtra("userId", userId));
                    }
                });


        fragmentManager = getSupportFragmentManager();

        sendDoseFragment = new SendDoseFragment();
        useDrugFragment = new UseDrugFragment();
        normalMonitorFragment = new NormalMonitorFragment();

        fragmentList.add(sendDoseFragment);
        fragmentList.add(useDrugFragment);
        fragmentList.add(normalMonitorFragment);

//        initDefaultFragment();

        pagerAdapter = new MyPagerAdapter(fragmentManager, fragmentList);
        vpFragments.setAdapter(pagerAdapter);
        vpFragments.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changeTabBtnState(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        RxView.clicks(llSendDrug)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        changeTabBtnState(0);
                        vpFragments.setCurrentItem(0);
                    }
                });

        RxView.clicks(llUseDrug)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        changeTabBtnState(1);
                        vpFragments.setCurrentItem(1);
                    }
                });

        RxView.clicks(llNormalMonitor)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        changeTabBtnState(2);
                        vpFragments.setCurrentItem(2);
                    }
                });
    }

    private void changeTabBtnState(int position){
        cleanState();
        switch (position){
            case 0:
                tvTitle.setText("发药管理");
                ivSendDrug.setImageResource(R.drawable.icon_dose_send_manager_choosed);
                tvSendDrug.setTextColor(getResources().getColor(R.color.main_color));
                break;
            case 1:
                tvTitle.setText("用药管理");
                ivUseDrug.setImageResource(R.drawable.icon_user_drug_manager_choosed);
                tvUseDrug.setTextColor(getResources().getColor(R.color.main_color));
                break;
            case 2:
                tvTitle.setText("日常监测");
                ivNromalMonitor.setImageResource(R.drawable.icon_normal_monitor_choosed);
                tvNormalMonitor.setTextColor(getResources().getColor(R.color.main_color));
                break;
        }
    }

    private void cleanState(){
        ivSendDrug.setImageResource(R.drawable.icon_dose_send_manager_unchoosed);
        tvSendDrug.setTextColor(getResources().getColor(R.color.main_black));
        ivUseDrug.setImageResource(R.drawable.icon_user_drug_manager_unchoosed);
        tvUseDrug.setTextColor(getResources().getColor(R.color.main_black));
        ivNromalMonitor.setImageResource(R.drawable.icon_normal_monitor_unchoosed);
        tvNormalMonitor.setTextColor(getResources().getColor(R.color.main_black));
    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");

        tvTitle.setText("发药管理");
    }

//    private void initDefaultFragment(){
//        showFragment(1);
//    }
//
//    private void showFragment(int id){
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        hideFragments(fragmentTransaction);
//        switch (id){
//            case 1:
//                if(sendDoseFragment != null){
//                    fragmentTransaction.show(sendDoseFragment);
//                }else{
//                    sendDoseFragment = new SendDoseFragment();
//                    fragmentTransaction.add(R.id.fl_fragment, sendDoseFragment);
//                }
//            break;
//            case  2:
//                if(useDrugFragment != null){
//                    fragmentTransaction.show(useDrugFragment);
//                }else{
//                    useDrugFragment = new UseDrugFragment();
//                    fragmentTransaction.add(R.id.fl_fragment, useDrugFragment);
//                }
//            break;
//            case  3:
//                if(normalMonitorFragment != null){
//                    fragmentTransaction.show(normalMonitorFragment);
//                }else{
//                    normalMonitorFragment = new NormalMonitorFragment();
//                    fragmentTransaction.add(R.id.fl_fragment, normalMonitorFragment);
//                }
//            break;
//        }
//
//        fragmentTransaction.commit();
//    }
//
//    public void hideFragments(FragmentTransaction ft) {
//        if (sendDoseFragment != null){
//            ft.hide(sendDoseFragment);
//        }
//        if (useDrugFragment != null){
//            ft.hide(useDrugFragment);
//        }
//        if (normalMonitorFragment != null) {
//            ft.hide(normalMonitorFragment);
//        }
//    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        FragmentManager fm;
        List<Fragment> fragmentList;
        public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> list) {
            super(fragmentManager);
            fm = fragmentManager;
            fragmentList = list;
        }

        @Override
        public Fragment getItem(int i) {
            Log.e(TAG, "" + i);
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsrOrg() {
        return usrOrg;
    }

    public void setUsrOrg(String usrOrg) {
        this.usrOrg = usrOrg;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
