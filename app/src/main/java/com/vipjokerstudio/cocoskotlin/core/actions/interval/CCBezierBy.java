package com.vipjokerstudio.cocoskotlin.core.actions.interval;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.CCBezierConfig;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;

import org.jbox2d.common.Vec2;

/**
 * An action that moves the target with a cubic Bezier curve by a certain distance.
 */
public class CCBezierBy extends CCIntervalAction {

    protected CCBezierConfig config;
    protected Vec2 startPosition;

    /** creates the action with a duration and a bezier configuration */
    public static CCBezierBy action(float t, CCBezierConfig c) {
        return new CCBezierBy(t, c);
    }

    /** initializes the action with a duration and a bezier configuration */
    protected CCBezierBy(float t, CCBezierConfig c) {
        super(t);
        config = c;
        startPosition = new Vec2(0, 0);
    }

    @Override
    public CCIntervalAction copy() {
        return new CCBezierBy(duration, config);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        startPosition = target.getPosition();
    }

    @Override
    public void update(float t) {
        float xa = 0;
        float xb = config.controlPoint_1.x;
        float xc = config.controlPoint_2.x;
        float xd = config.endPosition.x;

        float ya = 0;
        float yb = config.controlPoint_1.y;
        float yc = config.controlPoint_2.y;
        float yd = config.endPosition.y;

        float x = CCBezierConfig.bezierAt(xa, xb, xc, xd, t);
        float y = CCBezierConfig.bezierAt(ya, yb, yc, yd, t);
        target.setPosition(new Vec2(startPosition.x + x, startPosition.y + y));
    }

    @Override
    public CCBezierBy reverse() {
        // TODO: reverse it's not working as expected
        CCBezierConfig r = new CCBezierConfig();
        r.endPosition = Vec2Util.ccpNeg(config.endPosition);
        r.controlPoint_1 = Vec2Util.ccpAdd(config.controlPoint_2, Vec2Util.ccpNeg(config.endPosition));
        r.controlPoint_2 = Vec2Util.ccpAdd(config.controlPoint_1, Vec2Util.ccpNeg(config.endPosition));

        return new CCBezierBy(duration, r);
    }

}

