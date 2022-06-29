package com.vipjokerstudio.cocoskotlin.core.nodes

import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.vipjokerstudio.cocoskotlin.core.events.TouchReciever
import com.vipjokerstudio.cocoskotlin.core.types.CGRect
import com.vipjokerstudio.cocoskotlin.core.types.Color3B
import org.jbox2d.common.Vec2
import com.vipjokerstudio.cocoskotlin.core.types.CGAffineTransform
import com.vipjokerstudio.cocoskotlin.util.Converter
import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper
import com.vipjokerstudio.cocoskotlin.core.actions.base.CCAction
import com.vipjokerstudio.cocoskotlin.core.actions.CCActionManager
import kotlin.jvm.JvmOverloads
import com.vipjokerstudio.cocoskotlin.core.actions.CCScheduler
import com.vipjokerstudio.cocoskotlin.core.actions.UpdateCallback
import android.view.MotionEvent
import com.vipjokerstudio.cocoskotlin.core.types.Size
import java.util.*

open class CCNode: TouchReciever {
    open var opacity = 255
    @JvmField
    protected var bounds = CGRect()
    var scaleX = 1.0f
    var scaleY = 1.0f

    /**
     * The z order of the node relative to it's "brothers": children of the same parent
     */
    var zOrder: Int
        private set
    open var color: Color3B? = null
    open var rotation = 0.0f
    var anchorPointRef: Vec2
        protected set
    public var positionRef: Vec2
        protected set
    protected var anchorPointInPixels_: Vec2

    /**
     * A custom user data pointer
     */
    var userData: Any?
    protected val contentSize: Size


    /**
     * whether or not the node is running
     */
    var isRunning = false
        private set
    private var isRelativeAnchorPoint_: Boolean
    private val transform_: CGAffineTransform
    private val inverse_: CGAffineTransform
    private val matrix = Matrix()
    private val converter = Converter()
    private var isTransformDirty_: Boolean
    private var isInverseDirty_: Boolean

    /**
     * A weak reference to the parent
     */
    var parent: CCNode? = null
    @JvmField
    protected var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected var debugPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var scale: Float
        get() {
            if (scaleX == scaleY) {
                return scaleX
            } else {
                Log.w(LOG_TAG, "CCNode#scale. ScaleX != ScaleY. Don't know which one to return")
            }
            return 0f
        }
        set(s) {
            scaleY = s
            scaleX = scaleY
            isInverseDirty_ = true
            isTransformDirty_ = isInverseDirty_
        }

    open fun setRelativeAnchorPoint(newValue: Boolean) {
        isRelativeAnchorPoint_ = newValue
        isInverseDirty_ = true
        isTransformDirty_ = isInverseDirty_
    }

    fun setContentSize(size: Size) {
        setContentSize(size.width, size.height)
    }

    fun setContentSize(w: Float, h: Float) {
        if (!(contentSize.width == w && contentSize.height == h)) {
            contentSize[w] = h // = Size.make(size.width, size.height);
            anchorPointInPixels_[contentSize.width * anchorPointRef.x] = contentSize.height * anchorPointRef.y
            isInverseDirty_ = true
            isTransformDirty_ = isInverseDirty_
        }
    }


    fun setAnchorPoint(pnt: Vec2) {
        setAnchorPoint(pnt.x, pnt.y)
    }

    fun setAnchorPoint(x: Float, y: Float) {
        anchorPointRef.x = x
        anchorPointRef.y = y
    }

    fun getAnchorPoint(): Vec2 {
        return Vec2(anchorPointRef.x, anchorPointRef.y)
    }

    val boundingBox: CGRect
        get() {
            val rect = CGRect.make(0f, 0f, contentSize.width, contentSize.height)
            return CGRect.applyAffineTransform(rect, nodeToParentTransform())
        }

    fun getBoundingBoxYosit(outputRect: CGRect): CGRect {
        //CGRect output = CGRect.make(0, 0, contentSize.width, contentSize.height);
        outputRect.origin.x = 0f
        outputRect.origin.y = 0f
        outputRect.size.width = contentSize.width
        outputRect.size.height = contentSize.height
        return CGRect.applyAffineTransformYosit(outputRect, nodeToParentTransform())
    }

    fun getPosition(): Vec2 {
        return Vec2(positionRef.x, positionRef.y)
    }

    fun setPosition(pnt: Vec2) {
        setPosition(pnt.x, pnt.y)
    }

    open fun setPosition(x: Float, y: Float) {
        positionRef[x] = y // = Vec2.make(pnt.x, pnt.y);
        isInverseDirty_ = true
        isTransformDirty_ = isInverseDirty_
    }

    /**
     * A CCCamera object that lets you move the node using a gluLookAt
     */
    private var camera_: CCCamera?

    // camera: lazy alloc
    val camera: CCCamera
        get() {
            if (camera_ == null) camera_ = CCCamera()
            return camera_!!
        }

    /**
     * Whether of not the node is visible. Default is YES
     */
    // is visible
    open var visible: Boolean = true

    /**
     * A tag used to identify the node easily
     */
    // a tag. any number you want to assign to the node
    var tag: String

    // openGL real Z vertex
    protected var vertexZ_ = 0f

    // used internally to alter the zOrder variable. DON'T call this method manually
    private fun _setZOrder(z: Int) {
        zOrder = z
    }

    // array of children
    @JvmField
    protected var children = Collections.synchronizedList(ArrayList<CCNode?>())


    open fun addChild(child: CCNode?, z: Int, tag: String): CCNode {
        assert(child != null) { "Argument must be non-nil" }
        assert(child!!.parent == null) { "child already added. It can't be added again" }
        insertChild(child, z)
        child.tag = tag
        child.parent = this
        if (isRunning) {
            child.onEnter()
        }
        return this
    }

    fun addChild(child: CCNode, z: Int): CCNode {
        return addChild(child, z, child.tag)
    }

    open fun addChild(child: CCNode?): CCNode? {
        assert(child != null) { "Argument must be non-nil" }
        return addChild(child, child!!.zOrder, child.tag)
    }

    fun removeFromParentAndCleanup(cleanup: Boolean) {
        if (parent != null) {
            parent!!.removeChild(this, cleanup)
        }
    }

    fun removeSelf() {
        removeFromParentAndCleanup(true)
    }

    open fun removeChild(child: CCNode?, cleanup: Boolean) {
        // explicit nil handling
        if (child == null) return
        if (children.contains(child)) detachChild(child, cleanup)
    }

    fun removeChildByTag(tag: String, cleanup: Boolean) {
        val child = getChildByTag(tag)
        child?.let { removeChild(it, cleanup) }
            ?: Log.w(LOG_TAG, "removeChild: child not found")
    }

    open fun removeAllChildren(cleanup: Boolean) {
        // not using detachChild improves speed here
        if (children == null) return
        for (child in children) {
            if (isRunning) child!!.onExit()
            if (cleanup) child!!.cleanup()
            child!!.parent = null
        }
        children.clear()
    }

    fun getChildByTag(tag: String): CCNode? {
        if (children != null) for (node in children) {
            if (tag == node!!.tag) {
                return node
            }
        }
        return null
    }

    private fun detachChild(child: CCNode, doCleanup: Boolean) {
        if (isRunning) child.onExit()

        // If you don't do cleanup, the child's actions will not get removed and the
        // its scheduledSelectors_ dict will not get released!
        if (doCleanup) child.cleanup()

        // set parent nil at the end (issue #476)
        child.parent = null
        children.remove(child)
    }

    /**
     * Reorders a child according to a new z value.
     * The child MUST be already added.
     */
    open fun reorderChild(child: CCNode?, zOrder: Int) {
        assert(child != null) { "Child must be non-null" }
        children.remove(child)
        insertChild(child, zOrder)
    }

    open fun draw(canvas: CanvasWrapper?) {

//        canvas.drawRect(bounds.origin.x+1,bounds.origin.y+1,bounds.size.width-2,bounds.size.height-2, debugPaint);
    }

    open fun update(canvas: CanvasWrapper) {
        if (!visible) {
            return
        }
        canvas.save()
        transform(canvas)
        draw(canvas)
        if (children != null) {
            for (child in children) {
                child!!.update(canvas)
            }
        }
        postDraw(canvas)
        canvas.restore()
    }

    protected fun postDraw(wrapper: CanvasWrapper?) {}
    fun transform(wrapper: CanvasWrapper) {
        bounds.set(0f, 0f, contentSize.width, contentSize.height)
        var pivotX = contentSize.width * anchorPointRef.x
        //        float pivotY = contentSize.height - (contentSize.height * anchorPoint.y);
        var pivotY = contentSize.height * anchorPointRef.y
        bounds.setAnchor(anchorPointRef)
        pivotX = bounds.midX()
        pivotY = bounds.midY()
        val anchorInPixels = bounds.anchorInPixels
        pivotX = 0f
        pivotY = 0f
        wrapper.translate(positionRef.x, positionRef.y)
        wrapper.scale(scaleX, scaleY, pivotX, pivotY)
        wrapper.rotate(rotation, -pivotX, -pivotY)
        matrix.reset()
        matrix.preTranslate(positionRef.x, positionRef.y)
        matrix.preScale(scaleX, scaleY, pivotX, pivotY)
        matrix.preRotate(rotation, 0f, -pivotY)
    }

    fun transformAncestors(canvas: CanvasWrapper) {
        if (parent != null) {
            parent!!.transformAncestors(canvas)
            parent!!.transform(canvas)
        }
    }

    fun runAction(action: CCAction?): CCAction? {
        assert(action != null) { "Argument must be non-null" }
        CCActionManager.sharedManager().addAction(action, this, !isRunning)
        return action
    }

    fun stopAllActions() {
        CCActionManager.sharedManager().removeAllActions(this)
    }

    fun stopAction(action: CCAction?) {
        CCActionManager.sharedManager().removeAction(action)
    }

    fun stopAction(tag: Int) {
        assert(tag != CCAction.ACTION_TAG_INVALID) { "Invalid tag_" }
        CCActionManager.sharedManager().removeAction(tag, this)
    }

    fun getAction(tag: Int): CCAction {
        assert(tag != CCAction.ACTION_TAG_INVALID) { "Invalid tag_" }
        return CCActionManager.sharedManager().getAction(tag, this)
    }

    fun numberOfRunningActions(): Int {
        return CCActionManager.sharedManager().numberOfRunningActions(this)
    }

    /**
     * schedules the "update" selector with a custom priority. This selector will be called every frame.
     * Scheduled selectors with a lower priority will be called before the ones that have a higher value.
     * Only one "update" selector could be scheduled per node (You can't have 2 'update' selectors).
     *
     * @since v0.99.3
     */
    @JvmOverloads
    fun scheduleUpdate(priority: Int = 0) {
        CCScheduler.sharedScheduler().scheduleUpdate(this, priority, !isRunning)
    }

    /**
     * unschedules the "update" method.
     *
     * @since v0.99.3
     */
    fun unscheduleUpdate() {
        CCScheduler.sharedScheduler().unscheduleUpdate(this)
    }
    /**
     * schedules a custom selector with an interval time in seconds.
     * If time is 0 it will be ticked every frame.
     * If time is 0, it is recommended to use 'scheduleUpdate' instead.
     */
    /**
     * schedules a selector.
     * The scheduled selector will be ticked every frame
     */
    @JvmOverloads
    fun schedule(selector: String?, interval: Float = 0f) {
        assert(selector != null) { "Argument selector must be non-null" }
        assert(interval >= 0) { "Argument interval must be positive" }
        CCScheduler.sharedScheduler().schedule(selector, this, interval, !isRunning)
    }

    @JvmOverloads
    fun schedule(callback: UpdateCallback?, interval: Float = 0f) {
        CCScheduler.sharedScheduler().schedule(callback, this, interval, !isRunning)
    }

    /* unschedules a custom selector.*/
    fun unschedule(selector: String?) {
        // explicit null handling
        if (selector == null) return
        CCScheduler.sharedScheduler().unschedule(selector, this)
    }

    fun unschedule(callback: UpdateCallback?) {
        // explicit null handling
        if (callback == null) return
        CCScheduler.sharedScheduler().unschedule(callback, this)
    }

    /**
     * unschedule all scheduled selectors: custom selectors, and the 'update' selector.
     * Actions are not affected by this method.
     *
     * @since v0.99.3
     */
    fun unscheduleAllSelectors() {
        CCScheduler.sharedScheduler().unscheduleAllSelectors(this)
    }

    fun resumeSchedulerAndActions() {
        CCScheduler.sharedScheduler().resume(this)
        CCActionManager.sharedManager().resume(this)
    }

    fun pauseSchedulerAndActions() {
        CCScheduler.sharedScheduler().pause(this)
        CCActionManager.sharedManager().pause(this)
    }

    fun nodeToParentTransform(): CGAffineTransform {
        if (isTransformDirty_) {
            val zero = Vec2(0f, 0f)
            transform_.setToIdentity()
            if (!isRelativeAnchorPoint_ && anchorPointInPixels_ != zero) {
                transform_.translate(anchorPointInPixels_.x.toDouble(), anchorPointInPixels_.y.toDouble())
            }
            if (positionRef != zero) transform_.translate(positionRef.x.toDouble(), positionRef.y.toDouble())
            if (rotation != 0f) transform_.rotate(-Converter.CC_DEGREES_TO_RADIANS(rotation).toDouble())
            if (!(scaleX == 1f && scaleY == 1f)) transform_.scale(scaleX.toDouble(), scaleY.toDouble())
            if (anchorPointInPixels_ != zero) transform_.translate(
                -anchorPointInPixels_.x.toDouble(),
                -anchorPointInPixels_.y.toDouble()
            )
            isTransformDirty_ = false
        }
        return transform_
    }

    fun parentToNodeTransform(): CGAffineTransform {
        if (isInverseDirty_) {
//            CGAffineTransformUtil.inverse(nodeToParentTransform(), inverse_);
            isInverseDirty_ = false
        }
        return inverse_
    }

    private fun nodeToWorldTransform(): CGAffineTransform {
        var t = CGAffineTransform(nodeToParentTransform())
        var p = parent
        while (p != null) {

            // t = t.getTransformConcat(p.nodeToParentTransform());
            t = t.preConcatenate(p.nodeToParentTransform())
            p = p.parent
        }
        return t
    }

    private fun nodeToWorldTransform(ret: CGAffineTransform) {
        ret.setTransform(nodeToParentTransform())
        var p = parent
        while (p != null) {
            p = p.parent
        }
    }

    private fun worldToNodeTransform(ret: CGAffineTransform) {
        nodeToWorldTransform(ret)
        //        CGAffineTransformUtil.inverse(ret);
    }

    fun convertToNodeSpace(x: Float, y: Float): Vec2 {
        val result = Vec2(x, y)
        converter.reverse(result, matrix)
        return result
    }

    fun convertToWorldSpace(x: Float, y: Float): Vec2 {
        val nodePoint = Vec2(x, y)
        return nodeToWorldTransform().applyTransform(nodePoint)
    }

    /**
     * This is analog method, result is written to ret. No garbage.
     */
    fun convertToWorldSpace(x: Float, y: Float, ret: Vec2?) {
//        OneClassPool<CGAffineTransform> pool = PoolHolder.getInstance().getCGAffineTransformPool();

//        CGAffineTransform temp = pool.get();
//        nodeToWorldTransform(temp);

//        Vec2.applyAffineTransform(x, y, temp, ret);

//        pool.free(temp);
    }

    fun convertToNodeSpaceAR(x: Float, y: Float): Vec2 {
        val nodePoint = convertToNodeSpace(x, y)
        return nodePoint.sub(anchorPointInPixels_)
    }

    fun convertToWorldSpaceAR(x: Float, y: Float): Vec2 {
        var nodePoint = Vec2(x, y)
        nodePoint = nodePoint.add(anchorPointInPixels_)
        return convertToWorldSpace(nodePoint.x, nodePoint.y)
    }

    fun convertTouchToNodeSpace(event: MotionEvent): Vec2 {
//    	OneClassPool<Vec2> pool = PoolHolder.getInstance().getCGPointPool();
//    	Vec2 point = pool.get();

//    	int action = event.getAction();
//		int pid = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
//        Director.getInstance().convertToGL(Util5.getX(event, pid), Util5.getY(event, pid), point);
//
//
//    	float x = point.x, y = point.y;
//    	pool.free(point);
        val x = event.x
        val y = event.y
        return convertToNodeSpace(x, y)
    }

    interface CocosNodeSize {
        val width: Float
        val height: Float
    }

    // helper that reorder a child
    private fun insertChild(node: CCNode?, z: Int) {
        var index = 0
        var added = false
        for (i in children.indices) {
            val child = children[i]
            if (child!!.zOrder > z) {
                added = true
                children.add(index, node)
                break
            }
            ++index
        }
        if (!added) children.add(node)
        node!!._setZOrder(z)
    }

    /**
     * Stops all running actions and schedulers
     */
    open fun cleanup() {

        // actions
        stopAllActions()
        unscheduleAllSelectors()

        // timers
        if (children != null) for (i in children.indices) {
            val node = children[i]
            node!!.cleanup()
        }
    }

    override fun toString(): String {
        return "<instance of " + this.javaClass + "| Tag = " + tag + ">"
    }

    /**
     * callback that is called every time the CCNode enters the 'stage'.
     * If the CCNode enters the 'stage' with a transition, this callback is called when the transition starts.
     * During onEnter you can't a "sister/brother" node.
     */
    open fun onEnter() {
        if (children != null) for (child in children) {
            child!!.onEnter()
        }
        resumeSchedulerAndActions()
        // activateTimers();
        isRunning = true
    }

    /**
     * callback that is called when the CCNode enters in the 'stage'.
     * If the CCNode enters the 'stage' with a transition, this callback is called
     * when the transition finishes.
     *
     * @since v0.8
     */
    open fun onEnterTransitionDidFinish() {
        if (children != null) for (child in children) {
            child!!.onEnterTransitionDidFinish()
        }
    }

    /**
     * callback that is called every time the CCNode leaves the 'stage'.
     * If the CCNode leaves the 'stage' with a transition, this callback is called
     * when the transition finishes.
     * During onExit you can't a "sister/brother" node.
     */
    open fun onExit() {

        // deactivateTimers();
        pauseSchedulerAndActions()
        isRunning = false
        if (children != null) for (child in children) {
            child!!.onExit()
        }
    }

    override fun onTouchStart(x: Float, y: Float) {
        val vector2 = convertToNodeSpace(x, y)
        if (children != null) {
            for (child in children) {
                child!!.onTouchStart(vector2.x, vector2.y)
            }
        }
    }

    override fun onTouchMove(x: Float, y: Float) {
        val vec2 = convertToNodeSpace(x, y)
        if (children != null) {
            for (child in children) {
                child!!.onTouchMove(vec2.x, vec2.y)
            }
        }
    }

    override fun onTouchEnd(x: Float, y: Float) {
        val vector2 = convertToNodeSpace(x, y)
        if (children != null) {
            for (child in children) {
                child!!.onTouchEnd(vector2.x, vector2.y)
            }
        }
    }

    companion object {
        private val LOG_TAG = CCNode::class.java.simpleName
        const val NODE_TAG_INVALID = ""
    }

    /**
     * initializes the node
     */
    init {
        positionRef = Vec2(0f, 0f)
        transform_ = CGAffineTransform.identity()
        inverse_ = CGAffineTransform.identity()

        // "whole screen" objects. like Scenes and Layers, should set relativeAnchorPoint to false        
        isRelativeAnchorPoint_ = true
        anchorPointInPixels_ = Vec2(0f, 0f)
        anchorPointRef = Vec2(0f, 0f)
        contentSize = Size.zero()
        isInverseDirty_ = true
        isTransformDirty_ = isInverseDirty_
        zOrder = 0

        tag = NODE_TAG_INVALID

        // lazy alloc
        camera_ = null


        // userData is always inited as nil
        userData = null
        debugPaint.color = Color.RED
        debugPaint.style = Paint.Style.STROKE
        debugPaint.strokeWidth = 2f
    }
}