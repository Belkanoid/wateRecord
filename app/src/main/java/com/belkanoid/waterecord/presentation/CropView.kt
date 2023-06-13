package com.belkanoid.waterecord.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Math.abs
import java.lang.Math.max
import java.lang.Math.min


class CropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val startPoint = PointF(0f, 0f)
    private val endPoint = PointF(0f, 0f)
    private val centerPoint = PointF(width/2f, height/2f)

    private var mLeft = 100f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = height/2f + 100f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTop = h/2f - 100f
        mRight = w/2f - 100f
    }

    //720 1448
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE


        canvas?.drawRect(
            mLeft,
            mTop,
            mRight,
            mBottom,
            paint
        )

        Log.d("Recors", "$mLeft, $mTop, $mRight, $mBottom")

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                PointF(event.x, event.y).let {
                    startPoint.set(it)
                    endPoint.set(it)
                    true
                }
            }

            MotionEvent.ACTION_MOVE -> {
            }
        }
        postInvalidate()
        return true
    }
}