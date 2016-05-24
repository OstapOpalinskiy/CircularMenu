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
import android.view.View;


public class CircularMenuView extends View implements ValueAnimator.AnimatorUpdateListener {
    private Context context;
    private Paint sectorPaint;
    private RectF arcBounds;
    private int sectorCount;
    private int arcAngle;
    private int r;
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

        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "arcAngle", 360);
        animator.setDuration(1000);
        animator.addUpdateListener(this);
        animator.start();

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

        int centerX = (int)arcBounds.centerX();
        int centerY = (int)arcBounds.centerY();
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

        int startAngle = 0;
        for (int i = 0; i <= sectorCount; i++) {
            startAngle = i * arcAngle;
            canvas.drawArc(arcBounds, startAngle, startAngle + arcAngle, true, sectorPaint);

        }
    }

    private void drawCenter(Canvas canvas) {
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        sectorPaint.setColor(Color.GREEN);

        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), 30, sectorPaint);
        sectorPaint.setColor(Color.YELLOW);
        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), 20, sectorPaint);
    }

    private void drawArks(Canvas canvas) {
        sectorPaint.setColor(Color.RED);
        sectorPaint.setStyle(Paint.Style.STROKE);
        sectorPaint.setStrokeWidth(3);

        canvas.drawArc(arcBounds, 0, 360, false, sectorPaint);
    }

    private void drawLine(Canvas canvas, int endAngle, int pos) {
        float x = (float) (r * Math.cos(Math.toRadians(360 - endAngle - arcAngle/4))) / 2;
        float y = (float) (r * Math.sin(Math.toRadians(360 - endAngle - arcAngle/4))) / 2;
        canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, sectorPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        r = Math.min(w, h)/2;
        Log.d("TAG", "onSizeChange in view");
        arcBounds = new RectF(0, 0, r, r);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("TAG", "onMeasure()");
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Log.d("TAG", "onActionUpdate()");
        invalidate();
    }
}
