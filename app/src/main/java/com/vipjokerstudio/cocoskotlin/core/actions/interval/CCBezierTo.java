package com.vipjokerstudio.cocoskotlin.core.actions.interval;

import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.CCBezierConfig;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;

import org.jbox2d.common.Vec2;


/** An action that moves the target with a cubic Bezier curve to a destination point.
 @since v0.8.2
 */
public class CCBezierTo extends CCBezierBy {

	/** We need to record the original config.  Repeating a bezier movement is continually
	 *  more and more distorted if we continue to subtract the start position.
	 */
	final CCBezierConfig originalconfig;
	
    /** creates the action with a duration and a bezier configuration */
    public static CCBezierTo action(float t, CCBezierConfig c) {
        return new CCBezierTo(t, c);
    }

    /** initializes the action with a duration and a bezier configuration */
    protected CCBezierTo(float t, CCBezierConfig c) {
        super(t, c);
        originalconfig = new CCBezierConfig();
        originalconfig.controlPoint_1 = new Vec2(c.controlPoint_1.x, c.controlPoint_1.y);
        originalconfig.controlPoint_2 = new Vec2(c.controlPoint_2.x, c.controlPoint_2.y);
        originalconfig.endPosition = new Vec2(c.endPosition.x, c.endPosition.y);
    }

    @Override
    public CCBezierTo copy() {
        return new CCBezierTo(duration, config);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);

        config.controlPoint_1 = Vec2Util.ccpSub(originalconfig.controlPoint_1, startPosition);
        config.controlPoint_2 = Vec2Util.ccpSub(originalconfig.controlPoint_2, startPosition);
        config.endPosition = Vec2Util.ccpSub(originalconfig.endPosition, startPosition);
    }

    @Override
    public CCBezierTo reverse() {
        // TODO: reverse it's not working as expected
        CCBezierConfig r = new CCBezierConfig();
        r.endPosition = Vec2Util.ccpNeg(config.endPosition);
        r.controlPoint_1 = Vec2Util.ccpAdd(config.controlPoint_2, Vec2Util.ccpNeg(config.endPosition));
        r.controlPoint_2 = Vec2Util.ccpAdd(config.controlPoint_1, Vec2Util.ccpNeg(config.endPosition));

        return new CCBezierTo(duration, r);
    }
}

