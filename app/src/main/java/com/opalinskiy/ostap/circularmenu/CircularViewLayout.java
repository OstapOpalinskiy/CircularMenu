package com.opalinskiy.ostap.circularmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Evronot on 24.05.2016.
 */
public class CircularViewLayout extends ViewGroup {
    private CircularMenuView pieView;
    private GestureDetector detector;

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

//    public CircularViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void init() {
        Log.d("TAG", "init() in layout");
        setWillNotDraw(false);
        pieView = new CircularMenuView(getContext());
        this.addView(pieView);
        detector = new GestureDetector(getContext(), new GestureListener());
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("TAG", "onMeasure in layout");

        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        int minh = w  + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int diameter = Math.min(w, h);
        pieView.layout(0, 0, diameter, diameter);
        Log.d("TAG", "diameter from view.layout()" +  diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TAG", "onDraw in layout");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TAG", "onTouch()");
        boolean result = detector.onTouchEvent(event);
        return result;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("TAG", "onScroll()");
            // Set the pie rotation directly.
            float scrollTheta = vectorToScalarScroll(
                    distanceX,
                    distanceY,
                    e2.getX() - pieView.getCenterX(),
                    e2.getY() - pieView.getCenterY());
            pieView.rotateTo(pieView.getRotation() - (int) scrollTheta / 4);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("TAG", "onDown()");
            // The user is interacting with the pie, so we want to turn on acceleration
            // so that the interaction is smooth.
//            pieView.accelerate();
//            if (isAnimationRunning()) {
//                stopScrolling();
//            }
            return true;
        }
    }


    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        // get the length of the vector
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        // decide if the scalar should be negative or positive by finding
        // the dot product of the vector perpendicular to (x,y).
        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }
}
