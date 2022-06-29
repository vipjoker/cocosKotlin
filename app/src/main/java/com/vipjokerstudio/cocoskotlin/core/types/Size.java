package com.vipjokerstudio.cocoskotlin.core.types;


import android.graphics.Rect;

public class Size {
    public float width, height;
    public Rect rect = new Rect();

    private Size() {
        this(0, 0);
    }

    private Size(float w, float h) {
        width = w;
        height = h;
    }

    public static Size make(float w, float h) {
        return new Size(w, h);
    }

    public static Size zero() {
        return new Size(0, 0);
    }
    
	public void set(Size s) {
		width = s.width; 
		height = s.height;
	}
	
	public void set(float w, float h) {  	
		width = w; 
		height = h;
	}
	
	private static Size ZERO_SIZE = Size.zero();
	public static Size getZero() {
		return ZERO_SIZE;
	}
	
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public static boolean equalToSize(Size s1, Size s2) {
        return s1.width == s2.width && s1.height == s2.height;
    }

    public Rect toRect(){
	    rect.set(0,0,(int)width,(int)height);
	    return rect;
    }

    public String toString() {
        return "<" + width + ", " + height + ">";
    }
}
