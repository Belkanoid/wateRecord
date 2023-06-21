package com.belkanoid.waterecord.presentation
import android.app.Application
import com.belkanoid.waterecord.di.DaggerRecordComponent

class Application: Application() {

    val component by lazy {
        DaggerRecordComponent.factory().create(this)
    }

}