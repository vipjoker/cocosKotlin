package com.vipjokerstudio.cocoskotlin.render;

import android.graphics.Canvas;

public interface Renderer {


    public void draw(Canvas canvas, long time);

    void init(float width, float height);

    void moveDown();

    void moveLeft();

    void moveRight();

    void moveTop();

    void action();

    void zoomCamera(int zoomLevel);

    void onTouchStart(float x, float y);

    void onTouchMove(float x, float y);

    void onTouchEnd(float x, float y);
}


