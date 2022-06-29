package com.vipjokerstudio.cocoskotlin.core.actions.interval;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.Color3B;

/** Tints a CCNode that implements the CCNodeRGB protocol from current tint to a custom one.
 @since v0.7.2
 */
public class CCTintBy extends CCIntervalAction {
    protected Color3B delta;
    protected Color3B from;

    /** creates an action with duration and color */
    public static CCTintBy action(float t, Color3B c) {
        return new CCTintBy(t, c);
    }

    /** initializes the action with duration and color */
    protected CCTintBy(float t, Color3B c) {
        super(t);
        delta = new Color3B(c);
    }

    @Override
    public CCTintBy copy() {
        return new CCTintBy(duration, delta);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);

        Color3B c = (target).getColor();
        from = new Color3B(c);
    }

    @Override
    public void update(float t) {
        target.setColor(new Color3B((int) (from.r + delta.r * t),
                    (int) (from.g + delta.g * t),
                    (int) (from.b + delta.b * t)));
    }

    @Override
    public CCTintBy reverse() {
        return new CCTintBy(duration, new Color3B(-delta.r, -delta.g, -delta.b));
    }
}

