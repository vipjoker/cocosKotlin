package com.vipjokerstudio.cocoskotlin.core.actions.interval;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;

import org.jbox2d.common.Vec2;

/**
 * Moves a CCNode object simulating a parabolic jump movement by modifying it's position attribute.
*/
public class CCJumpBy extends CCIntervalAction {
    protected Vec2 startPosition;
    protected Vec2 delta;
    protected float height;
    protected int jumps;

    /** creates the action */
    public static CCJumpBy action(float time, Vec2 pos, float height, int jumps) {
        return new CCJumpBy(time, pos, height, jumps);
    }

    /** initializes the action */
    protected CCJumpBy(float time, Vec2 pos, float h, int j) {
        super(time);
        startPosition = new  Vec2(0,0);
        delta = new Vec2(pos.x, pos.y);
        height = h;
        jumps = j;
    }

    @Override
    public CCJumpBy copy() {
        return new CCJumpBy(duration, delta, height, jumps);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        Vec2 pnt = target.getPosition();
        startPosition = new Vec2(pnt.x, pnt.y);
    }

    @Override
    public void update(float t) {
        // parabolic jump (since v0.8.2)
        float frac = (t * jumps) % 1.0f;
        float y = height * 4 * frac * (1 - frac);
        y += delta.y * t;
        float x = delta.x * t;
        target.setPosition(new Vec2(startPosition.x + x, startPosition.y + y));
    }

    @Override
    public CCJumpBy reverse() {
        return new CCJumpBy(duration, Vec2Util.ccpNeg(delta), height, jumps);
    }
}

