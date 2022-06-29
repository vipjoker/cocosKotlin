package com.vipjokerstudio.cocoskotlin.serialization.data;

import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Oleh Makhobei  on 27.04.20.
 */
public class PbFixtureDef {
    private PbShapeDef shapeDef;
    private float density = 0.5f;
    private float friction = 0.5f;
    private float restitution = 0.5f;
    private boolean sensor ;

    public PbShapeDef getShapeDef() {
        return shapeDef;
    }

    public void setShapeDef(PbShapeDef shapeDef) {
        this.shapeDef = shapeDef;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public FixtureDef toFixtureDef(){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setDensity(density);
        fixtureDef.setFriction(friction);
        fixtureDef.setRestitution(restitution);
        fixtureDef.setSensor(sensor);
        fixtureDef.setShape(shapeDef.toShape());
        return fixtureDef;
    }
}
