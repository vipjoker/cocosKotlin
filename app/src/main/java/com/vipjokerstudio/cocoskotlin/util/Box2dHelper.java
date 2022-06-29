package com.vipjokerstudio.cocoskotlin.util;

import android.util.Log;


import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.jbox2d.dynamics.joints.WheelJointDef;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbBodyDef;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbFixtureDef;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbGameObject;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbJointDef;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbLevelData;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbObjectData;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbUserData;
import com.vipjokerstudio.cocoskotlin.serialization.data.PbVec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleh Makhobei  on 17.04.20.
 */
public class Box2dHelper {
    public static List<Body> getBodies(List<Body> array,World world) {
        array.clear();
        Body bodyList = world.getBodyList();

        while (bodyList != null){
            array.add(bodyList);
            bodyList = bodyList.m_next;
        }

        return array;
    }

    public static List<Joint> getJoints(List<Joint> joints, World world) {
        joints.clear();
        Joint jointList = world.getJointList();
        while (jointList != null){
            joints.add(jointList);
            jointList = jointList.m_next;
        }
        return joints;
    }


    public static List<Fixture> getFixtures(Body body,List<Fixture> fixtures) {
        fixtures.clear();

        Fixture fixtureList = body.m_fixtureList;
        while (fixtureList != null){

            fixtures.add(fixtureList);
            fixtureList = fixtureList.m_next;
        }

        return fixtures;
    }

    public static Joint findJointByName(String name,World world){
        List<Joint> joints = getJoints(new ArrayList<>(), world);
        for (Joint joint : joints) {
            Object userData = joint.getUserData();
            if(userData instanceof PbUserData){
                String objectId = ((PbUserData) userData).getObjectId();
                if(name.equals(objectId)){
                    return joint;
                }
            }

        }
        return null;
    }

    public static Body findBodyByName(String name,World world){
        List<Body> joints = getBodies(new ArrayList<>(), world);
        for (Body body : joints) {
            Object userData = body.getUserData();
            if(userData instanceof PbUserData){
                String objectId = ((PbUserData) userData).getObjectId();
                if(name.equals(objectId)){
                    return body;
                }
            }

        }
        return null;
    }


        public static void createLevel(PbLevelData levelData, World world){
        Map<String, Body> bodyMap = new HashMap<>();

        for (PbGameObject object : levelData.getObjects()) {
            String fileName = object.getFileName();
            PbObjectData objectByKey =new  PbObjectData();
            PbBodyDef bodyDef = objectByKey.getBodyDef();

            PbVec2 position = object.getPosition();
            bodyDef.setPosition(position);
            bodyDef.setAngle(object.getAngle());
            Body body =world.createBody(bodyDef.toBodyDef());
            body.setUserData(object.getUserData());

            String objectId = object.getUserData().getObjectId();
            bodyMap.put(objectId, body);
            for (PbFixtureDef fixture : objectByKey.getFixtures()) {
                body.createFixture(fixture.toFixtureDef());
            }
        }

        for (PbJointDef joint : levelData.getJoints()) {
            Body bodyA = bodyMap.get(joint.getBodyA());
            Body bodyB = bodyMap.get(joint.getBodyB());
            if(bodyA == null || bodyB == null){
                Log.e("Box2dHelper", "Cannot create level" );
                continue;
            }
            Joint worldJoint = null;
            switch (joint.getType()) {
                case DISTANCE: {
                    DistanceJointDef distanceJointDef = new DistanceJointDef();
                    distanceJointDef.bodyA = bodyA;
                    distanceJointDef.bodyB = bodyB;
                    distanceJointDef.length = joint.getLength();
                    distanceJointDef.localAnchorA.set(joint.getAnchorA().getX(), joint.getAnchorA().getY());
                    distanceJointDef.localAnchorB.set(joint.getAnchorB().getX(), joint.getAnchorB().getY());
                    worldJoint = world.createJoint(distanceJointDef);
                }
                break;
                case WHEEL: {
                    WheelJointDef wheelJointDef = new WheelJointDef();
                    wheelJointDef.bodyA = bodyA;
                    wheelJointDef.bodyB = bodyB;
                    wheelJointDef.localAnchorA.set(joint.getAnchorA().getX(), joint.getAnchorA().getY());
                    wheelJointDef.localAnchorB.set(joint.getAnchorB().getX(), joint.getAnchorB().getY());
                    wheelJointDef.localAxisA.set(joint.getLocalAxisA().getX(),joint.getLocalAxisA().getY());
                    wheelJointDef.frequencyHz = joint.getFrequency();
                    wheelJointDef.dampingRatio = joint.getDampingRatio();
                    wheelJointDef.maxMotorTorque = joint.getMaxForce();
                    worldJoint = world.createJoint(wheelJointDef);
                }
                break;

                case PRISMATIC: {
                    PrismaticJointDef def = new PrismaticJointDef();
                    def.bodyA = bodyA;
                    def.bodyB = bodyB;
                    def.collideConnected = joint.isCollideConnected();
                    def.enableLimit = joint.isEnableLimit();
                    def.localAnchorA.set(joint.getAnchorA().getX(), joint.getAnchorA().getY());
                    def.localAnchorB.set(joint.getAnchorB().getX(),joint.getAnchorB().getY());
                    def.localAxisA.set(joint.getLocalAxisA().getX(),joint.getLocalAxisA().getY());
                    def.lowerTranslation = joint.getLowerTranslation();
                    def.upperTranslation = joint.getUpperTranslation();
                    def.referenceAngle = joint.getReferenceAngle();
                    worldJoint = world.createJoint(def);
                }
                    break;
                case REVOLUTE:
                    RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                    revoluteJointDef.bodyA = bodyA;
                    revoluteJointDef.bodyB = bodyB;
                    revoluteJointDef.localAnchorA.set(joint.getAnchorA().getX(), joint.getAnchorA().getY());
                    revoluteJointDef.localAnchorB.set(joint.getAnchorB().getX(), joint.getAnchorB().getY());
                    revoluteJointDef.maxMotorTorque = joint.getMaxForce();
                    revoluteJointDef.lowerAngle = joint.getLowerAngle();
                    revoluteJointDef.upperAngle = joint.getUpperAngle();
                    revoluteJointDef.referenceAngle = joint.getReferenceAngle();
                    worldJoint = world.createJoint(revoluteJointDef);
                    break;
                case WELD: {
                    WeldJointDef weldJointDef = new WeldJointDef();
                    weldJointDef.bodyA = bodyA;
                    weldJointDef.bodyB = bodyB;
                    weldJointDef.localAnchorA.set(joint.getAnchorA().getX(), joint.getAnchorA().getY());
                    weldJointDef.localAnchorB.set(joint.getAnchorB().getX(), joint.getAnchorB().getY());
                    worldJoint = world.createJoint(weldJointDef);

                }
                break;
            }

            String name = joint.getName();
            if(name !=null && worldJoint != null ){
                PbUserData userData = new PbUserData();
                userData.setObjectId(name);
                worldJoint.setUserData(userData);
            }
        }
    }
}
