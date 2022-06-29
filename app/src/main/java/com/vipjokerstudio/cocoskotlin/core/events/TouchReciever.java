package com.vipjokerstudio.cocoskotlin.core.events;

/**
 * Created by Oleh Makhobei  on 16.03.20.
 */
public interface TouchReciever {

    public void onTouchStart(float x, float y) ;


    public void onTouchMove(float x, float y) ;



    public void onTouchEnd(float x, float y) ;

}
