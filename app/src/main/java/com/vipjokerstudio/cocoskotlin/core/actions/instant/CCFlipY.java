package com.vipjokerstudio.cocoskotlin.core.actions.instant;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSprite;

/** Flips the sprite vertically
 @since v0.99.0
 */
public class CCFlipY extends CCInstantAction {
	boolean  flipY;

    public static CCFlipY action(boolean fy) {
        return new CCFlipY(fy);
    }

    public CCFlipY(boolean fy) {
        super();
        flipY = fy;
    }

    @Override
    public CCFlipY copy() {
        CCFlipY copy = new CCFlipY(flipY);
        return copy;
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        CCSprite sprite = (CCSprite)aTarget;
        sprite.setFlipY(flipY);
    }

    @Override
    public CCFlipY reverse() {
        return CCFlipY.action(!flipY);
    }
}

