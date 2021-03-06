package com.vipjokerstudio.cocoskotlin.core.actions.ease;


import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;

public class CCEaseBounceOut extends CCEaseBounce {

    public static CCEaseBounceOut action(CCIntervalAction action) {
        return new CCEaseBounceOut(action);
    }

    protected CCEaseBounceOut(CCIntervalAction action) {
        super(action);
    }

    @Override
    public CCEaseAction copy() {
        return new CCEaseBounceOut(other.copy());
    }

    @Override
    public void update(float t) {
        float newT = bounceTime(t);
        other.update(newT);
    }

    @Override
    public CCIntervalAction reverse() {
        return new CCEaseBounceIn(other.reverse());
    }

}
