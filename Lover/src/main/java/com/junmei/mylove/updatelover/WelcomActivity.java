package com.junmei.mylove.updatelover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.activity.MainActivity;
import com.junmei.mylove.updatelover.utils.DensityUtil;

import java.util.ArrayList;

public class WelcomActivity extends Activity {
    private ViewPager viewpager;
    private LinearLayout ll_point_group;
    private ArrayList<ImageView> listImageViews;
    private Button bt_to_mian;
    private TextView tv_into;
    private  TranslateAnimation translateAnimation1;
    private ScaleAnimation scaleAnimation;

    private int prePosition;//上一次被高亮显示的位置

    private final int[] imageIds={R.drawable.welcome1,
                                    R.drawable.welcome2,
                                    R.drawable.welcome3,
                                    R.drawable.welcome4};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);

            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        viewpager = (ViewPager)findViewById(R.id.viewpager);
        ll_point_group = (LinearLayout)findViewById(R.id.ll_point_group);
        bt_to_mian = (Button)findViewById(R.id.bt_to_mian22);
        tv_into = (TextView)findViewById(R.id.tv_into);

        listImageViews=new ArrayList<>();
        for (int i=0;i<imageIds.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);

            //把imageview 添加到集合中
            listImageViews.add(imageView);

            //创建点
            ImageView pointImagview=new ImageView(this);
            pointImagview.setBackgroundResource(R.drawable.point_selector);//去创建选择器

            int width=DensityUtil.dip2px(this,8);
            int height=DensityUtil.dip2px(this,8);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            //设置点之间的间距
            if(i!=0) {
                int leftMargin=DensityUtil.dip2px(this,8);
                params.leftMargin = leftMargin;
                pointImagview.setEnabled(false);//默认其他点为灰色
            }
            pointImagview.setLayoutParams(params);
            ll_point_group.addView(pointImagview);

        }

        viewpager.setAdapter(new MyPagerAdapter());

        //设置页面改变的监听
        viewpager.setOnPageChangeListener(new MyOnPageChangListener());

        //设置第0个点高亮
        //ll_point_group.getChildAt(0).setEnabled(true);
        ll_point_group.getChildAt(prePosition).setEnabled(true);

        viewpager.setCurrentItem(0);
        handler.sendEmptyMessageDelayed(0, 5000);

        //按钮的监听
        bt_to_mian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //textView文本的监听
        tv_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        translateAnimation1=btnAnimation();
        scaleAnimation=tvAnimation();


    }

    public TranslateAnimation btnAnimation(){
        TranslateAnimation translateAnimation1=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        translateAnimation1.setDuration(800);
        translateAnimation1.setFillAfter(false);
        translateAnimation1.setRepeatCount(100);
        translateAnimation1.setInterpolator(new LinearInterpolator());
        return translateAnimation1;
    }

    public ScaleAnimation tvAnimation(){
        ScaleAnimation scaleAnimation=new ScaleAnimation(1f,1.3f,1f,1.3f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setRepeatCount(100);
        scaleAnimation.setInterpolator(new LinearInterpolator());

        return scaleAnimation;
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return listImageViews==null?0:listImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=listImageViews.get(position);
            //注意一定要记得把图片加到容器中（即viewpager中）。
            container.addView(imageView);

            //给imageView设置触摸事件
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeMessages(0);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(0, 5000);
                            break;
                    }
                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //注意该方法一定要注释掉，否则留着会崩溃
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    private class MyOnPageChangListener implements ViewPager.OnPageChangeListener {
        @Override
        //当页面滚动的时候回调该方法
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        //某个页面被选中的时候回调
        public void onPageSelected(int position) {
            //把之前高亮的点设置为灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前位置的点设置为红色
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition=position;
            
            if(prePosition==imageIds.length-1) {
                bt_to_mian.setVisibility(View.VISIBLE);
                tv_into.setVisibility(View.VISIBLE);
                bt_to_mian.startAnimation(translateAnimation1);
                tv_into.startAnimation(scaleAnimation);
            }else{
                bt_to_mian.setVisibility(View.GONE);
                tv_into.setVisibility(View.GONE);
                bt_to_mian.clearAnimation();
                tv_into.clearAnimation();
            }


        }

        private boolean isDragging=false;
        @Override
        //状态改变的时候回调
        public void onPageScrollStateChanged(int state) {
            if(state==viewpager.SCROLL_STATE_DRAGGING) {
                isDragging=true;
                handler.removeMessages(0);
            }else if(state==viewpager.SCROLL_STATE_IDLE) {
                
            }else if(state==viewpager.SCROLL_STATE_SETTLING && isDragging) {
                isDragging=false;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0,4000);
            }

        }
    }
}
