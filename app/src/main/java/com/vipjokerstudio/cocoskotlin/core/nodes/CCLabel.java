package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.nodes.TypeFaceCache;
import com.vipjokerstudio.cocoskotlin.core.types.Size;


/** CCLabel is a subclass of CCTextureNode that knows how to render text labels
 *
 * All features from CCTextureNode are valid in CCLabel
 *
 * CCLabel objects are slow. Consider using CCLabelAtlas or CCBitmapFontAtlas instead.
 */

public class CCLabel extends CCNode {

    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private Size _dimensions;
    private TextAlignment _alignment;
    private String _fontName;
    private float _fontSize;
    private String text;

    /** creates a CCLabel from a fontname, alignment, dimension and font size */
    public static CCLabel makeLabel(String string, final Size dimensions, TextAlignment alignment,
                                    String fontname, float fontsize) {
        return new CCLabel(string, dimensions, alignment, fontname, fontsize);
    }

    /** creates a CCLabel from a fontname and font size */
    public static CCLabel makeLabel(String string, String fontname, float fontsize) {
        return new CCLabel(string, Size.make(0, 0), TextAlignment.CENTER, fontname, fontsize);
    }

    /** initializes the CCLabel with a font name and font size */
    protected CCLabel(CharSequence string, String fontname, float fontsize) {
        this(string, Size.make(0,0), TextAlignment.CENTER, fontname, fontsize);
    }

    /** initializes the CCLabel with a font name, alignment, dimension and font size */
    protected CCLabel(CharSequence string, final Size dimensions, TextAlignment alignment,
                      String name, float size) {
    	super();
        Typeface typeface = TypeFaceCache.getInstance().getFont(name);

        _dimensions = dimensions;
        _alignment = alignment;
        _fontName = name;
        _fontSize = size;

        paint.setColor(Color.WHITE);
        paint.setTextAlign(parseTextAlign(alignment));
        paint.setTextSize(size);
        paint.setTypeface(typeface);
        setString(string);
    }

    public void setTextColor(int color){
        paint.setColor(color);
    }

    private Paint.Align parseTextAlign(TextAlignment alignment){
        switch (alignment){
            case LEFT:return Paint.Align.LEFT;
            case RIGHT:return Paint.Align.RIGHT;
            default:return Paint.Align.CENTER;
        }
    }
    public void setString(CharSequence seq) {   	
    	if(text != null && text.equals(seq))
    		return;
    	text = seq.toString();
    }



    @Override
    public void draw(CanvasWrapper canvas) {
        super.draw(canvas);
        canvas.drawText(text, getPositionRef().x, getPositionRef().y,paint);

    }

    public String toString() {
        return "CCLabel <" + CCLabel.class.getSimpleName() + " = " + this.hashCode()
                + " | FontName = " + _fontName + ", FontSize = " + _fontSize + ">";
    }

}
