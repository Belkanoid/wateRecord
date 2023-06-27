package com.belkanoid.waterecord.di

import android.app.Application
import androidx.fragment.app.DialogFragment
import com.belkanoid.waterecord.presentation.main.MainFragment
import com.belkanoid.waterecord.presentation.recordList.RecordsListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface RecordComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: RecordsListFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
        ) : RecordComponent
    }
}