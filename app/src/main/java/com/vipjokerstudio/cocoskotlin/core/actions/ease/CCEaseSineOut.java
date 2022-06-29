package com.vipjokerstudio.cocoskotlin.core.actions.ease;


import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;

public class CCEaseSineOut extends CCEaseAction {

    public static CCEaseSineOut action(CCIntervalAction action) {
        return new CCEaseSineOut(action);
    }

    protected CCEaseSineOut(CCIntervalAction action) {
        super(action);
    }

	@Override
	public CCEaseSineOut copy() {
		return new CCEaseSineOut(other.copy());
	}

    @Override
    public void update(float t) {
        other.update((float)Math.sin(t * (float) Math.PI / 2));
    }

    @Override
    public CCIntervalAction reverse() {
        return new CCEaseSineIn(other.reverse());
    }

}
