package com.junmei.mylove.updatelover.domain;

import android.graphics.Paint;

public class CirCle {
    private float cx;
    private float cy;
    private float radius;
    private Paint paint;

    public CirCle(float cx, float cy, float radius, Paint paint) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.paint = paint;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "cx=" + cx +
                ", cy=" + cy +
                ", radius=" + radius +
                ", paint=" + paint +
                '}';
    }
}
