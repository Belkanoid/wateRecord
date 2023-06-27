package com.belkanoid.waterecord.presentation.main

import android.util.Log
import android.view.MotionEvent
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.MeteringPointFactory
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.domain.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: RecordRepository
): ViewModel() {



    fun focusEvent(event: MotionEvent, camera: Camera, width: Int, height: Int): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                true
            }
            MotionEvent.ACTION_UP -> {
                val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                    width.toFloat(), height.toFloat()
                )
                val autoFocusPoint = factory.createPoint(event.x, event.y)
                try {
                    camera.cameraControl.startFocusAndMetering(
                        FocusMeteringAction.Builder(
                            autoFocusPoint,
                            FocusMeteringAction.FLAG_AF
                        ).apply {
                            disableAutoCancel()
                        }.build()
                    )
                } catch (e: CameraInfoUnavailableException) {
                    Log.d("ERROR", "cannot access camera", e)
                }
                true
            }
            else -> false // Unhandled event.
        }
    }

    fun addRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecord(record)
        }
    }
}