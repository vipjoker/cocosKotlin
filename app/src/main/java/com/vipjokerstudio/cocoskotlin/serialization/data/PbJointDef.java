package com.vipjokerstudio.cocoskotlin.serialization.data;

/**
 * Created by Oleh Makhobei  on 01.05.20.
 */
public class PbJointDef {
    private String name;
    private String bodyA;
    private String bodyB;
    private PbJointType type;
    private PbVec2 anchorA = PbVec2.create(0,0);
    private PbVec2 anchorB = PbVec2.create(0,0);
    private PbVec2 localAxisA = PbVec2.create(0,0);
    private float length;
    private float frequency;
    private float dampingRatio;
    private float maxForce;
    private float upperTranslation;
    private float lowerTranslation;

    private float upperAngle;
    private float lowerAngle;
    private float referenceAngle;
    private boolean collideConnected;
    private boolean enableLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public PbVec2 getLocalAxisA() {
        return localAxisA;
    }

    public void setLocalAxisA(PbVec2 localAxisA) {
        this.localAxisA = localAxisA;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getDampingRatio() {
        return dampingRatio;
    }

    public void setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
    }

    public String getBodyA() {
        return bodyA;
    }

    public void setBodyA(String bodyA) {
        this.bodyA = bodyA;
    }

    public String getBodyB() {
        return bodyB;
    }

    public void setBodyB(String bodyB) {
        this.bodyB = bodyB;
    }

    public PbJointType getType() {
        return type;
    }

    public void setType(PbJointType type) {
        this.type = type;
    }

    public PbVec2 getAnchorA() {
        return anchorA;
    }

    public void setAnchorA(PbVec2 anchorA) {
        this.anchorA = anchorA;
    }

    public PbVec2 getAnchorB() {
        return anchorB;
    }

    public void setAnchorB(PbVec2 anchorB) {
        this.anchorB = anchorB;
    }


    public float getUpperTranslation() {
        return upperTranslation;
    }

    public void setUpperTranslation(float upperTranslation) {
        this.upperTranslation = upperTranslation;
    }

    public float getLowerTranslation() {
        return lowerTranslation;
    }

    public void setLowerTranslation(float lowerTranslation) {
        this.lowerTranslation = lowerTranslation;
    }

    public boolean isCollideConnected() {
        return collideConnected;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public boolean isEnableLimit() {
        return enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public float getUpperAngle() {
        return upperAngle;
    }

    public void setUpperAngle(float upperAngle) {
        this.upperAngle = upperAngle;
    }

    public float getLowerAngle() {
        return lowerAngle;
    }

    public void setLowerAngle(float lowerAngle) {
        this.lowerAngle = lowerAngle;
    }

    public float getReferenceAngle() {
        return referenceAngle;
    }

    public void setReferenceAngle(float referenceAngle) {
        this.referenceAngle = referenceAngle;
    }
}
