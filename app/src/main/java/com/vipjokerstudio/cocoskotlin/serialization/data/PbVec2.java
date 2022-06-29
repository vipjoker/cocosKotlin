package com.vipjokerstudio.cocoskotlin.serialization.data;

public class PbVec2 {
    private float x;
    private float y;
    public float getX() {
            return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static PbVec2 create(float x,float y){
        PbVec2 v = new PbVec2();
        v.x = x;
        v.y = y;
        return v;
    }
}
