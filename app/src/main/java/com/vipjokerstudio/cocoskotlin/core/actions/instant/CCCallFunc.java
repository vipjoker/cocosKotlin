package com.vipjokerstudio.cocoskotlin.core.actions.instant;


import com.vipjokerstudio.cocoskotlin.core.events.SimpleCallback;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;

//
// CallFunc
//

/**
 * Calls a 'callback'
 */
public class CCCallFunc extends CCInstantAction {
    protected SimpleCallback callback;

    /**
     * creates the action with the callback
     */
    public static CCCallFunc action(SimpleCallback simpleCallback) {
        return new CCCallFunc(simpleCallback);
    }

    /**
     * creates an action with a callback
     */
    protected CCCallFunc(SimpleCallback simpleCallback) {
        callback = simpleCallback;
    }

    public CCCallFunc copy() {
        return new CCCallFunc(callback);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        execute();
    }

    /**
     * executes the callback
     */
    public void execute() {
        if (callback != null) {
            callback.onClick();
        }
    }
}
