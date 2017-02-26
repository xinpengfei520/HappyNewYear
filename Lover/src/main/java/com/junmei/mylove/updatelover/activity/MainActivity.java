package com.junmei.mylove.updatelover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.base.BaseFragment;
import com.junmei.mylove.updatelover.fragment.PictureFragment;
import com.junmei.mylove.updatelover.fragment.ResultFragment;
import com.junmei.mylove.updatelover.fragment.SearchFragment;
import com.junmei.mylove.updatelover.fragment.VideoFragment;
import com.junmei.mylove.updatelover.service.MusicService;
import com.junmei.mylove.updatelover.view.WaveView;

import java.util.ArrayList;

/**
 * 功能实现RadioGroup的切换
 */
// 必须要继承FragmentActivity，否则后面的getSupportFragmentManager()方法就不能用
public class MainActivity extends FragmentActivity {
    private ArrayList<BaseFragment> listFragments;
    private Fragment content;
    private RadioGroup rg_main;
    private int position;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MusicService.class);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        //1.用list来放各个fragment
        initFragment();

        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_searchsecret);
    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_searchsecret:
                    position = 0;
                    intent.putExtra("action", "play");
                    startService(intent);
                    break;

                case R.id.rb_youinmyeye:
                    position = 1;
                    //停止第0个音乐
                    intent.putExtra("action", "stop");
                    startService(intent);
                    stopService(intent);
                    WaveView.popupWindow.dismiss();

//                    //开启该fragment对应的音乐
//                    intent.putExtra("action", "play");
//                    startService(intent);
//                    Intent intent1=new Intent(MainActivity.this, AnimationPictureActivity.class);
//                    startActivity(intent1);
                    break;

                case R.id.rb_iwanttosay:
                    position = 2;
                    intent.putExtra("action", "stop");
                    startService(intent);
                    stopService(intent);
                    WaveView.popupWindow.dismiss();
                    break;

                case R.id.rb_waitresult:
                    position = 3;
                    intent.putExtra("action", "stop");
                    startService(intent);
                    stopService(intent);
                    WaveView.popupWindow.dismiss();
                    break;
            }

            //根据位置得到相应的fragment,即要显示的那个fragment
            Fragment toFragment = getFragment(position);
            //接着便跳转到要显示的fragment
            switchFragment(content, toFragment);
        }
    }


    private void initFragment() {
        //把集合创建
        listFragments = new ArrayList<>();
        //添加的时候要按一定顺序
        listFragments.add(new SearchFragment());//1
        listFragments.add(new PictureFragment());//2
        listFragments.add(new VideoFragment());//3
        listFragments.add(new ResultFragment());//4
    }

    //得到fragment
    private Fragment getFragment(int position) {
        return listFragments.get(position);
    }

    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        // Fragment fragment=getFragment(0);
        // 如果当前的和要去显示的不相等，我们才去显示
        if (content != toFragment) {
            // content=toFragment;
            if (toFragment != null) {  //下一个要t显示的不能为空
                // 不为空则开启事物

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!toFragment.isAdded()) { //判断下一个要显示的fragment添加过了没有
                    //如果没有添加过
                    //则先隐藏之前显示的(在操作fromFragment之前要先判断它不为空，否则会空指针的)
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //再添加下一个要显示的(！！！添加后还要记得提交)
                    transaction.add(R.id.fl_main_container, toFragment).commit();
                } else {
                    //若已经添加过了
                    //则也要先隐藏之前显示的
                    transaction.hide(fromFragment);
                    //在直接显示toFragment就可以了 (！也要记得提交)
                    transaction.show(toFragment).commit();
                }
                content = toFragment;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        intent.putExtra("action", "stop");
        startService(intent);
        stopService(intent);
    }
}
