package com.junmei.mylove.updatelover.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by junmei on 2016/10/5.
 */
public class VideoView extends android.widget.VideoView {
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVideoSize(int videoWith,int videoHeight){
        ViewGroup.LayoutParams l=getLayoutParams();
        l.width=videoWith;
        l.height=videoHeight;
        setLayoutParams(l);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}
