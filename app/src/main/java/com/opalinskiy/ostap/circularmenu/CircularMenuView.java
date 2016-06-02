package com.opalinskiy.ostap.circularmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import java.security.cert.CertificateNotYetValidException;
import java.util.LinkedList;
import java.util.List;


public class CircularMenuView extends View {

    private GestureDetector detector;
    private Scroller scroller;
    private ValueAnimator scrollAnimator;
    private Context context;
    private Paint sectorPaint;
    private Paint linePaint;
    private Paint centerPaintBig;
    private Paint centerPaintSmall;
    private Paint arcPaint;
    private Paint arcPaintShadow;
    private Paint linePaintShadow;

    private RectF arcBounds;
    private int sectorCount;
    private float arcAngle;
    private float startAngle;
    private int r;

    private int outerBoundStroke;
    private int outerBoundShadowColor;
    private int outerBoundColor;
    private int lineStroke;
    private int lineColor;
    private int lineShadowColor;
    private int sectorColor;
    private int highlightedSectorColor;
    private int selectedSectorShadowColor;
    private int selectedSectorShadowStrength;
    private int smallCenterColor;
    private int smallCenterShadowColor;
    private int bigCenterColor;

    private int centerX;
    private int centerY;
    private int[] images = {
            R.drawable.bluetooth,
            R.drawable.call_transfer,
            // R.drawable.callback,
//            R.drawable.cellular_network,
//            R.drawable.end_call,
            R.drawable.high_connection,
            R.drawable.mms
    };
    private Drawable drawable;
    private int selectedSector = -1;
    private List<Sector> sectorList;
    private List<Line> linesList;
    private int ANIMATE_TO_CENTER_SPEED = 500;

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

    private void init() {
        Log.d("TAG", "init in view");
        sectorCount = images.length;
        arcAngle = 360 / (float) sectorCount;
        startAngle = 360 - 90 - arcAngle / 2;
        sectorList = new LinkedList();
        linesList = new LinkedList();
        detector = new GestureDetector(getContext(), new GestureListener());
        scroller = new Scroller(getContext(), null, true);
        scrollAnimator = ValueAnimator.ofFloat(0, 1);
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tickScrollAnimation();
            }
        });
        fillSectorsList();
    }

    private void fillSectorsList() {
        float sectorAngle;
        for (int i = 0; i < sectorCount; i++) {
            sectorAngle = startAngle + arcAngle * i;
            Sector sector = new Sector(sectorAngle, sectorAngle + arcAngle, i);
            sectorList.add(sector);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSectors(canvas);
        drawLines(canvas);
        drawArks(canvas);
        if (selectedSector > -1) {
            highlightSelectedSector(canvas, selectedSector);
        }
        drawIcons(canvas);
        drawCenter(canvas);

    }

    private void drawIcons(Canvas canvas) {
        int sectorNumberAdjustment = 7 / sectorCount;
        float iconWidth = r / 13 * sectorNumberAdjustment;
        float leftStart = centerX - iconWidth;
        float leftEnd = centerX + iconWidth;
        float iconFromCenter = r * 0.4f;
        int topStart = (int) (centerY - iconFromCenter);
        int topEnd = (int) (centerY - (iconFromCenter - 1.5 * iconWidth));

        for (int i = 0; i < sectorCount; i++) {
            drawable = ContextCompat.getDrawable(context, images[i]);
            drawable.setBounds((int) leftStart, topStart, (int) leftEnd, topEnd);
            drawable.draw(canvas);
            canvas.rotate(arcAngle, centerX, centerY);
        }
    }

    private void drawSectors(Canvas canvas) {
        float startAngle;
        for (int i = 0; i < sectorList.size(); i++) {
            startAngle = sectorList.get(i).getStartAngle();
            canvas.drawArc(arcBounds, startAngle, arcAngle, true, sectorPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < linesList.size(); i++) {
            float x = linesList.get(i).getEndX();
            float y = linesList.get(i).getEndY();
            Log.d("TAG", "x: " + x +  " y: " + y);
            canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, linePaintShadow);
            canvas.drawLine(arcBounds.centerX(), arcBounds.centerY(), arcBounds.centerX() + x, arcBounds.centerY() + y, linePaint);
        }
    }

    private void drawCenter(Canvas canvas) {
        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), r / 8, centerPaintBig);
        canvas.drawCircle(arcBounds.centerX(), arcBounds.centerY(), r / 12, centerPaintSmall);
        canvas.drawCircle(centerX, centerY, r / 16, arcPaint);
        sectorPaint.setShader(null);
    }

    private void drawArks(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, r / 2 - 14, arcPaintShadow);
        canvas.drawCircle(centerX, centerY, r / 2 - 8, arcPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        r = Math.min(w, h);
        arcBounds = new RectF(0, 0, r, r);
        centerX = (int) arcBounds.centerX();
        centerY = (int) arcBounds.centerY();
        setPivotX(centerX);
        setPivotY(centerY);
        setPaints();
        fillLinesList();
    }

    private void fillLinesList() {
        for (int i = 0; i < sectorCount; i++) {
            float sectorAngle = startAngle + i * arcAngle;
            float endAngle = sectorAngle + arcAngle;
            float x = (float) ((r - 10) * Math.cos(Math.toRadians(endAngle))) / 2;
            float y = (float) ((r - 10) * Math.sin(Math.toRadians(endAngle))) / 2;
            Log.d("TAG", "In init:" + "x: " + x + " y: " + y);
            Line currentLine = new Line(centerX, centerY, x, y);
            linesList.add(currentLine);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getSectorOfAngle(float angle) {
        for (int i = 0; i < sectorList.size(); i++) {
            Sector currentSector = sectorList.get(i);
            if (currentSector.containsAngle(angle)) {
                return currentSector.getPos();
            }
        }
        return sectorCount;
    }

    private void highlightSelectedSector(Canvas canvas, int pos) {
        float startAngle = this.startAngle + pos * arcAngle;
        canvas.drawArc(arcBounds, startAngle, arcAngle, true, sectorPaint);

        sectorPaint.setColor(selectedSectorShadowColor);
        sectorPaint.setShader(null);
        canvas.drawArc(arcBounds, startAngle, arcAngle + selectedSectorShadowStrength, true, sectorPaint);

        sectorPaint.setColor(highlightedSectorColor);
        sectorPaint.setShader(new RadialGradient(centerX, centerY, 2 * r, highlightedSectorColor, Color.BLACK, Shader.TileMode.MIRROR));
        canvas.drawArc(arcBounds, startAngle, arcAngle, true, sectorPaint);

        sectorPaint.setColor(sectorColor);
        sectorPaint.setShader(null);
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

    public int getSector(float x, float y) {
        if (touchOnView(x, y)) {
            int sectorNumber = getSectorOfAngle(getAngleOfPoint(x, y));
            return sectorNumber;
        }
        return -1;
    }

    private boolean touchOnView(float x, float y) {
        float radius = (float) Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) * 2;
        return (radius < r);
    }

    public void rotateTo(float angle) {
        setRotation(angle);
    }


    // methods for scroll and fling
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = detector.onTouchEvent(event);
        return result;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float scrollTheta = vectorToScalarScroll(
                    distanceX, distanceY,
                    e2.getX() - centerX,
                    e2.getY() - centerY);
            rotateTo(getRotation() - (int) scrollTheta / 8);
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
                    0, (int) scrollTheta / 7,
                    0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            scrollAnimator.setDuration(scroller.getDuration());
            scrollAnimator.start();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            selectedSector = getSector(x, y);
            setActionSelectedSector();
            invalidate();
            return super.onSingleTapUp(e);
        }
    }

    private void setActionSelectedSector() {
        float rotationOffset = getRotationOffset();
        //  float rotationOffset = -startAngle + 0.5f * arcAngle;
        switch (selectedSector) {
            case 0:
                Toast.makeText(getContext(), R.string.bluetooth, Toast.LENGTH_SHORT).show();
                animate().rotation(arcAngle * 5 + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 1:
                Toast.makeText(getContext(), R.string.call_transfer, Toast.LENGTH_SHORT).show();
                animate().rotation(arcAngle * 4 + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 2:
                Toast.makeText(getContext(), R.string.callback, Toast.LENGTH_SHORT).show();
                animate().rotation(arcAngle * 3 + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 3:
                Toast.makeText(getContext(), R.string.cellular_network, Toast.LENGTH_SHORT).show();
                animate().rotation(arcAngle * 2 + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 4:
                Toast.makeText(getContext(), R.string.end_call, Toast.LENGTH_SHORT).show();
                animate().rotation(arcAngle + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 5:
                Toast.makeText(getContext(), R.string.high_connection, Toast.LENGTH_SHORT).show();
                animate().rotation(+rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
            case 6:
                Toast.makeText(getContext(), R.string.mms, Toast.LENGTH_SHORT).show();
                animate().rotation(-arcAngle + rotationOffset).setDuration(ANIMATE_TO_CENTER_SPEED);
                break;
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

    private float getRotationOffset() {
        float ratio;
        switch (sectorCount) {
            case 2:
                ratio = -1;
                break;
            case 3:
            case 7:
                ratio = 0.25f;
                break;
            case 4:
                ratio = 2.5f;
                break;
            case 5:
                ratio = 1.75f;
                break;
            case 6:
                ratio = 1;
                break;
            case 8:
                ratio = -0.5f;
                break;
            default:
                ratio = 1;
        }
        return -startAngle - ratio * arcAngle;
    }

    private void setPaints() {
        sectorPaint = new Paint();
        sectorPaint.setAntiAlias(true);
        sectorPaint.setColor(sectorColor);
        sectorPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStroke);

        linePaintShadow = new Paint();
        linePaintShadow.setColor(lineShadowColor);
        linePaintShadow.setAntiAlias(true);
        linePaintShadow.setStyle(Paint.Style.STROKE);
        linePaintShadow.setStrokeWidth(lineStroke * 2);

        arcPaint = new Paint();
        arcPaint.setColor(outerBoundColor);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(outerBoundStroke);

        arcPaintShadow = new Paint();
        arcPaintShadow.setColor(outerBoundShadowColor);
        arcPaintShadow.setAntiAlias(true);
        arcPaintShadow.setStyle(Paint.Style.STROKE);
        arcPaintShadow.setStrokeWidth(outerBoundStroke + outerBoundStroke / 2);

        centerPaintSmall = new Paint();
        centerPaintSmall.setAntiAlias(true);
        centerPaintSmall.setColor(smallCenterColor);
        centerPaintSmall.setShader(new RadialGradient(centerX, centerY, r / 4, smallCenterColor,
                Color.GRAY, Shader.TileMode.MIRROR));

        centerPaintBig = new Paint();
        centerPaintBig.setAntiAlias(true);
        centerPaintBig.setStyle(Paint.Style.FILL);
        centerPaintBig.setColor(bigCenterColor);
        centerPaintBig.setShader(new RadialGradient(centerX, centerY, r / 6, bigCenterColor, Color.GRAY, Shader.TileMode.MIRROR));
    }

    public void setDetector(GestureDetector detector) {
        this.detector = detector;
    }

    public void setOuterBoundStroke(int outerBoundStroke) {
        this.outerBoundStroke = outerBoundStroke;
    }

    public void setOuterBoundShadowColor(int outerBoundShadowColor) {
        this.outerBoundShadowColor = outerBoundShadowColor;
    }

    public void setOuterBoundColor(int outerBoundColor) {
        this.outerBoundColor = outerBoundColor;
    }

    public void setLineStroke(int lineStroke) {
        this.lineStroke = lineStroke;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineShadowColor(int lineShadowColor) {
        this.lineShadowColor = lineShadowColor;
    }

    public void setSectorColor(int sectorColor) {
        this.sectorColor = sectorColor;
    }

    public void setHighlightedSectorColor(int highlightedSectorColor) {
        this.highlightedSectorColor = highlightedSectorColor;
    }

    public void setSelectedSectorShadowColor(int selectedSectorShadowColor) {
        this.selectedSectorShadowColor = selectedSectorShadowColor;
    }

    public void setSelectedSectorShadowStrength(int selectedSectorShadowStrength) {
        this.selectedSectorShadowStrength = selectedSectorShadowStrength;
    }

    public void setSmallCenterColor(int smallCenterColor) {
        this.smallCenterColor = smallCenterColor;
    }

    public void setSmallCenterShadowColor(int smallCenterShadowColor) {
        this.smallCenterShadowColor = smallCenterShadowColor;
    }

    public void setBigCenterColor(int bigCenterColor) {
        this.bigCenterColor = bigCenterColor;
    }
}
