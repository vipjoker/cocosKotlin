package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCFadeIn;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCFadeOut;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.layers.CCColorLayer;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.types.Color3B;
import com.vipjokerstudio.cocoskotlin.core.types.Color4B;


/**
 * Fade Transition.
 * Fade out the outgoing scene and then fade in the incoming scene.
 */
public class CCFadeTransition extends CCTransitionScene {
    Color4B color;

    /**
     * creates the transition with a duration and with an RGB color
     */
    public static CCFadeTransition transition(float t, CCScene s, Color3B rgb) {
        return new CCFadeTransition(t, s, rgb);
    }
    
    /**
     * creates the transition with a duration
     */
    public static CCFadeTransition transition(float t, CCScene s) {
        return new CCFadeTransition(t, s);
    }

    /**
     * initializes the transition with a duration and with an RGB color
     */
    public CCFadeTransition(float d, CCScene s, Color3B rgb) {
        super(d, s);
        color = new Color4B(rgb.r, rgb.g, rgb.b, 0);
    }

    /**
     * initializes the transition with a duration
     */
    public CCFadeTransition(float d, CCScene s) {
        this(d, s, new Color3B(0, 0, 0));
    }

    @Override
    public void onEnter() {
        super.onEnter();

        CCColorLayer l = CCColorLayer.node(color);
        inScene.setVisible(false);

        addChild(l, 2, kSceneFade);


        CCNode f = getChildByTag(kSceneFade);

        CCIntervalAction a = CCSequence.actions(
                CCFadeIn.action(duration / 2),
                CCCallFunc.action(this::hideOutShowIn),
                CCFadeOut.action(duration / 2),
                CCCallFunc.action(this::finish));
        f.runAction(a);
    }

    @Override
    public void onExit() {
        super.onExit();
        removeChildByTag(kSceneFade, false);
    }

}
