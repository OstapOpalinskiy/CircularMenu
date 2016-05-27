package com.opalinskiy.ostap.circularmenu;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


public class CircularMenuView extends View {
    private GestureDetector detector;
    private Scroller scroller;
    private ValueAnimator scrollAnimator;

    private Context context;
    private Paint sectorPaint;
    private RectF arcBounds;
    private int sectorCount;
    private float arcAngle;
    private int r;
    int centerX;
    int centerY;
    private int[] images = {
            R.drawable.bluetooth,
            R.drawable.call_transfer,
            R.drawable.callback,
            R.drawable.cellular_network,
            R.drawable.end_call,
            R.drawable.high_connection,
            //      R.drawable.missed_call,
            R.drawable.mms

    };
    private Drawable drawable;
    private int selectedSector = -1;


    public CircularMenuView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CircularMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CircularMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(int arcAngle) {
        this.arcAngle = arcAngle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TAG", "onDraw()");
        drawSectors(canvas);
        drawLines(canvas);
        drawCenter(canvas);
        drawArks(canvas);
        drawIcons(canvas);
        if(selectedSector > -1){
            highlightSelectedSector(canvas, selectedSector);
        }
        drawCenter(canvas);

    }

    private void drawIcons(Canvas canvas) {

        int leftStart = (int) (centerX - (r * 0.07));
        int topStart = (int) (centerY - r * 0.4);
        int leftEnd = (int) (centerX + (r * 0.07));
        int topEnd = (int) (centerY - r * 0.26);

        for (int i = 0; i < sectorCount; i++) {
            drawable = ContextCompat.getDrawable(context, images[i]);
            drawable.setBounds(leftStart, topStart, leftEnd, topEnd);
            drawable.draw(canvas);
            canvas.rotate(arcAngle, centerX, centerY);
        }
    }

    private void drawLines(Canvas canvas) {
        sectorPaint.setColor(Color.RED);
        sectorPaint.setStyle(Paint.Style.STROKE);
        sectorPaint.setStrokeWidth(3);

        for (int i = 0; i < sectorCount; i++) {
            float startAngle = i * arcAngle;
            float endAngle = startAngle + arcAngle;
            float x = (float) ((r - 10) * Math.cos(Math.toRadians(endAngle))) / 2;
            float y = (float) ((r - 10) * Math.sin(Math.toRadians(endAngle))) / 2;
            canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, sectorPaint);
        }
    }

    private void drawSectors(Canvas canvas) {
        sectorPaint.setColor(Color.GRAY);
        sectorPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < sectorCount; i++) {
            float startAngle = i * arcAngle;
            canvas.drawArc(arcBounds, startAngle, arcAngle, true, sectorPaint);
        }
    }

    private void drawCenter(Canvas canvas) {
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        sectorPaint.setColor(Color.GREEN);

        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), r / 10, sectorPaint);
        sectorPaint.setColor(Color.YELLOW);
        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), r / 15, sectorPaint);
    }

    private void drawArks(Canvas canvas) {
        sectorPaint.setColor(Color.RED);
        sectorPaint.setStyle(Paint.Style.STROKE);
        sectorPaint.setStrokeWidth(7);

        RectF bounds = new RectF(arcBounds.left + 5, arcBounds.top + 5, arcBounds.right - 4, arcBounds.bottom - 4);

        canvas.drawArc(bounds, 0, 360, false, sectorPaint);
    }

    private void drawLine(Canvas canvas, int endAngle, int pos) {
        float x = (float) ((r - 10) * Math.cos(Math.toRadians(endAngle))) / 2;
        float y = (float) ((r - 10) * Math.sin(Math.toRadians(endAngle))) / 2;
        canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, sectorPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        r = Math.min(w, h);
        Log.d("TAG", "onSizeChange in view");
        arcBounds = new RectF(0, 0, r, r);
        Log.d("TAG", "Width from view onSizeChanged: " + w);
        Log.d("TAG", "Height from view onSizeChanged: " + h);

        centerX = (int) arcBounds.centerX();
        centerY = (int) arcBounds.centerY();

        setPivotX(centerX);
        setPivotY(centerY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


   // methods to highlight sector

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        Log.d("TAG", "init in view");
        sectorPaint = new Paint();
        sectorPaint.setColor(Color.RED);
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        sectorPaint.setStrokeWidth(1);
        sectorCount = images.length;
        arcAngle = 360 / (float) sectorCount;

        detector = new GestureDetector(getContext(), new GestureListener());
        scroller = new Scroller(getContext(), null, true);
        scrollAnimator = ValueAnimator.ofFloat(0, 1);
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tickScrollAnimation();
            }
        });
    }

    private void highlightSelectedSector(Canvas canvas, int pos) {
        Log.d("TAG", "highlightSelectedSector() pos: ");
        sectorPaint.setColor(Color.GREEN);
        sectorPaint.setStyle(Paint.Style.FILL);
        float rotation = getRotation();
        int rotatedSectors = (int) (rotation/arcAngle);
        float startAngle = (pos - rotatedSectors) * arcAngle;
        canvas.drawArc(arcBounds, startAngle, arcAngle, true, sectorPaint);
    }

    private int getSectorOfAngle(float angle) {

        Log.d("TAG", " raw angle: " + angle);
        Log.d("TAG", " rotation: " + getRotation());

        float checkAngle = Math.abs(arcAngle - 360);

        for (int i = 0; i <= sectorCount; i++) {
            if (angle  > checkAngle) {
                return i;
            }
            checkAngle = Math.abs(checkAngle - arcAngle);
            Log.d("TAG", " checkAngle: " + checkAngle);
        }
        return sectorCount;
    }

    private float getAngleOfPoint(float x, float y) {
        float angleRad = (float) Math.atan2((double) (y - centerY), (double) (x - centerX));
        float theta = (float) Math.toDegrees(angleRad);
        float angleDegrees = theta;
        if (theta < 0.0) {
            angleDegrees = 360 + theta;
        }
        return angleDegrees;
    }


    public int getSector(float x, float y){
        if(touchOnView(x, y)){
            int sectorNumber = getSectorOfAngle(getAngleOfPoint(x, y));
                return Math.abs(sectorNumber - sectorCount +1);
        }
        return -1;
    }

    private boolean touchOnView(float x, float y){
        float radius = (float) Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) * 2;
        return (radius < r);
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void rotateTo(float angle) {
        setRotation(angle);
    }

    public void setSelectedSector(int selectedSector) {
        this.selectedSector = selectedSector;
    }













    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = detector.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        Log.d("TAG", "x: " + x + " ,y: " + y );
        return result;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Set the pie rotation directly.
            float scrollTheta = vectorToScalarScroll(
                    distanceX, distanceY,
                    e2.getX() - centerX,
                    e2.getY() - centerY);
            rotateTo(getRotation() - (int) scrollTheta / 4);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float scrollTheta = vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - centerX,
                    e2.getY() - centerY);

            scroller.fling(0, (int) getRotation(),
                    0, (int) scrollTheta / 8,
                    0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            scrollAnimator.setDuration(scroller.getDuration());
            scrollAnimator.start();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //    Log.d("TAG", "onDown()");
            //  pieView.invalidate();
            float x = e.getX();
            float y = e.getY();
            int sectorNumber = getSector(x, y);

            //  Log.d("TAG", " SECTOR NUMBER RESULT: " + sectorNumber);

            if(sectorNumber > -1){

                setSelectedSector(sectorNumber);
                invalidate();
            }
            return true;
        }
    }

    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        float l = (float) Math.sqrt(dx * dx + dy * dy);
        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }

    private void tickScrollAnimation() {
        if (!scroller.isFinished()) {
            scroller.computeScrollOffset();
            setRotation(scroller.getCurrY());
        } else {
            scrollAnimator.cancel();
        }
    }

}
