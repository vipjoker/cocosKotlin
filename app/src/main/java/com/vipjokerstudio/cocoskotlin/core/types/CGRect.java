package com.vipjokerstudio.cocoskotlin.core.types;

import org.jbox2d.common.Vec2;

public class CGRect {
    public Vec2 origin;
    public Size size;
    public Vec2 anchor = new Vec2(0,0);
    public Vec2 anchorBuffer = new Vec2();
    public CGRect() {
        this(0, 0, 0, 0);
    }

    public CGRect(final Vec2 origin, final Size size) {
        this(origin.x, origin.y, size.width, size.height);
    }
    
    private static final CGRect ZERO_RECT = new CGRect(0, 0, 0, 0);
    public static CGRect getZero() {
    	return ZERO_RECT;
    }
    
    public static CGRect zero() {
        return new CGRect(0, 0, 0, 0);
    }

    public static CGRect make(final Vec2 origin, final Size size) {
        return new CGRect(origin.x, origin.y, size.width, size.height);
    }

    public static CGRect make(float x, float y, float w, float h) {
        return new CGRect(x, y, w, h);
    }
    
    public static CGRect make(CGRect r) {
    	return new CGRect(r.origin, r.size);
    }

    private CGRect(float x, float y, float w, float h) {
        origin = new Vec2(x, y);
        size = Size.make(w, h);
    }
    
	public void set(CGRect r) {
		origin.set(r.origin);
		size.set(r.size);
	}

	public void set(float x, float y, float w, float h) {
		origin.set(x, y);
		size.set(w, h);
	}

	public void offset(float dx,float dy){
        origin.addLocal(new Vec2(dx,dy));
    }

    public void setAnchor(Vec2 origin){
        this.anchor = origin;
        float offsetX = -origin.x * size.width;

        float offsetY = -origin.y * size.height;


        offset(offsetX,offsetY);
    }

    public Vec2 getAnchorInPixels(){
        anchorBuffer.x =(width() * anchor.x);
        anchorBuffer.y = (height() * anchor.y);
        return anchorBuffer;
    }


	public boolean contains(Vec2 pos){
        return contains(pos.x,pos.y);
    }

    public boolean contains(float x, float y) {
        return size.width > 0 && size.height > 0  // check for empty first
                && x >= origin.x && x < (origin.x + size.width)
                && y >= origin.y && y < (origin.y + size.height);
    }

    public String toString() {
        return "((" + origin.x + ", " + origin.y + "),(" + size.width + ", " + size.height + "))";
    }


    public static boolean equalToRect(final CGRect r1, final CGRect r2) {
        return r1.origin.equals(r2.origin) && Size.equalToSize(r1.size, r2.size);
    }

    /**
     * Returns true if aPoint is inside aRect.
     */
    public static boolean containsPoint(final CGRect aRect, final Vec2 aPoint) {
        return ((aPoint.x >= aRect.minX())
                && (aPoint.y >= aRect.minY())
                && (aPoint.x < aRect.maxX())
                && (aPoint.y < aRect.maxY()));
    }
    public boolean containsPoint(float x,float y){
        return ((x >= this.minX()
                && (y >= this.minY())
                && (x < this.maxX())
                && (y < this.maxY())));
    }

    public boolean containsPoint(final Vec2 aPoint) {
        return containsPoint(aPoint.x,aPoint.y);
    }

    public  boolean containsRect(final CGRect bRect) {
        return (!isEmptyRect(bRect)
                && (this.minX() <= bRect.minX())
                && (this.minY() <= bRect.minY())
                && (this.maxX() >= bRect.maxX())
                && (this.maxY() >= bRect.maxY()));
    }
    
    public static boolean intersects(CGRect a, CGRect b)
    {
    	return (a.origin.x >= (b.origin.x - a.size.width) && a.origin.x <= (b.origin.x - a.size.width) + (b.size.width + a.size.width)
    			&& a.origin.y >= (b.origin.y - a.size.height) && a.origin.y <= (b.origin.y - a.size.height) + (b.size.height + a.size.height));
    }

    static Vec2[] sp = new Vec2[4];
    static Vec2[] outputsp = new Vec2[4];
    static Vec2 min = new Vec2();
    static Vec2 max = new Vec2();
    public static CGRect applyAffineTransformYosit(CGRect aRect, CGAffineTransform matrix) {
    	//result.set(0, 0, 0, 0);
        CGRect r = aRect;
        Vec2[] p = sp;

        for (int i = 0; i < 4; i++) {
            //p[i] = Vec2.make(aRect.origin.x, aRect.origin.y);
        	if (p[i] == null) {
        		p[i] = new Vec2(aRect.origin.x, aRect.origin.y);
        		outputsp[i] = new Vec2();
        	} else {
        		p[i].x = aRect.origin.x;
        		p[i].y = aRect.origin.y;
        	}
        }

        p[1].x += aRect.size.width;
        p[2].y += aRect.size.height;
        p[3].x += aRect.size.width;
        p[3].y += aRect.size.height;

        for (int i = 0; i < 4; i++) {
            //p[i] = Vec2.applyAffineTransform(p[i], matrix);
            matrix.transform(sp, 0, outputsp, 0, 4);
            
        }

        
//        Vec2 min = Vec2.make(p[0].x, p[0].y),
//                max = Vec2.make(p[0].x, p[0].y);
        p = outputsp;
        max.x = min.x = p[0].x;
        max.y = min.y = p[0].y;
        
        for (int i = 1; i < 4; i++) {
            min.x = Math.min(min.x, p[i].x);
            min.y = Math.min(min.y, p[i].y);
            max.x = Math.max(max.x, p[i].x);
            max.y = Math.max(max.y, p[i].y);
        }

        r.origin.x = min.x; r.origin.y = min.y;
        r.size.width = max.x - min.x; r.size.height = max.y - min.y;

        return r;
    }
    
    public static CGRect applyAffineTransform(CGRect aRect, CGAffineTransform matrix) {
        CGRect r = CGRect.make(0, 0, 0, 0);
        Vec2[] p = new Vec2[4];

        for (int i = 0; i < 4; i++) {
            p[i] = new Vec2(aRect.origin.x, aRect.origin.y);
        }

        p[1].x += aRect.size.width;
        p[2].y += aRect.size.height;
        p[3].x += aRect.size.width;
        p[3].y += aRect.size.height;

        for (int i = 0; i < 4; i++) {
            p[i] = matrix.applyTransform(p[i]);
        }

        Vec2 min = new Vec2(p[0].x, p[0].y);
        Vec2 max = new Vec2(p[0].x, p[0].y);
        for (int i = 1; i < 4; i++) {
            min.x = Math.min(min.x, p[i].x);
            min.y = Math.min(min.y, p[i].y);
            max.x = Math.max(max.x, p[i].x);
            max.y = Math.max(max.y, p[i].y);
        }

        r.origin.x = min.x; r.origin.y = min.y;
        r.size.width = max.x - min.x; r.size.height = max.y - min.y;

        return r;
    }


    public  float maxX() {
        return origin.x + size.width;
    }

    public float maxY() {
        return origin.y + size.height;
    }

    /**
     * Returns the x-coordinate of aRect's middle point.
     */
    public  float midX() {
        return origin.x + (float) (size.width / 2.0);
    }

    public float midY() {
        return origin.y + (float) (size.height / 2.0);
    }


    public  float minX() {
        return origin.x;
    }

    public float minY() {
        return origin.y;
    }


    public float  width() {
        return size.width;
    }

    /**
     * Returns aRect's height.
     */
    public float height() {
        return size.height;
    }

    public static boolean isEmptyRect(CGRect aRect) {
        return (!(aRect.size.width > 0 && aRect.size.height > 0));
    }

    public enum Edge {
        MinXEdge,
        MinYEdge,
        MaxXEdge,
        MaxYEdge,
    }

    private static CGRect sRect = new CGRect();
    private static CGRect rRect = new CGRect();

    public static void divideRect(final CGRect aRect, CGRect[] slice, CGRect[] remainder, float amount, Edge edge) {

        if (slice == null) {
            slice = new CGRect[1];
        	slice[0] = sRect;
        }

        if (remainder == null) {
        	remainder = new CGRect[1];
            remainder[0] = rRect;
        }

        if (isEmptyRect(aRect)) {
            slice[0] = CGRect.make(0, 0, 0, 0);
            remainder[0] = CGRect.make(0, 0, 0, 0);
            return;
        }

        switch (edge) {
            case MinXEdge:
                if (amount > aRect.size.width) {
                    slice[0] = aRect;
                    remainder[0] = CGRect.make(aRect.maxX(),
                            aRect.origin.y,
                            0,
                            aRect.size.height);
                } else {
                    slice[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            amount,
                            aRect.size.height);
                    remainder[0] = CGRect.make(slice[0].maxX(),
                            aRect.origin.y,
                            aRect.maxX() - slice[0].maxX(),
                            aRect.size.height);
                }
                break;
            case MinYEdge:
                if (amount > aRect.size.height) {
                    slice[0] = aRect;
                    remainder[0] = CGRect.make(aRect.origin.x,
                            aRect.maxY(),
                            aRect.size.width, 0);
                } else {
                    slice[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            aRect.size.width,
                            amount);
                    remainder[0] = CGRect.make(aRect.origin.x,
                            slice[0].maxX(),
                            aRect.size.width,
                            aRect.maxY() - slice[0].maxY());
                }
                break;
            case MaxXEdge:
                if (amount > aRect.size.width) {
                    slice[0] = aRect;
                    remainder[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            0,
                            aRect.size.height);
                } else {
                    slice[0] = CGRect.make(aRect.maxX() - amount,
                            aRect.origin.y,
                            amount,
                            aRect.size.height);
                    remainder[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            slice[0].minX() - aRect.origin.x,
                            aRect.size.height);
                }
                break;
            case MaxYEdge:
                if (amount > aRect.size.height) {
                    slice[0] = aRect;
                    remainder[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            aRect.size.width,
                            0);
                } else {
                    slice[0] = CGRect.make(aRect.origin.x,
                            aRect.maxY() - amount,
                            aRect.size.width,
                            amount);
                    remainder[0] = CGRect.make(aRect.origin.x,
                            aRect.origin.y,
                            aRect.size.width,
                            slice[0].minY() - aRect.origin.y);
                }
                break;
            default:
                break;
        }
    }
}
