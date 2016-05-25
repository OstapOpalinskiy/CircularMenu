package com.opalinskiy.ostap.circularmenu;

import android.animation.ObjectAnimator;
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
import android.view.View;


public class CircularMenuView extends View implements ValueAnimator.AnimatorUpdateListener {
    private Context context;
    private Paint sectorPaint;
    private RectF arcBounds;
    private int sectorCount;
    private int arcAngle;
    private int pieRotation;
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
            R.drawable.missed_call,
            R.drawable.mms
    };
    private Drawable drawable;
    private GestureDetector detector;

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

    public int getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(int arcAngle) {
        this.arcAngle = arcAngle;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        Log.d("TAG", "init in view");
        sectorPaint = new Paint();

        sectorPaint.setColor(Color.RED);
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        sectorPaint.setStrokeWidth(1);
        sectorCount = 7;
        arcAngle = 360 / sectorCount;
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
    }

    private void drawIcons(Canvas canvas) {

//        int centerX = (int)arcBounds.centerX();
//        int centerY = (int)arcBounds.centerY();
        int leftStart = (int) (centerX - (r * 0.07));
        int topStart = (int) (centerY - r * 0.4);
        int leftEnd = (int) (centerX + (r * 0.07));
        int topEnd = (int) (centerY - r * 0.26);

        for(int i = 0; i < sectorCount; i++){
            drawable = ContextCompat.getDrawable(context, images[i]);
            drawable.setBounds(leftStart, topStart, leftEnd, topEnd );
            Log.d("TAG", "centerX = " + centerX + "  centerY = " + centerY);
            drawable.draw(canvas);
            canvas.rotate(arcAngle, centerX, centerY);
        }
    }

    private void drawLines(Canvas canvas) {
        sectorPaint.setColor(Color.RED);
        sectorPaint.setStyle(Paint.Style.STROKE);
        sectorPaint.setStrokeWidth(3);

        int startAngleLines = 0;
        int endAngleLines = 0;
        for (int i = 0; i < sectorCount; i++) {
            startAngleLines = i * arcAngle;
            endAngleLines = startAngleLines + arcAngle;
            drawLine(canvas, endAngleLines, i);
        }
    }

    private void drawSectors(Canvas canvas) {
        sectorPaint.setColor(Color.GRAY);
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        RectF bounds = new RectF (arcBounds.left + 5, arcBounds.top + 5, arcBounds.right - 5, arcBounds.bottom - 5);

        int startAngle = 0;
        for (int i = 0; i <= sectorCount; i++) {
            startAngle = i * arcAngle;
            canvas.drawArc(bounds, startAngle, startAngle + arcAngle, true, sectorPaint);

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

        RectF bounds = new RectF (arcBounds.left + 5, arcBounds.top + 5, arcBounds.right - 4, arcBounds.bottom - 4);

        canvas.drawArc(bounds, 0, 360, false, sectorPaint);
    }

    private void drawLine(Canvas canvas, int endAngle, int pos) {
        float x = (float) ((r - 10) * Math.cos(Math.toRadians(360 - endAngle - arcAngle/4))) / 2;
        float y = (float) ((r- 10) * Math.sin(Math.toRadians(360 - endAngle - arcAngle/4))) / 2;
        canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, sectorPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        r = Math.min(w, h);
        Log.d("TAG", "onSizeChange in view");
        arcBounds = new RectF(0, 0, r, r );
        Log.d("TAG", "Width from view onSizeChanged: " +  w);
        Log.d("TAG", "Height from view onSizeChanged: " +  h);

        centerX = (int)arcBounds.centerX();
        centerY = (int)arcBounds.centerY();

        setPivotX(centerX);
        setPivotY(centerY);

//        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "pieRotation", 720);
//        animator.setDuration(5000);
//        animator.addUpdateListener(this);
//        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
//        Log.d("TAG", "onActionUpdate()");
//        this.setPieRotation(pieRotation);
//        invalidate();
    }

    public void setPieRotation(int pieRotation) {
        this.pieRotation = pieRotation;
    }

    public int getPieRotation() {
        return pieRotation;
    }



    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void rotateTo(float angle){
        setRotation(angle);
    }
}
