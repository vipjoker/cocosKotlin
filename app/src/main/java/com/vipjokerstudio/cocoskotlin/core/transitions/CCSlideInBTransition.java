package com.vipjokerstudio.cocoskotlin.core.transitions;


import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCMoveBy;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;

/**
 * SlideInB Transition.
 * Slide in the incoming scene from the bottom border.
 */
public class CCSlideInBTransition extends CCSlideInLTransition {

    public static CCSlideInBTransition transition(float t, CCScene s) {
        return new CCSlideInBTransition(t, s);
    }

    public CCSlideInBTransition(float t, CCScene s) {
        super(t, s);
    }

    public void sceneOrder() {
        inSceneOnTop = true;
    }

    /**
     * initializes the scenes
     */
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(0,-(s.height-ADJUST_FACTOR));
    }

    protected CCIntervalAction action() {
        Size s = Director.getInstance().winSize();
        return CCMoveBy.action(duration, new Vec2(0,s.height-ADJUST_FACTOR));
    }

}
