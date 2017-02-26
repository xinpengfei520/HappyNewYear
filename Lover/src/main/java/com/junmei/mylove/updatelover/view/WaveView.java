package com.junmei.mylove.updatelover.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.domain.CirCle;
import com.junmei.mylove.updatelover.utils.DensityUtil;

import java.util.Random;

/**
 * Created by junmei on 2016/9/25.
 * 功能：说波纹的小创新
 * 当点击屏幕时，显示放射状水波纹，并且会随机显示文本内容
 */
public class WaveView extends View {
    private CirCle circle;
    private Paint paint;
    private Paint paintText;

    private int width;       //222
    private int height;         //222
    private Context context1;       //222
    private View popupView;          //222
    public static PopupWindow popupWindow;     //222
    private ScaleAnimation scaleAnimation;    //222
    private int num = 1;           //222

    //随机色
    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.rgb(0, 153, 0),
            Color.rgb(223, 17, 199), Color.rgb(84, 20, 132)};
    private String[] texts = new String[]{"我好喜欢你", "我要说说我自己", "我不高", "我不白", "我也不美"
            , "但我乐观积极", "我喜欢运动", "喜欢美食", "喜欢美景", "当然我最喜欢的", "就是你！", "对，就是你！！！"};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    // 以圆弧宽度为停止条件
                    if (paint.getStrokeWidth() == 333) {
                        // paintText=null;
                        //paintText.setAlpha(0);
                        circle = null;
                        return;
                    }
                    paint.setStrokeWidth(paint.getStrokeWidth() + 1);
                    paint.setAlpha(paint.getAlpha() - 5);
//                    int num3= DensityUtil.dip2px(context1,3);
                    circle.setRadius(circle.getRadius() + 3);
                    invalidate();
                    handler.sendEmptyMessageDelayed(1, 30);
                    break;
            }
        }
    };

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画圆环的画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        //2.初始化画文本的画笔
        paintText = new Paint();
//        int num50=DensityUtil.dip2px(context1,50);
        paintText.setTextSize(50);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);

        paintText.setColor(Color.WHITE);

        context1 = getContext();   //222
    }

    private int i = 0;   //222

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = canvas.getWidth();   //222
        height = canvas.getHeight() / 3;   //222

        if (num == 1) {
            showPop();
        }
        num++;

        if (circle != null) {
            Log.e("TAG", "circle.getRadius()==" + circle.getRadius());
            Log.e("TAG", "paint.getAlpha()==" + paint.getAlpha());
            Log.e("TAG", "paint.getStrokeWidth()==" + paint.getStrokeWidth());
            canvas.drawCircle(circle.getCx(), circle.getCy(), circle.getRadius(), circle.getPaint());

            //2.画文本
            if (i < texts.length) {       //333
//                int num100=DensityUtil.dip2px(context1,100);
                canvas.drawText(texts[i], circle.getCx() - 100, circle.getCy(), paintText);   //333
            } else {
                i = 0;    //333
            }
//            canvas.drawText(texts[i],circle.getCx()-100,circle.getCy(),paintText);
        }
    }

    private float eventX;
    private float eventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);/////
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                popupWindow.dismiss();       //222
//                i=new Random().nextInt(texts.length);
                i++;
                eventX = event.getX();
                eventY = event.getY();

                //每次down的时候重新将圆弧宽度设为5
                paint.setStrokeWidth(282);
                paint.setColor(colors[new Random().nextInt(colors.length)]);
//                int num10=DensityUtil.dip2px(context1,10);
                circle = new CirCle(eventX, eventY, 10, paint);

                invalidate();//为了保证它会去掉onDraw()方法

                //发消息实现动态变化
                handler.sendEmptyMessageDelayed(1, 30);

                break;
        }
        return true;
    }


    private void showPop() {            //222
        popupView = View.inflate(context1, R.layout.pw_linearlayout, null);
        //参数2,3：指明popupwindow的宽度和高度
        int num50 = DensityUtil.dip2px(context1, 50);
        popupWindow = new PopupWindow(popupView, width - num50, height);

        //设置背景图片， 必须设置，不然动画没作用
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //创建缩放动画
        scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        scaleAnimation.setDuration(1000);

        //显示
        int num30 = DensityUtil.dip2px(context1, 30);
        int num70 = DensityUtil.dip2px(context1, 70);
        popupWindow.showAsDropDown(this, num30, -height * 3 + num70);       //222

        //设置动画
        popupView.startAnimation(scaleAnimation);
    }

}
