package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper;
import com.vipjokerstudio.cocoskotlin.util.Box2dHelper;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import java.util.ArrayList;

public class CCPhysicsSprite extends CCSprite {
    public static final float FACTOR = 32;
    private Body body;
    private Vec2 buffer = new Vec2();
    Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CCPhysicsSprite(String filepath) {
        super(filepath);
        initPaint();
        setAnchorPoint(0.5f,0.5f);
    }

    private void initPaint() {
        getPaint().setColor(Color.argb(100, 168, 17, 30));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.argb(255, 168, 17, 30));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        this.stroke.setStyle(Paint.Style.STROKE);
        this.stroke.setColor(Color.GREEN);
        this.stroke.setStrokeWidth(3);
    }

    public void setStaticSquare(World pWorld,boolean isDynamic) {

        setStaticSquare(pWorld,isDynamic,1f,0.8f,0.5f);
    }



        public void setStaticSquare(World pWorld,boolean isDynamic,float density, float friction, float restitution) {
        BodyDef def = new BodyDef();
        def.type = isDynamic ? BodyType.DYNAMIC:BodyType.STATIC ;
        def.position.set(getPositionRef().x/FACTOR, getPositionRef().y/FACTOR);
        def.angle = (float) Math.toDegrees(getRotation());
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getContentSize().width/2)/FACTOR,(getContentSize().height/2)/FACTOR);///,Vector2.Zero,0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density =density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        this.body = pWorld.createBody(def);
        this.body.createFixture(fixtureDef);

    }

    public void createEmptyBody(World pWorld,boolean isDynamic) {
        BodyDef def = new BodyDef();
        def.type = isDynamic ? BodyType.DYNAMIC:BodyType.STATIC ;
        def.position.set(super.getPositionRef().x/FACTOR, getPositionRef().y/FACTOR);
        this.body = pWorld.createBody(def);
    }

    public void addPolygon(Vec2[] points){
        Vec2[] newPoints = new Vec2[points.length];

        for (int i = 0; i < points.length; i++) {
            Vec2 point = points[i];
            newPoints[i] = point.mul(1f/FACTOR);
        }


        PolygonShape shape = new PolygonShape();
        shape.set(newPoints,newPoints.length);///,Vector2.Zero,0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density =1;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef);
    }

    public void setCircleShape(World pWorld, float density, float friction, float restitution) {
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.position.set(getPositionRef().x/FACTOR, getPositionRef().y/FACTOR);
        CircleShape shape = new CircleShape();

        shape.setRadius((getContentSize().width/2)/FACTOR);//,(contentSize.height/2)/FACTOR);///,Vector2.Zero,0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density =density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        this.body = pWorld.createBody(def);
        this.body.createFixture(fixtureDef);

    }

    public void addCircleShape(float radius,float density,float friction,float restitution){
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density =density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef);
    }

//    public void createDistanceJoint(World world,Body body,Vector2 anchorA,Vector2 anchorB,float frequency,float dampingRatio,boolean collideConnected){
//        DistanceJointDef rearForkDistanceDef = new DistanceJointDef();
//        rearForkDistanceDef.initialize(this.body, body, anchorA, anchorB);
//        rearForkDistanceDef.frequencyHz = frequency;
//        rearForkDistanceDef.dampingRatio = dampingRatio;
//        rearForkDistanceDef.collideConnected = collideConnected;
//        world.createJoint(rearForkDistanceDef);
//    }

    public void createDistanceJoint(World world,Body body,Vec2 anchorA,Vec2 anchorB,float distance,float frequency,float dampingRatio,boolean collideConnected){
        DistanceJointDef rearForkDistanceDef = new DistanceJointDef();
        rearForkDistanceDef.bodyA = this.body;
        rearForkDistanceDef.bodyB = body;
        rearForkDistanceDef.localAnchorA.set(anchorA);
        rearForkDistanceDef.localAnchorB.set(anchorB);
        rearForkDistanceDef.length = distance;
        rearForkDistanceDef.frequencyHz = frequency;
        rearForkDistanceDef.dampingRatio = dampingRatio;
        rearForkDistanceDef.collideConnected = collideConnected;
        world.createJoint(rearForkDistanceDef);
    }

    public void createWeldJoint(World world,Body body,Vec2 anchor){
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(this.body, body, anchor);
        world.createJoint(weldJointDef);

    }


    public Joint createRevoluteJoint(World world, Body body, Vec2 anchor, Vec2 anchorB, float maxMotorTorque, float referenceAngle, boolean enableMotor){
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = this.body;
        revoluteJointDef.bodyB = body;
        revoluteJointDef.localAnchorA.set(anchor);
        revoluteJointDef.localAnchorB.set(anchorB);
        revoluteJointDef.maxMotorTorque = maxMotorTorque;
        revoluteJointDef.enableMotor = enableMotor;
        revoluteJointDef.referenceAngle = referenceAngle;
        return world.createJoint(revoluteJointDef);
    }

    public void createPrismaticJoint(World world, Body body,Vec2 anchorA,
                                     Vec2 anchorB,
                                     Vec2 localAxis,
                                     float lowerTranslation,
                                     float upperTranslation,
                float referenceAngle
            ,boolean enableLimit,boolean collideConnected){
        PrismaticJointDef jointDef = new PrismaticJointDef();
//        jointDef.initialize(this.body,  body, anchorA, anchorB);
        jointDef.bodyA = this.body;
        jointDef.bodyB = body;
        jointDef.localAnchorA.set(anchorA);
        jointDef.localAnchorB.set(anchorB);

        jointDef.localAxisA.set(localAxis);
        jointDef.referenceAngle = referenceAngle ;

        jointDef.enableLimit = enableLimit;
        jointDef.lowerTranslation = lowerTranslation;
        jointDef.upperTranslation = upperTranslation;
        jointDef.collideConnected = collideConnected;
        world.createJoint(jointDef);

    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void draw(CanvasWrapper canvas) {
        super.draw(canvas);
        if(body != null){
            setPosition(body.getPosition().x * FACTOR, body.getPosition().y * FACTOR);
            float rot = (float) Math.toDegrees(body.getAngle());
            setRotation(rot);
            drawPhysics(body,canvas);

        }

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if(body != null){
            body.setTransform(new Vec2(x/FACTOR,y/FACTOR),body.getAngle());
        }
    }

    @Override
    public void setRotation(float rot) {
        super.setRotation(rot);
        if(body != null){
            body.setTransform(body.getPosition(),(float) Math.toRadians(rot));
        }
    }

    private void drawPhysics(Body body, CanvasWrapper canvas){



        for (Fixture fixture : Box2dHelper.getFixtures(body,new ArrayList<>())) {
            switch (fixture.getShape().getType()) {
                case CHAIN:
                    final ChainShape shape = (ChainShape) fixture.getShape();
                    Vec2 prevPoint = null;
                    for (Vec2 v: shape.m_vertices) {
//                        shape.getVertex(i, buffer);
                        buffer = v.mul(32);
                        canvas.drawCircle(buffer.x, buffer.y, 10, stroke);
                        if(prevPoint == null){
                            prevPoint = new Vec2();
                        }else{

                            canvas.drawLine(prevPoint.x,prevPoint.y,buffer.x,buffer.y,stroke);
                        }

                        prevPoint.set(buffer.x, buffer.y);
                    }
                    break;
                case POLYGON:

                    PolygonShape polygonShape = (PolygonShape) fixture.getShape();
                   // final Vector2 position = body.getPosition().cpy().scl(32);
                    boolean isFirst = true;
                    Path p = new Path();
                    for (Vec2 v:polygonShape.m_vertices) {
                        buffer = v.mul(32);
                        if (isFirst) {
                            isFirst = false;
                            p.moveTo(buffer.x, -buffer.y);
                        } else {
                            p.lineTo(buffer.x, -buffer.y);
                        }
                    }

//                    p.offset(contentSize.width/2,-contentSize.height/2);
                    p.close();
                    canvas.save();
//                    canvas.rotate((float) Math.toDegrees(body.getAngle()), 0, 0);
                    canvas.drawPath(p, getPaint());
                    canvas.drawPath(p, strokePaint);
                    canvas.restore();
                    break;
                case CIRCLE:
                    CircleShape circleShape = (CircleShape) fixture.getShape();
                    float radius = circleShape.getRadius() * FACTOR;
                    Vec2 center = body.getPosition().mul(32);


                    canvas.drawCircle(getBounds().midX(),-getBounds().midY(), radius, getPaint());
                    canvas.drawCircle(getBounds().midX(),-getBounds().midY(), radius, strokePaint);

                    canvas.save();
                    canvas.rotate((float)Math.toDegrees(body.getAngle()),getBounds().midX(),-getBounds().midY());
                    canvas.drawLine(getBounds().midX(),-getBounds().midY(),getBounds().midX() + radius,-getBounds().midY(),strokePaint);

                    canvas.restore();
                    break;
            }
        }
    }


}
