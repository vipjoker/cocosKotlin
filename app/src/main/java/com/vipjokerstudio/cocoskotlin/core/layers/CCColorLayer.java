package com.vipjokerstudio.cocoskotlin.core.layers;

import android.graphics.Color;
import android.graphics.Paint;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Color3B;
import com.vipjokerstudio.cocoskotlin.core.types.Color4B;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

/**
 * CCColorLayer is a subclass of CCLayer that implements the CCRGBAProtocol protocol.
 * <p>
 * All features from CCLayer are valid, plus the following new features:
 * - opacity
 * - RGB colors
 */
public class CCColorLayer extends CCLayer implements CCNode.CocosNodeSize {
    protected Color3B color;
    protected int opacity;


    /**
     * creates a CCLayer with color. Width and height are the window size.
     */
    public static CCColorLayer node(Color4B color) {
        Size size = Director.getInstance().winSize();
        return new CCColorLayer(color, size.width, size.height);
    }

    /**
     * creates a CCLayer with color, width and height
     */
    public static CCColorLayer node(Color4B color, float w, float h) {
        return new CCColorLayer(color, w, h);
    }

    /**
     * initializes a CCLayer with color. Width and height are the window size.
     */
    protected CCColorLayer(Color4B color) {
        Size s = Director.getInstance().winSize();
        init(color, s.width, s.height);
    }

    /**
     * initializes a CCLayer with color, width and height
     */
    protected CCColorLayer(Color4B color, float w, float h) {
        init(color, w, h);
    }

    protected void init(Color4B color, float w, float h) {
        this.color = new Color3B(color.r, color.g, color.b);
        opacity = color.a;
        paint.setStyle(Paint.Style.FILL);
        updateColor();
        setContentSize(w, h);
    }

    private void updateColor() {
        paint.setColor(Color.rgb(color.r, color.g, color.b));
        paint.setAlpha(opacity);
    }

    @Override
    public void draw(CanvasWrapper canvas) {

        canvas.drawRect(0,0, getContentSize().width, getContentSize().height, paint);
        super.draw(canvas);
    }

    public Color3B getColor() {
        return Color3B.ccc3(color.r, color.g, color.b);
    }

    // Color Protocol
    public void setColor(Color3B color) {
        this.color = Color3B.ccc3(color.r, color.g, color.b);
        updateColor();
    }

    // Opacity Protocol
    public void setOpacity(int o) {
        opacity = o;
        updateColor();
    }

    public int getOpacity() {
        return opacity;
    }

    // Size protocol

    @Override
    public float getWidth() {
        return getContentSize().getWidth();
    }

    @Override
    public float getHeight() {
        return getContentSize().getHeight();
    }


    public void changeWidthAndHeight(float w, float h) {
        setContentSize(Size.make(w, h));
    }

    /**
     * change width
     */
    public void changeWidth(float w) {
        setContentSize(Size.make(w, getHeight()));
    }

    /**
     * change height
     */
    public void changeHeight(float h) {
        setContentSize(Size.make(getWidth(), h));
    }

}

