package com.vipjokerstudio.cocoskotlin.serialization.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PbJoint {
    private Long tag;
    private Integer bodyA;
    private Integer bodyB;
    private boolean collideConnected;
    private PbJointType type;
    private PbVec2 localAnchorA;
    private PbVec2 localAnchorB;
    private float refAngle;
    private boolean enableLimit;
    private float lowerLimit;
    private float upperLimit;
    private boolean enableMotor;
    private float motorSpeed;
    private float maxMotorTorque;
    private PbVec2 localAxisA;
    private float maxMotorForce;
    private float length;
    private float frequency;
    private float dampingRatio;
    private PbVec2 groundAnchorA;
    private PbVec2 groundAnchorB;
    private float lengthA;
    private float lengthB;
    private float ratio;
    private PbVec2 target;
    private float maxForce;
    private int joint1;
    private int joint2;
    private float maxTorque;
    private float maxLength;
    private List<Integer> joints = new ArrayList<>();
    private List<Integer> bodies = new ArrayList<>();


    public static PbJoint parseFrom(String json, Gson gson) {
        return gson.fromJson(json,PbJoint.class);
    }

    public PbJointType getType() {
        return type;
    }

    public boolean getCollideConnected() {
        return collideConnected;
    }

    public Long getTag() {
        return tag;
    }

    public boolean hasTag() {
        return tag != null;
    }

    public Integer getBodyB() {
        return bodyB;
    }

    public Integer getBodyA() {
        return bodyA;
    }

    public boolean getEnableLimit() {
            return enableLimit;
    }

    public boolean getEnableMotor() {
        return enableMotor;
    }

    public PbVec2 getLocalAnchorA() {
        return localAnchorA;
    }

    public PbVec2 getLocalAnchorB() {
        return localAnchorB;
    }

    public PbVec2 getLocalAxisA() {
        return localAxisA;
    }

    public float getLowerLimit() {
        return lowerLimit;
    }

    public float getMaxMotorForce() {
        return maxMotorForce;
    }

    public float getMotorSpeed() {
        return motorSpeed;
    }

    public float getRefAngle() {
        return refAngle;
    }

    public float getUpperLimit() {
        return upperLimit;
    }

    public float getMaxMotorTorque() {
        return maxMotorTorque;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getDampingRatio() {
        return dampingRatio;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public float getMaxTorque() {
        return maxTorque;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public int getJointsCount() {
        return joints.size();
    }

    public int getBodiesCount() {
        return bodies.size();
    }

    public int getBodies(int i) {
        return bodies.get(i);
    }

    public int getJoints(int i) {
        return joints.get(i);
    }

    public Integer getJoint1() {
        return joint1;
    }

    public Integer getJoint2() {
        return joint2;
    }

    public float getRatio() {
        return ratio;
    }

    public PbVec2 getGroundAnchorA() {
        return groundAnchorA;
    }

    public PbVec2 getGroundAnchorB() {
        return groundAnchorB;
    }

    public float getLengthA() {
        return lengthA;
    }

    public float getLengthB() {
            return lengthB;
    }

    public float getLength() {
        return length;
    }

    public PbVec2 getTarget() {
        return target;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public void clearTag() {
        this.tag = null;
    }

    public void setBodyA(Integer bodyA) {
        this.bodyA = bodyA;
    }

    public void setBodyB(Integer bodyB) {
        this.bodyB = bodyB;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public void setType(PbJointType type) {
        this.type = type;
    }

    public void setLocalAnchorA(PbVec2 localAnchorA) {
        this.localAnchorA = localAnchorA;
    }

    public void setLocalAnchorB(PbVec2 localAnchorB) {
        this.localAnchorB = localAnchorB;
    }

    public void setRefAngle(float refAngle) {
        this.refAngle = refAngle;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public void setLowerLimit(float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public void setUpperLimit(float upperLimit) {
        this.upperLimit = upperLimit;
    }

    public void setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
    }

    public void setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public void setMaxMotorTorque(float maxMotorTorque) {
        this.maxMotorTorque = maxMotorTorque;
    }

    public void setLocalAxisA(PbVec2 localAxisA) {
        this.localAxisA = localAxisA;
    }

    public void setMaxMotorForce(float maxMotorForce) {
        this.maxMotorForce = maxMotorForce;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public void setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
    }

    public void setGroundAnchorA(PbVec2 groundAnchorA) {
        this.groundAnchorA = groundAnchorA;
    }

    public void setGroundAnchorB(PbVec2 groundAnchorB) {
        this.groundAnchorB = groundAnchorB;
    }

    public void setLengthA(float lengthA) {
        this.lengthA = lengthA;
    }

    public void setLengthB(float lengthB) {
        this.lengthB = lengthB;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setTarget(PbVec2 target) {
        this.target = target;
    }

    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public void setJoint1(int joint1) {
        this.joint1 = joint1;
    }

    public void setJoint2(int joint2) {
        this.joint2 = joint2;
    }

    public void setMaxTorque(float maxTorque) {
        this.maxTorque = maxTorque;
    }

    public void addBodies(Integer integer) {
        bodies.add(integer);
    }

    public void addJoints(Integer integer) {
        joints.add(integer);
    }

    public void setMaxLength(float maxLength) {
        this.maxLength = maxLength;
    }

}
