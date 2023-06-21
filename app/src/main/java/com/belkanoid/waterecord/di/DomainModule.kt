package com.belkanoid.waterecord.di

import com.belkanoid.waterecord.data.repository.RecordRepositoryImpl
import com.belkanoid.waterecord.domain.repository.RecordRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainModule {

    @Binds
    @Singleton
    fun bindsRecordRepository(impl: RecordRepositoryImpl): RecordRepository
}