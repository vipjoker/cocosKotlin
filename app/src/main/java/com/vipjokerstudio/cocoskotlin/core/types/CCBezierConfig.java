package com.vipjokerstudio.cocoskotlin.core.types;

import org.jbox2d.common.Vec2;

/** bezier configuration structure
 */
public class CCBezierConfig {
    //! end position of the bezier
    public Vec2 endPosition;
    //! Bezier control point 1
    public Vec2 controlPoint_1;
    //! Bezier control point 2
    public Vec2 controlPoint_2;
    
    public CCBezierConfig() {
    	
    }

    // Bezier cubic formula:
    // 	((1 - t) + t)3 = 1 
    // Expands to 
    //   (1 - t)3 + 3t(1-t)2 + 3t2(1 - t) + t3 = 1 
    public static float bezierAt( float a, float b, float c, float d, float t) {
    	return (float)(Math.pow(1-t,3) * a
    			+ 3*t*(Math.pow(1-t,2)) * b
    			+ 3*Math.pow(t,2)*(1-t) * c
    			+ Math.pow(t,3) * d);
    }

}
