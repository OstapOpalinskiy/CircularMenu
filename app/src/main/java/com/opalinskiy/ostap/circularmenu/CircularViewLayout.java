package com.opalinskiy.ostap.circularmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Evronot on 24.05.2016.
 */
public class CircularViewLayout extends ViewGroup {

    private CircularMenuView pieView;
//    private GestureDetector detector;
//    private Scroller scroller;
//    private ValueAnimator scrollAnimator;

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

//        detector = new GestureDetector(getContext(), new GestureListener());
//        scroller = new Scroller(getContext(), null, true);
//        scrollAnimator = ValueAnimator.ofFloat(0, 1);
//        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                tickScrollAnimation();
//            }
//        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("TAG", "onMeasure in layout");
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        int minh = w + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int diameter = Math.min(w, h);
        pieView.layout(0, 0, diameter, diameter);
    //    Log.d("TAG", "diameter from view.layout()" + diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  Log.d("TAG", "onDraw in layout");
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean result = detector.onTouchEvent(event);
//        return result;
//    }
//
//    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            // Set the pie rotation directly.
//            float scrollTheta = vectorToScalarScroll(
//                    distanceX, distanceY,
//                    e2.getX() - pieView.getCenterX(),
//                    e2.getY() - pieView.getCenterY());
//            pieView.rotateTo(pieView.getRotation() - (int) scrollTheta / 4);
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            float scrollTheta = vectorToScalarScroll(
//                    velocityX,
//                    velocityY,
//                    e2.getX() - pieView.getCenterX(),
//                    e2.getY() - pieView.getCenterY());
//
//            scroller.fling(0, (int) pieView.getRotation(),
//                    0, (int) scrollTheta / 8,
//                    0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//            scrollAnimator.setDuration(scroller.getDuration());
//            scrollAnimator.start();
//            return true;
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//        //    Log.d("TAG", "onDown()");
//            //  pieView.invalidate();
//            float x = e.getX();
//            float y = e.getY();
//            int sectorNumber = pieView.getSector(x, y);
//
//          //  Log.d("TAG", " SECTOR NUMBER RESULT: " + sectorNumber);
//
//            if(sectorNumber > -1){
//                pieView.setSelectedSector(sectorNumber);
//                pieView.invalidate();
//            }
//            return true;
//        }
//    }
//
//    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
//        float l = (float) Math.sqrt(dx * dx + dy * dy);
//        float crossX = -y;
//        float crossY = x;
//
//        float dot = (crossX * dx + crossY * dy);
//        float sign = Math.signum(dot);
//
//        return l * sign;
//    }
//
//    private void tickScrollAnimation() {
//        if (!scroller.isFinished()) {
//            scroller.computeScrollOffset();
//            pieView.setRotation(scroller.getCurrY());
//        } else {
//            scrollAnimator.cancel();
//        }
//    }
}
