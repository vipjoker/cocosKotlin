package com.vipjokerstudio.cocoskotlin.core.actions.instant;

import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;

import org.jbox2d.common.Vec2;


/**
 * Places the node in a certain position
 */
public class CCPlace extends CCInstantAction {
    private Vec2 position;

    public static CCPlace action(Vec2 pnt) {
        return new CCPlace(pnt);
    }

    /**
     * creates a Place action with a position
     */
    protected CCPlace(Vec2 pnt) {
    	super();
        position = new Vec2(pnt.x, pnt.y);
    }

    @Override
    public CCPlace copy() {
        return new CCPlace(position);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        target.setPosition(position);
    }
}
