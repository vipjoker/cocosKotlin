package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.ease.CCEaseOut;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCMoveBy;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;


/**
 * SlideInL Transition.
 * Slide in the incoming scene from the left border.
 */
public class CCSlideInLTransition extends CCTransitionScene implements CCTransitionEaseScene {
	// The adjust factor is needed to prevent issue #442
	// One solution is to use DONT_RENDER_IN_SUBPIXELS images, but NO
	// The other issue is that in some transitions (and I don't know why)
	// the order should be reversed (In in top of Out or vice-versa).
    protected static final float ADJUST_FACTOR = 0.5f;

    public static CCSlideInLTransition transition(float t, CCScene s) {
        return new CCSlideInLTransition(t, s);
    }

    public CCSlideInLTransition(float t, CCScene s) {
        super(t, s);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        initScenes();

        CCIntervalAction in = action();
        CCIntervalAction out = action();

        inScene.runAction(easeAction(in));
        outScene.runAction(CCSequence.actions(
                easeAction(out),
                CCCallFunc.action(this::finish)));
    }

    public void sceneOrder() {
        inSceneOnTop = false;
    }

    /**
     * initializes the scenes
     */
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(-(s.width-ADJUST_FACTOR), 0);
    }

	/**
     * returns the action that will be performed
     */
    protected CCIntervalAction action() {
        Size s = Director.getInstance().winSize();
        return CCMoveBy.action(duration, new Vec2(s.width-ADJUST_FACTOR,0));
    }


    @Override
    public CCIntervalAction easeAction(CCIntervalAction action) {
        return CCEaseOut.action(action, 2.0f);
    }

}
