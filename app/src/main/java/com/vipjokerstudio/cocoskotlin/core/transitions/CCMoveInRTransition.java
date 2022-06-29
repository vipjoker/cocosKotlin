package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;


/**
 * MoveInR Transition.
 * Move in from to the right the incoming scene.
 */
public class CCMoveInRTransition extends CCMoveInLTransition {

    public static CCMoveInRTransition transition(float t, CCScene s) {
        return new CCMoveInRTransition(t, s);
    }

    public CCMoveInRTransition(float t, CCScene s) {
        super(t, s);
    }

    /**
     * initializes the scenes
     */
    @Override
    protected void initScenes() {
        inScene.setPosition(Director.getInstance().winSize().width, 0);
    }
}
