package com.vipjokerstudio.cocoskotlin.util;

import android.graphics.Matrix;
import android.util.Log;

import org.jbox2d.common.Vec2;


public class Converter {
    public static final float DEG_TO_RAD = 0.0174533f;
    public static final float RAD_TO_DEG = 57.295780f;
    public static final float CC_DIRECTOR_FPS_INTERVAL = 1;
    public static boolean COCOS2D_DEBUG = false;

    float[] floatBuffer = new float[2];
    Matrix reverse = new Matrix();


    public void reverse(Vec2 point, Matrix matrix) {
        reverse.reset();
        floatBuffer[0] = point.x;
        floatBuffer[1] = point.y;
        matrix.invert(reverse);
        reverse.mapPoints(floatBuffer);
        point.set(floatBuffer[0], floatBuffer[1]);
    }

    public static float toDeg(float rad){

        return (float)Math.toDegrees(rad);
    }

    public static float toRad(float deg){
        return (float)Math.toRadians(deg);
//        return deg / DEG_TO_RAD;
    }




    public static void CCLOG(final String logName, final String logStr) {
            Log.d(logName, logStr);

    }

    public static void CCLOGINFO(final String logName, final String logStr) {
            Log.i(logName, logStr);

    }

    public static void CCLOGERROR(final String logName, final String logStr) {
            Log.e(logName, logStr);
    }

    public static final float FLT_EPSILON = 0.000001f;
    public static final int INT_MIN = -2147483648;
    public static final int CC_MAX_PARTICLE_SIZE = 64;

    /// java doesn't support swap primitive types.
    /// public static void CC_SWAP(T x, T y);

    /**
     * @def CCRANDOM_MINUS1_1
     * returns a random float between -1 and 1
     */
    public static float CCRANDOM_MINUS1_1() {
        return (float) Math.random() * 2.0f - 1.0f;
    }

    /**
     * @def CCRANDOM_0_1
     * returns a random float between 0 and 1
     */
    public static float CCRANDOM_0_1() {
        return (float) Math.random();
    }

    /**
     * @def M_PI_2
     * Math.PI divided by 2
     */
    public static final float M_PI_2 = (float) (Math.PI / 2);


    /**
     * @def CC_DEGREES_TO_RADIANS
     * converts degrees to radians
     */
    public static float CC_DEGREES_TO_RADIANS(float angle) {
        return (angle / 180.0f * (float) Math.PI);
    }

    /**
     * @def CC_RADIANS_TO_DEGREES
     * converts radians to degrees
     */
    public static float CC_RADIANS_TO_DEGREES(float angle) {
        return (angle / (float) Math.PI * 180.0f);
    }
}
