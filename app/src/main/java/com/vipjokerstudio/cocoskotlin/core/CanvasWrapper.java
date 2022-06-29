package com.vipjokerstudio.cocoskotlin.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;

/**
 * Created by Oleh Makhobei  on 22.03.20.
 */
public class CanvasWrapper {
    private  Canvas canvas;
    private Rect src = new Rect();
    private RectF out = new RectF();
    public void setCanvas(Canvas canvas,float height) {

        canvas.save();
        canvas.translate(0, height);

        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }


    //    public void drawRect(RectF bounds, Paint debugPaint) {
//        canvas.drawRect(bounds,debugPaint);
//    }

    public void drawRect(float x,float y, float width,float height,Paint paint){
        out.set(0, 0, width, height);
        out.offset(x,-(height + y));
        canvas.drawRect(out,paint);
    }

    public void save() {
        canvas.save();
    }

    public void restore() {
        canvas.restore();
    }

    public void scale(float scaleX, float scaleY, float centerX, float centerY) {
        canvas.scale(scaleX,scaleY,centerX,centerY);
    }

    public void translate(float x, float y) {
        canvas.translate(x,-y);
    }

    public void rotate(float rotation, float centerX, float centerY) {
        canvas.rotate(-rotation,centerX,centerY);
    }

    public void drawCircle(float x, float y, float radius, Paint stroke) {
        canvas.drawCircle(x,y,radius,stroke);
    }

    public void drawLine(float x, float y, float x1, float y1, Paint paint) {
        canvas.drawLine(x,-y,x1,-y1,paint);
    }

    public void drawPath(Path path, Paint paint) {
        canvas.drawPath(path,paint);
    }

    public void drawText(String text, float x, float y, Paint textPaint) {
        canvas.drawText(text,x,-y,textPaint);
    }

    public void drawBitmap(Bitmap texture, Rect frameRect, RectF bounds, Paint paint) {
        canvas.drawBitmap(texture,frameRect,bounds,paint);
    }

    public void drawBitmap(Bitmap texture, Size size, Vec2 anchor) {

        src.set(0,0,texture.getWidth(),texture.getHeight());

        out.set(0,0,size.width,size.height);
        out.offset(0,-size.height);

        out.offset(-anchor.x * size.width,anchor.y * size.height);




























        canvas.drawBitmap(texture,src,out,null);
//        canvas.drawBitmap(texture,100,100,null);
    }

    public void drawPoint(float centerX, float centerY, Paint paint) {
        canvas.drawPoint(centerX,centerY,paint);
    }
}
