package com.healthmanage.ylis.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.activity.DoseManagerActivity;
import com.healthmanage.ylis.activity.HoursRecordActivity;
import com.healthmanage.ylis.activity.SendDrugActivity;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.GetRoomListModelTwoResponse;
import com.healthmanage.ylis.model.PeopleItemModelTwo;
import com.healthmanage.ylis.model.RoomItemModelTwo;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.UserRoomItemModelTwoView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class SendDoseFragment extends BaseFragment {

    private Dialog loading;
    private LinearLayout llRooms;
    private View view;

    private String userId, usrOrg, shiftId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_dose, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        getDate();
    }

    private void initView(){
        llRooms = (LinearLayout)view.findViewById(R.id.ll_rooms);
    }

    private void initData(){
        userId = ((DoseManagerActivity)getActivity()).getUserId();
        usrOrg = ((DoseManagerActivity)getActivity()).getUsrOrg();
        shiftId = ((DoseManagerActivity)getActivity()).getShiftId();
//        userId = "1";
    }

    private void getDate(){
        if (Network.checkNet(getContext())) {
            getElderlyList();
        }else{
          Toast.makeText(getContext(), R.string.need_connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void getElderlyList(){
        loading = LoadingDialog.loadingFind(getContext());
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("org", usrOrg);
        map.put("platType", "3");
        map.put("goodType", "11");


        Subscriber<GetRoomListModelTwoResponse> subscriber = new Subscriber<GetRoomListModelTwoResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
            }

            @Override
            public void onNext(GetRoomListModelTwoResponse taseList) {
                Log.e(TAG, "onNext");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                if (taseList != null) {
                    if (taseList.isSuccess()) {
//                        response = taseList;
                        dealData(taseList);
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }

                } else {
                    Log.e(TAG, "fail - null");
                }

            }
        };
        HttpMethodImp.getInstance().getSomeElderlyList(subscriber, map);

    }

    private void dealData(GetRoomListModelTwoResponse taseList) {
        if (taseList.isSuccess()) {
            if (llRooms.getChildCount() > 0) {
                llRooms.removeAllViews();
            }
            for (final RoomItemModelTwo item : taseList.getITEMS()) {
                final UserRoomItemModelTwoView roomItem = new UserRoomItemModelTwoView(
                        getActivity(), item);
                roomItem.getGvRoomList().setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int position, long arg3) {
                                PeopleItemModelTwo tempItem = item.getBeds().get(position);
                                if(StringUtils.isNotEmpty(tempItem.getElderlySta())){
                                    if(!tempItem.getElderlySta().equals("3")){
                                        setItemCheckd(tempItem, roomItem);
                                    }
                                }else{
                                    setItemCheckd(tempItem, roomItem);
                                }
                            }
                        });

                llRooms.addView(roomItem);
//                items.add(roomItem);
            }
        } else {
            Log.e(TAG, "fail -" + taseList.getMessage());
        }
    }

    private void setItemCheckd(PeopleItemModelTwo tempItem, UserRoomItemModelTwoView roomItem){
        Intent intent = new Intent(getActivity(), SendDrugActivity.class);
        intent.putExtra("elderlyName", tempItem.getElderlyName());
        intent.putExtra("elderlyId", tempItem.getElderlyId());
        intent.putExtra("elderlyRoom", roomItem.getRoomId());
        intent.putExtra("elderlyBed", tempItem.getBedNo());
        intent.putExtra("userId", userId);
        intent.putExtra("usrOrg", usrOrg);
        intent.putExtra("shiftId", shiftId);

        startActivityForResult(intent, 2);
//        if (StringUtils.isNotEmpty(tempItem.getWczt())) {
//            if (tempItem.getWczt().equals("0")) {
//                tempItem.setWczt("8");
//            } else if (tempItem.getWczt().equals("8")) {
//                tempItem.setWczt("0");
//            }
//        }else{
//            tempItem.setWczt("8");
//        }
//        roomItem.notifyDataSetChanged();
    }



    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
