package com.vipjokerstudio.cocoskotlin.serialization.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PbShape {
    private Long tag;
    private float radius;
    private PbShapeType type;
    private PbVec2 center;
    private PbVec2 centroid;
    private PbVec2 v0;
    private PbVec2 v1;
    private PbVec2 v2;
    private PbVec2 v3;
    private boolean has0;
    private boolean has3;
    private PbVec2 prev;
    private PbVec2 next;

    private List<PbVec2> points = new ArrayList<>();
    private List<PbVec2> normals = new ArrayList<>();
    public static PbShape parseFrom(String argInput, Gson gson) {
        return gson.fromJson(argInput,PbShape.class);
    }

    public PbShapeType getType() {
        return type;
    }

    public PbVec2 getCenter() {
        return center;
    }

    public PbVec2 getCentroid() {
        return centroid;
    }

    public int getPointsCount() {
        return points.size();
    }

    public PbVec2 getPoints(int i) {
        return points.get(i);
    }

    public List<PbVec2> getPoints() {
        return points;
    }

    public PbVec2 getNormals(int i) {
            return normals.get(i);
    }

    public PbVec2 getV0() {
        return v0;
    }

    public PbVec2 getV1() {
        return v1;
    }

    public PbVec2 getV2() {
        return v2;
    }

    public PbVec2 getV3() {
        return v3;
    }


    public boolean getHas3() {
        return has3;
    }

    public PbVec2 getPrev() {
        return prev;
    }

    public PbVec2 getNext() {
        return next;
    }

    public float getRadius() {
        return radius;
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

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setType(PbShapeType type) {
        this.type = type;
    }

    public void setCenter(PbVec2 center) {
        this.center = center;
    }

    public void setCentroid(PbVec2 centroid) {
        this.centroid = centroid;
    }

    public void addPoints(PbVec2 vecToPb) {
        points.add(vecToPb);
    }

    public void addNormals(PbVec2 vecToPb) {
        normals.add(vecToPb);
    }

    public void setV0(PbVec2 v0) {
        this.v0 = v0;
    }

    public void setV1(PbVec2 v1) {
        this.v1 = v1;
    }

    public void setV2(PbVec2 v2) {
        this.v2 = v2;
    }

    public void setV3(PbVec2 v3) {
        this.v3 = v3;
    }

    public void setHas0(boolean has0) {
        this.has0 = has0;
    }

    public boolean getHas0() {
        return has0;
    }

    public void setHas3(boolean has3) {
        this.has3 = has3;
    }

    public void setPrev(PbVec2 prev) {
        this.prev = prev;
    }

    public void setNext(PbVec2 next) {
        this.next = next;
    }

}
