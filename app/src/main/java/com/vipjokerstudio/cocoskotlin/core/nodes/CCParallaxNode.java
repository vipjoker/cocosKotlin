package com.vipjokerstudio.cocoskotlin.core.nodes;


import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;

/** CCParallaxNode: A node that simulates a parallax scroller
 * The children will be moved faster / slower than the parent according the the parallax ratio.
 */
public class CCParallaxNode extends CCNode {

    /** array that holds the offset / ratio of the children */
    private ArrayList<CCPointObject> parallaxArray_;
    private Vec2 lastPosition;

    static class CCPointObject {
        private float ratioX_;
        private float ratioY_;
        private float offsetX_;
        private float offsetY_;
        private CCNode child_;

        public CCPointObject(float ratioX, float ratioY, float offsetX, float offsetY) {
            ratioX_ = ratioX;
            ratioY_ = ratioY;
            offsetX_ = offsetX;
            offsetY_ = offsetY;
        }

        public CCNode getChild() {
            return child_;
        }

        public void setChild(CCNode child) {
            child_ = child;
        }

        public float getRatioX() {
            return ratioX_;
        }

        public float getRatioY() {
            return ratioY_;
        }

        public float getOffsetX() {
            return offsetX_;
        }

        public float getOffsetY() {
            return offsetY_;
        }
    }
    
    public static CCParallaxNode node() {
        return new CCParallaxNode();
    }

    protected CCParallaxNode() {
        parallaxArray_ = new ArrayList<CCPointObject>(5);
        lastPosition = new Vec2(-100,-100);
    }

    @Override
    public CCNode addChild(CCNode child, int z, String tag) {
        assert false : "ParallaxNode: use addChild:z:parallaxRatio:positionOffset instead";
        return null;
    }

    /** Adds a child to the container with a z-order, a parallax ratio and a position offset
    It returns self, so you can chain several addChilds.
    @since v0.8
    */
    public CCNode addChild(CCNode child, int z,
    		float ratioX, float ratioY, float offsetX, float offsetY) {
        assert child != null : "Argument must be non-null";
        CCPointObject obj = new CCPointObject(ratioX, ratioY, offsetX, offsetY);
        obj.setChild(child);
        parallaxArray_.add(obj);
	
        Vec2 pnt = getPosition();
        float x = pnt.x * ratioX + offsetX;
        float y = pnt.y * ratioY + offsetY;
        child.setPosition(new Vec2(x, y));
	
        return super.addChild(child, z, child.getTag());
    }

    @Override
    public void removeChild(CCNode node, boolean cleanup) {
        for( int i=0;i < parallaxArray_.size();i++) {
            CCPointObject point = parallaxArray_.get(i);
            if( point.getChild().equals(node) ) {
                parallaxArray_.remove(i);
                break;
            }
        }
        super.removeChild(node, cleanup);
    }

    @Override
    public void removeAllChildren(boolean cleanup) {
        parallaxArray_.clear();
        super.removeAllChildren(cleanup);
    }

    private Vec2 absolutePosition() {
        Vec2 ret = getPosition();
        CCNode cn = this;
	
        while (cn.getParent() != null) {
            cn = cn.getParent();
            Vec2 pnt = cn.getPosition();
            ret.x += pnt.x;
            ret.y += pnt.y;
        }
	
        return ret;
    }

    /*
     The positions are updated at visit because:
       - using a timer is not guaranteed that it will called after all the positions were updated
       - overriding "draw" will only precise if the children have a z > 0
    */
    @Override
    public void update(CanvasWrapper canvas) {
        Vec2 pos = absolutePosition();
        if( ! pos.equals(lastPosition) ) {
            for(int i=0; i < parallaxArray_.size(); i++ ) {
                CCPointObject point = parallaxArray_.get(i);
                float x = -pos.x + pos.x * point.getRatioX() + point.getOffsetX();
                float y = -pos.y + pos.y * point.getRatioY() + point.getOffsetY();
                point.getChild().setPosition(new Vec2(x, y));
            }

            lastPosition = pos;
        }

        super.update(canvas);
    }
    
}
