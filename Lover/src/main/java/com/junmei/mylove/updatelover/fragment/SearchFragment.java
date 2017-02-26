package com.junmei.mylove.updatelover.fragment;

import android.content.Intent;
import android.view.View;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.base.BaseFragment;

/**
 * Created by junmei on 2016/10/3.
 */
public class SearchFragment extends BaseFragment {
    private Intent intent;

//    private TextView textView;

    /**
     * 初始化视图
     */
    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.search_secret, null);

//        textView=new TextView(context);
//        textView.setTextSize(20);
//        textView.setTextColor(Color.RED);
//        textView.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
//        textView.setText("挖掘秘密~");
    }
}
