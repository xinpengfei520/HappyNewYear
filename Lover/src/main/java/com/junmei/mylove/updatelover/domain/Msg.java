package com.junmei.mylove.updatelover.domain;


import android.widget.LinearLayout;
import android.widget.TextView;

public class Msg {
    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private int type;
    private LinearLayout linear_left;
    private LinearLayout linear_right;
    private TextView mes_left;
    private TextView mes_right;
    private String content;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public void setLayout(LinearLayout linear) {
        if (type == TYPE_LEFT) {
            linear_left = linear;
        } else {
            linear_right = linear;
        }
    }

    public LinearLayout getLayout() {
        if (type == TYPE_LEFT) {
            return linear_left;
        } else {
            return linear_right;
        }
    }

    public void setTextView(TextView textView) {
        if (type == TYPE_LEFT) {
            this.mes_left = textView;
        } else {
            this.mes_right = textView;
        }
    }

    public TextView getTextView() {
        if (type == TYPE_LEFT) {
            return mes_left;
        } else {
            return mes_right;
        }
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
