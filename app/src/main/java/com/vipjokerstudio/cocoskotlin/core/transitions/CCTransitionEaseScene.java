package com.vipjokerstudio.cocoskotlin.core.transitions;

import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;


/** CCTransitionEaseScene can ease the actions of the scene protocol.
  @since v0.8.2
  */
public interface CCTransitionEaseScene {
    /** returns the Ease action that will be performed on a linear action.
      @since v0.8.2
      */
    public CCIntervalAction easeAction(CCIntervalAction action);
}
