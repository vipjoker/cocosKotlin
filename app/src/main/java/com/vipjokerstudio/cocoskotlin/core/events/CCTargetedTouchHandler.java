package com.vipjokerstudio.cocoskotlin.core.events;

import com.vipjokerstudio.cocoskotlin.core.protocol.CCTouchDelegateProtocol;

import java.util.ArrayList;


public class CCTargetedTouchHandler extends CCTouchHandler {

	boolean swallowsTouches;
	
	private ArrayList<Integer> claimedSet;
	
	public CCTargetedTouchHandler(CCTouchDelegateProtocol delegate, int priority, boolean swallow) {
		super(delegate, priority);
		swallowsTouches = swallow;
		claimedSet = new ArrayList<>();
	}
	
	void addClaimed(int pid) {
		if (!claimedSet.contains(pid)) {
			claimedSet.add(pid);
		}
	}
	
	void removeClaimed(int pid) {
		int ind = claimedSet.indexOf(pid);
		claimedSet.remove(ind);
	}

	boolean hasClaimed(int pid) {
		return claimedSet.contains(pid);
	}
}
