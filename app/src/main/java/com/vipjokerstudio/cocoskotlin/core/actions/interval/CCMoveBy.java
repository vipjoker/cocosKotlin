package com.vipjokerstudio.cocoskotlin.core.actions.interval;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;

import org.jbox2d.common.Vec2;

/**  Moves a CCNode object x,y pixels by modifying it's position attribute.
 x and y are relative to the position of the object.
 Duration is is seconds.
*/ 
public class CCMoveBy extends CCMoveTo {
    /** creates the action */
    public static CCMoveBy action(float duration, Vec2 pos) {
        return new CCMoveBy(duration, pos);
    }

    /** initializes the action */
    protected CCMoveBy(float t, Vec2 pos) {
        super(t, pos);
        delta.set(pos.x, pos.y);
    }

    @Override
    public CCMoveBy copy() {
        return new CCMoveBy(duration, delta);
    }

    @Override
    public void start(CCNode aTarget) {
    	float tmpx = delta.x;
		float tmpy = delta.y;

        super.start(aTarget);
        delta.set(tmpx, tmpy);
    }

    @Override
    public CCMoveBy reverse() {
        return new CCMoveBy(duration, Vec2Util.ccpNeg(delta));
    }
}

