package com.opalinskiy.ostap.circularmenu;

import android.util.Log;

/**
 * Created by Evronot on 31.05.2016.
 */
public class Sector {

    private float startAngle;
    private float endAngle;
    private int pos;

    public Sector(float startAngle, float endAngle,int pos){

        this.startAngle = remove360(startAngle);
        this.endAngle = remove360(endAngle);
        this.pos = pos;
    }

    public boolean containsAngle(float angle){
       boolean result;

        if(startAngle < endAngle ){
            result = (angle >= startAngle && angle < endAngle);
            Log.d("TAG", "sector NORMAL ");
        } else {
            Log.d("TAG", "sector NORMAL ");
            return angle < endAngle;
        }
        return result;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public float getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(float endAngle) {
        this.endAngle = endAngle;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "startAngle=" + startAngle +
                ", endAngle=" + endAngle +
                ", position=" + pos +
                ", over 360=" + (startAngle > endAngle) +
                '}';
    }

    private float remove360(float angle){
        if (angle > 360){
            angle -= 360;
        }
        return angle;
    }
}
