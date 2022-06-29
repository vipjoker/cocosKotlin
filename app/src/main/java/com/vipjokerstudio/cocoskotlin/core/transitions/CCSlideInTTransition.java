package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCMoveBy;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;


/**
 * SlideInT Transition.
 * Slide in the incoming scene from the top border.
 */
public class CCSlideInTTransition extends CCSlideInLTransition {

    public static CCSlideInTTransition transition(float t, CCScene s) {
        return new CCSlideInTTransition(t, s);
    }

    public CCSlideInTTransition(float t, CCScene s) {
        super(t, s);
    }

    public void sceneOrder() {
        inSceneOnTop = false;
    }

    /**
     * initializes the scenes
     */
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(0, s.height-ADJUST_FACTOR);
    }

    @Override
    public CCIntervalAction action() {
        Size s = Director.getInstance().winSize();
        return CCMoveBy.action(duration, new Vec2(0,-(s.height-ADJUST_FACTOR)));
    }

}
