package com.vipjokerstudio.cocoskotlin.core.nodes

import android.graphics.Color
import android.graphics.Paint
import com.vipjokerstudio.cocoskotlin.core.nodes.CCNode
import android.util.Log
import com.vipjokerstudio.cocoskotlin.core.nodes.TypeFaceCache
import com.vipjokerstudio.cocoskotlin.core.CanvasWrapper
import com.vipjokerstudio.cocoskotlin.core.events.SimpleCallback

class UIButton(var text: String) : CCNode() {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var isPessed = false
    private var clickCallback: SimpleCallback? = null



    constructor(text: String, typeFace: String?):this(text) {
        val font = TypeFaceCache.getInstance().getFont(typeFace)
        textPaint.typeface = font
        this.text = text
        textPaint.color = Color.WHITE
        textPaint.textSize = 50f
        textPaint.style = Paint.Style.FILL
        textPaint.textAlign = Paint.Align.CENTER
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        setContentSize(textPaint.measureText(text), 50f)
    }



    override fun draw(canvas: CanvasWrapper?) {
//        canvas.drawRect(bounds,paint);
        canvas?.drawRect(bounds.origin.x, bounds.origin.y, bounds.width(), bounds.height(), paint)
        val yPos = (textPaint.descent() + textPaint.ascent()) / 2
        canvas?.drawText(text, bounds.midX(), bounds.midY() + yPos, textPaint)
        canvas?.drawCircle(0f, 0f, 5f, debugPaint)
        super.draw(canvas)
    }

    override fun onTouchStart(x: Float, y: Float) {
        super.onTouchStart(x, y)
        val vec2 = convertToNodeSpace(x, y)
        if (bounds.contains(vec2.x, vec2.y)) {
            paint.color = Color.RED
            isPessed = true
        }
        Log.i("ButtonDebug", "onTouchStart: " + vec2.x + " y " + vec2.y)
    }

    override fun onTouchEnd(x: Float, y: Float) {
        super.onTouchEnd(x, y)
        paint.color = Color.BLUE
        val vec2 = convertToNodeSpace(x, y)
        if (bounds.contains(vec2.x, vec2.y) && clickCallback != null) {
            clickCallback?.onClick()
        }
        isPessed = false
    }

    fun setClickCallback(clickCallback: SimpleCallback?) {
        this.clickCallback = clickCallback
    }
}