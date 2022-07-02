package com.vipjokerstudio.cocoskotlin.core.nodes

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.vipjokerstudio.cocoskotlin.core.types.Color3B
import com.vipjokerstudio.cocoskotlin.core.types.CGRect
import org.jbox2d.common.Vec2
import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper
import com.vipjokerstudio.cocoskotlin.core.types.Color4B
import com.vipjokerstudio.cocoskotlin.core.types.Size
import com.vipjokerstudio.cocoskotlin.util.Converter
import java.util.HashMap


open class CCSprite : CCNode {

    private var animations: HashMap<String?, CCAnimation>? = null

    var flipY_ = false

    var flipX_ = false
    var opacity_ = 0

    override var opacity: Int
        get() = opacity_
        set(anOpacity) {
            opacity_ = anOpacity
            if (opacityModifyRGB_) color = colorUnmodified_
            updateColor()
        }

    var color_: Color3B? = null
    var colorUnmodified_: Color3B? = null
    var opacityModifyRGB_ = false
    private val frameRect = Rect()
    fun setOpacityModifyRGB(modify: Boolean) {
        if (opacityModifyRGB_ != modify) {
            val oldColor = color_
            opacityModifyRGB_ = modify
            color = oldColor
        }
    }

    override var color: Color3B?
        get() = if (opacityModifyRGB_) {
            Color3B(colorUnmodified_)
        } else Color3B(color_)
        set(color3) {
            color_!!.set(color3)
            colorUnmodified_!!.set(color3)
            if (opacityModifyRGB_) {
                color_!!.r = color3!!.r * opacity_ / 255
                color_!!.g = color3.g * opacity_ / 255
                color_!!.b = color3.b * opacity_ / 255
            }
            updateColor()
        }

    var texture: Bitmap? = null

    var rect: CGRect? = null
    var textureRectRotated = false

    var offsetPosition_: Vec2? = null
    var unflippedOffsetPositionFromCenter_: Vec2? = null
    var atlasIndex = 0


    var dirty_ // Sprite needs to be updated
            = false
    var recursiveDirty_ // Subchildren needs to be updated
            = false
    var hasChildren_ // optimization to check if it contain children
            = false
    var usesSpriteSheet = false
    var textureRect: CGRect?
        get() = rect
        set(rect) {
            setTextureRect(rect, textureRectRotated)
        }
    constructor(texture: Bitmap) {
        val width = texture.width
        val height = texture.height
        val rect = CGRect.make(0f, 0f, width.toFloat(), height.toFloat())
        init(texture, rect)
    }

    constructor(texture: Bitmap, rect: CGRect?) {
        init(texture, rect)
    }

    protected fun init(texture: Bitmap, rect: CGRect?) {
        assert(texture != null) { "Invalid texture for sprite" }
        // IMPORTANT: [self init] and not [super init];
        init()
        this.texture = texture
        textureRect = rect
    }

    constructor(spriteFrame: CCSpriteFrame?) {
        init(spriteFrame)
    }

    protected fun init(spriteFrame: CCSpriteFrame?) {
        assert(spriteFrame != null) { "Invalid spriteFrame for sprite" }
        textureRectRotated = spriteFrame!!.rotated
        init(spriteFrame.getTexture(), spriteFrame.getRect())
        setDisplayFrame(spriteFrame)
    }

    constructor(spriteFrameName: String?, isFrame: Boolean) {
        assert(spriteFrameName != null) { "Invalid spriteFrameName for sprite" }
        val frame = CCSpriteFrameCache.sharedSpriteFrameCache()
            .getSpriteFrame(spriteFrameName)
        init(frame)
    }

    constructor(filepath: String?) {
        assert(filepath != null) { "Invalid filename for sprite" }
        val texture = CCTextureCache.getInstance().addImage(filepath)
        if (texture != null) {
            val rect = CGRect.make(0f, 0f, 0f, 0f)
            rect.size.width = texture.width.toFloat()
            rect.size.height = texture.height.toFloat()
            init(texture, rect)
        } else {
            Converter.CCLOGERROR("CCSprite", "Unable to load texture from file: $filepath")
        }
    }



    constructor(filepath: String?, rect: CGRect?) {
        assert(filepath != null) { "Invalid filename for sprite" }
        val texture = CCTextureCache.getInstance().addImage(filepath)
        texture?.let { init(it, rect) }
    }

    constructor(image: Bitmap?, key: String?) {
        assert(image != null) { "Invalid CGImageRef for sprite" }

        // XXX: possible bug. See issue #349. New API should be added
        val texture = CCTextureCache.getInstance().addImage(image, key)
        val width = texture.width.toFloat()
        val height = texture.height.toFloat()
        val rect = CGRect.make(0f, 0f, width, height)
        init(texture, rect)
    }
    fun setTextureRect(x: Float, y: Float, w: Float, h: Float, rotated: Boolean) {
        setTextureRect(x, y, w, h, w, h, rotated)
    }

    fun setTextureRect(rect: CGRect?, rotated: Boolean) {
        setTextureRect(rect, rect!!.size, rotated)
    }

    fun useSelfRender() {
        atlasIndex = CCSpriteIndexNotInitialized
        usesSpriteSheet = false
        recursiveDirty_ = false
        dirty_ = recursiveDirty_
        val x1 = 0 + offsetPosition_!!.x
        val y1 = 0 + offsetPosition_!!.y
        val x2 = x1 + rect!!.size.width
        val y2 = y1 + rect!!.size.height
    }

    protected fun init() {
        dirty_ = false
        recursiveDirty_ = false

        offsetPosition_ = Vec2()
        unflippedOffsetPositionFromCenter_ = Vec2()
        rect = CGRect.make(0f, 0f, 1f, 1f)

        useSelfRender()
        opacityModifyRGB_ = true
        opacity_ = 255
        color_ = Color3B(Color3B.ccWHITE)
        colorUnmodified_ = Color3B(Color3B.ccWHITE)

        // update texture (calls updateBlendFunc)
        flipX_ = false
        flipY_ = flipX_

        // lazy alloc
        animations = null

        hasChildren_ = false

        setTextureRect(0f, 0f, 0f, 0f, textureRectRotated)
    }

    fun setDisplayFrame(frame: CCSpriteFrame?) {
        unflippedOffsetPositionFromCenter_!!.set(frame!!.offset)
        val newTexture = frame.getTexture()
        // update texture before updating texture rect
        if (texture == null) { //|| newTexture.name() != texture.name())
            texture = newTexture
        }

        // update rect
        setTextureRect(frame.rect, frame.originalSize, frame.rotated)
    }

    /** changes the display frame based on an animation and an index.  */
    fun setDisplayFrame(animationName: String?, frameIndex: Int) {
        if (animations == null) initAnimationDictionary()
        val anim = animations!![animationName]
        val frame = anim!!.frames()[frameIndex] as CCSpriteFrame
        setDisplayFrame(frame)
    }

    override var visible: Boolean
        get() = super.visible
        set(v) {
            if (v != visible) {
                super.visible = v
                if (usesSpriteSheet && !recursiveDirty_) {
                    recursiveDirty_ = true
                    dirty_ = recursiveDirty_
                    for (child in children) {
                        child.visible = v
                    }
                }
            }
        }

    /** adds an Animation to the Sprite.  */
    fun addAnimation(anim: CCAnimation) {
        // lazy alloc
        if (animations == null) initAnimationDictionary()
        animations!![anim.name()] = anim
    }

    /** returns an Animation given it's name.  */
    fun animationByName(animationName: String?): CCAnimation? {
        assert(animationName != null) { "animationName parameter must be non null" }
        return animations!![animationName]
    }

    fun updateColor() {
        val tmpR = color_!!.r / 255f
        val tmpG = color_!!.g / 255f
        val tmpB = color_!!.b / 255f
        val tmpA = opacity_ / 255f


        // renders using Sprite Manager
        if (usesSpriteSheet) {
            if (atlasIndex != CCSpriteIndexNotInitialized) {
                tmpColor4B.r = color_!!.r
                tmpColor4B.g = color_!!.g
                tmpColor4B.b = color_!!.b
                tmpColor4B.a = opacity_
            } else {
                // no need to set it recursively
                // update dirty_, don't update recursiveDirty_
                dirty_ = true
            }
        }
        // self render
        // do nothing
    }

    var flipX: Boolean
        get() = flipX_
        set(b) {
            if (flipX_ != b) {
                flipX_ = b
                textureRect = rect
            }
        }
    var flipY: Boolean
        get() = flipY_
        set(b) {
            if (flipY_ != b) {
                flipY_ = b
                textureRect = rect
            }
        }

    fun isFrameDisplayed(frame: CCSpriteFrame): Boolean {
        val r = frame.rect
        val p = frame.offset
        return CGRect.equalToRect(r, rect) && p == offsetPosition_
    }

    fun displayedFrame(): CCSpriteFrame {
        return CCSpriteFrame.frame(texture, rect, Vec2())
    }

    private fun initAnimationDictionary() {
        animations = HashMap()
    }

    private fun setTextureRect(rect: CGRect?, size: Size, rotated: Boolean) {
        setTextureRect(rect!!.origin.x, rect.origin.y, rect.size.width,
            rect.size.height, size.width, size.height, rotated)
        val x = unflippedOffsetPositionFromCenter_!!.x.toInt()
        val y = unflippedOffsetPositionFromCenter_!!.y.toInt()
        val width = size.width.toInt()
        val height = size.height.toInt()
        frameRect[x, y, width + x] = height + y
    }

    private fun setTextureRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        sw: Float,
        sh: Float,
        rotated: Boolean
    ) {
        rect!![x, y, w] = h
        textureRectRotated = rotated
        setContentSize(sw, sh)
        var relativeOffsetX = unflippedOffsetPositionFromCenter_!!.x
        var relativeOffsetY = unflippedOffsetPositionFromCenter_!!.y

        // issue #732
        if (flipX_) relativeOffsetX = -relativeOffsetX
        if (flipY_) relativeOffsetY = -relativeOffsetY
        offsetPosition_!!.x = relativeOffsetX + (contentSize.width - rect!!.size.width) / 2
        offsetPosition_!!.y = relativeOffsetY + (contentSize.height - rect!!.size.height) / 2

        // rendering using SpriteSheet
        if (usesSpriteSheet) {
            // update dirty_, don't update recursiveDirty_
            dirty_ = true
        } else { // self rendering
            // Atlas: Vertex
            val x1 = 0 + offsetPosition_!!.x
            val y1 = 0 + offsetPosition_!!.y
            val x2 = x1 + w
            val y2 = y1 + h
        }
    }

    override fun setRelativeAnchorPoint(relative: Boolean) {
        assert(!usesSpriteSheet) { "relativeTransformAnchor is invalid in CCSprite" }
        super.setRelativeAnchorPoint(relative)
    }

    override fun reorderChild(child: CCNode?, z: Int) {
        if (z == child!!.zOrder) return
        if (usesSpriteSheet) {
            removeChild(child, false)
            addChild(child, z)
        } else {
            super.reorderChild(child, z)
        }
    }

    override fun addChild(child: CCNode?, z: Int, aTag: String): CCNode {
        super.addChild(child, z, aTag)
        if (child is CCSprite && usesSpriteSheet) {
            val sprite = child
        }
        hasChildren_ = true
        return this
    }

    override fun removeChild(node: CCNode?, doCleanup: Boolean) {
        if (usesSpriteSheet) {
            val sprite = node as CCSprite?
        }
        super.removeChild(node, doCleanup)
        hasChildren_ = children.size > 0
    }

    override fun removeAllChildren(doCleanup: Boolean) {
        if (usesSpriteSheet) {
            for (child in children) {
                val sprite = child as CCSprite?
            }
        }
        super.removeAllChildren(doCleanup)
        hasChildren_ = false
    }

    override fun draw(canvas: CanvasWrapper?) {
        paint.strokeWidth = 1f
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        canvas!!.drawBitmap(texture, contentSize, getAnchorPoint())
        super.draw(canvas)
    }

    companion object {
        const val CCSpriteIndexNotInitialized = -0x1



        fun sprite(texture: Bitmap): CCSprite {
            return CCSprite(texture)
        }

        fun sprite(texture: Bitmap, rect: CGRect?): CCSprite {
            return CCSprite(texture, rect)
        }


        fun sprite(spriteFrame: CCSpriteFrame?): CCSprite {
            return CCSprite(spriteFrame)
        }

        fun sprite(spriteFrameName: String?, isFrame: Boolean): CCSprite {
            return CCSprite(spriteFrameName, isFrame)
        }


        fun sprite(filepath: String?): CCSprite {
            return CCSprite(filepath)
        }

        fun sprite(filepath: String?, rect: CGRect?): CCSprite {
            return CCSprite(filepath, rect)
        }

        fun sprite(image: Bitmap?, key: String?): CCSprite {
            return CCSprite(image, key)
        }

        private val tmpColor4B = Color4B.ccc4(0, 0, 0, 0)
        private val tmpColors = arrayOf(tmpColor4B, tmpColor4B, tmpColor4B, tmpColor4B)
    }
}