package com.vipjokerstudio.cocoskotlin.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import com.vipjokerstudio.cocoskotlin.core.nodes.Director
import java.io.IOException

object BitmapUtil {
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getBitmap(context: Context, drawableId: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            BitmapFactory.decodeResource(context.resources, drawableId)
        } else if (drawable is VectorDrawable) {
            getBitmap(drawable)
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }

    fun createTextureFromFilePath(path: String): Bitmap? {
        var bmp: Bitmap? = null
        try {
            val `is` = Director.getInstance().activity.assets.open(path)
            val opts = BitmapFactory.Options()
            //            opts.inPreferredConfig = tex.pixelFormat();
            bmp = BitmapFactory.decodeStream(`is`, null, opts)
            `is`.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return bmp
    }

}