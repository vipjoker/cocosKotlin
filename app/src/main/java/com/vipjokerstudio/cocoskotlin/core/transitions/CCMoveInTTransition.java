package com.vipjokerstudio.cocoskotlin.core.transitions;


import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;

/**
 * MoveInT Transition.
 * Move in from to the top the incoming scene.
 */
public class CCMoveInTTransition extends CCMoveInLTransition {

    public static CCMoveInTTransition transition(float t, CCScene s) {
        return new CCMoveInTTransition(t, s);
    }

    public CCMoveInTTransition(float t, CCScene s) {
        super(t, s);
    }

    /**
     * initializes the scenes
     */
    @Override
    protected void initScenes() {
        inScene.setPosition(0, Director.getInstance().winSize().height);
    }
}
