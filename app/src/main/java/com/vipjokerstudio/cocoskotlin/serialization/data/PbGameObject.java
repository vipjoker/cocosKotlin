package com.vipjokerstudio.cocoskotlin.serialization.data;


import org.jbox2d.common.Vec2;

import java.util.UUID;

/**
 * Created by Oleh Makhobei  on 01.05.20.
 */
public class PbGameObject {
    private PbUserData userData = new PbUserData();
    private String fileName;
    private PbVec2 position;
    private float angle;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public PbUserData getUserData() {
        return userData;
    }

    public void setUserData(PbUserData userData) {
        this.userData = userData;
    }

    public static PbGameObject create(String name, Vec2 position){
        PbGameObject gameObject = new PbGameObject();
        gameObject.setFileName(name);
        gameObject.setPosition(PbVec2.create(position.x,position.y));
        gameObject.setAngle(0);
        final PbUserData userData = new PbUserData();
        userData.setObjectId(name + "_" + UUID.randomUUID().toString());
        gameObject.setUserData(userData);
        return gameObject;
    }
}
