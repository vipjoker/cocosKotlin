package com.vipjokerstudio.cocoskotlin.core.actions.ease;


import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;

/**
 * Base class for Easing actions with rate parameters
 */
public class CCEaseRateAction extends CCEaseAction {
	/** rate value for the actions */
	float rate;

	/** Creates the action with the inner action and the rate parameter */
	public static CCEaseRateAction action(CCIntervalAction action, float rate) {
		return new CCEaseRateAction(action, rate);
	}
	
	/** Initializes the action with the inner action and the rate parameter */
    protected CCEaseRateAction(CCIntervalAction action, float aRate) {
        super(action);
        rate = aRate;
    }

    @Override
    public CCEaseRateAction copy() {
        return new CCEaseRateAction(other.copy(), rate);
    }

    @Override
    public CCIntervalAction reverse() {
        return new CCEaseRateAction(other.reverse(), 1 / rate);
    }

}
