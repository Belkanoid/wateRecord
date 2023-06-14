package com.belkanoid.waterecord.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Math.abs
import java.lang.Math.max
import kotlin.math.sqrt


class CropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val startPoint = PointF(0f, 0f)
    private val endPoint = PointF(0f, 0f)
    private val maxPoint = PointF(0f, 0f)
    private val centerPoint = PointF(0f, 0f)
//    private val region = Region()

    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

    fun getCroppedRectangle(): Rect = Rect(mLeft.toInt(), mTop.toInt(), mRight.toInt(), mBottom.toInt())

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLeft = 100f
        mTop = h/4f - 100f
        mRight = w - 100f
        mBottom = h/4f + 100f
        maxPoint.set(PointF(w.toFloat(), h.toFloat()))
        centerPoint.set(PointF(w/2f, h/4f))

    }

    //720 1448
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL


        val rectBackground = Rect(0, 0, maxPoint.x.toInt(), maxPoint.y.toInt())
        val croppedRect = Rect(mLeft.toInt(), mTop.toInt(), mRight.toInt(), mBottom.toInt())
        val region = Region()
        region.set(rectBackground)
        region.op(croppedRect, Region.Op.XOR)

        canvas?.drawRect(rectBackground, Paint().apply {color = Color.BLACK; style = Paint.Style.FILL; alpha = 90})
        canvas?.drawRect(croppedRect, paint.apply { alpha = 0 })

        canvas?.drawPath(region.boundaryPath, paint.apply {color = Color.BLACK; style = Paint.Style.FILL; alpha = 90})

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
                endPoint.set(event.x, event.y)
                val diffX = max(abs(startPoint.x - endPoint.x)*0.03f, 0.2f)
                val diffY = max(abs(startPoint.y - endPoint.y)*0.03f, 0.2f)

                if(length(startPoint)<length(endPoint)){
                    fromCenter(diffX, diffY)
                }
                else {
                    toCenter(diffX, diffY)
                }
            }
        }
        invalidate()
        return true
    }

    private fun fromCenter(diffX: Float, diffY: Float) {
        Log.d("Recors", "fromCenter")
        if(mLeft>50f) mLeft-=diffX
        if(mTop>centerPoint.y - 100f)  mTop-= diffY
        if(mRight<maxPoint.x-50f) mRight+=diffX
        if(mBottom<centerPoint.y + 100f) mBottom+=diffY

    }
    private fun toCenter(diffX: Float, diffY: Float) {
        Log.d("Recors", "toCenter")
        if(mLeft<centerPoint.x - 50f) mLeft+=diffX
        if(mTop<centerPoint.y - 50f)  mTop+= diffY
        if(mRight>centerPoint.x + 50f) mRight-=diffX
        if(mBottom>centerPoint.y + 50f) mBottom-=diffY
    }

    private fun length(point: PointF): Float {
        return sqrt(
            (point.x - centerPoint.x)*(point.x - centerPoint.x) + (point.y - centerPoint.y)*(point.y - centerPoint.y)
        )
    }
}