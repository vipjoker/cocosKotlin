package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.camera.CCOrbitCamera;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCHide;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCShow;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCDelayTime;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;


/**
 * FlipX Transition.
 * Flips the screen horizontally.
 * The front face is the outgoing scene and the back face is the incoming scene.
 */
public class CCFlipXTransition extends CCOrientedTransitionScene {

    public static CCFlipXTransition transition(float t, CCScene s, int orientation) {
        return new CCFlipXTransition(t, s, orientation);
    }

    public CCFlipXTransition(float t, CCScene s, int orientation) {
        super(t, s, orientation);
    }

    public void onEnter() {
        super.onEnter();

        CCIntervalAction inA, outA;
        inScene.setVisible(false);

        float inDeltaZ, inAngleZ;
        float outDeltaZ, outAngleZ;

        if (orientation == tOrientation.kOrientationRightOver) {
            inDeltaZ = 90;
            inAngleZ = 270;
            outDeltaZ = 90;
            outAngleZ = 0;
        } else {
            inDeltaZ = -90;
            inAngleZ = 90;
            outDeltaZ = -90;
            outAngleZ = 0;
        }

        inA = CCSequence.actions(CCDelayTime.action(duration / 2), CCShow.action(),
                CCOrbitCamera.action(duration / 2, 1, 0, inAngleZ, inDeltaZ, 0, 0),
                CCCallFunc.action(this::finish));
        outA = CCSequence.actions(
                CCOrbitCamera.action(duration / 2, 1, 0, outAngleZ, outDeltaZ, 0, 0),
                CCHide.action(), CCDelayTime.action(duration / 2));

        inScene.runAction(inA);
        outScene.runAction(outA);
    }
}
