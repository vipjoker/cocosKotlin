package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.util.Log;

import com.vipjokerstudio.cocoskotlin.core.nodes.Director;
import com.vipjokerstudio.cocoskotlin.core.types.CGRect;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

/**
 * Created by Oleh Makhobei  on 14.03.20.
 */
public class CCCamera {

    private CCNode parent;
    private CCNode target;
    private float width;
    private float height;
    private Vec2 offset;

    public static float getZEye() {
            return 0;
    }

    public void restore() {

    }

    public void getEye(float[] ex, float[] ey, float[] ez) {

    }

    public void getCenter(float[] cx, float[] cy, float[] cz) {

    }

    public void setEye(float i, float j, float k) {

    }

    public void getUp(float[] x, float[] y, float[] z) {

    }

    public void setupTarget(CCNode parent,CCNode target ,Vec2 offset){
        this.parent = parent;
        this.target = target;
        this.offset = offset;
    }

    public void setBounderies(float width,float height){
        this.width = Math.max( Director.getInstance().getWidth(),width - Director.getInstance().getWidth());
        this.height = Math.max(Director.getInstance().getHeight(), height - Director.getInstance().getHeight());
    }

    public void update(){
        if(target != null && offset != null){

        //    moveCameraTo(target.getPosition(),offset);
        }
    }

    public void moveCamera(float x, float y) {
        float posX = MathUtils.clamp(parent.getPosition().x + x,  -width,0);
        float posY = MathUtils.clamp(parent.getPosition().y + y,  -height,0);
        Log.i("MOVE_CAMERA", "moveCamera: x " + posX + " y " + posY);
        parent.setPosition(posX,posY);

    }

    public void moveCameraTo(Vec2 pos) {

        float posX = MathUtils.clamp(pos.x,  -width,0);
        float posY = MathUtils.clamp(pos.y,  -height,0);
        Log.i("MOVE_CAMERA", "moveCamera: x " + posX + " y " + posY);
        parent.setPosition(posX,posY);

    }
    public void moveCameraTo(float x,float y) {

//        float posX = MathUtils.clamp(x,  -width,0);
//        float posY = MathUtils.clamp(y,  -height,0);
//        Log.i("MOVE_CAMERA", "moveCamera: x " + posX + " y " + posY);
//        parent.setPosition(posX,posY);
        parent.setPosition(x,y);

    }

    public void moveCameraTo(Vec2 pos,Vec2 offset) {

        float posX = MathUtils.clamp(-pos.x + offset.x,  -width,0);
        float posY = MathUtils.clamp(-pos.y + offset.y,  -height,0);
        Log.i("MOVE_CAMERA", "moveCamera: x " + posX + " y " + posY);
        parent.setPosition(posX,posY);

    }
}
