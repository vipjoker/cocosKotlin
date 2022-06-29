package com.vipjokerstudio.cocoskotlin.serialization.data;

import com.google.gson.Gson;

public class PbFixture {
    private Long tag;
    private float density;
    private float friction;
    private float restitution;
    private boolean sensor;
    private PbShape shape;
    private PbFilter filter;

    public static PbFixture parseFrom(String argInput, Gson gson) {
        return gson.fromJson(argInput,PbFixture.class);
    }

    public float getDensity() {
        return density;
    }

    public PbFilter getFilter() {
        return filter;
    }


    public float getFriction() {
        return friction;
    }

    public boolean getSensor() {
        return sensor;
    }

    public float getRestitution() {
        return restitution;
    }

    public PbShape getShape() {
        return shape;
    }

    public boolean hasTag() {
        return tag != null;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public void setShape(PbShape shape) {
        this.shape = shape;
    }

    public void setFilter(PbFilter filter) {
        this.filter = filter;
    }
}
