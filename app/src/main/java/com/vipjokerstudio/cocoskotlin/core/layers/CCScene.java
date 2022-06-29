package com.vipjokerstudio.cocoskotlin.core.layers;


import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;
import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

/** CCScene is a subclass of CCNode that is used only as an abstract concept.
 
 CCScene an CCNode are almost identical with the difference that CCScene has it's
 anchor point (by default) at the center of the screen.

 For the moment CCScene has no other logic than that, but in future releases it might have
 additional logic.

 It is a good practice to use and CCScene as the parent of all your nodes.
*/
public class CCScene extends CCNode {

    public static CCScene node() {
        return new CCScene();
    }

    protected CCScene() {
    	super();
    	
        Size s = Director.getInstance().winSize();
        
        setRelativeAnchorPoint(false);

        setAnchorPoint(0.5f, 0.5f);
        setContentSize(s);	
    }
//
    @Override
    public void draw(CanvasWrapper canvas) {

//        canvas.clipRect(0,0,contentSize.width,contentSize.height);
        super.draw(canvas);
    }
}

