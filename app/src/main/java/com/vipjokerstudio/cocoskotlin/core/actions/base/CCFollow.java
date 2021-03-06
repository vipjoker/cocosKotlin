package com.vipjokerstudio.cocoskotlin.core.actions.base;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.CGRect;
import com.vipjokerstudio.cocoskotlin.core.types.Size;
import com.vipjokerstudio.cocoskotlin.core.types.Vec2Util;

import org.jbox2d.common.Vec2;

/** CCFollow is an action that "follows" a node.
 
 Eg:
	[layer runAction: [CCFollow actionWithTarget:hero]];
 
 Instead of using CCCamera as a "follower", use this action instead.
 @since v0.99.2
 */
public class CCFollow extends CCAction {
	/* node to follow */
	CCNode followedNode_;
	
	/* whether camera should be limited to certain area */
	/** alter behavior - turn on/off boundary */
	boolean boundarySet;
	public void setBoundarySet(boolean flag) {
		boundarySet = flag;
	}
	public boolean getBoundarySet() {
		return boundarySet;
	}
	
	/* if screensize is bigger than the boundary - update not needed */
	boolean boundaryFullyCovered;
	
	/* fast access to the screen dimensions */
	Vec2 halfScreenSize;
	Vec2 fullScreenSize;
	
	/* world boundaries */
	float leftBoundary;
	float rightBoundary;
	float topBoundary;
	float bottomBoundary;
	

	/** creates the action with no boundary set */
	public static CCFollow action(CCNode followedNode) {
		return new CCFollow(followedNode);
	}

	/** creates the action with a set boundary */
	public static CCFollow action(CCNode followedNode, CGRect rect) {
		return new CCFollow(followedNode, rect);
	}

	/** initializes the action */
	protected CCFollow(CCNode followedNode) {
		super();

		followedNode_ = followedNode;
		boundarySet = false;
		boundaryFullyCovered = false;

		Size winSize = Director.getInstance().winSize();
		fullScreenSize = new Vec2(winSize.width, winSize.height);
		halfScreenSize = Vec2Util.ccpMult(fullScreenSize, .5f);
	}

	/** initializes the action with a set boundary */
	protected CCFollow(CCNode followedNode, CGRect rect) {
		super();
		
		followedNode_ = followedNode;
		boundarySet = true;
		boundaryFullyCovered = false;
		
		Size winSize = Director.getInstance().winSize();
		fullScreenSize = new Vec2(winSize.width, winSize.height);
		halfScreenSize = Vec2Util.ccpMult(fullScreenSize, .5f);
		
		leftBoundary = -((rect.origin.x+rect.size.width) - fullScreenSize.x);
		rightBoundary = -rect.origin.x ;
		topBoundary = -rect.origin.y;
		bottomBoundary = -((rect.origin.y+rect.size.height) - fullScreenSize.y);
		
		if(rightBoundary < leftBoundary) {
			// screen width is larger than world's boundary width
			//set both in the middle of the world
			rightBoundary = leftBoundary = (leftBoundary + rightBoundary) / 2;
		}
		
		if(topBoundary < bottomBoundary) {
			// screen width is larger than world's boundary width
			//set both in the middle of the world
			topBoundary = bottomBoundary = (topBoundary + bottomBoundary) / 2;
		}
		
		if( (topBoundary == bottomBoundary) && (leftBoundary == rightBoundary) )
			boundaryFullyCovered = true;
	}

    @Override
    public CCFollow copy() {
    	CCFollow f = new CCFollow(this.followedNode_);
    	f.setTag(this.getTag());
    	return f;
    }
    

	@Override
	public boolean isDone()	{
		return ( ! followedNode_.isRunning());
	}
	
	@Override
	public void stop() {
		target = null;
		super.stop();
	}

	private static float CLAMP(float x, float y, float z) {
		return Math.max(Math.min(x, y), z);
	}
	
	@Override
	public void step(float dt) {
		if(boundarySet) {
			// whole map fits inside a single screen, no need to modify the position - unless map boundaries are increased
			if(boundaryFullyCovered)
				return;
			
			Vec2 tempPos = Vec2Util.ccpSub( halfScreenSize, followedNode_.getPosition());
			target.setPosition(new Vec2(CLAMP(tempPos.x,leftBoundary,rightBoundary),
					CLAMP(tempPos.y,bottomBoundary,topBoundary)));
		}
		else {
			target.setPosition(Vec2Util.ccpSub(halfScreenSize, followedNode_.getPosition()));
		}
	}
	@Override
	public void update(float time) {
		// TODO Auto-generated method stub
		
	}
}
