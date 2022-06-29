package com.vipjokerstudio.cocoskotlin.core.nodes;


import android.graphics.Bitmap;

import com.vipjokerstudio.cocoskotlin.core.types.CGRect;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;

/** A CCSpriteFrame has:
  - texture: A CCTexture2D that will be used by the CCSprite
  - rectangle: A rectangle of the texture


  You can modify the frame of a CCSprite by doing:

  CCSpriteFrame *frame = [CCSpriteFrame frameWithTexture:texture rect:rect offset:offset];
  [sprite setDisplayFrame:frame];
  */
public class CCSpriteFrame {
    /** rect of the frame */
    CGRect rect;

    /** Flag shows that Zwoptex rotated texture for optimizations */
    Boolean rotated;

    /** offset of the frame */
    Vec2 offset;
    /** original size of the trimmed image */
    Size originalSize;
    /** texture of the frame */
    Bitmap texture;
    public CGRect getRect() {
    	return CGRect.make(rect);
    }
    


    public Vec2 getOffset() {
    	return new Vec2(offset.x, offset.y);
    }
    
    public Vec2 getOffsetRef() {
    	return offset;
    }
    

    public Bitmap getTexture() {
    	return texture;
    }
    

    public Boolean getRotated() {
    	return rotated;
    }

    /** Create a CCSpriteFrame with a texture, rect and offset.
      It is assumed that the frame was not trimmed.
      */
    public static CCSpriteFrame frame(Bitmap texture, CGRect rect, Vec2 offset) {
        return new CCSpriteFrame(texture, rect, offset);
    }

    /** Create a CCSpriteFrame with a texture, rect, offset and originalSize.
      The originalSize is the size in pixels of the frame before being trimmed.
      */
    public static CCSpriteFrame frame(Bitmap texture, CGRect rect, Vec2 offset, Size originalSize ){
        return new CCSpriteFrame(texture, rect, offset, originalSize, false);
    }
    
    /** Create a CCSpriteFrame with a texture, rect, offset, originalSize and rotated.
    	The originalSize is the size in pixels of the frame before being trimmed.
    */
    public static CCSpriteFrame frame(Bitmap texture, CGRect rect, Boolean rotated, Vec2 offset, Size originalSize ){
        return new CCSpriteFrame(texture, rect, offset, originalSize, rotated);
    }

    /** Initializes a CCSpriteFrame with a texture, rect and offset.
      It is assumed that the frame was not trimmed.
      */
    protected CCSpriteFrame (Bitmap texture, CGRect rect, Vec2 offset) {
        this(texture, rect, offset, rect.size, false);
    }

    /** Initializes a CCSpriteFrame with a texture, rect, offset and originalSize.
      The originalSize is the size in pixels of the frame before being trimmed.
      */
    protected CCSpriteFrame (Bitmap texture, CGRect rect, Vec2 offset, Size originalSize, Boolean rotated) {
        this.texture = texture;
        this.offset = offset;
        this.rect = rect;
        this.originalSize = originalSize;
        this.rotated = rotated;
    }

    public CCSpriteFrame copy() {
        CCSpriteFrame copy = new CCSpriteFrame(texture, rect, offset, originalSize, rotated);
        return copy;
    }
}

