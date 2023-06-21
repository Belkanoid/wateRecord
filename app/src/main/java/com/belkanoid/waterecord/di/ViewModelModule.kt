package com.belkanoid.waterecord.di

import androidx.lifecycle.ViewModel
import com.belkanoid.waterecord.presentation.main.MainViewModel
import com.belkanoid.waterecord.presentation.recordList.RecordListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindsMainViewModel(viewModel: MainViewModel) : ViewModel

    @IntoMap
    @ViewModelKey(RecordListViewModel::class)
    @Binds
    fun bindsRecordListViewModel(viewModel: RecordListViewModel) : ViewModel
}