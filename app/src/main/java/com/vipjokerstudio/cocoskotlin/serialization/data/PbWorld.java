package com.vipjokerstudio.cocoskotlin.serialization.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PbWorld {
    private Long tag;
    private PbVec2 gravity;
    private boolean autoClearForces;
    private boolean allowSleep;
    private boolean continuousPhysics;
    private boolean warmStarting;
    private boolean subStepping;
    private List<PbBody> bodies = new ArrayList<>();
    private List<PbJoint> joints = new ArrayList<>();

    public PbVec2 getGravity() {
        return gravity;
    }

    public boolean getAutoClearForces() {
        return autoClearForces;
    }


    public int getBodiesCount() {
        return bodies.size();
    }

    public PbBody getBodies(int i) {
        return bodies.get(i);
    }

    public List<PbBody> getBodies() {
        return bodies;
    }

    public int getJointsCount() {
        return joints.size();
    }

    public PbJoint getJoints(int i) {
        return joints.get(i);
    }

    public List<PbJoint> getJoints() {
        return joints;
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

    public void setGravity(PbVec2 vecToPb) {
        this.gravity = vecToPb;
    }

    public void setAutoClearForces(boolean autoClearForces) {
        this.autoClearForces = autoClearForces;
    }


    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public boolean getAllowSleep() {
        return allowSleep;
    }

    public void setContinuousPhysics(boolean continuousPhysics) {
        this.continuousPhysics = continuousPhysics;
    }

    public boolean getContinuousPhysics() {
        return continuousPhysics;
    }

    public void setWarmStarting(boolean warmStarting) {
        this.warmStarting = warmStarting;
    }

    public boolean getWarmStarting() {
        return warmStarting;
    }

    public void setSubStepping(boolean subStepping) {
        this.subStepping = subStepping;
    }

    public boolean getSubStepping() {
        return subStepping;
    }

    public void addBodies(PbBody serializeBody) {
        bodies.add(serializeBody);
    }

    public void addJoints(PbJoint serializeJoint) {
        joints.add(serializeJoint);
    }


    public static PbWorld parseFrom(String json, Gson gson) {
        return gson.fromJson(json, PbWorld.class);
    }

}
