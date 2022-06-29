package com.vipjokerstudio.cocoskotlin.core.types;


import com.vipjokerstudio.cocoskotlin.util.Converter;

import org.jbox2d.common.Vec2;

public class Vec2Util {
    private static final float epsilon = 0.00000012f;

    
    private static final Vec2 ZERO_POINT = new Vec2(0, 0);
    public static Vec2 getZero() {
    	return ZERO_POINT;
    }
    
    public static Vec2 zero() {
        return new Vec2(0, 0);
    }

    public static Vec2 make(float x, float y) {
        return new Vec2(x, y);
    }






    public static float distance(Vec2 p1, Vec2 p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Multiply point by floating, write result in the same point.
     * @param v - src/dst point
     * @param s - factor value
     */
    public static void mult(Vec2 v, float s) {
        v.x *= s;
        v.y *= s;
    }

    /**
     * Multiply point by floating, write result in another point.
     * @param v - src point
     * @param s - factor value
     * @param res - dst point
     */
    public static void mult(Vec2 v, float s, Vec2 res) {
        res.x = v.x * s;
        res.y = v.y * s;
    }

    public static void applyAffineTransform(Vec2 p, CGAffineTransform t, Vec2 res) {
        applyAffineTransform(p.x, p.y, t, res);
    }

    public static void applyAffineTransform(float x, float y, CGAffineTransform t, Vec2 res) {
        res.x = (float) (x * t.m00 + y * t.m01 + t.m02);
        res.y = (float) (x * t.m10 + y * t.m11 + t.m12);
    }

    public static void zero(Vec2 p) {
        p.x = 0;
        p.y = 0;
    }

    public static void normalize(Vec2 src, Vec2 dst) {
        float invLen = 1 / ccpLength(src);
        dst.set(src.x * invLen, src.y * invLen);
    }

    public static void add(Vec2 first, Vec2 second, Vec2 ret) {
        ret.x = first.x + second.x;
        ret.y = first.y + second.y;
    }

    public static void add(Vec2 v, Vec2 toAdd) {
        v.x += toAdd.x;
        v.y += toAdd.y;
    }

    public static void sub(Vec2 first, Vec2 second, Vec2 ret) {
        ret.x = first.x - second.x;
        ret.y = first.y - second.y;
    }

    public static void sub(Vec2 v, Vec2 toAdd) {
        v.x -= toAdd.x;
        v.y -= toAdd.y;
    }




    public static boolean equalToPoint(Vec2 p1, Vec2 p2) {
        return p1.x == p2.x && p1.y == p2.y;
    }

    public static Vec2 applyAffineTransform(Vec2 aPoint, CGAffineTransform aTransform) {
        return aTransform.applyTransform(aPoint);
    }

    /**
     * Helper macro that creates a CCPoint
     *
     * @return CCPoint
     */
    public static Vec2 create(float x, float y) {
        return new Vec2(x, y);
    }


    /**
     * Returns opposite of point.
     *
     * @return CCPoint
     */
    public static Vec2 ccpNeg(final Vec2 v) {
        return create(-v.x, -v.y);
    }

    /**
     * Calculates sum of two points.
     *
     * @return CCPoint
     */
    public static Vec2 ccpAdd(final Vec2 v1, final Vec2 v2) {
        return create(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Calculates difference of two points.
     *
     * @return CCPoint
     */
    public static Vec2 ccpSub(final Vec2 v1, final Vec2 v2) {
        return create(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * Returns point multiplied by given factor.
     *
     * @return CCPoint
     */
    public static Vec2 ccpMult(final Vec2 v, final float s) {
        return create(v.x * s, v.y * s);
    }

    /**
     * Calculates midpoint between two points.
     *
     * @return CCPoint
     */
    public static Vec2 ccpMidpoint(final Vec2 v1, final Vec2 v2) {
        return ccpMult(ccpAdd(v1, v2), 0.5f);
    }

    /**
     * Calculates dot product of two points.
     *
     * @return float
     */
    public static float ccpDot(final Vec2 v1, final Vec2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /**
     * Calculates cross product of two points.
     *
     * @return float
     */
    public static float ccpCross(final Vec2 v1, final Vec2 v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    /**
     * Calculates perpendicular of v, rotated 90 degrees counter-clockwise -- cross(v, perp(v)) >= 0
     *
     * @return CCPoint
     */
    public static Vec2 ccpPerp(final Vec2 v) {
        return create(-v.y, v.x);
    }

    /**
     * Calculates perpendicular of v, rotated 90 degrees clockwise -- cross(v, rperp(v)) <= 0
     *
     * @return CCPoint
     */
    public static Vec2 ccpRPerp(final Vec2 v) {
        return create(v.y, -v.x);
    }

    /**
     * Calculates the projection of v1 over v2.
     *
     * @return CCPoint
     */
    public static Vec2 ccpProject(final Vec2 v1, final Vec2 v2) {
        return ccpMult(v2, ccpDot(v1, v2) / ccpDot(v2, v2));
    }

    /**
     * Rotates two points.
     *
     * @return CCPoint
     */
    public static Vec2 ccpRotate(final Vec2 v1, final Vec2 v2) {
        return create(v1.x * v2.x - v1.y * v2.y, v1.x * v2.y + v1.y * v2.x);
    }

    /**
     * Unrotates two points.
     *
     * @return CCPoint
     */
    public static Vec2 ccpUnrotate(final Vec2 v1, final Vec2 v2) {
        return create(v1.x * v2.x + v1.y * v2.y, v1.y * v2.x - v1.x * v2.y);
    }

    /**
     * Calculates the square length of a CCPoint (not calling sqrt() )
     *
     * @return float
     */
    public static float ccpLengthSQ(final Vec2 v) {
        return ccpDot(v, v);
    }

    /** Calculates distance between point and origin
     @return CGFloat
     @since v0.7.2
     */
    public static float ccpLength(final Vec2 v) {
        return (float)Math.sqrt(ccpLengthSQ(v));
    }

    /**
     * Calculates the distance between two points
     *
     * @return float
     */
    public static float ccpDistance(final Vec2 v1, final Vec2 v2) {
        return ccpLength(ccpSub(v1, v2));
    }

    /**
     * Returns point multiplied to a length of 1.
     *
     * @return CCPoint
     */
    public static Vec2 ccpNormalize(final Vec2 v) {
        return ccpMult(v, 1.0f / ccpLength(v));
    }

    /**
     * Converts radians to a normalized vector.
     *
     * @return CCPoint
     */
    public static Vec2 ccpForAngle(final float a) {
        return create((float)Math.cos(a), (float)Math.sin(a));
    }

    /**
     * Converts a vector to radians.
     *
     * @return float
     */
    public static float ccpToAngle(final Vec2 v) {
        return (float) Math.atan2(v.y, v.x);
    }

    /**
     *  Caculate the rotation(in degrees) between two points,
     *      so that when we move from one point to the other,
     *      we can set the correct rotation to head to that point.
     *
     * @param from
     * @param to
     * @return the rotation in degrees
     */
    public static float ccpCalcRotate(final Vec2 from, final Vec2 to) {
        float o = to.x - from.x;
        float a = to.y - from.y;
        float at = Converter.toDeg((float) Math.atan(o / a));

        if (a < 0) {
            if (o < 0)
                at = 180 + Math.abs(at);
            else
                at = 180 - Math.abs(at);
        }

        return at;
    }


    /** @returns the angle in radians between two vector directions
      @since v0.99.1
      */
    public static float ccpAngle(Vec2 a, Vec2 b) {
        float angle = (float)Math.acos(ccpDot(ccpNormalize(a), ccpNormalize(b)));
        if( Math.abs(angle) < epsilon) return 0.f;
        return angle;
    }

    /** Linear Interpolation between two points a and b
     @returns
        alpha == 0 ? a
        alpha == 1 ? b
        otherwise a value between a..b
     @since v0.99.1
     */
    public static Vec2 ccpLerp(Vec2 a, Vec2 b, float alpha) {
        return ccpAdd(ccpMult(a, 1.f - alpha), ccpMult(b, alpha));
    }

    /** Clamp a value between from and to.
      @since v0.99.1
      */
    public static float clampf(float value, float min_inclusive, float max_inclusive) {
        if (min_inclusive > max_inclusive) {
            float tmp = min_inclusive;
            min_inclusive = max_inclusive;
            max_inclusive = tmp;
        }

        return value < min_inclusive ? min_inclusive : value < max_inclusive? value : max_inclusive;
    }

    /** Clamp a point between from and to.
      @since v0.99.1
      */
    public static Vec2 ccpClamp(Vec2 p, Vec2 min_inclusive, Vec2 max_inclusive) {
        return create(clampf(p.x,min_inclusive.x,max_inclusive.x),
                    clampf(p.y, min_inclusive.y, max_inclusive.y));
    }

    /** Quickly convert Size to a Vec2
      @since v0.99.1
      */
    public static Vec2 ccpFromSize(Size s) {
        return create(s.width, s.height);
    }

    /** @returns if points have fuzzy equality which means equal with some degree of variance.
      @since v0.99.1
      */
    public static boolean ccpFuzzyEqual(Vec2 a, Vec2 b, float var) {
        if(a.x - var <= b.x && b.x <= a.x + var)
            if(a.y - var <= b.y && b.y <= a.y + var)
                return true;
        return false;
    }

    /** Multiplies a nd b components, a.x*b.x, a.y*b.y
      @returns a component-wise multiplication
      @since v0.99.1
      */
    public static Vec2 ccpCompMult(Vec2 a, Vec2 b) {
        return create(a.x * b.x, a.y * b.y);
    }

    /** @returns the signed angle in radians between two vector directions
      @since v0.99.1
      */
    public static float ccpAngleSigned(Vec2 a, Vec2 b) {
        Vec2 a2 = ccpNormalize(a);
        Vec2 b2 = ccpNormalize(b);
        float angle = (float)Math.atan2(a2.x * b2.y - a2.y * b2.x, ccpDot(a2, b2));
        if( Math.abs(angle) < epsilon) return 0.f;
        return angle;
    }

    /** Rotates a point counter clockwise by the angle around a pivot
      @param v is the point to rotate
      @param pivot is the pivot, naturally
      @param angle is the angle of rotation cw in radians
      @returns the rotated point
      @since v0.99.1
      */
    public static Vec2 ccpRotateByAngle(Vec2 v, Vec2 pivot, float angle) {
        Vec2 r = ccpSub(v, pivot);
        float t = r.x;
        float cosa = (float)Math.cos(angle);
        float sina = (float)Math.sin(angle);
        r.x = t*cosa - r.y*sina;
        r.y = t*sina + r.y*cosa;
        r = ccpAdd(r, pivot);
        return r;
    }

    /** A general line-line intersection test
      @param p1 
      is the startpoint for the first line P1 = (p1 - p2)
      @param p2 
      is the endpoint for the first line P1 = (p1 - p2)
      @param p3 
      is the startpoint for the second line P2 = (p3 - p4)
      @param p4 
      is the endpoint for the second line P2 = (p3 - p4)
      is the range for a hitpoint in P1 (pa = p1 + s*(p2 - p1))
      is the range for a hitpoint in P3 (pa = p2 + t*(p4 - p3))
      @return bool 
      indicating successful intersection of a line
      note that to truly test intersection for segments we have to make 
      sure that s & t lie within [0..1] and for rays, make sure s & t > 0
      the hit point is		p3 + t * (p4 - p3);
      the hit point also is	p1 + s * (p2 - p1);
      @since v0.99.1
      */
    public static boolean ccpLineIntersect(Vec2 p1, Vec2 p2,
                                           Vec2 p3, Vec2 p4, Vec2 ret){
        Vec2 p13, p43, p21;
        float d1343, d4321, d1321, d4343, d2121;
        float numer, denom;

        p13 = ccpSub(p1, p3);

        p43 = ccpSub(p4, p3);

        //Roughly equal to zero but with an epsilon deviation for float 
        //correction
        if (ccpFuzzyEqual(p43, new Vec2(0,0), epsilon))
            return false;

        p21 = ccpSub(p2, p1);

        //Roughly equal to zero
        if (ccpFuzzyEqual(p21,new Vec2(0,0), epsilon))
            return false;

        d1343 = ccpDot(p13, p43);
        d4321 = ccpDot(p43, p21);
        d1321 = ccpDot(p13, p21);
        d4343 = ccpDot(p43, p43);
        d2121 = ccpDot(p21, p21);

        denom = d2121 * d4343 - d4321 * d4321;
        if (Math.abs(denom) < epsilon)
            return false;
        numer = d1343 * d4321 - d1321 * d4343;

        ret.x = numer / denom;
        ret.y = (d1343 + d4321 * ret.x) / d4343;

        return true;
    }
}

