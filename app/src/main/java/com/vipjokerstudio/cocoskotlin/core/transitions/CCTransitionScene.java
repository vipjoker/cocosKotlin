package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.actions.UpdateCallback;
import com.vipjokerstudio.cocoskotlin.core.events.CCTouchDispatcher;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;

import org.jbox2d.common.Vec2;


public class CCTransitionScene extends CCScene {

    protected static final String kSceneFade = "FADE";

    /**
     * Orientation Type used by some transitions
     */
    public interface tOrientation {
        /// An horizontal orientation where the Left is nearer
        public static int kOrientationLeftOver = 0;
        /// An horizontal orientation where the Right is nearer
        public static int kOrientationRightOver = 1;
        /// A vertical orientation where the Up is nearer
        public static int kOrientationUpOver = 0;
        /// A vertical orientation where the Bottom is nearer
        public static int kOrientationDownOver = 1;
    }


    /**
     * Base class for Transition scenes
     */
    protected CCScene inScene;
    protected CCScene outScene;
    protected float duration;
    protected boolean inSceneOnTop;
    protected boolean sendCleanupToScene;

    /**
     * creates a base transition with duration and incoming scene
     */
    public static CCTransitionScene transition(float t, CCScene s) {
        return new CCTransitionScene(t, s);
    }

    /**
     * initializes a transition with duration and incoming scene
     */
    protected CCTransitionScene(float t, CCScene s) {
        assert s != null : "Argument scene must be non-null";

        duration = t;

        // Don't retain them, it will be reatined when added
        inScene = s;
        outScene = Director.getInstance().getRunningScene();

        if (inScene == outScene) {
            throw new TransitionWithInvalidSceneException("Incoming scene must be different from the outgoing scene");
        }

        // disable events while transition
        CCTouchDispatcher.sharedDispatcher().setDispatchEvents(false);
        sceneOrder();
    }

    protected void sceneOrder() {
        // add both scenes
        inSceneOnTop = true;
    }

    @Override
    public void draw(CanvasWrapper canvas) {
        if( inSceneOnTop) {
            outScene.update(canvas);
            inScene.update(canvas);
        } else {
            inScene.update(canvas);
            outScene.update(canvas);
        }
    }

    
    private UpdateCallback setNewSceneCallback = new UpdateCallback() {
		@Override
		public void update(float d) {
			setNewScene(d);
		}
	};
    
    public void finish() {
        /* clean up */
        inScene.setVisible(true);
        inScene.setPosition(new Vec2());
        inScene.setScale(1.0f);
        inScene.setRotation(0.0f);
        inScene.getCamera().restore();

        outScene.setVisible(false);
        outScene.setPosition(new Vec2());
        outScene.setScale(1.0f);
        outScene.setRotation(0.0f);
        outScene.getCamera().restore();

        schedule(setNewSceneCallback);
    }

    public void setNewScene(float dt) {
        unschedule(setNewSceneCallback);

        sendCleanupToScene = Director.getInstance().getSendCleanupToScene();
        Director.getInstance().replaceScene(inScene);

        // enable events after transition
        CCTouchDispatcher.sharedDispatcher().setDispatchEvents(true);

        // issue #267
        outScene.setVisible(true);
    }

    /**
     * used by some transitions to hide the outter scene
     */
    public void hideOutShowIn() {
        inScene.setVisible(true);
        outScene.setVisible(false);
    }

    // custom onEnter
    @Override
    public void onEnter() {
        super.onEnter();
        inScene.onEnter();
        // outScene should not receive the onEnter callback
    }

    // custom onExit

    @Override
    public void onExit() {
        super.onExit();
        outScene.onExit();

        // inScene should not receive the onExit callback
        // only the onEnterTransitionDidFinish
        inScene.onEnterTransitionDidFinish();
    }

    @Override
    public void onEnterTransitionDidFinish() {
        super.onEnterTransitionDidFinish();
    }

    // custom cleanup
    @Override
    public void cleanup() {
        super.cleanup();

        if (sendCleanupToScene) {
            outScene.cleanup();
        }
    }
    
    static class TransitionWithInvalidSceneException extends RuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = -1743490059348093939L;

		public TransitionWithInvalidSceneException(String reason) {
            super(reason);
        }
    }
}

