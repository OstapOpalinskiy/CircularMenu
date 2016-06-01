package com.opalinskiy.ostap.circularmenu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;


public class CircularViewLayout extends ViewGroup {
    private CircularMenuView pieView;

    private int outerBoundStroke;
    private int outerBoundShadowColor;
    private int outerBoundColor;
    private int lineStroke;
    private int lineColor;
    private int lineShadowColor;
    private int sectorColor;
    private int selectedSectorColor;
    private int selectedSectorShadowColor;
    private int smallCenterColor;
    private int smallCenterShadowColor;
    private int bigCenterColor;
    private int selectedSectorShadowStrength;

    public CircularViewLayout(Context context) {
        super(context);
        init();
    }

    public CircularViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources r =  getContext().getResources();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircularViewLayout, 0, 0);
        try {
            outerBoundStroke = a.getInt(R.styleable.CircularViewLayout_outerBoundStroke, 10);
            outerBoundColor = a.getColor(R.styleable.CircularViewLayout_outerBoundColor, Color.RED);
            outerBoundShadowColor = a.getColor(R.styleable.CircularViewLayout_outerBoundShadowColor,
                    r.getColor(R.color.colorDarkRed));

            lineColor = a.getColor(R.styleable.CircularViewLayout_lineColor, Color.RED);
            lineShadowColor = a.getColor(R.styleable.CircularViewLayout_lineShadowColor,
                    r.getColor(R.color.colorDarkRed));
            lineStroke = a.getInt(R.styleable.CircularViewLayout_lineStroke, 7);

            sectorColor = a.getColor(R.styleable.CircularViewLayout_sectorColor,
                    r.getColor(R.color.colorDarkPurple));
            selectedSectorColor = a.getColor(R.styleable.CircularViewLayout_selectedSectorColor, Color.GREEN);
            selectedSectorShadowColor = a.getColor(R.styleable.CircularViewLayout_selectedSectorShadowColor,
                   r.getColor(R.color.colorDarkGreen));
           selectedSectorShadowStrength = a.getColor(R.styleable.CircularViewLayout_sectorShadowStrength, 3);

            smallCenterColor = a.getColor(R.styleable.CircularViewLayout_smallCenterColor, Color.RED);
            smallCenterShadowColor = a.getColor(R.styleable.CircularViewLayout_smallCenterShadowColor,
                    r.getColor(R.color.colorDarkRed));
            bigCenterColor = a.getColor(R.styleable.CircularViewLayout_bigCenterColor, Color.GREEN);

        } finally {
            a.recycle();
        }
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

        pieView.setOuterBoundStroke(outerBoundStroke);
        pieView.setOuterBoundShadowColor(outerBoundShadowColor);
        pieView.setOuterBoundColor(outerBoundColor);

        pieView.setLineStroke(lineStroke);
        pieView.setLineColor(lineColor);
        pieView.setLineShadowColor(lineShadowColor);

        pieView.setSectorColor(sectorColor);
        pieView.setHighlightedSectorColor(selectedSectorColor);
        pieView.setSelectedSectorShadowColor(selectedSectorShadowColor);
        pieView.setSelectedSectorShadowStrength(selectedSectorShadowStrength);

        pieView.setSmallCenterColor(smallCenterColor);
        pieView.setSmallCenterShadowColor(smallCenterShadowColor);
        pieView.setBigCenterColor(bigCenterColor);

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
