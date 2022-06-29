/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.vipjokerstudio.cocoskotlin.serialization.json;

import com.google.gson.Gson;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.*;
import com.vipjokerstudio.cocoskotlin.serialization.JbSerializer;
import com.vipjokerstudio.cocoskotlin.serialization.SerializationHelper;
import com.vipjokerstudio.cocoskotlin.serialization.UnsupportedListener;
import com.vipjokerstudio.cocoskotlin.serialization.UnsupportedObjectException;
import com.vipjokerstudio.cocoskotlin.serialization.UnsupportedObjectException.Type;
import com.vipjokerstudio.cocoskotlin.serialization.data.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Protobuffer serializer implementation.
 * 
 * @author Daniel
 * 
 */
public class PbSerializer implements JbSerializer {
  private Gson gson = new Gson();
  private UnsupportedListener listener = null;

  public PbSerializer() {
  }



  public PbSerializer(UnsupportedListener argListener) {
    listener = argListener;
  }


  @Override
  public void setUnsupportedListener(UnsupportedListener argListener) {
    listener = argListener;
  }

  @Override
  public String serialize(World argWorld) {
    final PbWorld world = serializeWorld(argWorld);
    final String json = gson.toJson(world);
    return json;
  }

  public PbWorld serializeWorld(World argWorld) {
    final PbWorld builder = new PbWorld();


    builder.setGravity(vecToPb(argWorld.getGravity()));
    builder.setAutoClearForces(argWorld.getAutoClearForces());
    builder.setAllowSleep(argWorld.isAllowSleep());
    builder.setContinuousPhysics(argWorld.isContinuousPhysics());
    builder.setWarmStarting(argWorld.isWarmStarting());
    builder.setSubStepping(argWorld.isSubStepping());

    Body cbody = argWorld.getBodyList();
    int cnt = 0;
    HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();

    while (cbody != null) {
      builder.addBodies(serializeBody(cbody));
      bodies.put(cbody, cnt);
      cnt++;
      cbody = cbody.m_next;
    }
    cnt = 0;
    HashMap<Joint, Integer> joints = new HashMap<Joint, Integer>();
    Joint cjoint = argWorld.getJointList();
    // first pass
    while (cjoint != null) {
      if (SerializationHelper.isIndependentJoint(cjoint.getType())) {
        builder.addJoints(serializeJoint(cjoint, bodies, joints));
        joints.put(cjoint, cnt);
        cnt++;
      }
      cjoint = cjoint.m_next;
    }

    // second pass for dependent joints
    cjoint = argWorld.getJointList();
    while (cjoint != null) {
      if (!SerializationHelper.isIndependentJoint(cjoint.getType())) {
        builder.addJoints(serializeJoint(cjoint, bodies, joints));
        joints.put(cjoint, cnt);
        cnt++;
      }
      cjoint = cjoint.m_next;
    }

    return builder;
  }

  @Override
  public String serialize(Body argBody) {
    PbBody builder = serializeBody(argBody);

    final String json = gson.toJson(builder);
    return json;
  }

  @Override
  public PbBody serializeBody(Body argBody) {
    PbBody builder = new PbBody();

    switch (argBody.getType()) {
      case DYNAMIC:
        builder.setType(PbBodyType.DYNAMIC);
        break;
      case KINEMATIC:
        builder.setType(PbBodyType.KINEMATIC);
        break;
      case STATIC:
        builder.setType(PbBodyType.STATIC);
        break;
      default:
        UnsupportedObjectException e = new UnsupportedObjectException("Unknown body type: "
            + argBody.getType(), Type.BODY);
        if (listener == null || listener.isUnsupported(e)) {
          throw e;
        }
        return null;
    }
    builder.setPosition(vecToPb(argBody.getPosition()));
    builder.setAngle(argBody.getAngle());
    builder.setLinearVelocity(vecToPb(argBody.getLinearVelocity()));
    builder.setAngularVelocity(argBody.getAngularVelocity());
    builder.setLinearDamping(argBody.getLinearDamping());
    builder.setAngularDamping(argBody.getAngularDamping());
    builder.setGravityScale(argBody.getGravityScale());

    builder.setBullet(argBody.isBullet());
    builder.setAllowSleep(argBody.isSleepingAllowed());
    builder.setAwake(argBody.isAwake());
    builder.setActive(argBody.isActive());
    builder.setFixedRotation(argBody.isFixedRotation());

    Fixture curr = argBody.m_fixtureList;
    while (curr != null) {
      builder.addFixtures(serializeFixture(curr));
      curr = curr.m_next;
    }

    return builder;
  }

  @Override
  public String serialize(Fixture argFixture) {
    final PbFixture fixture = serializeFixture(argFixture);
    String json = gson.toJson(fixture);
    return json;

  }

  @Override
  public PbFixture serializeFixture(Fixture argFixture) {
    final PbFixture builder = new PbFixture();


    builder.setDensity(argFixture.m_density);
    builder.setFriction(argFixture.m_friction);
    builder.setRestitution(argFixture.m_restitution);
    builder.setSensor(argFixture.m_isSensor);

    builder.setShape(serializeShape(argFixture.m_shape));

    builder.setFilter(serializeFilter(argFixture.m_filter));
    return builder;
  }

  @Override
  public String serialize(Shape argShape) {
    PbShape builder = serializeShape(argShape);
    final String json = gson.toJson(builder);
    return json;
  }

  public PbShape serializeShape(Shape argShape) {
    final PbShape builder =new  PbShape();

    builder.setRadius(argShape.m_radius);

    switch (argShape.m_type) {
      case CIRCLE:
        CircleShape c = (CircleShape) argShape;
        builder.setType(PbShapeType.CIRCLE);
        builder.setCenter(vecToPb(c.m_p));
        break;
      case POLYGON:
        PolygonShape p = (PolygonShape) argShape;
        builder.setType(PbShapeType.POLYGON);
        builder.setCentroid(vecToPb(p.m_centroid));
        for (int i = 0; i < p.m_count; i++) {
          builder.addPoints(vecToPb(p.m_vertices[i]));
          builder.addNormals(vecToPb(p.m_normals[i]));
        }
        break;
      case EDGE:
        EdgeShape e = (EdgeShape) argShape;
        builder.setType(PbShapeType.EDGE);
        builder.setV0(vecToPb(e.m_vertex0));
        builder.setV1(vecToPb(e.m_vertex1));
        builder.setV2(vecToPb(e.m_vertex2));
        builder.setV3(vecToPb(e.m_vertex3));
        builder.setHas0(e.m_hasVertex0);
        builder.setHas3(e.m_hasVertex3);
        break;
      case CHAIN:
        ChainShape h = (ChainShape) argShape;
        builder.setType(PbShapeType.CHAIN);
        for (int i = 0; i < h.m_count; i++) {
          builder.addPoints(vecToPb(h.m_vertices[i]));
        }
        builder.setPrev(vecToPb(h.m_prevVertex));
        builder.setNext(vecToPb(h.m_nextVertex));
        builder.setHas0(h.m_hasPrevVertex);
        builder.setHas3(h.m_hasNextVertex);
        break;
      default:
        UnsupportedObjectException ex = new UnsupportedObjectException(
            "Currently only encodes circle and polygon shapes", Type.SHAPE);
        if (listener == null || listener.isUnsupported(ex)) {
          throw ex;
        }
        return null;
    }

    return builder;
  }

  @Override
  public String serialize(Joint argJoint, Map<Body, Integer> argBodyIndexMap,
      Map<Joint, Integer> argJointIndexMap) {
    PbJoint builder = serializeJoint(argJoint, argBodyIndexMap, argJointIndexMap);
    final String json = gson.toJson(builder);
    return json;
  }

  public PbJoint serializeJoint(Joint joint, Map<Body, Integer> argBodyIndexMap,
      Map<Joint, Integer> argJointIndexMap) {
    final PbJoint builder =new  PbJoint();

    Body bA = joint.getBodyA();
    Body bB = joint.getBodyB();

    if (!argBodyIndexMap.containsKey(bA)) {
      throw new IllegalArgumentException("Body " + bA + " is not present in the index map");
    }
    builder.setBodyA(argBodyIndexMap.get(bA));

    if (!argBodyIndexMap.containsKey(bB)) {
      throw new IllegalArgumentException("Body " + bB + " is not present in the index map");
    }
    builder.setBodyB(argBodyIndexMap.get(bB));

    builder.setCollideConnected(joint.getCollideConnected());

    switch (joint.getType()) {
      case REVOLUTE: {
        RevoluteJoint j = (RevoluteJoint) joint;
        builder.setType(PbJointType.REVOLUTE);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setRefAngle(j.getReferenceAngle());
        builder.setEnableLimit(j.isLimitEnabled());
        builder.setLowerLimit(j.getLowerLimit());
        builder.setUpperLimit(j.getUpperLimit());
        builder.setEnableMotor(j.isMotorEnabled());
        builder.setMotorSpeed(j.getMotorSpeed());
        builder.setMaxMotorTorque(j.getMaxMotorTorque());
        break;
      }
      case PRISMATIC: {
        PrismaticJoint j = (PrismaticJoint) joint;
        builder.setType(PbJointType.PRISMATIC);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setLocalAxisA(vecToPb(j.getLocalAxisA()));
        builder.setRefAngle(j.getReferenceAngle());
        builder.setEnableLimit(j.isLimitEnabled());
        builder.setLowerLimit(j.getLowerLimit());
        builder.setUpperLimit(j.getUpperLimit());
        builder.setEnableMotor(j.isMotorEnabled());
        builder.setMaxMotorForce(j.getMaxMotorForce());
        builder.setMotorSpeed(j.getMotorSpeed());
        break;
      }
      case DISTANCE: {
        DistanceJoint j = (DistanceJoint) joint;
        builder.setType(PbJointType.DISTANCE);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setLength(j.getLength());
        builder.setFrequency(j.getFrequency());
        builder.setDampingRatio(j.getDampingRatio());
        break;
      }
      case PULLEY: {
        PulleyJoint j = (PulleyJoint) joint;
        builder.setType(PbJointType.PULLEY);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setGroundAnchorA(vecToPb(j.getGroundAnchorA()));
        builder.setGroundAnchorB(vecToPb(j.getGroundAnchorB()));
        builder.setLengthA(j.getLengthA());
        builder.setLengthB(j.getLengthB());
        builder.setRatio(j.getRatio());
        break;
      }
      case MOUSE: {
        MouseJoint j = (MouseJoint) joint;
        builder.setType(PbJointType.MOUSE);
        builder.setTarget(vecToPb(j.getTarget()));
        builder.setMaxForce(j.getMaxForce());
        builder.setFrequency(j.getFrequency());
        builder.setDampingRatio(j.getDampingRatio());
        break;
      }
      case GEAR: {
        GearJoint j = (GearJoint) joint;
        builder.setType(PbJointType.GEAR);
        builder.setRatio(j.getRatio());
        if (!argJointIndexMap.containsKey(j.getJoint1())) {
          throw new IllegalArgumentException("Joint 1 not in map");
        }
        int j1 = argJointIndexMap.get(j.getJoint1());
        if (!argJointIndexMap.containsKey(j.getJoint2())) {
          throw new IllegalArgumentException("Joint 2 not in map");
        }
        int j2 = argJointIndexMap.get(j.getJoint2());

        builder.setJoint1(j1);
        builder.setJoint2(j2);
        break;
      }
      case FRICTION: {
        FrictionJoint j = (FrictionJoint) joint;
        builder.setType(PbJointType.FRICTION);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setMaxForce(j.getMaxForce());
        builder.setMaxTorque(j.getMaxTorque());
        break;
      }
      case CONSTANT_VOLUME: {
        ConstantVolumeJoint j = (ConstantVolumeJoint) joint;
        builder.setType(PbJointType.CONSTANT_VOLUME);

        for (int i = 0; i < j.getBodies().length; i++) {
          Body b = j.getBodies()[i];
          DistanceJoint djoint = j.getJoints()[i];
          if (!argBodyIndexMap.containsKey(b)) {
            throw new IllegalArgumentException("Body " + b + " is not present in the index map");
          }
          builder.addBodies(argBodyIndexMap.get(b));
          if (!argJointIndexMap.containsKey(djoint)) {
            throw new IllegalArgumentException("Joint " + djoint
                + " is not present in the index map");
          }
          builder.addJoints(argJointIndexMap.get(djoint));
        }
        break;
      }
      case WHEEL: {
        WheelJoint j = (WheelJoint) joint;
        builder.setType(PbJointType.WHEEL);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setLocalAxisA(vecToPb(j.getLocalAxisA()));
        builder.setEnableMotor(j.isMotorEnabled());
        builder.setMaxMotorTorque(j.getMaxMotorTorque());
        builder.setMotorSpeed(j.getMotorSpeed());
        builder.setFrequency(j.getSpringFrequencyHz());
        builder.setDampingRatio(j.getSpringDampingRatio());
        break;
      }
      case ROPE: {
        RopeJoint j = (RopeJoint) joint;
        builder.setType(PbJointType.ROPE);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setMaxLength(j.getMaxLength());
        break;
      }
      case WELD: {
        WeldJoint j = (WeldJoint) joint;
        builder.setType(PbJointType.WELD);
        builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
        builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
        builder.setRefAngle(j.getReferenceAngle());
        builder.setFrequency(j.getFrequency());
        builder.setDampingRatio(j.getDampingRatio());
        break;
      }
      default:
        UnsupportedObjectException e = new UnsupportedObjectException("Unknown joint type: "
            + joint.getType(), Type.JOINT);
        if (listener == null || listener.isUnsupported(e)) {
          throw e;
        }
        return null;
    }
    return builder;
  }

  public PbFilter serializeFilter(Filter argFilter) {
    PbFilter builder = new PbFilter();
    builder.setCategoryBits(argFilter.categoryBits);
    builder.setGroupIndex(argFilter.groupIndex);
    builder.setMaskBits(argFilter.maskBits);

    return builder;
  }

  private PbVec2 vecToPb(Vec2 argVec) {
    if (argVec == null) {
      return null;
    }
    PbVec2 vec = new PbVec2();
    vec.setX(argVec.x);
    vec.setY(argVec.y);
    return vec;

  }
}
