package com.belkanoid.waterecord.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.view.View
import android.view.ViewTreeObserver
import androidx.camera.core.impl.utils.MatrixExt.postRotate

inline fun View.afterMeasured(crossinline block: () -> Unit) {
    if (measuredWidth > 0 && measuredHeight > 0) {
        block()
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    block()
                }
            }
        })
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Image.getBitmap(): Bitmap {
    val buffer = this.planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer[bytes]
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
}