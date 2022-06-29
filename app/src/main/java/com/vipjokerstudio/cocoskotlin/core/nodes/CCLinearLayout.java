package com.vipjokerstudio.cocoskotlin.core.nodes;

/**
 * Created by Oleh Makhobei  on 17.03.20.
 */
public class CCLinearLayout extends CCNode {

    private int spacing;

    @Override
    public CCNode addChild(CCNode child) {
        CCNode newNode = super.addChild(child,0);
        layout();
        return newNode;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        layout();
    }

    private void layout() {
        float yPosition = 0;
        float xPosition = Director.getInstance().getWidth() / 2;
        for (CCNode ccNode : children) {
            yPosition += ccNode.getContentSize().height + spacing;
            ccNode.setPosition(xPosition, yPosition);
        }
    }
}

