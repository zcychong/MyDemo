package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by YHT on 2016/12/23.
 */

public class ChooseItemActivity extends BaseActivity {
    private Activity context;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.gv_list) GridViewForScrollView gvList;
    @Bind(R.id.et_new_unit) EditText etNewUnit;
    @Bind(R.id.tv_new_unit_commit) TextView tvNewUnitCommit;
    private String[] items;
    private int type;
    private GvSimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);

        context = this;

        initView();
    }

    private void initView() {
        ButterKnife.bind(context);

        llBack.setVisibility(View.VISIBLE);
        RxView.clicks(llBack)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });


        RxView.clicks(tvNewUnitCommit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String str = etNewUnit.getText().toString();
                        if(StringUtils.isNotEmpty(str)){
                            Intent intent = new Intent();
                            intent.putExtra("name", str);
                            setResult(type,intent);
                            finish();
                        }else{
                            Toast.makeText(context, "请输入自定义单位!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        type = getIntent().getIntExtra("type", 0);
        Log.e(TAG, "111111 --" + type);

        if(type== 0){
            items = getResources().getStringArray(R.array.drug_unit);
            tvTitle.setText("请选择药品单位");
        }else if(type == 1){
            items = getResources().getStringArray(R.array.drug_unit);
            tvTitle.setText("请选择每次药品用量单位");
        }
        else if(type== 2){
            items = getResources().getStringArray(R.array.drug_frequency);
            tvTitle.setText("请选择用药频次");
        }else if(type== 3){
            items = getResources().getStringArray(R.array.drug_usage);
            tvTitle.setText("请选择药品用法");
        }

        adapter = new GvSimpleAdapter(context, items);
        gvList.setAdapter(adapter);

        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("name", items[position]);
                setResult(type,intent);
                finish();
            }
        });

    }

    class GvSimpleAdapter extends BaseAdapter{
        private String[] items;
        private Context mContext;
        private GvSimpleAdapter(Context context, String[] list){
            items = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.sannitation_item, null);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_sannitation);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

//            TextView tvName = (TextView)convertView.findViewById(R.id.tv_sannitation);

            viewHolder.tvName.setText(items[position]);

            return convertView;
        }

        class ViewHolder {
            TextView tvName;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
