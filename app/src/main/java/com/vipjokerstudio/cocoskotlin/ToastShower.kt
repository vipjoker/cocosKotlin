package com.vipjokerstudio.cocoskotlin

import android.content.Context
import android.util.Log
import android.widget.Toast

class ToastShower(val context:Context) {
    fun show(msg:String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }
}