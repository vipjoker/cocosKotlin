package com.vipjokerstudio.cocoskotlin.core.actions.camera;


import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCIntervalAction;
import com.vipjokerstudio.cocoskotlin.core.actions.interval.CCReverseTime;
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode;

/** Base class for CCCamera actions
 */
public abstract class CCCameraAction extends CCIntervalAction {
    protected float centerXOrig;
    protected float centerYOrig;
    protected float centerZOrig;

    protected float eyeXOrig;
    protected float eyeYOrig;
    protected float eyeZOrig;

    protected float upXOrig;
    protected float upYOrig;
    protected float upZOrig;

    protected CCCameraAction(float t) {
        super(t);
    }

    @Override
    public void start(CCNode aTarget) {
        super.start(aTarget);

        float x[] = new float[1];
        float y[] = new float[1];
        float z[] = new float[1];

        target.getCamera().getCenter(x, y, z);
        centerXOrig = x[0];
        centerYOrig = y[0];
        centerZOrig = z[0];

        target.getCamera().getEye(x, y, z);
        eyeXOrig = x[0];
        eyeYOrig = y[0];
        eyeZOrig = z[0];

        target.getCamera().getUp(x, y, z);
        upXOrig = x[0];
        upYOrig = y[0];
        upZOrig = z[0];
    }

    @Override
    public CCReverseTime reverse() {
        return CCReverseTime.action(this);
    }

}

