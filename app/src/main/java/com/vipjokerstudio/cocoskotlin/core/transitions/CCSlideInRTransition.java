package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCMoveBy;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;


/**
 * SlideInR Transition.
 * Slide in the incoming scene from the right border.
 */
public class CCSlideInRTransition extends CCSlideInLTransition {

    public static CCSlideInRTransition transition(float t, CCScene s) {
        return new CCSlideInRTransition(t, s);
    }

    public CCSlideInRTransition(float t, CCScene s) {
        super(t, s);
    }

    @Override
    public void sceneOrder() {
        inSceneOnTop = true;
    }

    /**
     * initializes the scenes
     */
    @Override
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(s.width-ADJUST_FACTOR, 0);
    }

    @Override
    public CCIntervalAction action() {
        Size s = Director.getInstance().winSize();
        return CCMoveBy.action(duration, new Vec2(-(s.width-ADJUST_FACTOR),0));
    }
}
