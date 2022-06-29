package com.vipjokerstudio.cocoskotlin.core.events;

import android.view.MotionEvent;

import com.vipjokerstudio.cocoskotlin.core.protocol.CCMotionEventProtocol;
import com.vipjokerstudio.cocoskotlin.core.protocol.CCTouchDelegateProtocol;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * CCTouchDispatcher.
 * Singleton that handles all the touch events.
 * The dispatcher dispatches events to the registered TouchHandlers.
 * There are 2 different type of touch handlers:
 * - Standard Touch Handlers
 * - Targeted Touch Handlers
 * <p>
 * The Standard Touch Handlers work like the CocoaTouch touch handler: a set of touches is passed to the delegate.
 * On the other hand, the Targeted Touch Handlers only receive 1 touch at the time, and they can "swallow" touches (avoid the propagation of the event).
 * <p>
 * Firstly, the dispatcher sends the received touches to the targeted touches.
 * These touches can be swallowed by the Targeted Touch Handlers. If there are still remaining touches, then the remaining touches will be sent
 * to the Standard Touch Handlers.
 *
 * @since v0.8.0
 */
/*
 * TODO: FIXME: CCStandardTouchDelegateProtocol, CCTargetedTouchDelegateProtocol is not fully ported
 */
public class CCTouchDispatcher {
    public enum ccTouchSelectorFlag {
        ccTouchSelectorNoneBit(1 << 0),
        ccTouchSelectorBeganBit(1 << 0),
        ccTouchSelectorMovedBit(1 << 1),
        ccTouchSelectorEndedBit(1 << 2),
        ccTouchSelectorCancelledBit(1 << 3),
        ccTouchSelectorAllBits(ccTouchSelectorBeganBit.flag | ccTouchSelectorMovedBit.flag
                | ccTouchSelectorEndedBit.flag | ccTouchSelectorCancelledBit.flag);

        ccTouchSelectorFlag(int val) {
            this.flag = val;
        }

        public int getFlag() {
            return flag;
        }

        private final int flag;
    }

    class ccTouchHandlerHelperData {
        public String touchesSel;
        public String touchSel;
        public int ccTouchSelectorType;
    }

    public static final int ccTouchBegan = 0;
    public static final int ccTouchMoved = 1;
    public static final int ccTouchEnded = 2;
    public static final int ccTouchCancelled = 3;
    public static final int ccTouchMax = 4;

    public static final boolean kEventHandled = true;
    public static final boolean kEventIgnored = false;
 /*
@interface CCTouchDispatcher : NSObject <EAGLTouchDelegate>
{
	NSMutableArray	*targetedHandlers;
	NSMutableArray	*standardHandlers;

	BOOL			locked;
	BOOL			toAdd;
	BOOL			toRemove;
	NSMutableArray	*handlersToAdd;
	NSMutableArray	*handlersToRemove;
	BOOL			toQuit;

	// 4, 1 for each type of event
	struct ccTouchHandlerHelperData handlerHelperData[ccTouchMax];
}*/
    /**
     * Listeners for raw MotionEvents
     */
    private ArrayList<CCMotionEventProtocol> motionListeners;

    private ArrayList<CCTargetedTouchHandler> targetedHandlers;
    private ArrayList<CCTouchHandler> touchHandlers;
    /**
     * Whether or not the events are going to be dispatched. Default: YES
     */
    private boolean dispatchEvents;

    public boolean getDispatchEvents() {
        return dispatchEvents;
    }

    public void setDispatchEvents(boolean b) {
        dispatchEvents = b;
    }

    private static CCTouchDispatcher INSTANCE = new CCTouchDispatcher();

    /**
     * singleton of the CCTouchDispatcher
     */
    public static CCTouchDispatcher sharedDispatcher() {
        return INSTANCE;
    }

    protected CCTouchDispatcher() {
        dispatchEvents = true;
        targetedHandlers = new ArrayList<>();
        touchHandlers = new ArrayList<>();
        motionListeners = new ArrayList<>();
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addHandler(final CCTouchHandler handler, final ArrayList array) {


        int i = 0;
        for (int ind = 0; ind < array.size(); ind++) {
            CCTouchHandler h = (CCTouchHandler) array.get(ind);
            if (h.getPriority() < handler.getPriority())
                i++;

            if (h.getDelegate() == handler.getDelegate())
                throw new RuntimeException("Delegate already added to touch dispatcher.");
        }
        array.add(i, handler);


    }

    public void addDelegate(CCTouchDelegateProtocol delegate, int prio) {
        addHandler(new CCTouchHandler(delegate, prio), touchHandlers);
    }

    public void addTargetedDelegate(CCTouchDelegateProtocol delegate, int prio, boolean swallowsTouches) {
        addHandler(new CCTargetedTouchHandler(delegate, prio, swallowsTouches), targetedHandlers);
    }

    public void removeDelegate(final CCTouchDelegateProtocol delegate) {
        if (delegate == null)
            return;


        for (int ind = 0; ind < targetedHandlers.size(); ind++) {
            CCTouchHandler handler = targetedHandlers.get(ind);
            if (handler.getDelegate() == delegate) {
                targetedHandlers.remove(handler);
                break;
            }
        }

        for (int ind = 0; ind < touchHandlers.size(); ind++) {
            CCTouchHandler handler = touchHandlers.get(ind);
            if (handler.getDelegate() == delegate) {
                touchHandlers.remove(handler);
                break;
            }
        }
    }


    public void removeAllDelegates() {


        targetedHandlers.clear();
        touchHandlers.clear();

    }

    public void addMotionListener(CCMotionEventProtocol listener) {
        synchronized (motionListeners) {
            motionListeners.add(listener);
        }
    }

    public void removeMotionListener(CCMotionEventProtocol listener) {
        synchronized (motionListeners) {
            motionListeners.remove(listener);
        }
    }

    public void removeAllMotionListeners() {
        synchronized (motionListeners) {
            motionListeners.clear();
        }
    }

    /**
     * Changes the priority of a previously added delegate. The lower the number,
     * the higher the priority
     */
    // -(void) setPriority:(int) priority forDelegate:(id) delegate;
    public void setPriority(final int priority, final CCTouchHandler delegate) {
        if (delegate == null)
            throw new RuntimeException("Got null touch delegate");


        CCTouchHandler handler = null;
        @SuppressWarnings("rawtypes")
        ArrayList list = null;
        int i = 0;
        for (i = 0; i < targetedHandlers.size(); i++) {
            handler = targetedHandlers.get(i++);
            if (handler.getDelegate() == delegate) {
                list = targetedHandlers;
                break;
            }
        }

        if (list == null) {
            for (i = 0; i < touchHandlers.size(); i++) {
                handler = touchHandlers.get(i);
                if (handler.getDelegate() == delegate) {
                    list = touchHandlers;
                    break;
                }
            }

            if (list == null)
                throw new RuntimeException("Touch delegate not found");
        }

        if (handler.getPriority() != priority) {
            handler.setPriority(priority);

            list.remove(i);
            addHandler(handler, list);
        }
    }


    private final Queue<MotionEvent> eventQueue = new ConcurrentLinkedQueue<>();

    public void queueMotionEvent(MotionEvent event) {
        if (dispatchEvents) {
            // copy event for queue
            MotionEvent eventForQueue = MotionEvent.obtain(event);

            eventQueue.add(eventForQueue);
        }
    }

    public void update() {
        MotionEvent event;

        while ((event = eventQueue.poll()) != null) {

            if (dispatchEvents) {

                proccessTouches(event);

                int action = event.getAction();
                int actionCode = action & MotionEvent.ACTION_MASK;
                int pid = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;


                boolean swallowed = false;

                for (int ind = 0; ind < targetedHandlers.size(); ind++) {
                    CCTargetedTouchHandler handler = targetedHandlers.get(ind);

                    boolean claimed = false;

                    switch (actionCode) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            claimed = handler.ccTouchesBegan(event);
                            if (claimed) {
                                handler.addClaimed(pid);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            if (handler.hasClaimed(pid)) {
                                claimed = true;
                                handler.ccTouchesCancelled(event);
                                handler.removeClaimed(pid);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (handler.hasClaimed(pid)) {
                                claimed = true;
                                handler.ccTouchesMoved(event);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            if (handler.hasClaimed(pid)) {
                                claimed = true;
                                handler.ccTouchesEnded(event);
                                handler.removeClaimed(pid);
                            }
                            break;
                    }


                    if (claimed && handler.swallowsTouches) {
                        swallowed = true;
                        break;
                    }
                }

                if (!swallowed) {
                    // handle standart delegates
                    switch (actionCode) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            touchesBegan(event);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            touchesCancelled(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            touchesMoved(event);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            touchesEnded(event);
                            break;
                    }
                }

            }

            event.recycle();
        }
    }

    //
    // dispatch events
    //
    private void touchesBegan(MotionEvent event) {
        if (dispatchEvents) {
            for (int ind = 0; ind < touchHandlers.size(); ind++) {
                CCTouchHandler handler = touchHandlers.get(ind);
                handler.ccTouchesBegan(event);
//                if( handler.ccTouchesBegan(event) == kEventHandled )
//                    break;
            }
        }
    }

    private void touchesMoved(MotionEvent event) {
        if (dispatchEvents) {
            for (int ind = 0; ind < touchHandlers.size(); ind++) {
                CCTouchHandler handler = touchHandlers.get(ind);
                handler.ccTouchesMoved(event);
//                if( handler.ccTouchesMoved(event) == kEventHandled )
//                    break;
            }
        }
    }

    private void touchesEnded(MotionEvent event) {
        if (dispatchEvents) {
            for (int ind = 0; ind < touchHandlers.size(); ind++) {
                CCTouchHandler handler = touchHandlers.get(ind);
                handler.ccTouchesEnded(event);
//                if( handler.ccTouchesEnded(event) == kEventHandled )
//                    break;
            }
        }
    }

    private void touchesCancelled(MotionEvent event) {
        if (dispatchEvents) {
            for (int ind = 0; ind < touchHandlers.size(); ind++) {
                CCTouchHandler handler = touchHandlers.get(ind);
                handler.ccTouchesCancelled(event);
//                if( handler.ccTouchesCancelled(event) == kEventHandled )
//                    break;
            }
        }
    }

    private void proccessTouches(MotionEvent event) {
        synchronized (motionListeners) {
            for (int i = 0; i < motionListeners.size(); i++)
                motionListeners.get(i).onTouch(event);
        }
    }
}

