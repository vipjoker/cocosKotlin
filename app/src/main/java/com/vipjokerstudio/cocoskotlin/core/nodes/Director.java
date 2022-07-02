package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.actions.CCScheduler;
import com.vipjokerstudio.cocoskotlin.core.events.CCKeyDispatcher;
import com.vipjokerstudio.cocoskotlin.core.events.CCTouchDispatcher;
import com.vipjokerstudio.cocoskotlin.core.layers.CCScene;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSpriteFrameCache;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCTextureCache;
import com.vipjokerstudio.cocoskotlin.core.nodes.InitListener;
import com.vipjokerstudio.cocoskotlin.core.transitions.CCTransitionScene;
import com.vipjokerstudio.cocoskotlin.core.types.CGRect;
import com.vipjokerstudio.cocoskotlin.core.types.Size;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;
import com.vipjokerstudio.cocoskotlin.render.Renderer;
import com.vipjokerstudio.cocoskotlin.util.Converter;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class Director implements Renderer {
    private static final String LOG_TAG = Director.class.getSimpleName();

    public static final int ORIENTATION_PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    private static final double DEFAULT_FPS = 60.0f;
    private static final boolean FAST_FPS_DISPLAY = true;

    private Paint fpsPaint;
    private int deviceOrientation_;
    private static Activity theApp = null;
    private static Director INSTANCE = new Director();

    public static Director getInstance() {
        return INSTANCE;
    }
    private InitListener initListener;
    private Size size = Size.make(0, 0);
    private final CanvasWrapper wrapper = new CanvasWrapper();

    @Override
    public void init(float width, float height) {
        setAnimationInterval(1.0 / 60);

        size.width = width;
        size.height = height;
        if(initListener != null){
            initListener.onInit();
        }
    }

    protected Director() {

        fpsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fpsPaint.setColor(Color.GREEN);
        fpsPaint.setTextSize(40);
        displayFPS = true;
        runningCCScene_ = null;
        nextCCScene_ = null;

        oldAnimationInterval_ = animationInterval_ = 1.0 / DEFAULT_FPS;
        sceneStack = new ArrayList<>();

        // landscape
        deviceOrientation_ = ORIENTATION_PORTRAIT;

        // FPS
        frames_ = 0;

        // paused?
        isPaused = false;

        // for iphone 4?
        contentScaleFactor_ = 1;

        surfaceSize_ = Size.zero();
        isContentScaleSupported_ = false;
//        }
    }


    public void setOnInitListener(InitListener listener){
        this.initListener = listener;
    }

    public float getWidth() {
        return size.width;
    }

    public float getHeight() {
        return size.height;
    }

    public Activity getActivity() {
        return theApp;
    }

    public void setActivity(Activity activity) {
        theApp = activity;
    }


    public int getDeviceOrientation() {
        return deviceOrientation_;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setDeviceOrientation(int orientation) {
        if (deviceOrientation_ != orientation) {
            deviceOrientation_ = orientation;
            switch (deviceOrientation_) {
                case ORIENTATION_PORTRAIT:
                    theApp.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case ORIENTATION_LANDSCAPE:
                    theApp.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                default:
                    Log.w(LOG_TAG, "Director: Unknown device orientation");
                    break;
            }
        }
    }


    /* display FPS ? */
    private int frames_;
    private float accumDt_;
    private float frameRate_;
    /**
     * Whether or not to display the FPS on the bottom-left corner
     */
    /* display FPS ? */
    private boolean displayFPS;


    /* is the running scene paused */
    private boolean isPaused;

    /**
     * Whether or not the Director is paused
     */
    public boolean getIsPaused() {
        return isPaused;
    }

    /* The running CCScene */
    private CCScene runningCCScene_;

    /* will be the next 'runningCCScene' in the next frame */
    private CCScene nextCCScene_;

    /* If YES, then "old" CCScene will receive the cleanup message */
    private boolean sendCleanupToCCScene_;

    /**
     * Whether or not the replaced CCScene will receive the cleanup message.
     * If the new CCScene is pushed, then the old CCScene won't receive the "cleanup" message.
     * If the new CCScene replaces the old one, the it will receive the "cleanup" message.
     *
     * @since v0.99.0
     */
    public boolean getSendCleanupToScene() {
        return sendCleanupToCCScene_;
    }

    /* scheduled CCScenes */
    private ArrayList<CCScene> sceneStack;

    /* last time the main loop was updated */
    private long lastUpdate_;

    /* delta time since last tick to main loop */
    private float dt;

    /* whether or not the next delta time will be zero */
    private boolean nextDeltaTimeZero_;

    public boolean getNextDeltaTimeZero() {
        return nextDeltaTimeZero_;
    }

    public void setNextDeltaTimeZero(boolean dtz) {
        nextDeltaTimeZero_ = dtz;
    }


    /* screen, different than surface size */
    private Size surfaceSize_;

    /* content scale factor */
    private float contentScaleFactor_;

    /**
     * The size in pixels of the surface. It could be different than the screen size.
     * High-res devices might have a higher surface size than the screen size.
     * In non High-res device the contentScale will be emulated.
     * <p>
     * Warning: Emulation of High-Res on iOS < 4 is an EXPERIMENTAL feature.
     *
     * @since v0.99.4
     */
    public void setContentScaleFactor(float scaleFactor) {
        if (scaleFactor != contentScaleFactor_) {
            contentScaleFactor_ = scaleFactor;
            surfaceSize_ = Size.make(size.width * scaleFactor, size.height * scaleFactor);


            // update projection

        }
    }

    public float getContentScaleFactor() {
        return contentScaleFactor_;
    }

    /* contentScaleFactor could be simulated */
    private boolean isContentScaleSupported_;

    private float accumDtForProfiler_;

    /**
     * The current running CCScene. Director can only run one CCScene at the time
     */

    public CCScene getRunningScene() {
        return runningCCScene_;
    }

    /**
     * The FPS value
     */
    // internal timer
    private double animationInterval_;
    private double oldAnimationInterval_;
//    private Timer  animationTimer_;

    public double getAnimationInterval() {
        return animationInterval_;
    }

    public void setAnimationInterval(double interval) {
        animationInterval_ = interval;

//        if (animationTimer_ != null) {
//            stopAnimation();
//            startAnimation();
//        }
    }


    public void setDisplayFPS(boolean value) {
        displayFPS = value;
    }


    /**
     * sets the OpenGL default values
     */
    public void setGLDefaultValues() {
        // This method SHOULD be called only after openGLView_ was initialized


    }


    @Override
    public void draw(Canvas canvas, long time) {
        Log.i("DEBUG", "draw: " + time);
        CCTouchDispatcher.sharedDispatcher().update();
        CCKeyDispatcher.sharedDispatcher().update();
        drawCCScene(canvas);


//        waitForFPS();
    }


    public void onSizeChanged(float width, float height) {
        surfaceSize_.set(width, height);
        setGLDefaultValues();
        initOpenGLViewWithView(Size.make(width, height));
    }


    private double sleepInterval = 0;


    /**
     * Draw the CCScene.
     * This method is called every frame. Don't call it manually.
     */
    public void drawCCScene(Canvas canvas) {

        /* calculate "global" dt */
        calculateDeltaTime();
        canvas.drawColor(Color.BLACK);
        /* tick before glClear: issue #533 */
        if (!isPaused) {
            CCScheduler.sharedScheduler().tick(dt);
        }


        /* to avoid flickr, nextCCScene MUST be here: after tick and before draw.
         XXX: Which bug is this one. It seems that it can't be reproduced with v0.9 */
        if (nextCCScene_ != null) {
            setNextScene();
        }


        /* draw the CCScene */
        if (runningCCScene_ != null) {

            wrapper.setCanvas(canvas, getHeight());
            runningCCScene_.update(wrapper);
            canvas.restore();
        }
        if (displayFPS) {
            showFPS(canvas);
        }


    }

    private void calculateDeltaTime() {
        long now = System.currentTimeMillis();

        // new delta time
        if (nextDeltaTimeZero_) {
            dt = 0;
            nextDeltaTimeZero_ = false;
        } else {
            dt = (now - lastUpdate_) * 0.001f;
            dt = Math.max(0, dt);
        }

        lastUpdate_ = now;
    }

    /**
     * returns the size of the OpenGL view in pixels, according to the landspace
     */
    public Size winSize() {
        return Size.make(getWidth(), getHeight());
    }


    public boolean getLandscape() {
        return deviceOrientation_ == ORIENTATION_LANDSCAPE;
    }

    public void setLandscape(boolean on) {
        if (on)
            setDeviceOrientation(ORIENTATION_LANDSCAPE);
        else
            setDeviceOrientation(ORIENTATION_PORTRAIT);
    }

    // Director Integration with a Android view
    // is the view currently attached
    public boolean isOpenGLAttached() {
        return true;
        // return openGLView_.getParent() != null;
    }


    private CGRect getAppFrameRect(float targetRatio) {
        WindowManager w = theApp.getWindowManager();
        Size size = Size.make(w.getDefaultDisplay().getWidth(), w.getDefaultDisplay().getHeight());

        float currentRation = size.width / size.height;
        Size newSize = Size.make(size.width, size.height);
        Vec2 offset =new Vec2(0, 0);

        if (currentRation > targetRatio) {
            newSize.width = targetRatio * size.height;
            offset.x = (size.width - newSize.width) / 2;
        } else if (currentRation < targetRatio) {
            newSize.height = size.width / targetRatio;
            offset.y = (size.height - newSize.height) / 2;
        }

        CGRect rect = CGRect.make(offset, newSize);

        return rect;
    }

    public void setScreenSize(float width, float height) {
        size.set(width, height);
    }

    private boolean initOpenGLViewWithView(Size size) {
        surfaceSize_ = size;


        CCTouchDispatcher.sharedDispatcher().setDispatchEvents(true);


        return true;
    }

    public void showProfilers() {
        accumDtForProfiler_ += dt;
        if (accumDtForProfiler_ > 1.0f) {
            accumDtForProfiler_ = 0;

        }
    }


    /**
     * converts an OpenGL coordinate to a UIKit coordinate
     * Useful to convert node points to window points for calls such as glScissor
     */
    public Vec2 convertToUI(Vec2 glPoint) {
        Size winSize = surfaceSize_;
        int oppositeY = (int) (winSize.height - glPoint.y);

        Vec2 uiPoint = null;
        switch (deviceOrientation_) {
            case ORIENTATION_PORTRAIT:
                uiPoint = new Vec2(glPoint.x, oppositeY);
                break;

            case ORIENTATION_LANDSCAPE:
                // uiPoint = Vec2.create(glPoint.y, glPoint.x);
                uiPoint = new Vec2(glPoint.x, oppositeY);
                break;
            default:
                return null;
        }

        uiPoint = Vec2Util.ccpMult(uiPoint, 1 / contentScaleFactor_);
        return uiPoint;
    }

    // Director CCScene Management

    /**
     * Enters the Director's main loop with the given CCScene.
     * Call it to run only your FIRST CCScene.
     * Don't call it if there is already a running CCScene.
     */
    public void runWithScene(CCScene CCScene) {
        assert CCScene != null : "Argument must be non-null";
        assert runningCCScene_ == null : "You can't run a CCScene if another CCScene is running. Use replaceCCScene or pushCCScene instead";

        pushScene(CCScene);
//        startAnimation();
    }

    /**
     * Replaces the running CCScene with a new one. The running CCScene is terminated.
     * ONLY call it if there is a running CCScene.
     */
    public void replaceScene(CCScene CCScene) {
        assert CCScene != null : "Argument must be non-null";

        int index = sceneStack.size();

        sendCleanupToCCScene_ = true;
        sceneStack.set(index - 1, CCScene);
        nextCCScene_ = CCScene;
    }

    /**
     * Suspends the execution of the running CCScene, pushing it on the stack of suspended CCScenes.
     * The new CCScene will be executed.
     * Try to avoid big stacks of pushed CCScenes to reduce memory allocation.
     * ONLY call it if there is a running CCScene.
     */
    public void pushScene(CCScene CCScene) {
        assert CCScene != null : "Argument must be non-null";

        sendCleanupToCCScene_ = false;

        sceneStack.add(CCScene);
        nextCCScene_ = CCScene;
    }

    /**
     * Pops out a CCScene from the queue.
     * This CCScene will replace the running one.
     * The running CCScene will be deleted.
     * If there are no more CCScenes in the stack the execution is terminated.
     * ONLY call it if there is a running CCScene.
     */
    public void popScene() {
        assert runningCCScene_ != null : "A running CCScene is needed";

        sceneStack.remove(sceneStack.size() - 1);
        int c = sceneStack.size();

        if (c == 0) {
            end();
        } else {
            nextCCScene_ = sceneStack.get(c - 1);
        }
    }

    /**
     * Removes cached all cocos2d cached data.
     * It will purge the CCTextureCache, CCSpriteFrameCache, CCBitmapFont cache
     *
     * @since v0.99.3
     */

    public void purgeCachedData() {
        //CCBitmapFontAtlas .purgeCachedData();
        CCSpriteFrameCache.purgeSharedSpriteFrameCache();
        CCTextureCache.purgeSharedTextureCache();

    }

    /**
     * Ends the execution, releases the running CCScene.
     * It doesn't remove the OpenGL view from its parent. You have to do it manually.
     */
    public void end() {
//    	synchronized(Director.class) {
//    		if (INSTANCE == null) {
//    			return;
//    		}

        if (runningCCScene_ != null) {
            runningCCScene_.onExit();
            runningCCScene_.cleanup();
            runningCCScene_ = null;
        }
        nextCCScene_ = null;

        // remove all objects.
        // runWithCCScene might be executed after 'end'.
        sceneStack.clear();

        // don't release the event handlers
        // They are needed in case the director is run again
        CCTouchDispatcher.sharedDispatcher().removeAllDelegates();

//		stopAnimation();
        // detach();

        // Purge bitmap cache
        // CCBitmapFontAtlas.purgeCachedData();

        // Purge all managers
        CCSpriteFrameCache.purgeSharedSpriteFrameCache();
        // CCScheduler.purgeSharedScheduler();
        // CCActionManager.purgeSharedManager();
        CCTextureCache.purgeSharedTextureCache();

        // OpenGL view
//    		openGLView_ = null;
//    		INSTANCE = null;
//            if (FPSLabel_ != null)
//                FPSLabel_ = null;
//    	}
    }

    public void setNextScene() {
        boolean runningIsTransition = runningCCScene_ instanceof CCTransitionScene;
        boolean newIsTransition = nextCCScene_ instanceof CCTransitionScene;

        // If it is not a transition, call onExit
        if (runningCCScene_ != null && !newIsTransition) {
            runningCCScene_.onExit();

            // issue #709. the root node (CCScene) should receive the cleanup message too
            // otherwise it might be leaked.
            if (sendCleanupToCCScene_)
                runningCCScene_.cleanup();
        }

        runningCCScene_ = nextCCScene_;
        nextCCScene_ = null;

        if (!runningIsTransition) {
            runningCCScene_.onEnter();
            runningCCScene_.onEnterTransitionDidFinish();
        }
    }


    /**
     * Pauses the running CCScene.
     * The running CCScene will be _drawed_ but all scheduled timers will be paused
     * While paused, the draw rate will be 4 FPS to reduce CPU consuption
     */
    public void pause() {
        if (isPaused)
            return;

        oldAnimationInterval_ = animationInterval_;

        // when paused, don't consume CPU
        setAnimationInterval(1 / 4.0);
        isPaused = true;
    }

    /**
     * Resumes the paused CCScene
     * The scheduled timers will be activated again.
     * The "delta time" will be 0 (as if the game wasn't paused)
     */
    public void resume() {
        if (!isPaused)
            return;

        setAnimationInterval(oldAnimationInterval_);

        lastUpdate_ = System.currentTimeMillis();

        isPaused = false;
        dt = 0;
    }


    private StringBuilder fpsBuilder = new StringBuilder();

    private void showFPS(Canvas canvas) {

        if (FAST_FPS_DISPLAY) {
            // display the FPS using a LabelAtlas
            // updates the FPS every frame

            frames_++;
            accumDt_ += dt;

            if (accumDt_ > Converter.CC_DIRECTOR_FPS_INTERVAL) {
                frameRate_ = frames_ / accumDt_;
                frames_ = 0;
                accumDt_ = 0;

                int fpsInt = (int) frameRate_;
                int fpsFract = (int) ((frameRate_ - fpsInt) * 10);

                fpsBuilder.delete(0, fpsBuilder.length());
                fpsBuilder.append(fpsInt);
                fpsBuilder.append('.');
                fpsBuilder.append(fpsFract);


            }

            canvas.drawText(fpsBuilder.toString(), 100, 100, fpsPaint);
        } else {
            // display the FPS using a manually generated Texture (very slow)
            // updates the FPS 3 times per second aprox.

            frames_++;
            accumDt_ += dt;

            if (accumDt_ > Converter.CC_DIRECTOR_FPS_INTERVAL) {
                frameRate_ = frames_ / accumDt_;
                frames_ = 0;
                accumDt_ = 0;
            }

            String str = String.format("%.2f", frameRate_);

        }
    }

    private boolean mTranslucentBackground = false;

    public boolean onKeyDown(KeyEvent event) {
        if (!CCKeyDispatcher.sharedDispatcher().getDispatchEvents())
            return false;
        CCKeyDispatcher.sharedDispatcher().queueMotionEvent(event);
        return true;
    }

    //added by Ishaq
    public boolean onKeyUp(KeyEvent event) {
        if (!CCKeyDispatcher.sharedDispatcher().getDispatchEvents())
            return false;

        CCKeyDispatcher.sharedDispatcher().queueMotionEvent(event);
        return true;
    }

    //added by Ishaq
    public void setIsEnableKeyEvent(boolean b) {
        CCKeyDispatcher.sharedDispatcher().setDispatchEvents(b);
    }

    //added by Ishaq
    public boolean isEnableKeyEvent() {
        return CCKeyDispatcher.sharedDispatcher().getDispatchEvents();
    }

    public boolean canPopScene() {
        return sceneStack.size() > 1;
//        return !sceneStack.isEmpty();
    }


    @Override
    public void onTouchStart(float x, float y) {
        if(runningCCScene_ == null)return;
        y = y - getHeight();
        y *= -1;

        runningCCScene_.onTouchStart(x, y);
    }

    @Override
    public void onTouchMove(float x, float y) {
        if(runningCCScene_ == null)return;
        y = y - getHeight();
        y *= -1;
        runningCCScene_.onTouchMove(x, y);
    }

    @Override
    public void onTouchEnd(float x, float y) {
        if(runningCCScene_ == null)return;
        y = y - getHeight();
        y *= -1;
        runningCCScene_.onTouchEnd(x, y);
    }


    @Override
    public void moveDown() {

    }

    @Override
    public void moveLeft() {

    }

    @Override
    public void moveRight() {

    }

    @Override
    public void moveTop() {

    }

    @Override
    public void action() {

    }

    @Override
    public void zoomCamera(int zoomLevel) {

    }

}

