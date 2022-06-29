package com.vipjokerstudio.cocoskotlin.serialization.data;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobei  on 28.04.20.
 */
public class PbShapeDef {
    private PbShapeType shapeType = PbShapeType.CIRCLE;
    private List<PbVec2> points = new ArrayList<>();
    private float radius = 1;

    public PbShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(PbShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public List<PbVec2> getPoints() {
        return points;
    }

    public void setPoints(List<PbVec2> points) {
        this.points = points;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Shape toShape(){

        Vec2[] vec2 = new Vec2[points.size()];
        for (int i = 0; i < vec2.length; i++) {


            PbVec2 pbVec2 = points.get(i);
            vec2[i] = new Vec2(pbVec2.getX(),pbVec2.getY());
        }


        switch (shapeType){
            case POLYGON:
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.set(vec2,vec2.length);
                return  polygonShape;
            case CIRCLE:
                CircleShape shape = new CircleShape();
                shape.setRadius(radius);
                return shape;
            case CHAIN:
                ChainShape chainShape = new ChainShape();
                chainShape.createChain(vec2,vec2.length);
                return chainShape;

            case EDGE:
                 EdgeShape edgeShape = new EdgeShape();
                PbVec2 begin = points.get(0);
                PbVec2 end = points.get(1);


                edgeShape.set(
                        new Vec2(begin.getX(),begin.getY()),
                        new Vec2(end.getX(),end.getY()))
                ;
                return edgeShape;
        }
        throw new RuntimeException("Unsupported shape type");
    }
}
