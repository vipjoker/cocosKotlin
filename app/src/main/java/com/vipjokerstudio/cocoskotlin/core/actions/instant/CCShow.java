package com.vipjokerstudio.cocoskotlin.core.actions.instant;


import com.vipjokerstudio.cocoskotlin.core.actions.base.CCFiniteTimeAction;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;

/**
 * Show the node
 */
public class CCShow extends CCInstantAction {

    public static CCShow action() {
        return new CCShow();
    }

	@Override
	public CCShow copy() {
		return new CCShow();
	}

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        target.setVisible(true);
    }

    @Override
    public CCFiniteTimeAction reverse() {
        return new CCHide();
    }
}
