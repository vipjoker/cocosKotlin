package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;


/**
 * MoveInB Transition.
 * Move in from to the bottom the incoming scene.
 */
public class CCMoveInBTransition extends CCMoveInLTransition {

    public static CCMoveInBTransition transition(float t, CCScene s) {
        return new CCMoveInBTransition(t, s);
    }

    public CCMoveInBTransition(float t, CCScene s) {
        super(t, s);
    }

    /**
     * initializes the scenes
     */
    protected void initScenes() {
        Size s = Director.getInstance().winSize();
        inScene.setPosition(0, -s.height);
    }
}
