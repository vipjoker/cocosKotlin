package com.vipjokerstudio.cocoskotlin.core.nodes;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCAnimation;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSpriteFrame;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSpriteFrameCache;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCTextureCache;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCTextureProtocol;
import com.vipjokerstudio.cocoskotlin.core.types.CGRect;
import com.vipjokerstudio.cocoskotlin.core.types.Color3B;
import com.vipjokerstudio.cocoskotlin.core.types.Color4B;
import com.vipjokerstudio.cocoskotlin.core.types.Size;
import com.vipjokerstudio.cocoskotlin.util.Converter;

import org.jbox2d.common.Vec2;

import java.util.HashMap;

/** CCSprite is a 2d image ( http://en.wikipedia.org/wiki/Sprite_(computer_graphics) )
 *
 * CCSprite can be created with an image, or with a sub-rectangle of an image.
 *
 * If the parent or any of its ancestors is a CCSpriteSheet then the following features/limitations are valid
 *	- Features when the parent is a CCSpriteSheet:
 *		- MUCH faster rendering, specially if the CCSpriteSheet has many children. All the children will be drawn in a single batch.
 *
 *	- Limitations
 *		- Camera is not supported yet (eg: CCOrbitCamera action doesn't work)
 *		- GridBase actions are not supported (eg: CCLens, CCRipple, CCTwirl)
 *		- The Alias/Antialias property belongs to CCSpriteSheet, so you can't individually set the aliased property.
 *		- The Blending function property belongs to CCSpriteSheet, so you can't individually set the blending function property.
 *		- Parallax scroller is not supported, but can be simulated with a "proxy" sprite.
 *
 *  If the parent is an standard CCNode, then CCSprite behaves like any other CCNode:
 *    - It supports blending functions
 *    - It supports aliasing / antialiasing
 *    - But the rendering will be slower: 1 draw per children.
 *
 */
public class CCSprite extends CCNode implements CCTextureProtocol {


	
	/// CCSprite invalid index on the CCSpriteSheet
	public static final int CCSpriteIndexNotInitialized = 0xffffffff;

    /** 
     * Whether or not an CCSprite will rotate, scale or translate with it's parent.
     * Useful in health bars, when you want that the health bar translates with it's parent
     * but you don't want it to rotate with its parent.
     * @since v0.99.0
     */
	//! Translate with it's parent
	public static final int CC_HONOR_PARENT_TRANSFORM_TRANSLATE =  1 << 0;
	//! Rotate with it's parent
	public static final int CC_HONOR_PARENT_TRANSFORM_ROTATE	=  1 << 1;
	//! Scale with it's parent
	public static final int CC_HONOR_PARENT_TRANSFORM_SCALE		=  1 << 2;

	//! All possible transformation enabled. Default value.
	public static final int CC_HONOR_PARENT_TRANSFORM_ALL		=  CC_HONOR_PARENT_TRANSFORM_TRANSLATE
            | CC_HONOR_PARENT_TRANSFORM_ROTATE | CC_HONOR_PARENT_TRANSFORM_SCALE;

   
    private HashMap<String, CCAnimation> animations;

	// image is flipped
    /** whether or not the sprite is flipped vertically.
     * It only flips the texture of the sprite, and not the texture of the sprite's children.
     * Also, flipping the texture doesn't alter the anchorPoint.
     * If you want to flip the anchorPoint too, and/or to flip the children too use: 
     * sprite.scaleY *= -1;
    */
	public boolean flipY_;

	/** whether or not the sprite is flipped horizontally. 
     * It only flips the texture of the sprite, and not the texture of the sprite's children.
     * Also, flipping the texture doesn't alter the anchorPoint.
     * If you want to flip the anchorPoint too, and/or to flip the children too use:
     * sprite.scaleX *= -1;
    */
	public boolean flipX_;

	// opacity and RGB protocol
    /** opacity: conforms to CCRGBAProtocol protocol */
    int		opacity_;

    public int getOpacity() {
        return opacity_;
    }

    public void setOpacity(int anOpacity) {
        opacity_			= anOpacity;

        // special opacity for premultiplied textures
        if( opacityModifyRGB_ )
            setColor(colorUnmodified_);
        updateColor();
    }

    /** RGB colors: conforms to CCRGBAProtocol protocol */
	Color3B color_;
	Color3B colorUnmodified_;
	boolean		opacityModifyRGB_;

	private Rect frameRect = new Rect();

    public void setOpacityModifyRGB(boolean modify) {
        if (opacityModifyRGB_ != modify) {
            Color3B oldColor	= this.color_;
            opacityModifyRGB_	= modify;
            setColor(oldColor);
        }
    }

    public Color3B getColor() {
        if(opacityModifyRGB_){
            return new Color3B(colorUnmodified_);
        }
        return new Color3B(color_);
    }

    public void setColor(Color3B color3) {
        color_.set(color3);
        colorUnmodified_.set(color3);

        if( opacityModifyRGB_ ){
            color_.r = color3.r * opacity_/255;
            color_.g = color3.g * opacity_/255;
            color_.b = color3.b * opacity_/255;
        }

        updateColor();
    }

	//
	// Data used when the sprite is self-rendered
	//
	Bitmap texture;				// Texture used to render the sprite

    /** conforms to CCTextureProtocol protocol */

	// texture pixels
	CGRect rect;
	Boolean rectRotated_ = false;
	
    /** offset position of the sprite. Calculated automatically by editors like Zwoptex.
      @since v0.99.0
    */
	Vec2 offsetPosition_;	// absolute
	Vec2 unflippedOffsetPositionFromCenter_;

	//
	// Data used when the sprite is rendered using a CCSpriteSheet
	//
    /** weak reference of the CCTextureAtlas used when the sprite is rendered using a CCSpriteSheet */

    /** The index used on the TextureATlas.
     * Don't modify this value unless you know what you are doing */
	public		int			atlasIndex;	// Absolute (real) Index on the SpriteSheet

    /** weak reference to the CCSpriteSheet that renders the CCSprite */

	// whether or not to transform according to its parent transformations
    /** whether or not to transform according to its parent transfomrations.
     * Useful for health bars. eg: Don't rotate the health bar, even if the parent rotates.
     * IMPORTANT: Only valid if it is rendered using an CCSpriteSheet.
     * @since v0.99.0
    */
    int                     honorParentTransform_;

    /** whether or not the Sprite needs to be updated in the Atlas */
	boolean					dirty_;					// Sprite needs to be updated
	boolean					recursiveDirty_;		// Subchildren needs to be updated
	boolean					hasChildren_;			// optimization to check if it contain children
	
	// vertex coords, texture coords and color info
    /** buffers that are going to be rendered */
    /** the quad (tex coords, vertex coords and color) information */



    


    boolean usesSpriteSheet;

    public CGRect getTextureRect() {
        return rect;
    }
    
    public Boolean getTextureRectRotated()
    {
    	return rectRotated_;
    }

    /** Creates an sprite with a texture.
      The rect used will be the size of the texture.
      The offset will be (0,0).
      */
    public static CCSprite sprite(Bitmap texture) {
        return new CCSprite(texture);
    }

    /** Creates an sprite with a texture and a rect.
      The offset will be (0,0).
      */
    public static CCSprite sprite(Bitmap texture, CGRect rect) {
        return new CCSprite(texture, rect);
    }

    /** Creates an sprite with an sprite frame.
    */
    public static CCSprite sprite(CCSpriteFrame spriteFrame) {
        return new CCSprite(spriteFrame);
    }

    /** Creates an sprite with an sprite frame name.
      An CCSpriteFrame will be fetched from the CCSpriteFrameCache by name.
      If the CCSpriteFrame doesn't exist it will raise an exception.
      @since v0.9
      */
    public static CCSprite sprite(String spriteFrameName, boolean isFrame) {
        return new CCSprite(spriteFrameName, isFrame);
    }

    /** Creates an sprite with an image filepath.
      The rect used will be the size of the image.
      The offset will be (0,0).
      */
    public static CCSprite sprite(String filepath) {
        return new CCSprite(filepath);
    }

    /** Creates an sprite with an image filepath and a rect.
      The offset will be (0,0).
      */
    public static CCSprite sprite(String filepath, CGRect rect) {
        return new CCSprite(filepath, rect);
    }
    
    /** Creates an sprite with a CGImageRef.
     * BE AWARE OF the fact that copy of image is stored in memory,
     * use assets method if you can.
     * 
     * @deprecated Use spriteWithCGImage:key: instead. Will be removed in v1.0 final
//     */
//    public static CCSprite sprite(Bitmap image) {
//        return new CCSprite(image);
//    }

    /** Creates an sprite with a CGImageRef and a key.
      The key is used by the CCTextureCache to know if a texture was already created with this CGImage.
      For example, a valid key is: @"sprite_frame_01".
      If key is nil, then a new texture will be created each time by the CCTextureCache. 

    * BE AWARE OF the fact that copy of image is stored in memory,
    * use assets method if you can.
      
      @since v0.99.0
      */
    public static CCSprite sprite(Bitmap image, String key) {
        return new CCSprite(image, key);
    }

    /** Creates an sprite with an CCSpriteSheet and a rect
    */


    /** Initializes an sprite with a texture.
      The rect used will be the size of the texture.
      The offset will be (0,0).
      */
    public CCSprite(Bitmap texture) {
        int width = texture.getWidth();
        int height = texture.getHeight();
        CGRect rect = CGRect.make(0, 0, width, height);
	    init(texture, rect);
    }


    public CCSprite(Bitmap texture, CGRect rect) {
    	init(texture, rect);
    }
    
    /** Initializes an sprite with a texture and a rect.
      The offset will be (0,0).
      */
    protected void init(Bitmap texture, CGRect rect) {
        assert texture!=null:"Invalid texture for sprite";
        // IMPORTANT: [self init] and not [super init];
        init();
        setTexture(texture);
        setTextureRect(rect);
    }

    /** Initializes an sprite with an sprite frame.
    */
    public CCSprite(CCSpriteFrame spriteFrame) {
    	init(spriteFrame);
    }
    
    protected void init(CCSpriteFrame spriteFrame) {
        assert spriteFrame!=null:"Invalid spriteFrame for sprite";
        
        rectRotated_ = spriteFrame.rotated;
        init(spriteFrame.getTexture(), spriteFrame.getRect());
        setDisplayFrame(spriteFrame);    	
    }

    /** Initializes an sprite with an sprite frame name.
      An CCSpriteFrame will be fetched from the CCSpriteFrameCache by name.
      If the CCSpriteFrame doesn't exist it will raise an exception.
      @since v0.9
      */
    public CCSprite(String spriteFrameName, boolean isFrame) {
        assert spriteFrameName!=null:"Invalid spriteFrameName for sprite";
        CCSpriteFrame frame = CCSpriteFrameCache.sharedSpriteFrameCache()
            .getSpriteFrame(spriteFrameName);
        init(frame);
    }

    /** Initializes an sprite with an image filepath.
      The rect used will be the size of the image.
      The offset will be (0,0).
      */
    public CCSprite(String filepath) {
        assert filepath!=null:"Invalid filename for sprite";

        Bitmap texture = CCTextureCache.getInstance().addImage(filepath);
        if( texture != null) {
            CGRect rect = CGRect.make(0, 0, 0, 0);
            rect.size.width = texture.getWidth();
            rect.size.height= texture.getHeight();





            init(texture, rect);
        } else {
		Converter.CCLOGERROR("CCSprite", "Unable to load texture from file: " + filepath);
        }
    }

    public CCSprite() {
    	init();
    }
    
    /** Initializes an sprite with an image filepath, and a rect.
      The offset will be (0,0).
      */
    public CCSprite(String filepath, CGRect rect) {
        assert filepath!=null:"Invalid filename for sprite";

        Bitmap texture = CCTextureCache.getInstance().addImage(filepath);
        if( texture != null) {
            init(texture, rect);
        }
    }



    /** Initializes an sprite with a CGImageRef and a key
      The key is used by the CCTextureCache to know if a texture was already created with this CGImage.
      For example, a valid key is: @"sprite_frame_01".
      If key is nil, then a new texture will be created each time by the CCTextureCache. 
      @since v0.99.0
      */
    public CCSprite(Bitmap image, String key) {
        assert image!=null:"Invalid CGImageRef for sprite";

        // XXX: possible bug. See issue #349. New API should be added
        Bitmap texture = CCTextureCache.getInstance().addImage(image, key);

        float width= texture.getWidth();
        float height = texture.getHeight();
        CGRect rect = CGRect.make(0, 0, width, height );

        init(texture, rect);
    }

    /** Initializes an sprite with an CCSpriteSheet and a rect
    */


    /** updates the texture rect of the CCSprite.
    */

    
	public void setTextureRect(float x, float y, float w, float h, Boolean rotated) {
    	setTextureRect(x, y, w, h, w, h, rotated);
    }
	
	public void setTextureRect(CGRect rect, Boolean rotated) {
	    setTextureRect(rect, rect.size, rotated);
    }
	
    public void setTextureRect(CGRect rect) {
	    setTextureRect(rect, rectRotated_);
    }

    /** tell the sprite to use self-render.
      @since v0.99.0
      */
    public void useSelfRender() {
        atlasIndex = CCSpriteIndexNotInitialized;
        usesSpriteSheet = false;
        dirty_ = recursiveDirty_ = false;

        float x1 = 0 + offsetPosition_.x;
        float y1 = 0 + offsetPosition_.y;
        float x2 = x1 + rect.size.width;
        float y2 = y1 + rect.size.height;


    }


    protected void init() {

		dirty_ = false;
        recursiveDirty_ = false;
		
		// zwoptex default values
		offsetPosition_ = new Vec2();
		unflippedOffsetPositionFromCenter_ = new Vec2();
        rect = CGRect.make(0, 0, 1, 1);
		
		// by default use "Self Render".
		// if the sprite is added to an SpriteSheet,
        // then it will automatically switch to "SpriteSheet Render"
		useSelfRender();
		
		opacityModifyRGB_			= true;
		opacity_					= 255;
		color_                      = new Color3B(Color3B.ccWHITE);
        colorUnmodified_	        = new Color3B(Color3B.ccWHITE);
				
		// update texture (calls updateBlendFunc)
		setTexture(null);
		
		flipY_ = flipX_ = false;
		
		// lazy alloc
		animations = null;
		
		// default transform anchor: center
//		anchorPoint.set(0.5f, 0.5f);
		
		
		honorParentTransform_ = CC_HONOR_PARENT_TRANSFORM_ALL;
		hasChildren_ = false;
		
		// Atlas: Color

		// Atlas: Vertex		
		// updated in "useSelfRender"		
		// Atlas: TexCoords
		setTextureRect(0, 0, 0, 0, rectRotated_);
    }

    /** sets a new display frame to the CCSprite. */
    public void setDisplayFrame(CCSpriteFrame frame) {
        unflippedOffsetPositionFromCenter_.set(frame.offset);

        Bitmap newTexture = frame.getTexture();
        // update texture before updating texture rect
        if ( texture == null ) {//|| newTexture.name() != texture.name())
            setTexture(newTexture);
        }

        // update rect
        setTextureRect(frame.rect, frame.originalSize, frame.rotated);
    }


    /** changes the display frame based on an animation and an index. */
    public void setDisplayFrame(String animationName, int frameIndex) {
        if (animations == null)
            initAnimationDictionary();

        CCAnimation anim = animations.get(animationName);
        CCSpriteFrame frame = (CCSpriteFrame) anim.frames().get(frameIndex);
        setDisplayFrame(frame);
    }

    @Override
    public void setVisible(boolean v) {
        if( v != getVisible()) {
        	super.setVisible(v);
            if( usesSpriteSheet && ! recursiveDirty_ ) {
                dirty_ = recursiveDirty_ = true;
                if(children != null)
	                for (CCNode child: children) {
	                    child.setVisible(v);
	                }
            }
        }
    }


    /** adds an Animation to the Sprite. */
    public void addAnimation(CCAnimation anim) {
        // lazy alloc
        if (animations == null)
            initAnimationDictionary();

        animations.put(anim.name(), anim);
    }

    /** returns an Animation given it's name. */
    public CCAnimation animationByName(String animationName) {
        assert animationName != null : "animationName parameter must be non null";
        return animations.get(animationName);
    }
    
    private static final Color4B tmpColor4B = Color4B.ccc4(0, 0, 0, 0);
    private static final Color4B[] tmpColors = new Color4B[] { tmpColor4B, tmpColor4B, tmpColor4B, tmpColor4B };
    public void updateColor() {		
        float tmpR = color_.r/255.f;
        float tmpG = color_.g/255.f;
        float tmpB = color_.b/255.f;
        float tmpA = opacity_/255.f;
        

        // renders using Sprite Manager
        if(usesSpriteSheet) {
            if( atlasIndex != CCSpriteIndexNotInitialized) {
            	tmpColor4B.r = color_.r; tmpColor4B.g = color_.g; tmpColor4B.b = color_.b; tmpColor4B.a = opacity_;

            } else {
                // no need to set it recursively
                // update dirty_, don't update recursiveDirty_
                dirty_ = true;
            }
        }
        // self render
        // do nothing
    }
    
    public void setFlipX(boolean b) {
        if( flipX_ != b ) {
            flipX_ = b;
            setTextureRect(rect);
        }
    }

    public boolean getFlipX() {
        return flipX_;
    }

    public void setFlipY(boolean b) {
        if( flipY_ != b ) {
            flipY_ = b;	
            setTextureRect(rect);
        }	
    }

    public boolean getFlipY() {
        return flipY_;
    }

    public void setTexture(Bitmap texture) {
        assert !usesSpriteSheet : "CCSprite: setTexture doesn't work when the sprite is rendered using a CCSpriteSheet";

        // accept texture==nil as argument
        assert (texture==null || texture instanceof Bitmap) : "setTexture expects a CCTexture2D. Invalid argument";
        this.texture = texture;
    }

    public Bitmap getTexture() {
        return texture;
    }

    /** returns whether or not a CCSpriteFrame is being displayed */
    public boolean isFrameDisplayed(CCSpriteFrame frame) {
        CGRect r = frame.rect;
        Vec2 p = frame.offset;
        return (CGRect.equalToRect(r, rect) && p.equals(offsetPosition_));
    }

    /** returns the current displayed frame. */
    public CCSpriteFrame displayedFrame() {
	    return CCSpriteFrame.frame(getTexture(), rect, new Vec2());
    }


    private void initAnimationDictionary() {
        animations = new HashMap<String, CCAnimation>();
    }

    private void setTextureRect(CGRect rect, Size size, Boolean rotated) {
    	setTextureRect(rect.origin.x, rect.origin.y, rect.size.width, rect.size.height, size.width, size.height, rotated);
    	int x =(int)unflippedOffsetPositionFromCenter_.x;
    	int y =(int)unflippedOffsetPositionFromCenter_.y;
    	int width = (int)size.width;
    	int height = (int)size.height;
    	frameRect.set(x,y,width + x , height + y);
    }
	private void setTextureRect(float x, float y, float w, float h, float sw, float sh, boolean rotated) {
        rect.set(x, y, w, h);
        rectRotated_ = rotated;

        setContentSize(sw, sh);

        float relativeOffsetX = unflippedOffsetPositionFromCenter_.x;
        float relativeOffsetY = unflippedOffsetPositionFromCenter_.y;
        
        // issue #732
        if( flipX_ )
        	relativeOffsetX = - relativeOffsetX;
        if( flipY_ )
        	relativeOffsetY = - relativeOffsetY;

        offsetPosition_.x = relativeOffsetX + (getContentSize().width - rect.size.width) / 2;
        offsetPosition_.y = relativeOffsetY + (getContentSize().height - rect.size.height) / 2;

        // rendering using SpriteSheet
        if(usesSpriteSheet) {
            // update dirty_, don't update recursiveDirty_
            dirty_ = true;
        } else { // self rendering
            // Atlas: Vertex
            float x1 = 0 + offsetPosition_.x;
            float y1 = 0 + offsetPosition_.y;
            float x2 = x1 + w;
            float y2 = y1 + h;

        }
    }

    public void setRelativeAnchorPoint(boolean relative) {
        assert !usesSpriteSheet :"relativeTransformAnchor is invalid in CCSprite";
        super.setRelativeAnchorPoint(relative);
    }

    public void reorderChild(CCNode child, int z) {
        // assert child != null: "Child must be non-nil";
        // assert children.has(child): "Child doesn't belong to Sprite";

        if( z == child.getZOrder() )
            return;

        if(usesSpriteSheet) {
            // XXX: Instead of removing/adding, it is more efficient to reorder manually
            removeChild(child, false);
            addChild(child, z);
        } else {
            super.reorderChild(child, z);
        }
    }

    @Override
    public CCNode addChild(CCNode child, int z, String aTag) {
        super.addChild(child, z, aTag);
        
        if(child instanceof CCSprite && usesSpriteSheet) {
        	CCSprite sprite = (CCSprite)child;
        }

        hasChildren_ = true;

        return this;
    }

    public void removeChild(CCNode node, boolean doCleanup) {
        if(usesSpriteSheet) {
        	CCSprite sprite = (CCSprite) node;
        }

        super.removeChild(node, doCleanup);

        hasChildren_ = (children.size() > 0);
    }

    public void removeAllChildren(boolean doCleanup) {
        if(usesSpriteSheet) {
            for( CCNode child : children) {
            	CCSprite sprite = (CCSprite)child;
            }
        }

        super.removeAllChildren(doCleanup);
        hasChildren_ = false;
    }
    public void draw(CanvasWrapper canvas) {

        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(texture, getContentSize(),getAnchorPoint());
        super.draw(canvas);


    }


}

