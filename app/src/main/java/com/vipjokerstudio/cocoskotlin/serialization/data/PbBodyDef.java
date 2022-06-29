package com.vipjokerstudio.cocoskotlin.serialization.data;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

/**
 * Created by Oleh Makhobei  on 27.04.20.
 */
public class PbBodyDef {
    private PbVec2 position = PbVec2.create(0,0);
    private float angle;
    private PbBodyType bodyType = PbBodyType.STATIC;


    public PbVec2 getPosition() {
        return position;
    }

    public void setPosition(PbVec2 position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public PbBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(PbBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public BodyDef toBodyDef (){
        BodyDef bodyDef = new BodyDef();
        switch (bodyType){
            case STATIC:
                bodyDef.setType(BodyType.STATIC);
                break;
            case DYNAMIC:
                bodyDef.setType(BodyType.DYNAMIC);
                break;
            case KINEMATIC:
                bodyDef.setType(BodyType.KINEMATIC);
                break;
        }

        bodyDef.getPosition().set(position.getX(),position.getY());
        bodyDef.setAngle((float) Math.toDegrees(angle));
        return bodyDef;
    }
}
