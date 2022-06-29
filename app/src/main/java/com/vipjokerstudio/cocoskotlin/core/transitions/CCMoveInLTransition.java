package com.vipjokerstudio.cocoskotlin.core.transitions;


import com.vipjokerstudio.cocoskotlin.core.actions.ease.CCEaseOut;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCMoveTo;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;

/**
 * MoveInL Transition.
 * Move in from to the left the incoming scene.
 */
public class CCMoveInLTransition extends CCTransitionScene implements CCTransitionEaseScene {

    public static CCMoveInLTransition transition(float t, CCScene s) {
        return new CCMoveInLTransition(t, s);
    }

    protected CCMoveInLTransition(float t, CCScene s) {
        super(t, s);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        initScenes();

        CCIntervalAction a = action();

        inScene.runAction(CCSequence.actions(
                easeAction(a),
                CCCallFunc.action(this::finish)));
    }

    /**
     * returns the action that will be performed
     */
    protected CCIntervalAction action() {
        return CCMoveTo.action(duration, new Vec2());
    }

    @Override
    public CCIntervalAction easeAction(CCIntervalAction action) {
        return CCEaseOut.action(action, 2.0f);
    }

    /**
     * initializes the scenes
     */
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(-s.width, 0);
    }
}
