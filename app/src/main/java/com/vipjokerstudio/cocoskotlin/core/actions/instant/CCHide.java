package com.vipjokerstudio.cocoskotlin.core.actions.instant;

import com.vipjokerstudio.cocoskotlin.core.actions.base.CCFiniteTimeAction;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;


/**
 * Hide the node
 */
public class CCHide extends CCInstantAction {

    public static CCHide action() {
        return new CCHide();
    }

	@Override
	public CCHide copy() {
		return new CCHide();
	}

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        target.setVisible(false);
    }

    @Override
    public CCFiniteTimeAction reverse() {
        return new CCShow();
    }
}
