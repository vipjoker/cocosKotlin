package com.vipjokerstudio.cocoskotlin.serialization.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobei  on 01.05.20.
 */
public class PbLevelData {

    private String name;
    private PbVec2 gravity = new PbVec2();
    private List<PbJointDef> joints = new ArrayList<>();
    private List<PbGameObject> objects = new ArrayList<>();

    public static PbLevelData createEmpty() {
            return new PbLevelData();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PbVec2 getGravity() {
        return gravity;
    }

    public void setGravity(PbVec2 gravity) {
        this.gravity = gravity;
    }

    public List<PbJointDef> getJoints() {
        return joints;
    }

    public void setJoints(List<PbJointDef> joints) {
        this.joints = joints;
    }

    public List<PbGameObject> getObjects() {
        return objects;
    }

    public void setObjects(List<PbGameObject> objects) {
        this.objects = objects;
    }

    public  PbGameObject findById(String userId){
        for (PbGameObject object : objects) {
            String id = object.getUserData().getObjectId();
            if(userId.equals(id)){
                return  object;
            }
        }
        return null;
    }





}
