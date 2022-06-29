package com.vipjokerstudio.cocoskotlin.core.actions.interval;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.Color3B;

/** Tints a CCNode that implements the CCNodeRGB protocol from current tint to a custom one.
 @warning This action doesn't support "reverse"
 @since v0.7.2
*/
public class CCTintTo extends CCIntervalAction {
	protected Color3B to;
    protected Color3B from;

    /** creates an action with duration and color */
    public static CCTintTo action(float t, Color3B c) {
        return new CCTintTo(t, c);
    }

    /** initializes the action with duration and color */
    protected CCTintTo(float t, Color3B c) {
        super(t);
        to = new Color3B(c);
    }

    @Override
    public CCTintTo copy() {
        return new CCTintTo(duration, to);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);


        from = target.getColor();
    }

    @Override
    public void update(float t) {
        ( target).setColor(
                new Color3B((int) (from.r + (to.r - from.r) * t),
                        (int) (from.g + (to.g - from.g) * t),
                        (int) (from.b + (to.b - from.b) * t)));
    }
}

