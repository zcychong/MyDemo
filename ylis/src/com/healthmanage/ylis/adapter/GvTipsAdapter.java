package com.healthmanage.ylis.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.SimpleEntity;
import com.healthmanage.ylis.view.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by YHT on 2016/12/16.
 */

public class GvTipsAdapter extends BaseAdapter {
    private Context mContext;
    private List<SimpleEntity> list = new ArrayList<SimpleEntity>();
    private GridViewForScrollView gridViewForScrollView;
    public GvTipsAdapter(Context context, GridViewForScrollView view,int id){
        mContext = context;
        gridViewForScrollView = view;
        String[] array = context.getResources().getStringArray(id);
        for(String item : array){
            SimpleEntity simpleEntity = new SimpleEntity(item);
            list.add(simpleEntity);
        }
    }

    public String click(int position){
        String type = "2";
        if( position >= 0 && position <= list.size() ){

            if(position == list.size()-1){
                if(list.get(position).getState().equals("0")){
                    for(int i=0;i<list.size(); i++){
                        list.get(i).setState("0");
                    }
                    list.get(position).setState("1");
                    notifyDataSetChanged();
                     return "1";
                }else{
                    list.get(position).setState("0");
                    notifyDataSetChanged();
                    return "0";
                }
            }else{
                if(list.size() > 7){
                    if(position == 6){
                        if(list.get(position).getState().equals("0")){
                            list.get(position).setState("1");
                        }else{
                            list.get(position).setState("0");
                        }
                        notifyDataSetChanged();
                        return "6";
                    }
                }
                if(list.get(position).getState().equals("0")){
                    list.get(position).setState("1");
                    list.get(list.size() -1).setState("0");
                }else{
                    list.get(position).setState("0");
                }
            }
            notifyDataSetChanged();
        }
        return "2";
    }

    public String getCheckText(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<list.size()-1; i++){
            if(list.get(i).getState().equals("1")){
                if(stringBuilder.length() > 0){
                    stringBuilder.append(list.get(i).getStrName());
                }else{
                    stringBuilder.append(list.get(i).getStrName() + ",");
                }

            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.tips_item, null);
            viewholder = new ViewHolder();
            viewholder.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            viewholder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);
            viewholder.ivAddException = (ImageView) convertView.findViewById(R.id.iv_add_exception);
            viewholder.rlTips = (RelativeLayout) convertView.findViewById(R.id.rl_tips);
            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        if(list.get(position) != null){
            if(StringUtils.isNotEmpty(list.get(position).getStrName())){
                viewholder.tvName.setText(list.get(position).getStrName());
            }else{
                viewholder.tvName.setText("");
            }
            if(StringUtils.isNotEmpty(list.get(position).getState())){
                if(list.get(position).getState().equals("1")){
                    viewholder.ivChecked.setVisibility(View.VISIBLE);
                    viewholder.tvName.setTextColor(mContext.getResources().getColor(R.color.tip_border_green));
                    viewholder.rlTips.setBackgroundResource(R.drawable.border_default_tip_bg);
                }else{
                    viewholder.rlTips.setBackgroundResource(R.drawable.border_black);
                    viewholder.tvName.setTextColor(mContext.getResources().getColor(R.color.main_black));
                    viewholder.ivChecked.setVisibility(View.GONE);
                }
            }
        }

        if(position == list.size() -1){
            viewholder.ivAddException.setVisibility(View.VISIBLE);
            viewholder.tvName.setTextColor(mContext.getResources().getColor(R.color.main_black));
        }else{
            viewholder.ivAddException.setVisibility(View.GONE);
        }


        return convertView;
    }
    class ViewHolder {
        TextView tvName;
        ImageView ivChecked, ivAddException;
        RelativeLayout rlTips;

    }
}
