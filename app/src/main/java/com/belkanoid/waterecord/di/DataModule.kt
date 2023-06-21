package com.belkanoid.waterecord.di

import android.app.Application
import com.belkanoid.waterecord.data.database.RecordDatabase
import com.belkanoid.waterecord.data.database.dao.RecordDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideShoppingDao(application: Application) : RecordDao {
        return RecordDatabase.getInstance(application).recordDao()
    }
}