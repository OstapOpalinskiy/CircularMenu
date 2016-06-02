package com.opalinskiy.ostap.circularmenu;


public class Line {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    public Line(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }


    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    @Override
    public String toString() {
        return "Line{" +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }
}
