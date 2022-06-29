package com.vipjokerstudio.cocoskotlin.core.actions.interval;

import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;

import org.jbox2d.common.Vec2;


//
// MoveTo
//

/** Moves a CCNode object to the position x,y. x and y are absolute coordinates by modifying it's position attribute.
*/
public class CCMoveTo extends CCIntervalAction {
    private Vec2 endPosition;
    private Vec2 startPosition;
    protected Vec2 delta;

    /** creates the action */
    public static CCMoveTo action(float t, Vec2 pos) {
        return new CCMoveTo(t, pos);
    }

    /** initializes the action */
    protected CCMoveTo(float t, Vec2 pos) {
        super(t);
        startPosition = new Vec2();
        endPosition = new Vec2(pos.x, pos.y);
        delta = new Vec2();
    }
    
    /**
     * Lets extend basic functionality for reuse action.
     */
    public void setEndPosition(Vec2 pos) {
    	endPosition.set(pos);
    }

    @Override
    public CCIntervalAction copy() {
        return new CCMoveTo(duration, endPosition);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);

        startPosition.set(target.getPositionRef());
        delta.set(endPosition.x - startPosition.x, endPosition.y - startPosition.y);
    }

    @Override
    public void update(float t) {
        target.setPosition(startPosition.x + delta.x * t,
        					startPosition.y + delta.y * t);
    }
}
