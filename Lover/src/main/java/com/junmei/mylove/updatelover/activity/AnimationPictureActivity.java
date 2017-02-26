package com.junmei.mylove.updatelover.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.service.MusicService;

public class AnimationPictureActivity extends Activity {

    // 用来承载的图片的ImageView
    private ImageView iv;

    // 渐变的图片
    private Drawable[] drawables;

    // 动画
    private Animation[] animations;

    // 记录当前图片的index
    private int currentItem;

    private ScaleAnimation scaleAnimation;

    private Intent intent1;
    private ImageView im_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一进来就开启音乐
        intent1 = new Intent(this, MusicService.class);
        intent1.putExtra("action", "play1");
        startService(intent1);

        //requestWindowFeature(featrueId),它的功能是启用窗体的扩展特性。
        // 参数是Window类中定义的常量。FEATURE_NO_TITLE 无标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animationpicture);

        iv = (ImageView) findViewById(R.id.iv);
        im_return = (ImageView) findViewById(R.id.im_return);

        im_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Toast.makeText(MainActivity.this, "进入了！", Toast.LENGTH_SHORT).show();
//
//
//                //只要页面不见就停止音乐
//                //切换页面是停止音乐
//                intent.putExtra("action", "stop");
//                startService(intent);
//
//                startActivity(new Intent(MainActivity.this,FindActivity.class));
//                // startActivity(new Intent(MainActivity.this,Test.class));
//
//            }
//        });

        drawables = new Drawable[]{
                getResources().getDrawable(R.drawable.pic1),
                getResources().getDrawable(R.drawable.pic2),
                getResources().getDrawable(R.drawable.pic3),
                getResources().getDrawable(R.drawable.pic4),
                getResources().getDrawable(R.drawable.pic5),
                getResources().getDrawable(R.drawable.pic6),
                getResources().getDrawable(R.drawable.pic7)};

        animations = new Animation[]{
                AnimationUtils.loadAnimation(this, R.anim.guide_start),
                AnimationUtils.loadAnimation(this, R.anim.guide_ing),
                AnimationUtils.loadAnimation(this, R.anim.guide_end)};

        // 设置动画持续时间和监听
        for (int i = 0; i < animations.length; i++) {
            animations[i].setDuration(1500);
            animations[i].setAnimationListener(new MyAnimationListener(i));
        }


        iv.setImageDrawable(drawables[0]);
        iv.setAnimation(animations[0]);
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        private int index;

        public MyAnimationListener(int index) {
            this.index = index;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        // 动画结束调用
        @Override
        public void onAnimationEnd(Animation animation) {
            if (index < (animations.length - 1)) {
                iv.startAnimation(animations[index + 1]);
            } else {
                currentItem++;
                if (currentItem > (drawables.length - 1)) {
                    currentItem = 0;//会循环

                    //不会循环，播完直接进入
                    // Intent intent=new Intent(MainActivity.this, FindActivity.class);
                    //  startActivity(intent);
                    //


                }
                iv.setImageDrawable(drawables[currentItem]);
                iv.startAnimation(animations[0]);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        intent1.putExtra("action", "stop");
        startService(intent1);
        stopService(intent1);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次页面重新出现就开始播音乐
        //一进来就开启音乐
        intent1 = new Intent(this, MusicService.class);
        intent1.putExtra("action", "play1");
        startService(intent1);
    }
}
