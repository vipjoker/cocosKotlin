package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCDelayTime;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCJumpBy;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCScaleTo;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;


/**
 * JumpZoom Transition.
 * Zoom out and jump the outgoing scene, and then jump and zoom in the incoming
 */
public class CCJumpZoomTransition extends CCTransitionScene {

    public static CCJumpZoomTransition transition(float t, CCScene s) {
        return new CCJumpZoomTransition(t, s);
    }

    public CCJumpZoomTransition(float t, CCScene s) {
        super(t, s);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        Size size = Director.getInstance().winSize();
        
        float width = size.width;
        // float height = size.getHeight();

        inScene.setScale(0.5f);
        inScene.setPosition(width, 0);

        inScene.setAnchorPoint(0.5f, 0.5f);
        outScene.setAnchorPoint(0.5f, 0.5f);

        CCIntervalAction jump = CCJumpBy.action(duration / 4, new Vec2(-width, 0), width / 4, 2);
        CCIntervalAction scaleIn = CCScaleTo.action(duration / 4, 1.0f);
        CCIntervalAction scaleOut = CCScaleTo.action(duration / 4, 0.5f);

        CCIntervalAction jumpZoomOut = CCSequence.actions(scaleOut, jump);
        CCIntervalAction jumpZoomIn = CCSequence.actions(jump.copy(), scaleIn);

        CCIntervalAction delay = CCDelayTime.action(duration / 2);

        outScene.runAction(jumpZoomOut);
        inScene.runAction(CCSequence.actions(delay, jumpZoomIn,
                CCCallFunc.action(this::finish)));
    }
}
