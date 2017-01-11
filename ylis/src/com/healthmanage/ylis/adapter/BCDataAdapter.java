package com.healthmanage.ylis.adapter;

import java.util.ArrayList;
import java.util.List;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.deviceentity.BCData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BCDataAdapter extends BaseAdapter{
	private Context mContext;
	private List<BCData> listBcDatas = new ArrayList<BCData>();
	
	public BCDataAdapter(Context context, List<BCData> list){
		mContext = context;
		for(BCData listBcData : list){
			listBcDatas.add(listBcData);
		}
	}
	
	public void setData( List<BCData> list){
		listBcDatas.clear();
		for(BCData listBcData : list){
			listBcDatas.add(listBcData);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(listBcDatas != null && listBcDatas.size() != 0){
			return listBcDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if(listBcDatas != null && listBcDatas.size() != 0){
			return listBcDatas.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
    	ViewHolder viewholder;
        if(convertView==null){  
        	view = View.inflate(mContext, R.layout.cdisease_bc,null);
        	viewholder = new ViewHolder();
        	viewholder.tvUroVal = (TextView)view.findViewById(R.id.tv_uroVal);
        	viewholder.tvBldVal = (TextView)view.findViewById(R.id.tv_bldVal);
        	viewholder.tvBilVal = (TextView)view.findViewById(R.id.tv_bilVal);
        	viewholder.tvKetVal = (TextView)view.findViewById(R.id.tv_ketVal);
        	viewholder.tvGluVal = (TextView)view.findViewById(R.id.tv_gluVal);
        	viewholder.tvProVal = (TextView)view.findViewById(R.id.tv_proVal);
        	viewholder.tvPhVal = (TextView)view.findViewById(R.id.tv_phVal);
        	viewholder.tvNitVal = (TextView)view.findViewById(R.id.tv_nitVal);
        	viewholder.tvLeuVal = (TextView)view.findViewById(R.id.tv_leuVal);
        	viewholder.tvSgVal = (TextView)view.findViewById(R.id.tv_sgVal);
        	viewholder.tvVcVal = (TextView)view.findViewById(R.id.tv_vcVal);
        	
        	viewholder.tvTime = (TextView)view.findViewById(R.id.bcTime);
        	viewholder.tvDate = (TextView)view.findViewById(R.id.bc_date);
        	
        	view.setTag(viewholder);
        }else{  
        	view = convertView;
        	viewholder = (ViewHolder) view.getTag();  
        }
        BCData temp = listBcDatas.get(position);
        viewholder.tvUroVal.setText(temp.getUroVal());
    	viewholder.tvBldVal.setText(temp.getBldVal());
    	viewholder.tvBilVal.setText(temp.getBilVal());
    	viewholder.tvKetVal.setText(temp.getKetVal());
    	viewholder.tvGluVal.setText(temp.getGluVal());
    	viewholder.tvProVal.setText(temp.getProVal());
    	viewholder.tvPhVal.setText(temp.getPhVal());
    	viewholder.tvNitVal.setText(temp.getNitVal());
    	viewholder.tvLeuVal.setText(temp.getLeuVal());
    	viewholder.tvSgVal.setText(temp.getSgVal());
    	viewholder.tvVcVal.setText(temp.getVcVal());
    	
    	viewholder.tvTime.setText(temp.getTime());
    	viewholder.tvDate.setText(temp.getDate());
        
		return view;
	}
	
	class ViewHolder{
		TextView tvUroVal;
		TextView tvBldVal;
		TextView tvBilVal;
		TextView tvKetVal;
		TextView tvGluVal;
		TextView tvProVal;
		TextView tvPhVal;
		TextView tvNitVal;
		TextView tvLeuVal;
		TextView tvSgVal;
		TextView tvVcVal;
		TextView tvTime;
		TextView tvDate;
	}

}
