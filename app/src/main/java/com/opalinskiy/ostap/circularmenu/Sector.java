package com.opalinskiy.ostap.circularmenu;


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
        } else {
            return angle >startAngle || angle <= endAngle;
        }
        return result;
    }

    public int getPos() {
        return pos;
    }

    public float getStartAngle() {
        return startAngle;
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
