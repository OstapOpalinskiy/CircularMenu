package com.opalinskiy.ostap.circularmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;


public class CircularViewLayout extends ViewGroup {
    private CircularMenuView pieView;

    public CircularViewLayout(Context context) {
        super(context);
        init();
    }

    public CircularViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d("TAG", "init() in layout");
        setWillNotDraw(false);
        pieView = new CircularMenuView(getContext());
        this.addView(pieView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("TAG", "onMeasure in layout");
        int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minWidth, MeasureSpec.getSize(widthMeasureSpec));
        int minHeight = w + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minHeight);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int diameter = Math.min(w, h);
        pieView.layout(0, 0, diameter, diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
