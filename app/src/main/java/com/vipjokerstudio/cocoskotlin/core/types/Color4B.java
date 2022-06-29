package com.vipjokerstudio.cocoskotlin.core.types;

/** RGBA color composed of 4 bytes
@since v0.8
*/
public class Color4B {
    public static final int size = 4;

    public int r;
    public int g;
    public int b;
    public int a;

    public Color4B(int rr, int gg, int bb, int aa) {
        r = rr;
        g = gg;
        b = bb;
        a = aa;
    }

    public Color4B(float rr, float gg, float bb, float aa) {
        r = (int)(rr * 255);
        g = (int)(gg * 255);
        b = (int)(bb *255);
        a = (int)(aa *255);
    }

    public byte[] toByteArray() {
        return new byte[]{(byte) r, (byte) g, (byte) b, (byte) a};
    }
    
    public float[] toFloatArray() {
    	return new float[] {r/255f, g/255f, b/255f, a/255f};
    }

    //! helper macro that creates an Color4B type
    public static Color4B ccc4(final int r, final int g, final int b, final int a) {
        return new Color4B(r, g, b, a);
    }

    public String toString() {
        return "< r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + " >";
    }


    public static Color4B RED = ccc4(255, 0, 0, 255);
    public static Color4B GREEN = ccc4(0, 255, 0, 255);
    public static Color4B BLUE = ccc4(0, 0, 255, 255);
    public static Color4B BLACK = ccc4(0, 0, 0, 255);
}

