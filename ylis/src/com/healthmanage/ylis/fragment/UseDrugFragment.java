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
import com.healthmanage.ylis.activity.AddDrugInfoActivity;
import com.healthmanage.ylis.activity.DoseManagerActivity;
import com.healthmanage.ylis.activity.ElderlyDoseDetailActivity;
import com.healthmanage.ylis.activity.TaskDetailActivity;
import com.healthmanage.ylis.activity.UserMonitorActivity;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.model.GetRoomListModelTwoResponse;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.UserRoomItemView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class UseDrugFragment extends BaseFragment {
    private Dialog loading;
    private LinearLayout llAddDrug;
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
        view = inflater.inflate(R.layout.fragment_use_drug, container, false);
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
        llAddDrug = (LinearLayout)view.findViewById(R.id.ll_add_drug);
        llRooms = (LinearLayout)view.findViewById(R.id.ll_rooms);

        llAddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDrugInfoActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("usrOrg", usrOrg);
                intent.putExtra("shiftId", shiftId);
                startActivity(intent);

            }
        });
    }

    private void initDate(){
        userId = ((DoseManagerActivity)getActivity()).getUserId();
        usrOrg = ((DoseManagerActivity)getActivity()).getUsrOrg();
        shiftId = ((DoseManagerActivity)getActivity()).getShiftId();
        userId = "1";
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
        map.put("goodType", "11");
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
                                    Intent intent = new Intent(getActivity(), ElderlyDoseDetailActivity.class);
                                    intent.putExtra("usrOrg", usrOrg);
                                    intent.putExtra("elderlyName", taseList.getITEMS().get(index).getBeds().get(position)
                                            .getElderlyName());
                                    intent.putExtra("bedNo", taseList.getITEMS().get(index).getBeds().get(position)
                                            .getBedNo());
                                    intent.putExtra("roomNo", taseList.getITEMS().get(index).getRoomNo());
                                    intent.putExtra("elderlyId", taseList.getITEMS().get(index).getBeds().get(position)
                                        .getElderlyId());
                                    startActivity(intent);
//                                    if(!taseList.getITEMS().get(index).getBeds()
//                                            .get(position).getElderlySta().equals("3")){
//                                        Intent intent = new Intent(getContext(),
//                                                TaskDetailActivity.class);
//                                        intent.putExtra("userId", userId);
////                                        intent.putExtra("shiftId", shiftId);
//                                        intent.putExtra("ishous", taseList
//                                                .getITEMS().get(index).getBeds()
//                                                .get(position).getIshous());
//                                        intent.putExtra("elderlyId", taseList
//                                                .getITEMS().get(index).getBeds()
//                                                .get(position).getElderlyId());
//                                        intent.putExtra("bedId", taseList
//                                                .getITEMS().get(index).getBeds()
//                                                .get(position).getBedNo());
//                                        intent.putExtra("peoName", taseList
//                                                .getITEMS().get(index).getBeds()
//                                                .get(position).getElderlyName());
//                                        intent.putExtra("roomId", taseList
//                                                .getITEMS().get(index).getRoomNo());
//                                        Log.e(TAG, "elderlyId="
//                                                + taseList.getITEMS().get(index)
//                                                .getBeds().get(position)
//                                                .getElderlyId());
//                                        startActivityForResult(intent, 0);
//                                    }
//                                    Intent intent = new Intent(getActivity(), AddDrugInfoActivity.class);
//                                    intent.putExtra("userId", userId);
//                                    intent.putExtra("usrOrg", usrOrg);
//                                    intent.putExtra("shiftId", shiftId);
////                                    intent.putExtra("elderlyId", taseList.getITEMS().get(index).getBeds().get(position)
////                                        .getElderlyId());
//                                    startActivity(intent);
                                }
                            });
                }

//                for (int i = 0; i < taseList.getITEMS().size(); i++) {
//                    for (int j = 0; j < taseList.getITEMS().get(i).getBeds()
//                            .size(); j++) {
//                        if (i != 0 || j != 0) {
//                            peopleNames.append(",");
//                        }
//                        peopleNames.append(taseList.getITEMS().get(i).getBeds()
//                                .get(j).getElderlyName());
//                    }
//                }
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
