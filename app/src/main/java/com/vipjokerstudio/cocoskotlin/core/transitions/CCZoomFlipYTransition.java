package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.camera.CCOrbitCamera;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCCallFunc;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCHide;
import com.vipjokerstudio.cocoskotlin.core.actions.instant.CCShow;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCDelayTime;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCScaleTo;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSequence;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCSpawn;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;


// TODO

/**
 * ZoomFlipY Transition.
 * Flips the screen vertically doing a little zooming out/in
 * The front face is the outgoing scene and the back face is the incoming scene.
 */
public class CCZoomFlipYTransition extends CCOrientedTransitionScene {

    public static CCZoomFlipYTransition transition(float t, CCScene s, int orientation) {
        return new CCZoomFlipYTransition(t, s, orientation);
    }

    public CCZoomFlipYTransition(float t, CCScene s, int orientation) {
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

        inA = CCSequence.actions(
                CCDelayTime.action(duration / 2),
                CCSpawn.actions(
                        CCOrbitCamera.action(duration / 2, 1, 0, inAngleZ, inDeltaZ, 90, 0),
                        CCScaleTo.action(duration / 2, 1),
                        CCShow.action()
                ),
                CCCallFunc.action(this::finish));
        outA = CCSequence.actions(
                CCSpawn.actions(
                        CCOrbitCamera.action(duration / 2, 1, 0, outAngleZ, outDeltaZ, 90, 0),
                        CCScaleTo.action(duration / 2, 0.5f)
                ),
                CCHide.action(),
                CCDelayTime.action(duration / 2));

        inScene.setScale(0.5f);
        inScene.runAction(inA);
        outScene.runAction(outA);
    }
}
