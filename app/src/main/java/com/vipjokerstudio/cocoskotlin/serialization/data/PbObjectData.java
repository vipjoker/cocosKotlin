package com.vipjokerstudio.cocoskotlin.serialization.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobei  on 27.04.20.
 */
public class PbObjectData {
    private String name;
    private PbBodyDef bodyDef;
    private List<PbFixtureDef> fixtures = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PbBodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(PbBodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public List<PbFixtureDef> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<PbFixtureDef> fixtures) {
        this.fixtures = fixtures;
    }

    public static PbObjectData crateEmpty(){
        PbObjectData objectData = new PbObjectData();
        objectData.setBodyDef( new PbBodyDef());
        List<PbFixtureDef> fixtureDefs = new ArrayList<>();
        objectData.setFixtures(fixtureDefs);
        return objectData;
    }
}
