package com.vipjokerstudio.cocoskotlin.serialization.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PbBody {
    private PbBodyType type;
    private Long tag;
    private PbVec2 position;
    private float angle;
    private PbVec2 linearVelocity;
    private float angularVelocity;
    private float linearDamping;
    private float angularDamping;
    private float gravityScale;
    private boolean bullet;
    private boolean allowSleep;
    private boolean awake;
    private boolean active;
    private boolean fixedRotation;
    private List<PbFixture> fixtures = new ArrayList<>();


    public static PbBody parseFrom(String json, Gson gson) {
        return gson.fromJson(json,PbBody.class);
    }

    public PbVec2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public float getGravityScale() {
        return gravityScale;
    }


    public PbBodyType getType() {
        return type;
    }

    public int getFixturesCount() {
        return fixtures.size();
    }

    public PbFixture getFixtures(int i) {
        return fixtures.get(i);
    }

    public List<PbFixture> getFixtures() {
        return fixtures;
    }

    public PbVec2 getLinearVelocity() {
        return linearVelocity;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public boolean hasTag() {
        return tag != null;
    }

    public Long getTag() {
        return tag;
    }

    public void setType(PbBodyType type) {
        this.type = type;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public void setPosition(PbVec2 position) {
        this.position = position;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setLinearVelocity(PbVec2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public boolean getBullet() {
        return bullet;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public boolean getAllowSleep() {
        return allowSleep;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public boolean getAwake() {
        return awake;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean getFixedRotation() {
        return fixedRotation;
    }

    public void addFixtures(PbFixture serializeFixture) {
        fixtures.add(serializeFixture);
    }
}
