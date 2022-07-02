package com.vipjokerstudio.testgame

import android.graphics.Bitmap
import android.util.Log
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSprite

class Player(val bitmap:Bitmap):CCSprite(bitmap) {
    var touched = false
    override fun onTouchStart(x: Float, y: Float) {
        super.onTouchStart(x, y)
        if(bounds.contains(x,y)){
            touched = true
        }else{

        }
    }

    override fun onTouchMove(x: Float, y: Float) {
        Log.i("Player77", "onTouchMove: ${x} ${y}")
        super.onTouchMove(x, y)
        if(touched){
            setPosition(x,y)
        }
    }

    override fun onTouchEnd(x: Float, y: Float) {
        super.onTouchEnd(x, y)
        touched = false
    }
}