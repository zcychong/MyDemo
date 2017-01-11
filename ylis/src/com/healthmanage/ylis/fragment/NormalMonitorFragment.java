package com.healthmanage.ylis.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.activity.DeviceManagerActivity;
import com.healthmanage.ylis.activity.DoseManagerActivity;
import com.healthmanage.ylis.activity.UserMonitorActivity;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.UserRoomItemView;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import rx.Subscriber;

public class NormalMonitorFragment extends BaseFragment {
    private Dialog loading;
    private LinearLayout llRooms;
    private View view;
    private LinearLayout llDeviceManager;

    private String userId, usrOrg, shiftId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_normal_monitor, container, false);




        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initDate();

        getData();
    }

    private void initView(){
        llDeviceManager = (LinearLayout)view.findViewById(R.id.ll_device_manager);
        llDeviceManager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeviceManagerActivity.class));
            }
        });

        llRooms = (LinearLayout)view.findViewById(R.id.ll_rooms);
    }

    private void initDate(){
        userId = ((DoseManagerActivity)getActivity()).getUserId();
        usrOrg = ((DoseManagerActivity)getActivity()).getUsrOrg();
        shiftId = ((DoseManagerActivity)getActivity()).getShiftId();
    }

    private void getData(){
        if(Network.checkNet(getContext())){
            getPeopleListModel1();
        }else{
            Toast.makeText(getContext(), R.string.need_connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void getPeopleListModel1() {
        loading = LoadingDialog.loadingFind(getActivity());
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("org", usrOrg);
        map.put("platType", "3");
        map.put("goodType", "12");
        Subscriber<TastListResponse> subscriber = new Subscriber<TastListResponse>() {

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(final TastListResponse taseList) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, "onNext");
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        dealResponseData(taseList);
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getSomeElderlyList1(subscriber, map);
    }

    private void dealResponseData(final TastListResponse taseList) {
        if (llRooms.getChildCount() > 0) {
            llRooms.removeAllViews();
        }
        if (taseList != null) {
            if (taseList.isSuccess()) {
                // tvTitle.setText(taseList.getOrgName());
                for (int i = 0; i < taseList.getITEMS().size(); i++) {
                    final int index = i;
                    UserRoomItemView roomView = new UserRoomItemView(getActivity(),
                            taseList.getITEMS().get(i));
                    llRooms.addView(roomView);
                    roomView.getGvRoomList().setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int position, long arg3) {

                                    Intent intent = new Intent(getActivity(), UserMonitorActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("usrOrg", usrOrg);
                                    intent.putExtra("elderlyId", taseList.getITEMS().get(index).getBeds().get(position).getElderlyId());
                                    intent.putExtra("elderlyName", taseList.getITEMS().get(index).getBeds().get(position).getElderlyName());
                                    intent.putExtra("roomNo", taseList.getITEMS().get(index).getRoomNo());
                                    intent.putExtra("bedNo", taseList.getITEMS().get(index).getBeds().get(position).getBedNo());
                                    startActivity(intent);
                                }
                            });
                }

            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(getActivity());

    }
}
