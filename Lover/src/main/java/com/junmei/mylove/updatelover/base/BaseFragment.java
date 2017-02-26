package com.junmei.mylove.updatelover.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by junmei on 2016/10/3.
 */
public abstract class BaseFragment extends Fragment {

    //在父类中统一创建上下文
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    //要求孩子必须实现该方法，并返回view
    public abstract View initView();

    /**
     * 当activity创建好的时候调用
     * 得到Fragment的视图对视图进行数据的设置，联网请求
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 子类不一定要重写该方法,当需要给视图绑定数据，或者联网请求数据并绑定数据的时候就重写该方法
     */
    public void initData() {
    }
}
